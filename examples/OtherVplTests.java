
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(VplGrader.class)
public class OtherVplTests {

    @Test
    @VplTestcase(points=25, showFullStacktraceOnFailure=false)
    public void testOne() {
        assertTrue(true);
        assertEquals(0,1);
    }

    @Test
    public void testTwo() {
    	fail("This went wrong");
    }
    
    //@Test
    @VplTestcase
    public void testThree() {
    	assertTrue("Eins sollte 1 sein",1==2);
    }
    
    @Test(expected=IllegalStateException.class)
    @VplTestcase
    public void testFour() 
    {
    	throw new IllegalStateException("Das geht so nicht");
    }
}
