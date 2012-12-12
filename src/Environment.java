/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rohit
 */

import java.util.HashMap;

public class Environment extends ControlItem
{
    HashMap<String, ControlItem> table = new HashMap<String, ControlItem>();
    Environment prev;
    int number;
    
    public Environment(Environment prev, int number)
    {
        super (ControlType.ENV, "");
        this.prev = prev;
        this.number = number;
    }
    
    public void add(String k, ControlItem v)
    {
        table.put(k, v);
    }
    
    public ControlItem lookup(ControlItem key)
    {
        String k = key.val;
        if (table.containsKey(k))
        {
            return table.get(k);
        }
        
        if (prev != null)
        {
            return prev.lookup(key);
        }
        
        else
        {
            return key;
        }
    }
}
