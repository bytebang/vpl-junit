import java.util.Scanner;

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
	
	public static void main(String[] args) 
	{
		System.out.println("Erste Zeile");
		Scanner sc = new Scanner(System.in);
		String in = sc.nextLine();
		System.out.println("test");
		sc.close();
	}
	
}
