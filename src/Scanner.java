/********* PLP FALL 2012, RPAL Parser
 * ROHIT CHAUHAN
 * 8858-9136
 * */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.*;
import java.io.FileInputStream;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.MappedByteBuffer;
import java.nio.charset.Charset;

/**
 *
 * @author Rohit
 */


public class Scanner {
    
    private LineConvertor lc;
    private java.util.Scanner sc;    
    private String srcPath;
    
    public Scanner(String filename)
    {
        srcPath = filename;
    }   
        
    public ArrayList<Token> GetAllTokens(String filename) throws IOException
    {
        ArrayList<Token> tokens = new ArrayList<Token>();        
        String [] lines = readFile(filename).split("\n");
        Token tk;
        
        for (String line : lines)
        {
            lc = new LineConvertor(line);
            
            while (lc.hasNext())
            {
                tk = new Token((String)lc.next());
                if (tk.Type() == TokenType.COMMENT || tk.Type() == TokenType.SPACE)
                {
                    continue;
                }
                tokens.add(tk);
            }
        }
        
        return tokens;
    }
    
    private String readFile(String path) throws IOException
    {       
        FileInputStream stream = new FileInputStream(new File(path));
       try 
       {           
           FileChannel fc = stream.getChannel();
           MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

           return Charset.defaultCharset().decode(bb).toString();
       }
       finally
       {
           stream.close();
       }
}
}

