import java.io.File;
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
import java.util.stream.Collectors;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;


/**
 * Tests a complete JUnit file.
 * @author hg
 */
public class VplJUnitTester extends org.junit.runner.notification.RunListener
{
	Map<String, Throwable> points = new LinkedHashMap<>();
	Map<String, List<StyleViolation>> deductions = new LinkedHashMap<>();

	Pattern pointregex = Pattern.compile(".*_(\\d{1,})P.*");

	/**
	 * Runs All JUnit Testcases with the annotation {@see VplTestcase} of all given classes.
	 *
	 * @param args Classes to run the tests against
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException
	{

	    VplJUnitTester st = new VplJUnitTester();
	    List<String> classesToRun = new ArrayList<>();

	    // STEP 1: Check for files that should be tested

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

	    // STEP 2: Run tests for all classes in the arguments.
	    System.out.println("Running JUnit tests");
        JUnitCore core = new JUnitCore();
        core.addListener(st);

	    for(String classname : classesToRun)
	    {
	        System.out.println("\t" + classname);
	    	core.run(Class.forName(classname));
	    }


	    // STEP 3: Run stylechecks against the sourcefiles
	    System.out.println("Running checkstyle");
        List<String> stylechecks = findStyleChecks();
        List<File> sourcefiles = findSourceFiles();
	    if(stylechecks.isEmpty() == false && sourcefiles.isEmpty() == false)
	    {
	        if(CheckstyleRunner.getCheckstyleExecutable().exists() == true)
	        {
        	    for(String check : stylechecks)
        	    {
                    System.out.println("\tCheck " + (new File(check)).getName() + " against " + sourcefiles.toString());
        	        List<StyleViolation> violations = CheckstyleRunner.run(check, sourcefiles);
        	        st.deductions.put(check, violations);
        	    }
	        }
	        else
	        {
	            System.out.println("Comment :=>> Cannot check for style violations because checkstyle was not found.");
	        }

	    }

	    // STEP 4: Summary for JUnit
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

	    // STEP 5: Summary for checkstyle
	    for(String check : st.deductions.keySet())
	    {
	        Integer max_deduction =  st.getDeductionsForCheckName(check);
	        List<StyleViolation> violations = st.deductions.get(check);
	        Integer drain = Math.min(max_deduction, violations.size());
	        String checkName = (new File(check)).getName();
	        if(drain == 0)
	        {
	            System.out.println("Comment :=>> " + checkName  + " ... no violations");
				continue;
	        }

	        System.out.println("Comment :=>> " + checkName  + " ... -" + drain + " Points because of " + violations.size() + " "
	            + (violations.size() == 1?"violation":"violations"));

	        // Reduce the points
	        totalPoints = totalPoints - drain;

	        // Inform the user
	        Map<String, List<StyleViolation>> violationsPerType = violations.stream()
	                .collect(Collectors.groupingBy(StyleViolation::getType));

	        // Give the user a hint of what went wrong
	        System.out.println("<|--");
	        for(String violationtype : violationsPerType.keySet())
	        {
	            List<StyleViolation> sv = violationsPerType.get(violationtype);
	            System.out.println(" *** " + violationtype + " (" + sv.size() + " " + (violations.size() == 1?"violation":"violations") + ") ***");

	            for(StyleViolation v : sv)
	            {
	                System.out.println("        o " + v.getFile().getName() + ":" + v.getLine() + " -> " + v.getMessage());
	            }
	        }
	        System.out.println("--|>");

	    }
	    System.out.println("\nGrade :=>> " + Math.max(totalPoints,0));
	}

    /**
     * Gives minus points for the check
     * @param check
     * @return
     */
    private Integer getDeductionsForCheckName(String check)
    {
	    Pattern p = Pattern.compile(".*-([0-9]{1,})P.xml");

	    Matcher m = p.matcher(check);

        if(m.matches())
        {
            String points = m.group(1);
            return Integer.parseInt(points);
        }

        return Integer.MAX_VALUE;
    }

    /**
     * Searches checkstyle_files.
     * @return
     */
    private static List<String> findStyleChecks()
    {
	    List<String> foundChecks = new ArrayList<String>();
	    try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("."), "checkstyle*.xml"))
        {
            for (Path entry : stream)
            {
                foundChecks.add(entry.toAbsolutePath().normalize().toString());
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return foundChecks;
    }

    /**
     * Searches checkstyle_files.
     * @return
     */
    private static List<File> findSourceFiles()
    {
        List<File> foundSources = new ArrayList<>();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("."), "*.java"))
        {
            for (Path entry : stream)
            {
                // Ignore everything that looks like a JUnit-test
                if(entry.getFileName().toString().matches(".*[Tt]est[s]?.java") == false)
                {
                    foundSources.add(entry.toFile());
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return foundSources;
    }

    /**
     * Searches Classes which look like testclasses in the directory
     * @param string
     * @return
     */
    public static List<String> findTestClasses()
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
