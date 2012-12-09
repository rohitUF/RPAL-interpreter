/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rohit
 */
public class beta extends ControlItem
{
    delta cond;
    delta then;
    delta e;
    public beta(delta cond, delta then, delta e)
    {
        super(ControlType.BETA, "");
        this.cond = cond;        
        this.then = then;
        this.e = e;
    }
}
