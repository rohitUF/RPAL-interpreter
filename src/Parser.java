/********* PLP FALL 2012, RPAL Parser
 * ROHIT CHAUHAN
 * 8858-9136
 * */

import java.util.*;

/**
 *
 * @author Rohit
 */
public class Parser {
    private Scanner sc;
    private AST ast;
    private Token tk;
    ArrayList<Token> tokens;
    
    public Parser()
    {        
        ast  = new AST();               
    }
    
    public AST StartParsing(ArrayList<Token> tokens)
    {
        this.tokens = tokens;
        // start with expression E
        E();
        return ast;
    }
    
    private Token GetNextToken()
    {
        if (tokens.size() > 0)
        {
          /*  String s = tokens.get(0).Value();
            if (s.equals("Print"))
            {
                System.out.println("Got in tokens");
            }*/
            return tokens.get(0);            
        }
        
        else
        {
            return new Token("EOF");
        }
    }
    
    private Token GetNextToken(int i)
    {
        if (tokens.size() > i)
        {
            return tokens.get(i);            
        }
        
        else
        {
            return new Token("EOF");
        }
    }
    
    private void read()
    {
        tokens.remove(0);
    }
    
    /* E-> 'let' D 'in' E  => 'let'
     *  -> 'fn' Vb+ '.' E => 'lambda'
     *  -> Ew;
     */
    private void E()
    {        
        switch(GetNextToken().Value())
        {
            case "let":
                read();
                D();
                
                if (GetNextToken().Value().equals("in"))
                {
                   read();
                   E();
                   ast.buildTree("let", 2);
                }
                else
                {
                    System.out.println("In expected after let but found " + GetNextToken().Value() + "Exiting");
                    System.exit(0);
                }
                break;
                
            case "fn":          
                read();
                int vbNums = 0;                
                while (GetNextToken().IsVbToken())
                {
                    vbNums++;
                    Vb();                    
                }
                
                if (GetNextToken().Value().equals("."))
                {            
                    read();
                    E();
                    ast.buildTree("lambda", 1+vbNums);
                }
                
                else
                {
                    System.out.println(". expected " + GetNextToken().Value() + " Exiting");
                    System.exit(0);
                }
                break;
                
            default:
                Ew();        
        }
    }
    
    private void Ew()
    {
        T();
        if (GetNextToken()!= null && GetNextToken().Value().contains("where"))
        {
            read();
            Dr();
            ast.buildTree("where", 2);
        }
    }
    
    private void D()
    {
        Da();        
        if (GetNextToken().Value().equals("within"))
        {            
            read();
            D();
            ast.buildTree("within", 2);
        }
    }
    
    private void Da()
    {
        int andNums = 0;
        Dr();        
        while(GetNextToken().Value().equals("and"))
        {
            andNums++;
            read();
            Dr();            
        }
        
        if (andNums > 0)
        {
            ast.buildTree("and", andNums+1);
        }        
    }
    
    private void Dr()
    {        
        if(GetNextToken().Value().equals("rec"))
        {           
            read();
            Db();
            ast.buildTree("rec", 1);
        }
        else
        {
            Db();
        }
    }
    
    private void Db()
    {        
        if (VlNext())
        {
            Vl();
            read();
            E();
            ast.buildTree("=", 2);
        }
        
        else if (GetNextToken().Type() == TokenType.IDENTIFIER) 
            {
                // fcn_form case
                ast.pushNode("<ID:" + GetNextToken().Value() + ">");
                
                // Mistake number 3
                read();
                //end mistake
                
                int vbNums = 0;
                while (GetNextToken().IsVbToken())
                {
                    vbNums++;
                    Vb();                    
                }
                
                if (GetNextToken().Value().equals("="))
                {
                    read();
                    E();
                    ast.buildTree("function_form", 2+vbNums);
                }
                
                else
                {
                    System.out.println("Should get an equals after fcn_form, Exiting");
                    System.exit(0);
                }
            }
                
        // Mistake number 2
        
      /*  else if (GetNextToken().Value().equals("("))
        {
            D();
            read();
           
            if (!(GetNextToken().Value().equals(")")))
            {
                System.out.println("Should get a closed parenthesis, Exiting");
                System.exit(0);
            }
        } */
        
        // end mistake
    }
    
    private boolean VlNext()
    {
        if(GetNextToken().Type() == TokenType.IDENTIFIER)
        {
            if(GetNextToken(1).Value().equals("=") || GetNextToken(1).Value().equals(","))
            {
                return true;
            }
        }
        return false;
    }
    
    private void Vl()
    {
        int comNum = 0;
        
        if (GetNextToken().Type() == TokenType.IDENTIFIER)
        {
            while (GetNextToken().Type() == TokenType.IDENTIFIER)
            {
                ast.pushNode("<ID:" + GetNextToken().Value() + ">");
                read();
                
                if (GetNextToken().Value().equals(","))
                {
                    read();
                    comNum++;
                    
                    if (GetNextToken().Type() != TokenType.IDENTIFIER)
                    {
                        System.out.println("Should have at least one id after comma");
                        System.exit(0);
                    }
                }
                else
                {
                    break;
                }
            }
            
            if (comNum > 0)
            {
                ast.buildTree(",", comNum + 1);          
            }
        }
        else
        {
            System.out.println("No Identifier found");
            System.exit(0);
        }
    }
    
    
    
