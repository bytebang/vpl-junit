import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;


/**
 * Tests a complete JUnit file
 * @author hg
 */
public class VplJUnitTester extends org.junit.runner.notification.RunListener
{
	Map<String, Throwable> points = new LinkedHashMap<>();
	
	Pattern pointregex = Pattern.compile(".*_(\\d{1,})P.*");
	/**
	 * Runs All JUnit Testcases with the annotation {@see VplTestcase} of all given classes
	 *   
	 * @param args Classes to run the tests against
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException
	{
	    JUnitCore core= new JUnitCore();
	    VplJUnitTester st = new VplJUnitTester();
	    core.addListener(st);
	    
	    List<String> classesToRun = new ArrayList<>();
	    
	    // No args -> Check all Classes in the current directory
	    if(args.length == 0)
	    {
	    	// Look for classes in the current directory
	    	classesToRun.addAll(findTestClasses());
		    
	    	// Santiy check
		    if(classesToRun.isEmpty())
		    {
		    	System.out.println("There are no classes in the directory " + Paths.get(".").toAbsolutePath() + " which could be tested !");
		    	System.out.println(" Option A: Hand over the classes which should be tested as arguments to the jar");
		    	System.out.println(" Option B: Ensure that your testclasses names are endig with  'Tests'  (eg. SimpleTests)");
		    	System.out.println(" Or have a look into the documentation: https://github.com/bytebang/vpl-junit");
		    	return;
		    }
	    }
	    else
	    {
	    	classesToRun.addAll(Arrays.asList(args));
	    }

	    
	    // Run tests for all classes in the arguments.
	    for(String classname : classesToRun)
	    {
	    	core.run(Class.forName(classname));
	    }
	    
	    // Summary for VPL
	    int totalPoints = 0;
	    for(String functionname : st.points.keySet())
	    {
	    	Throwable t = st.points.get(functionname);
	    	Integer points =  st.getPointsForFunctionName(functionname);

		if(points == null) // Testcase without points
		{
			continue;
		}

	    	if(t == null) // No Excaption -> Test has succeeded
	    	{
	    		totalPoints += points;
	    		System.out.println("Comment :=>> " + functionname + " ... success -> " + points + " Points");	
	    	}
	    	else
	    	{
	    		System.out.println("Comment :=>> " + functionname + " ... failed -> 0 Points because " + t.getMessage());
	    	}
	    }

	    System.out.println("\nGrade :=>> " + totalPoints);
	}

	/**
	 * Searches Classes which look like testclasses in the directory
	 * @param string
	 * @return
	 */
	private static List<String> findTestClasses()
	{
		List<String> foundClasses = new ArrayList<String>();

		try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("."), "*.class"))
        {
	        for (Path entry : stream) 
	        {
	        	String classFilename = entry.getFileName().toString();
	        	if(classFilename.matches(".*[Tt]est[s]?.class"))
	        	{
	        		foundClasses.add(classFilename.substring(0, classFilename.lastIndexOf(".class")));
	        	}
	        }
        }
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return foundClasses;
	}
	
	/**
	 * Returns the Points for a given function name.
	 * If the function contains no hint for points then 0 is returned.
	 * @param functionName
	 * @return
	 */
	public Integer getPointsForFunctionName(String functionName)
	{
		Matcher m = pointregex.matcher(functionName);
		if(m.matches())
		{
			String points = m.group(1);
			return Integer.parseInt(points);
		}
		
		return null;
	}
	
	/**
	 * Adds the points of the current test to the total sum of points
	 */
	@Override
	public void testFinished(Description description) throws Exception
	{
		String fnName = description.getTestClass().getName() + "." + description.getMethodName();
		this.points.putIfAbsent(fnName,  null);
	}
	
	/**
	 * If the test fails, then we subtract the points from the total sum
	 */
	@Override
	public void testFailure(Failure failure) throws Exception
	{
		String fnName = failure.getDescription().getTestClass().getName() + "." + failure.getDescription().getMethodName();
		this.points.putIfAbsent(fnName,  failure.getException());
	}
}
