import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class SchaltjahrTests {

	@Test
	public void test_1996_5P() throws IOException 
	{
		VplConsoleSimulator c = new VplConsoleSimulator("Schaltjahr");
		
		c.getNextNonEmptyOutputLine();
		c.enterLine("1996");
		
		c.assertOutput(line -> line.equals("Schaltjahr"), consoleIo -> consoleIo += "\n 1996 % 4 == 0" );
		
		//c.assertOutput("Schaltjahr");
	}

	@Test
	public void test_2000_5P() throws IOException 
	{
		VplConsoleSimulator c = new VplConsoleSimulator("Schaltjahr");
		
		c.getNextNonEmptyOutputLine();
		c.enterLine("2000");
		
		c.assertOutput("Schaltjahr");
		//c.assertOutputContains("Schaltjahr");
	}
	
	@Test
	public void test_1900_5P() throws IOException 
	{
		VplConsoleSimulator c = new VplConsoleSimulator("Schaltjahr");
		
		c.getNextNonEmptyOutputLine();
		c.enterLine("1900");
		
		c.assertOutput("Kein Schaltjahr");
	}

	
	@Test
	public void test_1993_5P() throws IOException 
	{
		VplConsoleSimulator c = new VplConsoleSimulator("Schaltjahr");
		
		c.getNextNonEmptyOutputLine();
		c.enterLine("1993");
		
		c.assertOutput("Kein Schaltjahr");
	}

	@Test
	public void test_1790_5P() throws IOException 
	{
		VplConsoleSimulator c = new VplConsoleSimulator("Schaltjahr");
		
		c.getNextNonEmptyOutputLine();
		c.enterLine("1790");
		
		c.assertOutput("Kein Schaltjahr");
	}
	
	@Test
	public void test_1820_5P() throws IOException 
	{
		VplConsoleSimulator c = new VplConsoleSimulator("Schaltjahr");
		
		c.getNextNonEmptyOutputLine();
		c.enterLine("1820");
		
		c.assertOutput("Schaltjahr");
	}
	
}
