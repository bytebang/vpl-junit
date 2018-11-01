import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class VplGrader extends BlockJUnit4ClassRunner
{
	
	int sumPoints = 0;
	
	public VplGrader(Class<?> klass) throws InitializationError
	{
		super(klass);
	}
	
    /**
     * Returns the methods that run tests. Default implementation returns all
     * methods annotated with {@code @VplTestcase} on this class and superclasses that
     * are not overridden.
     */
	@Override
    protected List<FrameworkMethod> computeTestMethods() 
    {
        return getTestClass().getAnnotatedMethods(VplTestcase.class);
    }

	
	/**
	 * Creates the moodle output for the GradedTestcase-Annotated Methods
	 * 		Comment :=>> Test One: failure. 0 marks
			<|-- 
			expected:<3> but was:<5>
			 --|>
 
	 */
    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
    	
    	Statement s = super.methodInvoker(method, test);
    	VplTestcase testcasesettings = (VplTestcase) method.getAnnotation(VplTestcase.class);
    	Test testcase = (Test) method.getAnnotation(Test.class);
    	try 
    	{
    		if(testcase != null)
    		{
    			s.evaluate();
    			System.out.println("\nComment :=>> " + method.getDeclaringClass().getName() + "." + method.getName() + " ... success -> " + testcasesettings.points() + "/" + testcasesettings.points() + " Points");
    		}
		}
		catch (Throwable e) 
    	{
			// If we want this Exception then the testcase is fulfilled
			if(e.getClass() == testcase.expected())
			{
	        	System.out.println("\nComment :=>> " + method.getDeclaringClass().getName() + "." + method.getName() + " ... success threw expected exception of type " + testcase.expected().getSimpleName() + " -> " + testcasesettings.points() + "/" + testcasesettings.points() + " Points");
			}
			else
			{
				System.out.println("\nComment :=>> " + method.getDeclaringClass().getName() + "." + method.getName() + " ... failed with Error '" + e.getMessage() + "' -> 0/" + testcasesettings.points() + " Points");
				
				// Print the Stacktrace as comment
				if(testcasesettings.showFullStacktraceOnFailure() == true)
				{
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					System.out.println("<|--"); 
					System.out.println(sw.toString());
					System.out.println("--|>");
				}
				
				
			}
		}
    	return s;
    }
    
}
