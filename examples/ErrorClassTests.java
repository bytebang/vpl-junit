import static org.junit.Assert.*;

import org.junit.Test;

public class ErrorClassTests {

	@Test
	public void testArrayIndexOutOfBoundsException_5P() 
	{
		//to see what happens if a student class produces a null Pointer exception
		ErrorClass e = new ErrorClass();
		assertTrue(e.produceArrayIndexOutOfBoundsException() == 2);
	}

	@Test
	public void testNullPointerException_5P()
	{
		ErrorClass e = new ErrorClass();
		assertTrue(e.produceNullPointerException());
	}
	
}
