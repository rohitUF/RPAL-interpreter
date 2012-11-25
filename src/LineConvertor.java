/********* PLP FALL 2012, RPAL Parser
 * ROHIT CHAUHAN
 * 8858-9136
 * */

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
/**
 *
 * @author Rohit
 */
public class LineConvertor implements Iterator {
    
    private static String TokensPattern = "((//).*)|[a-zA-Z](\\w|_)*|(\\*\\*)|\\->|'(\\\\'|[^'])*'|(\\d)*";
    private Matcher matcher;
    private String marker;
    private String hits;
    private String line;
    private int lastPos = 0;
    
    public LineConvertor(String line)
    {
        this.line = line;
        Pattern p = Pattern.compile(TokensPattern);
        matcher = p.matcher(line);
    }
    
    public Object next()
    {
        String out = null;
        
        if (marker != null)
        {
            out = marker;
            marker = null;
        }
        
        else if (hits != null)
        {
            out = hits;
            hits = null;
        }
        
        return out;
    }
    
    public boolean isNext()
    {
        if (marker != null && hits == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public boolean hasNext()
    {
        if (matcher == null)
        {
            return false;
        }
        
        if (marker != null || hits != null)
        {
            return true;
        }
        
        if (matcher.find())
        {
            marker = line.substring(lastPos, matcher.start());
            hits = matcher.group();
            lastPos = matcher.end();
        }
        
        else if (lastPos < line.length())
        {
            marker = line.substring(lastPos, line.length());
            matcher = null;
            lastPos = line.length();
        }
        
        return marker != null || hits != null;
    }   
    
    public void remove()
    {
        // no-op
    }
}
