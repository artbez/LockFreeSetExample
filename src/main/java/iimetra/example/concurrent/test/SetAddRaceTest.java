package iimetra.example.concurrent.test;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;
import org.openjdk.jcstress.infra.results.I_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;


@JCStressTest
@Description("Test race set check size")
@Outcome(id = "1, 1", expect = ACCEPTABLE, desc = "set contains 2 elements ")
@Outcome(id = "1, 0", expect = FORBIDDEN, desc = "set contains 1 element is race")
@Outcome(id = "0, 1", expect = FORBIDDEN, desc = "set contains 1 element is race")
@Outcome(expect = FORBIDDEN, desc = "Case violating atomicity")
public class SetAddRaceTest {

    @Actor
    public void actor1(SetState.EmptyState state) {
        state.set.add(0L);
    }

    @Actor
    public void actor2(SetState.EmptyState state) {
        state.set.add(1L);
    }

    @Arbiter
    public void arbiter(SetState.EmptyState state, II_Result result) {
        result.r1 = state.set.contains(0L) ? 1 : 0;
        result.r2 = state.set.contains(1L) ? 1 : 0;
    }
}

