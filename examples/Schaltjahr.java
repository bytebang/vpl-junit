import java.util.Scanner;

public class Schaltjahr {

	public static void main(String[] args) 
	{
		System.out.println("Geben Sie die Jahreszahl ein:");
		Scanner sc = new Scanner(System.in);
		
		int year = Integer.parseInt(sc.nextLine());

		if (year%400 == 0 || (year%100!=0 && year%4==0))
		{
			System.out.println("Schaltjahr");
		}
		else
		{
			System.out.println("Kein Schaltjahr");
		}
	}
}
