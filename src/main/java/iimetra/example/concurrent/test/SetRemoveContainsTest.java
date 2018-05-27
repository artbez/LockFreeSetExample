package iimetra.example.concurrent.test;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

@JCStressTest
@Description("Test race set remove and contains")
@Outcome(id = "0, 0", expect = ACCEPTABLE, desc = "0 means set does't contain element")
@Outcome(expect = FORBIDDEN, desc = "Case violating atomicity")
public class SetRemoveContainsTest {

    @Actor
    public void actor1(SetState.FewElementsState state, II_Result result) {
        state.set.remove(SetState.FewElementsState.FIRST);
        result.r1 = state.set.contains(SetState.FewElementsState.FIRST) ? 1 : 0;
    }

    @Actor
    public void actor2(SetState.FewElementsState state, II_Result result) {
        state.set.remove(SetState.FewElementsState.SECOND);
        result.r2 = state.set.contains(SetState.FewElementsState.SECOND) ? 1 : 0;
    }
}
