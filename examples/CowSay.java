
/**
 * A simple class which should just greets somebody 
 * This is a typical example for java beginners to get started with escaped characters
 * @author gue
 */
public class CowSay
{

    /**
     * Main method of the cowsay program. Expects the name of the cow to be greeted as argument
     * @param args
     */
    public static void main(String[] args)
	{

    	if(args.length == 0)
		{
			System.out.println("Please give the name in the first argument");
			return;
		 } // Intentionally wrong intendation to trigger checkstyle

		System.out.println(" __________________");
		System.out.println("< Hello " + args[0] + " >");
		System.out.println(" ------------------");
		System.out.println("        \\   ^__^");
		System.out.println("         \\  (oo)\\_______");
		System.out.println("            (__)\\       )\\/\\");
		System.out.println("                ||----w |");
		System.out.println("                ||     ||");
		
		System.exit(111);
	}

}

