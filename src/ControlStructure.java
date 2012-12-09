/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rohit
 */

import java.util.Stack;
import java.util.ArrayList;

// Class flattens the standard tree and store the control structure.
public class ControlStructure
{
    delta deltaZero = new delta();
    public delta getDeltaZero(Node stRoot)
    {
        preOrderTraversal(stRoot, deltaZero);
        return deltaZero;
    }
    
    private void preOrderTraversal(Node node, delta d)
    {       
       switch (node.toString())
       {
           case "lambda":
               // in case of lambda create a new lambda object and add it to current delta
               // first get this nodes left Child
               ArrayList<ControlItem> left = new ArrayList<ControlItem>();
               if (node.getChild(0).toString().equals(","))
               {
                   // get all children of the comma and set them as left of lambda
                   for (Node n : node.getChild(0).getChildren())
                   {
                       ControlItem i = new ControlItem();
                       i.val = getRawToken(n.toString());
                       left.add(i);
                   }
               }
               
               else
               {
                   ControlItem i = new ControlItem();
                   i.val = getRawToken(node.getChild(0).toString());
                   left.add(i);
               }
               
               // now traverse its right child to get the new delta
               delta lambdaDelta = new delta();
               preOrderTraversal(node.getChild(1), lambdaDelta);
               lambda l = new lambda(lambdaDelta, left);
               d.items.add(l);
               break;
               
           case "gamma":
               ControlItem g = new ControlItem(ControlType.GAMMA, "gamma");
               d.items.add(g);
               preOrderTraversal(node.getChild(0), d);
               preOrderTraversal(node.getChild(1), d);
               break;
               
           case "neg":
           case "not":
               ControlItem u = new ControlItem(ControlType.UNOP, node.toString());
               d.items.add(u);
               preOrderTraversal(node.getChild(0), d);
               break;
               
           case "or":
           case "and":
               ControlItem bl = new ControlItem(ControlType.BINOPL, node.toString());
               d.items.add(bl);
               preOrderTraversal(node.getChild(0), d);
               preOrderTraversal(node.getChild(1), d);
               break;
               
           case "gr":
           case "ge":
           case "ls":
           case "le":
           case "eq":
           case "ne":
           case "+":
           case "-":
           case "*":
           case "/":
           case "**":
               ControlItem ba = new ControlItem(ControlType.BINOPA, node.toString());
               d.items.add(ba);
               preOrderTraversal(node.getChild(0), d);
               preOrderTraversal(node.getChild(1), d);
               break;
               
           case "->":
               // Get a delta each for condition, then and else
               // delta for condition is in first child
               delta condition = new delta();
               preOrderTraversal(node.getChild(0), condition);
               
               delta then = new delta();
               preOrderTraversal(node.getChild(1), then);
               
               delta e = new delta();
               preOrderTraversal(node.getChild(2), e);
               
               // now add these to the beta
               beta b = new beta(condition, then, e);
               d.items.add(b);
               break;
               
           case "aug":
               ControlItem aug = new ControlItem(ControlType.AUG, "aug");
               d.items.add(aug);
               preOrderTraversal(node.getChild(0), d);
               preOrderTraversal(node.getChild(1), d);
               break;
               
           case "tau":
               ArrayList<ControlItem> c = new ArrayList<ControlItem>();
               for (Node n : node.getChildren())
               {
                   ControlItem i = new ControlItem();
                   i.val = getRawToken(n.toString());
                   c.add(i);
               }
               tau t = new tau(c);
               d.items.add(t);
               break;
               
           default:
               // for all the generic items, such as strings, ints, also for nil, ystar and boolean values
               ControlItem i = new ControlItem();
               i.val = getRawToken(node.toString());
               d.items.add(i);
               break;               
       }
    }
    private String getRawToken(String s)
    {
        int b = s.indexOf(':');
        b = b+1;
        if (b <= 0)
        {
            return s;
        }
        return s.substring(b, s.length() - 1);
    }
}
