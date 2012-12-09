/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rohit
 */

import java.util.ArrayList;
public class tau extends ControlItem
{
    public ArrayList<ControlItem> children;
    
    public tau(ArrayList<ControlItem> c)
    {
        super(ControlType.TAU, "");
        children = c;
    }
}
