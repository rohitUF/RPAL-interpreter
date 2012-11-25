/********* PLP FALL 2012, RPAL Parser
 * ROHIT CHAUHAN
 * 8858-9136
 * */

import java.util.*;
/**
 *
 * @author Rohit
 */
public class AST {    
    public void buildTree(String nodeName, int numChild)
    {
        Node newNode = new Node(nodeName);
        ArrayList<Node> children = new ArrayList<Node>();
        
      //  System.out.println("Adding node = "+nodeName + " with number of children = " + numChild);
        
        for (int i = 0; i< numChild; i++)
        {
            Node child = popNode();
            children.add(child);
        }
        
        for (int i = children.size() - 1; i>=0; i--)
        {
            Node child = children.get(i);
            newNode.addChild(child);
        }
        
        pushNode(newNode);
    }
    
    public void pushNode(String nodeName)
    {
      /*  if (nodeName.equals("<ID:Print>"))
        {
            System.out.println("got it");
        }
        */
        Node newNode = new Node (nodeName);
        if (root == null)
        {
           root = newNode; 
        }
        
        else if (currentNode != null)
        {
            currentNode.addChild(newNode);
        }
        
        currentNode = newNode;
    }
    
    public void pushNode(Node n)
    {
        if (root == null)
        {
            root = n;
        }
        
        else if (currentNode != null)
        {
            currentNode.addChild(n);
        }
        
        currentNode = n;
    }
    
    public Node popNode()
    {
        Node popNode = currentNode;
        if (popNode != null)
        {
            currentNode = popNode.getParent();
            if (currentNode == null)
            {
                root = null;
            }            
        }
        else
        {
            System.out.println("Error, trying to pop node when none exists");
            System.exit(0);
        }
        
        popNode.removeFromParent();
        return popNode;
    }
    
    // final tree output
    public String toString()
    {
        if (root == null)
        {
            return "";
        }
        
        // Mistake 4
        String s = root.traverse();
        s = s.substring(0, s.length() - 1);
        return s;
    }
    
    public Node getRoot()
    {
        return root;
    }
    
    private Node root;
    private Node currentNode;
}   
