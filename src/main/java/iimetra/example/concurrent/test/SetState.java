package iimetra.example.concurrent.test;

import iimetra.example.concurrent.set.LockFreeSet;
import iimetra.example.concurrent.set.LockFreeSetImpl;
import org.openjdk.jcstress.annotations.State;

@State
public class SetState {
    final LockFreeSet<Integer> set = new LockFreeSetImpl<>();
}
