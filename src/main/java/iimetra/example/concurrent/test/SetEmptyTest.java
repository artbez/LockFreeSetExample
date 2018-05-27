package iimetra.example.concurrent.test;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.III_Result;
import org.openjdk.jcstress.infra.results.I_Result;

@JCStressTest
@Description("Test set empty")
@Outcome(id = "1", expect = Expect.ACCEPTABLE, desc = "1 means set is empty")
@Outcome(expect = Expect.FORBIDDEN, desc = "Data race in removing")
public class SetEmptyTest {

    @Actor
    public void actor1(SetState.FewElementsState state) {
        state.set.remove(SetState.FewElementsState.FIRST);
    }

    @Actor
    public void actor2(SetState.FewElementsState state) {
        state.set.remove(SetState.FewElementsState.SECOND);
    }

    @Actor
    public void actor3(SetState.FewElementsState state) {
        state.set.remove(SetState.FewElementsState.THIRD);
    }

    @Arbiter
    public void arbiter(SetState.FewElementsState state, I_Result result) {
        result.r1 = state.set.isEmpty() ? 1 : 0;
    }
}
