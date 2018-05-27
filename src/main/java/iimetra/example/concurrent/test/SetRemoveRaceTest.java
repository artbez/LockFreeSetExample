package iimetra.example.concurrent.test;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.III_Result;
import org.openjdk.jcstress.infra.results.II_Result;
import org.openjdk.jcstress.infra.results.I_Result;


@JCStressTest
@Description("Test set remove")
@Outcome(id = "0, 0, 1", expect = Expect.ACCEPTABLE, desc = "Contains only 2")
@Outcome(expect = Expect.FORBIDDEN, desc = "Data race in removing")
public class SetRemoveRaceTest {

    @Actor
    public void actor1(SetState.FewElementsState state) {
        state.set.remove(SetState.FewElementsState.FIRST);
    }

    @Actor
    public void actor2(SetState.FewElementsState state) {
        state.set.remove(SetState.FewElementsState.SECOND);
    }

    @Arbiter
    public void arbiter(SetState.FewElementsState state, III_Result result) {
        result.r1 = state.set.contains(SetState.FewElementsState.FIRST) ? 1 : 0;
        result.r2 = state.set.contains(SetState.FewElementsState.SECOND) ? 1 : 0;
        result.r3 = state.set.contains(SetState.FewElementsState.THIRD) ? 1 : 0;
    }
}