    private void Vb()
    {
        if (GetNextToken().Value().equals("("))
        {
            read();
            
            if (GetNextToken().Value().equals(")"))
            {
                read();
                ast.pushNode("()");
            }
            
            else
            {
                Vl();
                if (GetNextToken().Value().equals(")"))
                {
                    read();
                }
                
                else
                {
                    System.out.println("No closing paranthesis");
                    System.exit(0);
                }
                
            }
        }
        else if (GetNextToken().Type() == TokenType.IDENTIFIER)
        {
            ast.pushNode("<ID:" + GetNextToken().Value() + ">");
            read();
        }
    }
    
    private void T()
    {
        Ta();
        int comNum = 0;
        while(GetNextToken().Value().equals(","))
        {
            comNum++;
            read();
            Ta();
        }
        
        if (comNum > 0)
        {
            ast.buildTree("tau", comNum+1);
        }
        
    }
    
    private void Ta()
    {
        Tc();
        while (GetNextToken().Value().equals("aug"))
        {
            read();
            Tc();
            ast.buildTree("aug", 2);
        }
    }
    
    private void Tc()
    {
        B();
        if (GetNextToken().Value().equals("->"))
        {
            read();
            Tc();
            
            if (GetNextToken().Value().equals("|"))
            {
                read();
                Tc();
                ast.buildTree("->", 3);
            }  
        else
            {
                System.out.append("Cant find a | conditional");
                System.exit(0);
            }
        }       
    }
    
    private void B()
    {
        Bt();
        
        while (GetNextToken().Value().equals("or"))
        {
            read();
            Bt();
            ast.buildTree("or", 2);
        }
    }
    
    private void Bt()
    {
        Bs();
        while (GetNextToken().Value().equals("&"))
        {
            read();
            Bs();
            ast.buildTree("&", 2);
        }
    }
    
    private void Bs()
    {
        if (GetNextToken().Value().equals("not"))
        {
            read();
            Bp();
            ast.buildTree("not", 1);
        }
        
        else
        {
            Bp();
        } 
    }
    private void Bp()
    {
        A();
        Token t = GetNextToken();
        if (t.Value().equals("gr") || t.Value().equals(">") || t.Value().equals("ge") || t.Value().equals(">=") ||
                t.Value().equals("ls") || t.Value().equals("<") || t.Value().equals("le") || t.Value().equals("<=") ||
                t.Value().equals("eq") || t.Value().equals("ne"))
        {
            read();
            A();
            ast.buildTree(t.Value(), 2);
        }
    }
    
    private void A()
    {
        if (GetNextToken().Value().equals("-"))
        {
            read();
            At();
            ast.buildTree("neg", 1);
        }
        
        else if (GetNextToken().Value().equals("+"))
        {
            read();
            At();
        }
        
        else
        {
            At();
        }
        
        while(GetNextToken().Value().equals("-") || GetNextToken().Value().equals("+"))
        {
            Token t = GetNextToken();
            read();
            
            At();
            ast.buildTree(t.Value(), 2);
        }
    }
    
    private void At()
    {
        Af();
        while (GetNextToken().Value().equals("*") || GetNextToken().Value().equals("/"))
        {
            Token t = GetNextToken();
            read();
            
            Af();
            ast.buildTree(t.Value(), 2);
        }
    }
    
    private void Af()
    {
        Ap();
        if (GetNextToken().Value().equals("**"))
        {
            read();
            Af();
            ast.buildTree("**", 2);
            
        }
    }
    
    private void Ap()
    {
        R();
        while (GetNextToken().Value().equals("@"))
        {
            read();
            if (GetNextToken().Type() == TokenType.IDENTIFIER)
            {
                ast.pushNode("<ID:"+GetNextToken().Value()+">");
                read();
                R();
                ast.buildTree("@", 3);
            }
        }
    }
    
    private void R()
    {
        Rn();
        Token tk = GetNextToken();
        while (tk.isRn())
        {
            Rn();
            ast.buildTree("gamma", 2);
            tk = GetNextToken();
        }
    }
    
    private void Rn()
    {
        Token t = GetNextToken();
        if (t.Value().equals("("))
        {
            read();
            E();
            if (!(GetNextToken().Value().equals(")")))
            {
                System.out.println("Could not get close parenthesis");
                System.exit(0);
                        
            }
            else
            {
                read();        
            }
            return;
        }
        
        if (t.Value().equals("true") || t.Value().equals("false") || t.Value().equals("nil") || t.Value().equals("dummy") ||
               t.Type() == TokenType.IDENTIFIER || t.Type() == TokenType.INTEGER || t.Type() == TokenType.STRING)
        {
            read();
            if (t.Type() == TokenType.IDENTIFIER)
            {
                ast.pushNode("<ID:"+t.Value()+">");
            }
            
            else if (t.Type() == TokenType.INTEGER)
            {
                ast.pushNode("<INT:"+t.Value()+">");
            }
            
            else if (t.Type() == TokenType.STRING)
            {
                ast.pushNode("<STR:"+t.Value()+">");
            }
            
            else if ( t.Value().equals("dummy"))
            {
                ast.pushNode("<dummy>");
            }
            
            // Mistake number 1
            
            else if (t.Value().equals("true"))
            {
                ast.pushNode("<true>");
            }
            
            else if (t.Value().equals("false"))
            {
                ast.pushNode("<false>");
            }
            
            // end mistake
            
            else if ( t.Value().equals("nil"))
            {
                ast.pushNode("<nil>");
            }
        }
        
        else
        {
           System.exit(0);
        }
    }       
}
