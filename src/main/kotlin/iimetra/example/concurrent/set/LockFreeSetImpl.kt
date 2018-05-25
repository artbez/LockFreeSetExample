package iimetra.example.concurrent.set

import java.util.concurrent.atomic.AtomicStampedReference

class LockFreeSetImpl<in T : Comparable<T>> : LockFreeSet<T> {

    private val head = SimpleNode<T>(null)

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

    override fun isEmpty(): Boolean {
        val foundPair = traverseUntil { isRemoved() }
        return foundPair.first == head && foundPair.second == null
    }

    override fun contains(value: T): Boolean {
        var next = head.next

        while (next != null && next.obj < value) {
            next = next.next
        }

        return next != null && next.obj == value && !next.isRemoved()
    }

    private inline fun findAndDo(value: T, doOnFound: SimpleNode<T>.(Node<T>?) -> Unit) {
        val foundPair = traverseUntil { obj < value }
        return foundPair.first.doOnFound(foundPair.second)
    }

    private inline fun traverseUntil(condition: Node<T>.() -> Boolean = { true }): Pair<SimpleNode<T>, Node<T>?> {
        var current = head
        var next = current.next

        while (next != null && next.condition()) {
            if (next.isRemoved()) {
                if (current.removeAttempt(next)) {
                    next = current.next
                } else {
                    current = head
                    next = current.next
                }
            } else {
                current = next
                next = next.next
            }
        }

        return current to next;
    }
}

private open class SimpleNode<T : Comparable<T>>(next: Node<T>?) {
    private var state = AtomicStampedReference(next, 0)

    val next: Node<T>?
        get() = state.reference

    fun isRemoved(): Boolean = state.stamp != 0

    fun markRemovedAttempt(forRemove: Node<T>?) = state.compareAndSet(forRemove, forRemove, 0, 1)

    fun removeAttempt(forRemove: Node<T>) = state.compareAndSet(forRemove, forRemove.next, 0, 0)

    fun addAttempt(oldChild: Node<T>?, newChild: Node<T>) = state.compareAndSet(oldChild, newChild, 0, 0)
}

private class Node<T : Comparable<T>>(val obj: T, next: Node<T>?) : SimpleNode<T>(next)