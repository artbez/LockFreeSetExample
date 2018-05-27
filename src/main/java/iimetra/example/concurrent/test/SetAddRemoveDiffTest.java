package iimetra.example.concurrent.test;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.III_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

@JCStressTest
@Description("Test race set add and remove different elements")
@Outcome(id = "1, 0, 0", expect = ACCEPTABLE, desc = "Added before removed")
@Outcome(expect = FORBIDDEN, desc = "Data race")
public class SetAddRemoveDiffTest {

    @Actor
    public void actor1(SetState.FewElementsState state) {
        state.set.add(1L);
    }

    @Actor
    public void actor2(SetState.FewElementsState state) {
        state.set.remove(SetState.FewElementsState.SECOND);
    }

    @Actor
    public void actor3(SetState.FewElementsState state) {
        state.set.remove(SetState.FewElementsState.FIRST);
    }

    @Arbiter
    public void arbiter(SetState.FewElementsState state, III_Result result) {
        result.r1 = state.set.contains(1L) ? 1 : 0;
        result.r2 = state.set.contains(SetState.FewElementsState.FIRST) ? 1 : 0;
        result.r3 = state.set.contains(SetState.FewElementsState.SECOND) ? 1 : 0;
    }
}
