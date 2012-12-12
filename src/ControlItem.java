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
    public SecondaryControlType sType;
    
    public ControlItem(ControlType type, String val)
    {
        this.type = type;
        this.val = val;
        this.sType = SecondaryControlType.NONE;
    }
    
    public ControlItem()
    {
        this(ControlType.GEN, "");
    }
    
    public ControlItem(ControlType type, SecondaryControlType sType, String val)
    {
        this.type = type;
        this.sType = sType;
        this.val = val;
    }
    
    public ControlItem(SecondaryControlType sType, String val)
    {
        this.type = ControlType.GEN;
        this.sType = sType;
        this.val = val;
    }
}
