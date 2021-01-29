import static org.junit.Assert.*;

import org.junit.Test;

public class ErrorClassTests {

	@Test
	public void testArrayIndexOutOfBoundsException_5P() 
	{
		//to see what happens if a student class produces a ArrayIndexOutOfBoundsException
		ErrorClass e = new ErrorClass();
		assertTrue(e.produceArrayIndexOutOfBoundsException() == 2);
	}

	@Test
	public void testNullPointerException_5P()
	{
		//to see what happens if a student class produces a null Pointer exception
		ErrorClass e = new ErrorClass();
		assertTrue(e.produceNullPointerException());
	}
	
	@Test
	public void testWrongClassName_5P()
	{
		//intentionally wrong
		ErrorClas e = new ErrorClas();
	}
	
	@Test
	public void testWrongFunctionName_5P()
	{
		//intentionally wrong
		ErrorClass e = new ErrorClass();
		e.func();
	}
	
	@Test
	public void testAssertionError_5P()
	{
		assertTrue(false);
	}
	
	@Test
	public void testWorking_5P()
	{
		//working test
		assertTrue(true);
	}
	
	
}
