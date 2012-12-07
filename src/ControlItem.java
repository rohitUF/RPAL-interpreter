/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rohit
 */

// Represents one item in the control stack of the CSE machine.
public class ControlItem
{
    public ControlType type;
    public String val;
    
    public ControlItem(ControlType type, String val)
    {
        this.type = type;
        this.val = val;
    }
    
    public ControlItem()
    {
        this(ControlType.GEN, "");
    }
}
