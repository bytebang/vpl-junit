
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

public class CowSayTests
{

	static List<String> correctCow = new ArrayList<>();

	@Before
	public void setup()
	{
		correctCow.clear();
		correctCow.add(" __________________");
		correctCow.add("< Hello HTL Leoben >");
		correctCow.add(" ------------------");
		correctCow.add("        \\   ^__^");
		correctCow.add("         \\  (oo)\\_______");
		correctCow.add("            (__)\\       )\\/\\");
		correctCow.add("                ||----w |");
		correctCow.add("                ||     ||");

	}

	/**
	 * Tests if the absence of an argument is handeled correctly
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
    public void testReactToNoParam_10P() throws IOException, InterruptedException 
    {
		// Start the CowSay class without parameter
		VplConsoleSimulator con = new VplConsoleSimulator("CowSay");

		if(con.getExitValue() != 0)
		{
			System.out.println(con.getError().stream().collect(Collectors.joining("\r\n")).toString());
			fail("Program did not exit with error code 0");
		}
		
		assertTrue(con.expectOutput("Please give the name in the first argument"));
    }
	
	/**
	 * Tests if the coq greets whomever we choose in the first argument
	 * @throws IOException
	 */
	@Test
    public void testReactToParam_10P() throws IOException 
    {
		VplConsoleSimulator con = new VplConsoleSimulator("CowSay", "OtherCow");
		
		List<String> actualCow = con.getOutput();
		
		boolean found = false;
		for(String s : actualCow)
		{
			if(s.contains("Hello OtherCow"))
			{
				found = true;
				break;
			}
		}
		assertTrue("You do not greet !", found);
    }
	
	/**
	 * Tests if the cow looks like a cow
	 * @throws IOException
	 */
	@Test
    public void testLooksLikeCow_80P() throws IOException 
    {
		VplConsoleSimulator con = new VplConsoleSimulator("CowSay", "HTL Leoben");
		
		List<String> actualCow = con.getOutput();
		assertTrue("Cow is not drawn correctly", actualCow.equals(correctCow));
		
    }

}
