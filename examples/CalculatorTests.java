
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;

//Since we share the console over multiple tests we need a guaranteed order of test execution
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class CalculatorTests 
{
    
    static VplConsoleSimulator c ;
    
    /**
     * Initializes the Calculator console
     */
    @BeforeClass
    public static void startup()
    {
        CalculatorTests.c = new VplConsoleSimulator("SimpleCalculator");
    }
    
    /**
     * Checks if the first non empty line is the banner
     * @throws IOException
     */
    @Test
    public void a_testBanner_10P() throws IOException 
    {
        String banner = c.getNextNonEmptyOutputLine();
        assertTrue(banner.equalsIgnoreCase("Welcome to the simple Calculator"));
    }
    
    /**
     * Igneores stuff up to the line which starts with 'OK lets go' and 
     * expectes the String 'Enter the first integer' afterwards
     * 
     * @throws IOException
     */
    @Test
    public void b_testFirstIntegerInput_10P() throws IOException 
    {
        c.skipOutputTill(a -> a.startsWith("OK lets go"));
        assertEquals(c.getNextNonEmptyOutputLine(), "Enter the first integer");
        c.enterLine("3");
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
        // Just check the result
        // assertTrue(c.expectOutput(a-> a.equalsIgnoreCase("THE SUM of 3 + 5 = 8")));
        
        // Check the result and give feedback
        assertTrue("THE RESULT IS WRONG " + System.getProperty("line.separator") + " GO AND CHECK YOUR CALCULATION", 
    			   c.expectOutput(a-> a.equalsIgnoreCase("THE SUM of 3 + 5 = 8")));
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
