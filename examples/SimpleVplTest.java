
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(VplGrader.class)
public class SimpleVplTest {

    @Test
    @VplTestcase(points=15)
    public void testOtherOne() {
        assertTrue(true);
    }

    @Test
    public void testOtherTwo() {
    	fail("This went wrong");
    }
    
    @Test
    public void testOtherThree() {
    	assertTrue("Eins sollte 1 sein",1==2);
    }
}
