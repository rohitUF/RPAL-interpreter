/********* PLP FALL 2012, RPAL Parser
 * ROHIT CHAUHAN
 * 8858-9136
 * */


import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
/**
 *
 * @author Rohit
 */
public class p1 {
    public static void main(String[] args) throws IOException
    {
        if (args.length != 2)
        {
            //System.out.println("Please provide src file name");
            return;
        }
        
        String switches = args[0];
        String srcFile = args[1];
        if (switches.equals("-l"))
        {
            printsource(srcFile);
            return;
        }
        
        else if (switches.equals("-ast"))
        {
            AST ast = getAST(srcFile);
            System.out.println(ast.toString());
        }
        
        else if (switches.equals("-st"))
        {
            AST st = getST(getAST(srcFile));
            System.out.println(st.toString());
        }
    }
    
    private static AST getAST(String srcFile) throws IOException
    {
        Scanner sc = new Scanner(srcFile);

        ArrayList<Token> tokens = sc.GetAllTokens(srcFile);

        Parser parser = new Parser();
        AST ast = parser.StartParsing(tokens);
        return ast;
    }
    
    private static AST getST(AST ast)
    {
        StandardTree st = new StandardTree();
        AST s = st.ASTtoSTConversion(ast);
        return s;
    }
    
    private static void printsource(String path) throws IOException
    {
        BufferedReader in = new BufferedReader(new FileReader(path)); 
        String text;
        while (in.ready())
        { 
            text = in.readLine(); 
            System.out.println(text); 
        }
        in.close();
    }
}
