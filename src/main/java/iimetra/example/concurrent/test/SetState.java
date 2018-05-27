package iimetra.example.concurrent.test;

import iimetra.example.concurrent.set.LockFreeSet;
import iimetra.example.concurrent.set.LockFreeSetImpl;
import org.openjdk.jcstress.annotations.State;

import java.util.HashSet;
import java.util.Set;


public class SetState {

    @State
    public static class EmptyState {
        public final LockFreeSet<Long> set = new LockFreeSetImpl<>();
        // Uncomment this in order to make sure that set HashSet is not thread-safe
        // final Set<Long> set = new HashSet<>();
    }

    @State
    public static class FewElementsState {

        public final LockFreeSet<Long> set = new LockFreeSetImpl<>();
        // Uncomment this in order to make sure that set HashSet is not thread-safe
        // final Set<Long> set = new HashSet<>();

        public static final Long FIRST = 0L;
        public static final Long SECOND = 2L;
        public static final Long THIRD = 4L;

        public FewElementsState() {
            set.add(FIRST);
            set.add(SECOND);
            set.add(THIRD);
        }
    }
}
