import java.util.Scanner;

/**
 * Example of a simple calculor. This class represents a typical student submission
 * @author gue
 *
 */
public class SimpleCalculator
{

	/**
	 * Starts the calculator
	 * @param args
	 */
	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);

		System.out.println("Welcome to the simple Calculator");
		System.out.println("================================");
		System.out.println("******* written by hg *********");		
		System.out.println("================================");
		System.out.println("OK lets go: 123");		
		
		System.out.println("Enter the first integer");
		int a = s.nextInt();
		
		System.out.println("Enter the operation [+, -, *]");
		String op = s.next();
		
		System.out.println("Enter the second integer");
		int b = s.nextInt();
		
		switch(op)
		{
			case "+":
				System.out.println("The sum of " + a + " + " + b + " = " + (a+b));
				break;
				
			case "-":
				System.out.println("The difference of " + a + " - " + b + " = " + (a-b));
				break;
							
			case "*":
				System.out.println("The multiplication of " + a + " * " + b + " = " + (a*b));
				break;
			
			default:
				System.out.println("I dont know what to do");
				break;
		}

		s.close();
		
		System.exit(111);
	}

}
