import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;


/**
 * Tests a complete JUnit file
 * @author hg
 */
public class VplJUnitTester extends org.junit.runner.notification.RunListener
{
	int totalPoints  = 0;
	
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
	    
	    // Summery for VPL
	    System.out.println("\nGrade :=>> " + st.totalPoints);
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
	 * Adds the points of the current test to the total sum of points
	 */
	@Override
	public void testFinished(Description description) throws Exception
	{
		VplTestcase myTestcase = (VplTestcase) description.getAnnotation(VplTestcase.class);
		totalPoints += myTestcase.points();
	}
	
	/**
	 * If the test fails, then we subtract the points from the total sum
	 */
	@Override
	public void testFailure(Failure failure) throws Exception
	{
		VplTestcase vplTestcase= (VplTestcase) failure.getDescription().getAnnotation(VplTestcase.class);
		totalPoints -= vplTestcase.points();
	}
}
