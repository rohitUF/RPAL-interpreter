/********* PLP FALL 2012, RPAL Parser
 * ROHIT CHAUHAN
 * 8858-9136
 * */

public class Token {    
    private TokenType type;
    private String value;
    private boolean active = true;
    
    public TokenType Type() { return type; }
    public String Value() { return value; }
    public void read() { active = false; }
    public boolean isActive() { return active; }
    public Token (String rawToken)
    {
        value = rawToken;
        if (value.equals("EOF"))
        {
            type = TokenType.EOF;
        }
        
        else if (IsKeyword(rawToken))
        {
            type = TokenType.KEYWORD;
        }
        
        else if (IsSpace(rawToken))
        {
            type = TokenType.SPACE;
        }
        
        else if (IsComment(rawToken))
        {
            type = TokenType.COMMENT;
        }
        
        else if (IsStringLiteral(rawToken))
        {
            type = TokenType.STRING;
        }
        
        else if (IsOperator(rawToken))
        {
            type = TokenType.OPERATOR;
        }
        
        else if (IsInteger(rawToken))
        {
            type = TokenType.INTEGER;
        }
        
        else if (IsPunction(rawToken))
        {
            type = TokenType.PUNCTION;
        }
        
        else
        {
           // if nothing else, then it should be an identifier
            type = TokenType.IDENTIFIER;
        }
    }
    
    private boolean IsKeyword(String s)
    {
        return (
                s.equals("let") || s.equals("in") || s.equals("where") || s.equals("fn") ||
                s.equals("aug") || s.equals("or") || s.equals("not") || s.equals("gr") ||
                s.equals("ge") || s.equals("ls") || s.equals("le") || s.equals("eq") ||
                s.equals("ne") || s.equals("true") || s.equals("false") || s.equals("nil") ||
                s.equals("dummy") || s.equals("within") || s.equals("and") || s.equals("rec")
                );        
    }
    
    private boolean IsOperator(String s)
    {
        return (
                s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") ||
                s.equals("<") || s.equals(">") || s.equals("&") || s.equals(".") ||
                s.equals("->") || s.equals("=") || s.equals(">=") || s.equals("@") ||
                s.equals("|") || s.equals("<=") || s.equals("**")
                );
    }
    
    private boolean IsPunction(String s)
    {
        return (
                s.equals("(") || s.equals(")") || s.equals(",")  || s.equals(";")
                );
    }
    
    private boolean IsSpace(String s)
    {
        return s.matches("\\s") || s.equals("");
    }
    
    private boolean IsStringLiteral(String s)
    {
        return s.startsWith("'") && s.startsWith("'");
    }
    
    private boolean IsComment(String s)
    {
        return s.startsWith("//");
    }
    
    private boolean IsInteger(String s)
    {
        boolean res;
        try {
            int i = Integer.parseInt(s);
            res = true;
        }
        catch (NumberFormatException e)
        {
            res = false;
        }
        return res;
    }
    
    public boolean IsVbToken()
    {
        if (type == TokenType.IDENTIFIER || value.equals("("))
        {
            return true;
        }
        
        return false;
    } 
    
    public boolean isRn()
    {
        if (value.equals("true") || value.equals("false") || value.equals("nil") || value.equals("dummy") || value.equals("(") ||
                type == TokenType.IDENTIFIER || type == TokenType.INTEGER || type == TokenType.STRING)
        {
            return true;
        }
        
        return false;
    }
}
