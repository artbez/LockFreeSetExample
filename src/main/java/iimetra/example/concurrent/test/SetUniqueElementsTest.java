package iimetra.example.concurrent.test;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.III_Result;
import org.openjdk.jcstress.infra.results.II_Result;

import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

@JCStressTest
@Description("Test set remove")
@Outcome(id = "1, 0", expect = Expect.ACCEPTABLE, desc = "Contains unique elements")
@Outcome(id = "1, 1", expect = Expect.FORBIDDEN, desc = "Contains non-unique elements")
@Outcome(expect = FORBIDDEN, desc = "Case violating atomicity")
public class SetUniqueElementsTest {

    @Actor
    public void actor1(SetState.EmptyState state) {
        state.set.add(0L);
    }

    @Actor
    public void actor2(SetState.EmptyState state) {
        state.set.add(0L);
    }

    @Arbiter
    public void arbiter(SetState.EmptyState state, II_Result result) {
        result.r1 = state.set.contains(0L) ? 1 : 0;
        state.set.remove(0L);
        result.r2 = state.set.contains(0L) ? 1 : 0;
    }
}
