package iimetra.example.concurrent.test;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Description;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.infra.results.II_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

@JCStressTest
@Description("Test race set add and contains")
@Outcome(id = "0, 1", expect = ACCEPTABLE, desc = "return 0 and 1")
@Outcome(expect = FORBIDDEN, desc = "Case violating atomicity")
public class SetAddContainsTest {

    @Actor
    public void actor1(SetState.EmptyState state, II_Result result) {
        state.set.add(0L);
        result.r1 = state.set.contains(0L) ? 0 : -1;
    }

    @Actor
    public void actor2(SetState.EmptyState state, II_Result result) {
        state.set.add(1L);
        result.r2 = state.set.contains(1L) ? 1 : -1;
    }
}
