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
    int curDelta = 0;
    
    public delta getDeltaZero(Node stRoot)
    {
        deltaZero.number = 0;
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
                       getRawToken(n.toString(), i);
                       left.add(i);
                   }
               }
               
               else
               {
                   ControlItem i = new ControlItem();
                   getRawToken(node.getChild(0).toString(), i);
                   left.add(i);
               }
               
               // now traverse its right child to get the new delta
               curDelta++;
               delta lambdaDelta = new delta();
               lambdaDelta.number = curDelta;
               preOrderTraversal(node.getChild(1), lambdaDelta);
               lambda l = new lambda(lambdaDelta, left);
               d.items.push(l);
               break;
               
           case "gamma":
               ControlItem g = new ControlItem(ControlType.GAMMA, "gamma");
               d.items.push(g);
               preOrderTraversal(node.getChild(0), d);
               preOrderTraversal(node.getChild(1), d);
               break;
               
           case "neg":
           case "not":
               ControlItem u = new ControlItem(ControlType.UNOP, node.toString());
               d.items.push(u);
               preOrderTraversal(node.getChild(0), d);
               break;
               
           case "or":
           case "&":
               ControlItem bl = new ControlItem(ControlType.BINOPL, node.toString());
               d.items.push(bl);
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
               d.items.push(ba);
               preOrderTraversal(node.getChild(0), d);
               preOrderTraversal(node.getChild(1), d);
               break;
               
           case "->":
               // Get a delta each for condition, then and else
               curDelta++;
               delta then = new delta();
               then.number = curDelta;
               preOrderTraversal(node.getChild(1), then);
               
               curDelta++;
               delta e = new delta();
               e.number = curDelta;
               preOrderTraversal(node.getChild(2), e);
               
               // now add these to the beta
               beta b = new beta(then, e);
               d.items.push(b);
               
               // now add condition to the current delta.
               preOrderTraversal(node.getChild(0), d);
               break;
               
           case "aug":
               ControlItem aug = new ControlItem(ControlType.AUG, "aug");
               d.items.push(aug);
               preOrderTraversal(node.getChild(0), d);
               preOrderTraversal(node.getChild(1), d);
               break;
               
           case "tau":              
               tau t = new tau(node.getChildren().size());
               d.items.push(t);
               for (Node n : node.getChildren())
               {
                   preOrderTraversal(n, d);
               }
               break;
               
           default:
               // for all the generic items, such as strings, ints, also for nil, ystar and boolean values
               ControlItem i = new ControlItem();
               getRawToken(node.toString(), i);
               d.items.push(i);
               break;               
       }
    }
    private void getRawToken(String s, ControlItem item)
    {       
        int b = s.indexOf(':');
        b = b+1;
        if (b <= 0)
        {
            item.val = s;
            if (s.equals("<true>") || s.equals("<false>"))
            {
                item.sType = SecondaryControlType.BOOLEAN;
            }
            
            else if (s.equals("<dummy>"))
            {
                item.sType = SecondaryControlType.DUMMY;
            }
            
            else if (s.equals("<nil>"))
            {
                item.sType = SecondaryControlType.NIL;
            }
            
            else
            {
                item.sType = SecondaryControlType.NONE;           
            }
        }
        else
        {        
            if (s.startsWith("<ID"))
                item.sType = SecondaryControlType.IDENTIFIER;
            else if (s.startsWith("<INT"))
                item.sType = SecondaryControlType.INT;
            else if (s.startsWith("<STR"))
                item.sType = SecondaryControlType.STRING;

            item.val = s.substring(b, s.length() - 1);
        }
        // finally remove any quotes or angular bracket
        item.val = item.val.replaceAll("'", "");
        item.val = item.val.replaceAll("<", "");
        item.val = item.val.replaceAll(">", "");
    }
}
