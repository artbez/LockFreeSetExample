package iimetra.example.concurrent.test;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;


@JCStressTest
@Description("Test race set check size")
@Outcome(id = "2", expect = ACCEPTABLE, desc = "size of set = 2 ")
@Outcome(id = "1", expect = FORBIDDEN, desc = "size of set = 1 is race")
@Outcome(expect = FORBIDDEN, desc = "Case violating atomicity.")
public class SetSizeTest {

    @Actor
    public void actor1(SetState state) {
        state.set.add(0);
    }

    @Actor
    public void actor2(SetState state) {
        state.set.add(1);
    }

    @Arbiter
    public void arbiter(SetState state, I_Result result) {
        result.r1 = (state.set.contains(0) ? 1 : 0) + (state.set.contains(1) ? 1 : 0);
    }
}

