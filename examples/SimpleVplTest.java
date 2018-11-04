
import static org.junit.Assert.*;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SimpleVplTest {

    @Test
    public void testOtherOne_25P() {
        assertTrue(true);
    }

    @Test
    public void testOtherTwo_10P() {
    	fail("This went wrong");
    }
    
    @Test
    public void testOtherThree_10P() {
    	assertTrue("Eins sollte 1 sein",1==2);
    }
}
