/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rohit
 */

import java.util.ArrayList;
public class lambda extends ControlItem
{
    public delta d;
    public ArrayList<ControlItem> left;
    public Environment env;
    
    lambda(delta d, ArrayList<ControlItem> leftChild)
    {
        super(ControlType.LAMBDA, "");
        this.d = d;
        this.left = leftChild;
    }   
}
