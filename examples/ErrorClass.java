
public class ErrorClass 
{
	public int produceArrayIndexOutOfBoundsException()
	{
		int [] arr = {1,2};
		return arr[2];
	}
	
	public boolean produceNullPointerException()
	{
		return getString().contains("test");
	}
	
	private String getString()
	{
		return null;
	}
	
}
