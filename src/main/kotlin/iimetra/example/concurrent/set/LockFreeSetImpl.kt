package iimetra.example.concurrent.set

import java.util.concurrent.atomic.AtomicMarkableReference
import java.util.concurrent.atomic.AtomicStampedReference

class LockFreeSetImpl<in T : Comparable<T>> : LockFreeSet<T> {

    private val head = SimpleNode<T>(null)

    // wait-free
    override fun add(value: T): Boolean {
        while (true) {
            findAndDo(value) { next ->
                if (next != null && next.obj == value && !next.isRemoved()) {
                    return false
                }

                if (addAttempt(next, Node(value, next))) {
                    return true
                }
            }
        }
    }

    // wait-free
    override fun remove(value: T): Boolean {
        while (true) {
            findAndDo(value) { next ->
                if (next != null && next.obj == value && !next.isRemoved()) {
                    if (next.markRemovedAttempt(next.next)) {
                        return true
                    }
                } else {
                    return false
                }
            }
        }
    }

    // lock-free
    override fun isEmpty(): Boolean {
        val foundPair = traverseUntil(withReturn = true) { isRemoved() }
        return foundPair.first == head && foundPair.second == null
    }

    // wait-free
    override fun contains(value: T): Boolean {
        val next = traverseUntil(withReturn = false) { obj < value }.second
        return next != null && next.obj == value && !next.isRemoved()
    }

    private inline fun findAndDo(value: T, doOnFound: SimpleNode<T>.(Node<T>?) -> Unit) {
        val foundPair = traverseUntil(withReturn = false) { obj < value }
        return foundPair.first.doOnFound(foundPair.second)
    }

    private inline fun traverseUntil(withReturn: Boolean, condition: Node<T>.() -> Boolean = { true }): Pair<SimpleNode<T>, Node<T>?> {
        var current = head
        var next = current.next

        loop@ while (next != null && next.condition()) {
            if (next.isRemoved()) {
                val successfulRemove = current.removeAttempt(next)

                when {
                    withReturn && !successfulRemove -> {
                        current = head
                        next = current.next
                        continue@loop
                    }

                    successfulRemove -> {
                        next = next.next
                        continue@loop
                    }
                }
            }

            current = next
            next = current.next
        }

        return current to next
    }
}

private open class SimpleNode<T : Comparable<T>>(next: Node<T>?) {
    private var state = AtomicMarkableReference<Node<T>>(next, false)

    val next: Node<T>?
        get() = state.reference

    fun isRemoved(): Boolean = state.isMarked

    fun markRemovedAttempt(forRemove: Node<T>?) = state.compareAndSet(forRemove, forRemove, false, true)

    fun removeAttempt(forRemove: Node<T>) = state.compareAndSet(forRemove, forRemove.next, false, false)

    fun addAttempt(oldChild: Node<T>?, newChild: Node<T>) = state.compareAndSet(oldChild, newChild, false, false)
}

private class Node<T : Comparable<T>>(val obj: T, next: Node<T>?) : SimpleNode<T>(next)