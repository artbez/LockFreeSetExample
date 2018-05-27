package iimetra.example.concurrent.test;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.III_Result;
import org.openjdk.jcstress.infra.results.II_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

@JCStressTest
@Description("Test race set add and remove same element")
@Outcome(id = "1, 1, 0", expect = ACCEPTABLE, desc = "Added before removed")
@Outcome(id = "1, 0, 1", expect = ACCEPTABLE, desc = "Added after removed")
@Outcome(expect = FORBIDDEN, desc = "Data race")
public class SetAddRemoveSameTest {

    @Actor
    public void actor1(SetState.EmptyState state, III_Result result) {
        Boolean added = state.set.add(0L);
        result.r1 = added ? 1 : 0;
    }

    @Actor
    public void actor2(SetState.EmptyState state, III_Result result) {
        Boolean removed = state.set.remove(0L);
        result.r2 = removed ? 1 : 0;
    }

    @Arbiter
    public void arbiter(SetState.EmptyState state, III_Result result) {
        Boolean contains = state.set.contains(0L);
        result.r3 = contains ? 1: 0;
    }
}
