/********* PLP FALL 2012, RPAL Parser
 * ROHIT CHAUHAN
 * 8858-9136
 * */

/**
 *
 * @author Rohit
 */
public enum TokenType
{
    KEYWORD { public String toString() { return "KEYWORD"; } },
    IDENTIFIER { public String toString() { return "IDENTIFIER"; } },
    INTEGER { public String toString() { return "INTEGER"; } },
    OPERATOR { public String toString() { return "OPERATOR"; } },
    STRING { public String toString() { return "STRING"; } },
    PUNCTION { public String toString() { return "PUNCTION"; } },
    SPACE { public String toString() { return "SPACE"; } },
    COMMENT { public String toString() { return "COMMENT"; } },
    EOF
}
