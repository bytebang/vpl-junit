
import static org.junit.Assert.*;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class OtherVplTests {

    @Test
    public void testOne_5P() {
        assertTrue(true);
        assertEquals(0,1);
    }

    @Test
    public void testTwo_10P() {
    	fail("This went wrong");
    }
    
    @Test
    public void testThree_20P() {
    	assertTrue("Eins sollte 1 sein",1==2);
    }
    
    @Test(expected=IllegalStateException.class)
    public void testFour_20P() 
    {
    	throw new IllegalStateException("Das geht so nicht");
    }
}
