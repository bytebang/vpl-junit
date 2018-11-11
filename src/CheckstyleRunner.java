import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enables a user to run a locally installed checkstyle against a bunch of files.
 * @author gue
 *
 */
public class CheckstyleRunner
{

    /**
     * Runs checkstyle
     * 
     * @param checkstyleconfig
     * @param filesToCheck
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static List<StyleViolation> run(String checkstyleconfig, List<File> filesToCheck) throws IOException, InterruptedException
    {
        List<StyleViolation> violations = new ArrayList<>();

        // Find the executable
        File cs = getCheckstyleExecutable();

        for (File srcfile : filesToCheck) 
        {
            // Get the name of the File which corresponds to the classname
            String[] cmdline = { cs.toString(), "-c", checkstyleconfig, srcfile.getAbsolutePath().toString() };

            Process process = new ProcessBuilder(cmdline).start(); // Go for it !

            // Wait till ended
            process.waitFor();

            // Get the input and output streams
            BufferedReader inReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), Charset.defaultCharset()));

            String line;
            boolean inAudit = false;
            while ((line = inReader.readLine()) != null) 
            {
                if (line.equalsIgnoreCase("Starting audit...")) 
                {
                    inAudit = true;
                    continue;
                }

                if (line.equalsIgnoreCase("Audit done.")) 
                {
                    inAudit = false;
                    continue;
                }

                if (inAudit == true) 
                {
                    StyleViolation sv = StyleViolation.build(line);
                    if (sv != null) 
                    {
                        violations.add(sv);
                    }
                }
            }
        }

        return violations;
    }

    /**
     * Finds the executable checkstyle by traversing the path environment variable
     * 
     * @return
     */
    public static File getCheckstyleExecutable()
    {
        List<String> paths = Arrays.asList(System.getenv("PATH").split(":"));

        for (String path : paths) 
        {
            File cs = new File(path + File.separator + "checkstyle");
            if (cs.exists() && cs.canExecute()) 
            {
                return cs;
            }
        }
        return null;
    }

}
