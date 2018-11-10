
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Tests the calculator class
 * @author gue
 *
 */
//Since we share the console over multiple tests we need a guaranteed order of test execution
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class CalculatorTests 
{
	
	static VplConsoleSimulator c;
	
    /**
     * Initializes the Calculator console
     */
    @BeforeClass
    public static void startup()
    {
    	CalculatorTests.c = new VplConsoleSimulator("SimpleCalculator");
    }
    
    /**
     * Expects the prompt to enter an operation
     * @throws IOException
     */
    @Test
    public void c_testOperatorInput_10P() throws IOException 
    {
    	assertTrue(c.expectOutput(a-> a.equals("Enter the operation [+, -, *]")));
    	c.enterLine("+");
    }
    
    /**
     * Expects to enter the second integer
     * @throws IOException
     */
    @Test
    public void c_testSecondIntegerInput_10P() throws IOException 
    {
    	assertTrue(c.expectOutput(a-> a.equals("Enter the second integer")));
    	c.enterLine("5");
    }
    
    /**
     * Checks if the result is OK
     * @throws IOException
     */
    @Test
    public void d_testSumResult_10P() throws IOException 
    {
    	assertTrue(c.expectOutput(a-> a.equalsIgnoreCase("THE SUM of 3 + 5 = 8")));
    }
    
    /**
     * Checks if the program terminated correctly
     * @throws IOException
     */
    @Test
    public void e_testExitValue_10P() throws IOException, InterruptedException 
    {
    	assertTrue(c.getExitValue() == 111);
    }
    
    /**
     * Does actually nothing. It just prints out the console so far.
     * @throws IOException
     */
    @Test
    public void f_showConsoleLog() throws IOException 
    {
        System.out.println(c.getFullConsoleIO());
    }
    
    /**
     * Independend Test to check if difference was implemented correctly
     * @throws IOException
     */
    @Test
    public void g_compactTestDifference_20P() throws IOException 
    {
    	VplConsoleSimulator con = new VplConsoleSimulator("SimpleCalculator");
    	
    	con.skipOutputTill(a-> a.equals("Enter the first integer"));
    	con.enterLine("6");
    	
    	con.skipOutputTill(a-> a.equals("Enter the operation [+, -, *]"));
    	con.enterLine("-");
    	
    	con.skipOutputTill(a-> a.equals("Enter the second integer"));
    	con.enterLine("2");
    	
    	assertTrue(con.expectOutput("The difference of 6 - 2 = 4"));
    	
    }
    
    /**
     * Independend Test to check if multiplication was implemented correctly
     * @throws IOException
     */
    @Test
    public void h_compactTestMultiplication_20P() throws IOException 
    {
    	VplConsoleSimulator con = new VplConsoleSimulator("SimpleCalculator");
    	
    	con.skipOutputTill(a-> a.equals("Enter the first integer"));
    	con.enterLine("6");
    	
    	con.skipOutputTill(a-> a.equals("Enter the operation [+, -, *]"));
    	con.enterLine("*");
    	
    	con.skipOutputTill(a-> a.equals("Enter the second integer"));
    	con.enterLine("3");
    	
    	assertTrue(con.expectOutput("The multiplication of 6 * 3 = 18"));
    	
    }
    
    /**
     * Independend Test to check if wrong operators are handeled correctly
     * @throws IOException
     */
    @Test
    public void i_compactTestWrongOperator_20P() throws IOException 
    {
    	VplConsoleSimulator con = new VplConsoleSimulator("SimpleCalculator");
    	
    	con.skipOutputTill(a-> a.equals("Enter the first integer"));
    	con.enterLine("6");
    	
    	con.skipOutputTill(a-> a.equals("Enter the operation [+, -, *]"));
    	con.enterLine("/");
    	
    	con.skipOutputTill(a-> a.equals("Enter the second integer"));
    	con.enterLine("3");
    	
    	assertTrue(con.expectOutput("I dont know what to do"));
    	
    }
}
