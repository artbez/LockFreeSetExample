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
@Outcome(id = "0, 1", expect = ACCEPTABLE, desc = "return 0L and 1L")
@Outcome(expect = FORBIDDEN, desc = "Case violating atomicity.")
public class SetAddContainsTest {

    @Actor
    public void actor1(SetState state, II_Result result) {
        state.set.add(0);
        result.r1 = state.set.contains(0) ? 0 : -1;
    }

    @Actor
    public void actor2(SetState state, II_Result result) {
        state.set.add(1);
        result.r2 = state.set.contains(1) ? 1 : -1;
    }
}
