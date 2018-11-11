import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a single Checkstyle violation
 * @author gue
 *
 */
public class StyleViolation
{
    private File file;
    private String line;
    private String message;
    private String type;
    
    private Pattern violation = Pattern.compile("\\[WARN\\] (.*):([0-9]{1,}):(.*) \\[(.*)\\]");
    
    /**
     * Private to prevent instantiation
     */
    private StyleViolation()
    {
        
    }

    /**
     * Stlye violations are built from the output of the checkstyle log
     * @param log
     */
    public static StyleViolation build(String logline)
    {
        StyleViolation sv = new StyleViolation();

        Matcher m = sv.violation.matcher(logline);
        if(m.matches())
        {
            sv.file = new File(m.group(1));
            sv.line = m.group(2);
            sv.message = m.group(3);
            sv.type = m.group(4);
            return sv;
        }
        
        return null;
    }

    public File getFile()
    {
        return file;
    }

    public String getLine()
    {
        return line;
    }

    public String getMessage()
    {
        return message;
    }

    public String getType()
    {
        return type;
    }

    public Pattern getViolation()
    {
        return violation;
    }
}
