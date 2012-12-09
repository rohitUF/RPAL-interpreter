    
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rohit
 */
public class StandardTree {
    
    // ast root node, after conversion root of the standard tree
    private Node ast;
    
    StandardTree()
    {
        ast = null;
    }
    
    // Start transformations
    public void let(Node node)
    {
        Node X = null;
        Node E = null;
        Node P = null;

        Node lam = new Node("lambda");
        if (!node.toString().equals("let"))
        {
                System.out.println("Should be let node, Error !");
                return;
        }

        node.setValue("gamma");

        if (!node.getChild(0).toString().equals("="))
        {
            System.out.println("left child of let is not =");
            return;
        }
        P = node.getChild(1);
        X = node.getChild(0).getChild(0);
        E = node.getChild(0).getChild(1);


        node.setChild(0, lam);
        node.setChild(1, E);
        lam.setChild(0, X);
        lam.setChild(1, P);                        
    }
    
    public void where(Node node)
    {
        Node X = null;
	Node E = null;
	Node P = null;
        
        Node lam = new Node("lambda");
        if (!node.toString().equals("where"))
        {
            System.out.println("Expected where node, ERROR");
            return;
        }
        node.setValue("gamma");
        if (!node.getChild(1).toString().equals("="))
        {
            System.out.println("right child of where is not =");
            return;
        }
        
        P = node.getChild(0);
        X = node.getChild(1).getChild(0);
        E = node.getChild(1).getChild(1);

        lam.setChild(0, X);
        lam.setChild(1, P);
        
        node.setChild(0, lam);
        node.setChild(1, E);
    }
    
    public void fcn_form(Node n)
    {
        if (!n.toString().equals("function_form"))
        {
            System.out.println("expected function form ERROR !");
            return;
        }
        ArrayList<Node>nChildren = new ArrayList<Node>(n.getChildren());
        
        n.setValue("=");        
        Node prev = n;        
        for (int i = 1; i < (nChildren.size() - 1); i++)
        {
            Node lamda = new Node("lambda");
            Node v = nChildren.get(i);
            lamda.setChild(0, v);
            prev.setChild(1, lamda);
            prev = lamda;
        }
        Node E = nChildren.get(nChildren.size() - 1);
        prev.setChild(1, E);
    }    
    
    public void within(Node n)
    {
        Node x1 = null;
        Node x2 = null;
        
        Node e1 = null;
        Node e2 = null;
        
        Node gamma = new Node("gamma");
        Node lambda = new Node("lambda");
        
        if (!n.toString().equals("within"))
        {
            System.out.println("within expected, ERROR!");
            return;
        }
        
        if (!n.getChild(0).toString().equals("="))
        {
            System.out.println("left child of within is not =");
            return;
        }
        

        x1 =  n.getChild(0).getChild(0);
        e1 = n.getChild(0).getChild(1);
        
        if (!n.getChild(1).toString().equals("="))
        {
            System.out.println("right child of within is not =");
            return;
        }
        
      
        x2 =  n.getChild(1).getChild(0);
        e2 = n.getChild(1).getChild(1);
    
         
         n.setValue("=");
         lambda.addChild(x1);
         lambda.addChild(e2);
         gamma.addChild(lambda);
         gamma.addChild(e1);
         
         n.setChild(0, x2);
         n.setChild(1, gamma);
    }
    
    public void rec(Node n)
    {
        Node x = null;
        Node e = null;
        
        Node gamma = new Node("gamma");
        Node lambda = new Node("lambda");
        Node ystar = new Node("<Y*>");
        if (!n.toString().equals("rec"))
        {
            System.out.println("Expected rec, ERROR !");
            return;
        }
        
        if (!n.getChild(0).toString().equals("="))
        {
            System.err.println("child of rec is not =");
            return;
        }
        
        x = n.getChild(0).getChild(0);
        e = n.getChild(0).getChild(1);
        Node xcopy = new Node(x);
            
        n.setValue("=");
        lambda.addChild(xcopy);
        lambda.addChild(e);
        gamma.addChild(ystar);
        gamma.addChild(lambda);
        n.setChild(0, x);
        n.addChild(gamma);
    }
    
    public void and(Node n)
    {
        if (!n.toString().equals("and"))
        {
            System.out.println("and expected, ERROR !");
            return;
        }
        
        ArrayList<Node>nChildren = new ArrayList<Node>(n.getChildren());
        Node comma = new Node(",");
        Node tau = new Node("tau");
        
        Node x = null;
        Node e = null;
        
        for (Node child : nChildren)
        {
            if (!child.toString().equals("="))
            {
                System.out.println("One of the children of and is not =");
                return;
            }
            x = child.getChild(0);
            e = child.getChild(1);
            
            comma.addChild(x);
            tau.addChild(e);
        }
        
        n.setValue("=");
        n.setChild(0, comma);
        n.setChild(1, tau);
        n.remExtraChildren();
    }
    
    public void at(Node n)
    {
        if (!n.toString().equals("@"))
        {
            System.out.println("expected @, ERROR !");
            return;
        }
        
        Node e1 = null;
        Node e2 = null;
        Node N = null;
        
        Node gamma = new Node("gamma");
        
        e1 = n.getChild(0);
        N = n.getChild(1);
        e2 = n.getChild(2);       
     
        
        n.setValue("gamma");
        n.setChild(0, gamma);
        n.setChild(1, e2);
        
        gamma.addChild(N);
        gamma.addChild(e1);
        n.remExtraChildren();        
    }
    
    public void convertNode(Node n)
    {
        switch(n.toString())
        {
            case "let":
                let(n);
                break;
                
            case "where":
                where(n);
                break;
                
            case "within":
                within(n);
                break;
                
            case "function_form":
                fcn_form(n);
                break;
                
            case "rec":
                rec(n);
                break;
                
            case "@":
                at(n);
                break;
                
            case "and":
                and(n);
                break;
                
            default:
                return;
        }
    }
    
    public void transform(Node n)
    {
        // perform post-order traversal
        for (Node c : n.getChildren())
        {
            transform(c);
        }
        
        convertNode(n);
    }
    
    public AST ASTtoSTConversion(AST ast)
    {
        transform(ast.getRoot());
        return ast;
    }
}
