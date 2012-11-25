/********* PLP FALL 2012, RPAL Parser
 * ROHIT CHAUHAN
 * 8858-9136
 * */

import java.util.*;
 
public class Node
{ 
    private Node parent;
    private ArrayList<Node> children = new ArrayList<Node>();	
	//private Node left;
	//private Node right;
    private String value = null;
	//private boolean bVal;
 
    public Node(String nodeName)
    {
        value = nodeName;
    }
    
    public Node(Node n)
    {
        value = n.toString();
        this.setParent(n.getParent());
        children.addAll(n.getChildren());
    }
    	
    public void setValue(String val)
    {
            value = val;
    }

    public Node getChild(int num)
    {
            if (num < children.size())
            {
                    return children.get(num);
            }

            return null;
    }

    public void setChild(int num, Node n)
    {
            if (num < children.size())
            {
                children.set(num, n);
                n.setParent(this);
            }

            else
            {
                    //System.out.println("Trying to set " + n +"th node  but only " + children.size() + " nodes exist");
                    while(children.size() < num)
                    {
                        children.add(null);
                    }

                    children.add(n);
                    n.setParent(this);
            }		
    }
    
    public void remExtraChildren()
    {
        if (children.size() > 2)
        {
            children.subList(2, children.size()).clear();
        }
    }
        
 
    public void addChild(Node n)
    {
        n.setParent(this);
        children.add(n);
    }
 
    public void removeChild(Node n)
    {
        children.remove(n);
    }
 
    public ArrayList<Node> getChildren()
    {
        return children;
    }
    
    public String toString()
    {
        return value;
    }
    
    public String traverse()
    {
        return traverseTree(this, "");
    }
    
    private String traverseTree(Node n, String prevNodeStr)
    {
        StringBuffer sb = new StringBuffer();        
        sb.append(prevNodeStr + n.toString() + " \n");
        
        for(Node child : n.getChildren()) 
        {
            sb.append(traverseTree(child, prevNodeStr+"."));
        }
        return sb.toString();
    }
    
    public void setParent(Node n)
    {        
        if(parent != null)
        {
            removeFromParent();
        }
        parent = n;
    }
   
    public void removeFromParent()
    {
        if(parent!=null)
        {
            parent.removeChild(this);
        }
        parent = null;      
    }
 
    public Node getParent()
    {
        return parent;
    }
}