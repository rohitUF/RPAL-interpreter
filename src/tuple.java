/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rohit
 */

import java.util.ArrayList;
public class tuple extends ControlItem
{
    public ArrayList<ControlItem> storage;
        
    public tuple(ArrayList<ControlItem>s)
    {
        super(ControlType.TUPLE, "");
        storage = s;
    }
}
