/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rohit
 */

import java.util.ArrayList;
import java.util.Stack;

public class delta
{
    public Stack<ControlItem> items = new Stack<ControlItem>();
    public int number;
    public void putOnStack(Stack<ControlItem> s)
    {
       for (ControlItem i : items) 
       {
           s.push(i);
       }
       this.number = number;
    }
    
    public void print()
    {
        Stack<delta> temp = new Stack<delta>();
        temp.push(this);
        int i = 0;
        
        while (i < temp.size())
        {
            delta d = temp.get(i);
            System.out.print("Delta" + i + " : ");
            for (ControlItem item : d.items)
            {
                // first check the type
                switch(item.type)
                {                    
                    case LAMBDA:
                        // add its delta to the arraylist
                        lambda l = (lambda) item;
                        temp.add(l.d);
                        System.out.print("lambda" + (temp.size()-1) + " ");
                        for (ControlItem it : l.left)
                        {
                            System.out.print(it.val + " ");
                        }
                        break;
                        
                    case BETA:
                        // add its deltas to the temp
                        beta b = (beta) item;
                        temp.add(b.then);
                        temp.add(b.e);
                        
                        int s = temp.size();
                        // first print the deltas
                        System.out.print("delta" + (s -2) + " delta" + (s-1) + " " + "beta" + " " );                                           
                        break;
                        
                    case TAU:
                        tau t = (tau) item;
                        System.out.print("tau" + t.n + " ");     
                        break;
                        
                    default:
                        // just print it
                        System.out.print(item.val + " ");
                        break;                
                }
            }
            i++;
            System.out.println();
        }
    }
}


