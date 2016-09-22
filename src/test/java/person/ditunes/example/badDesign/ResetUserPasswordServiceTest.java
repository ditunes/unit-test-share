package person.ditunes.example.badDesign;

import org.junit.Test;

/**
 * Created by linhan on 16/9/22.
 */
public class ResetUserPasswordServiceTest {

    @Test
    public void testS(){
        ResetUserPasswordService s = new ResetUserPasswordService();
        s.reset("22");
    }
}
