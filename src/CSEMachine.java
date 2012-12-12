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
import java.util.Iterator;

public class CSEMachine
{
    delta deltaZero;
    Stack<ControlItem> runStack = new Stack<ControlItem>();
    ArrayList<Environment>E = new ArrayList<Environment>();
    Environment currentEnv;
    int currEnvNumber;
    Stack<ControlItem> control;
    
    public CSEMachine(delta d)
    {
        deltaZero = d;
    }
    
    public void run()
    {
        control = deltaZero.items;
        
        // put primitive environment
        currentEnv = new Environment(null, 0);
        currEnvNumber = 0; // E0
        E.add(currentEnv);
        
        while (!control.empty())
        {
            ControlItem item = control.peek();
            switch(item.type)
            {
                case GEN:
                    rule1();
                    break;
                    
                case LAMBDA:
                    rule2();
                    break;
                    
                case GAMMA:
                    rule3();
                    break;
                    
                case ENV:
                    rule5();
                    break;
                    
                case BINOPA:
                case BINOPL:
                    rule6();
                    break;
                    
                case UNOP:
                    rule7();
                    break;
                    
                case BETA:
                    rule8();
                    break;
                    
                case TAU:
                    rule9();
                    break;
                    
                case AUG:
                    aug();
                    break;
            }
        }
    }
    
    // Stacking a name
    private void rule1()
    {
        ControlItem item = control.pop();
        
        // look up in current environment for value
        ControlItem out = currentEnv.lookup(item);
        runStack.push(out);       
    }
    
    //Stacking a lambda
    private void rule2()
    {
      // add current Environment to it
        ControlItem item = control.pop();
        lambda l = (lambda)item;
        l.env = currentEnv;
        runStack.push(l);
    }
    
    // apply rator
    private void rule3()
    {
        ControlItem item = control.pop();
        FunctionTypes ftype = getFunctionType(runStack.peek());
        
        switch (ftype)
        {
            case LAMBDA:
                rule4();
                break;
                
            case PRINT:
                printtop();
                break;
                
            case TUPLE:
                rule10();
                break;
                
            case ISDUMMY:
            case ISFUNCTION:
            case ISTUPPLE:
            case ISINTEGER:
            case ISSTRING:
            case ISTRUTHVALUE:
            case NULL:
                is(ftype);
                break;
                
            case CONC:
                conc();
                break;
                
            case STEM:
                stem();
                break;
                
            case STERN:
                stern();
                break;
                
            case ITOS:
                itos();
                break;
                
            case ORDER:
                order();
                break;
                
            case ETA:
                rule13();
                break;
                
            case YSTAR:
                rule12();
                break;
                
            default:
                //System.out.println("Gamma applied on unkown function : " + item.val);
                break;
                
        }
    }
    
    private FunctionTypes getFunctionType(ControlItem i)
    {
        switch(i.type)
        {
            case LAMBDA:
                return FunctionTypes.LAMBDA;    
                
            case ETA:
                return FunctionTypes.ETA;
                
            case TUPLE:
                return FunctionTypes.TUPLE;
                
            case GEN:
                if (i.sType == SecondaryControlType.IDENTIFIER)
                {
                    String s = i.val.toLowerCase().trim();
                    switch (s)
                    {
                        case "conc":
                            return FunctionTypes.CONC;
                            
                        case "itos":
                            return FunctionTypes.ITOS;
                            
                        case "stem":
                            return FunctionTypes.STEM;
                            
                        case "print":
                            return FunctionTypes.PRINT;
                            
                        case "order":
                            return FunctionTypes.ORDER;
                            
                        case "stern":
                            return FunctionTypes.STERN;
                            
                        case "istuple":
                            return FunctionTypes.ISTUPPLE;
                            
                        case "isdummy":
                            return FunctionTypes.ISDUMMY;
                            
                        case "isstring":
                            return FunctionTypes.ISSTRING;
                            
                        case "isinteger":
                            return FunctionTypes.ISINTEGER;
                            
                        case "isfunction":
                            return FunctionTypes.ISFUNCTION;
                            
                        case "istruthvalue":
                            return FunctionTypes.ISTRUTHVALUE;
                            
                        default:
                            return FunctionTypes.UNKNOWN;
                    }
                }                   
                
                if (i.val.equals("ystar"))
                {
                    return FunctionTypes.YSTAR;
                }
        }
        return FunctionTypes.UNKNOWN;
    }
    
    // Applying a lambda closure
    private void rule4()
    {
        // Get the lambda operator from run stack
        lambda l = (lambda)runStack.pop();
        
        //create new environment
       currEnvNumber++;
       Environment env = new Environment(l.env, currEnvNumber);
        
        // Are there more than one variables in this lambda ?
        if (l.left.size() > 1)
        {
            // next item on runStack must be a tuple
            ControlItem i = runStack.pop();
            if (i.type != ControlType.TUPLE)
            {
                System.out.println("After a comma lambda, could not get a tuple");
                System.exit(0);
            }
            tuple tup = (tuple)i;
            
            // now add these tuple values with lambda variables in environment
            if (l.left.size() != tup.storage.size())
            {
                System.out.println("size mismatch between lamda variables and tuples");
                System.exit(0);
            }
            
            int num = 0;
            while (num < l.left.size())
            {
                env.add(l.left.get(num).val, tup.storage.get(num));
                num++;
            }
        }
        else
        {
           env.add(l.left.get(0).val, runStack.pop());
        }
        
       E.add(env);
       currentEnv = env;
       
       // put these new environment marker in both stacks
       control.push(currentEnv);
       runStack.push(currentEnv);
       
       // finally add the delta in front of the control stack
       l.d.putOnStack(control);
    }
    
    //exit from environment
    private void rule5()
    {
        // Remove Environment from the control stack
        control.pop();
        if (runStack.peek().type == ControlType.ENV)
        {
            runStack.pop();
        }
        
        else
        {
            int i = runStack.size() - 1;
            while (i >= 0 && runStack.get(i).type != ControlType.ENV)
            {
                i--;
            }
            
            if (i >= 0)
            {
                runStack.remove(i);
            }
        }
        
        // now update the current environment
        int i = runStack.size() - 1;
        while (i >= 0 && runStack.get(i).type != ControlType.ENV)
        {
            i--;
        }
        
        if (i >= 0)
        {
            currentEnv = (Environment)runStack.get(i);
            currEnvNumber = currentEnv.number;
        }
        else
        {
            currentEnv = E.get(0);
            currEnvNumber = 0;
        }
    }
    
    private void printtop()
    {
        runStack.pop();
        ControlItem item = runStack.pop();
        if (item.type == ControlType.GEN)
        {
            item.val = item.val.replace("\\t", "\t");
            item.val = item.val.replace("\\n", "\n");
            System.out.print(item.val);
        }        
        else if (item.type == ControlType.LAMBDA)
        {
            lambda l = (lambda)item;
            System.out.print("[lambda closure: " + l.left.get(0).val + l.d.number + "]");
        }
        else if (item.type == ControlType.ETA)
        {
            eta e = (eta) item;
            lambda l = e.l;
            System.out.print("[eta closure: " + l.left.get(0).val + l.d.number + "]");
        }
        else if (item.type == ControlType.TUPLE)
        {
            tuple t = (tuple)item;
            Iterator<ControlItem> i = t.storage.iterator();
            System.out.print("(");
           while(i.hasNext())
           {
               System.out.print(i.next().val);
               if (i.hasNext())
                   System.out.print(", ");
           }
           System.out.print(")");
        }
    }
    
    // Binary operators
    private void rule6()
    {
        ControlItem binop = control.pop();
        ControlItem rand1 = runStack.pop();
        ControlItem rand2 = runStack.pop();
        
        if (binop.type == ControlType.BINOPA)
        {
            // Airthmatic operation
            if (rand1.sType == SecondaryControlType.INT && rand2.sType == SecondaryControlType.INT)
            {
                // simple int airthmatic
                int r1 = Integer.parseInt(rand1.val);
                int r2 = Integer.parseInt(rand2.val);
                
                switch (binop.val)
                {
                    case "+":
                        runStack.push(new ControlItem(SecondaryControlType.INT, Integer.toString(r1+r2)));
                        break;
                        
                    case "-":
                        runStack.push(new ControlItem(SecondaryControlType.INT, Integer.toString(r1-r2)));
                        break;
                        
                    case "*":
                        runStack.push(new ControlItem(SecondaryControlType.INT, Integer.toString(r1*r2)));
                        break;
                        
                    case "/":
                        runStack.push(new ControlItem(SecondaryControlType.INT, Integer.toString(r1/r2)));
                        break;
                        
                    case "**":
                        runStack.push(new ControlItem(SecondaryControlType.INT, Integer.toString((int)Math.pow(r1, r2))));
                        break;
                        
                    case "gr":
                        runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, r1 > r2 ? "true" : "false"));
                        break;
                        
                    case "ge":
                        runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, r1 >= r2 ? "true" : "false"));
                        break;
                            
                    case "ls":
                        runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, r1 < r2 ? "true" : "false"));
                        break;
                                
                    case "le":
                        runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, r1 <= r2 ? "true" : "false"));
                        break;
                        
                    case "eq":
                        runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, r1 == r2 ? "true" : "false"));
                        break;
                        
                    case "ne":
                        runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, r1 != r2 ? "true" : "false"));
                        break;
                        
                    default:
                        System.out.println("Unknown BINOPA");
                }
            }
            else if (rand1.sType == SecondaryControlType.STRING && rand2.sType == SecondaryControlType.STRING)
            {
                // String comparisons
                
                String r1 = rand1.val;
                String r2 = rand2.val;
                
                switch(binop.val)
                {
                    case "gr":
                        runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, r1.compareTo(r2) > 0 ? "true" : "false"));
                        break;
                        
                    case "ge":
                        runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, r1.compareTo(r2) >= 0 ? "true" : "false"));
                        break;
                        
                    case "ls":
                        runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, r1.compareTo(r2) < 0 ? "true" : "false"));
                        break;
                        
                    case "le":
                        runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, r1.compareTo(r2) <= 0 ? "true" : "false"));
                        break;
                        
                    case "eq":
                        runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, r1.compareTo(r2) == 0 ? "true" : "false"));
                        break;
                        
                    case "ne":
                        runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, r1.compareTo(r2) != 0 ? "true" : "false"));
                        break;
                        
                    default:
                        System.out.println("Unknown Binop on Strings");
                }                
            }
            else
            {
                System.out.println("incompatible operand types for binop " + binop.val);
            }
        }
        
        else
        {
            // Logical Binary operators
            boolean b1 = Boolean.parseBoolean(rand1.val);
            boolean b2 = Boolean.parseBoolean(rand2.val);
            
            switch(binop.val)
            {
                case "or":
                    runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, Boolean.toString(b2 || b1)));
                    break;
                    
                case "and":
                    runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, Boolean.toString(b2 && b1)));
                    break;
            }
        }
    }
    
    // unary operations
    private void rule7()
    {
        ControlItem unop = control.pop();
        ControlItem rand = runStack.pop();
        
        switch (unop.val)
        {
            case "neg":
                runStack.push(new ControlItem(SecondaryControlType.INT, Integer.toString(-Integer.parseInt(rand.val))));
                break;
                
            case "not":
                runStack.push(new ControlItem(SecondaryControlType.BOOLEAN, Boolean.toString(!Boolean.parseBoolean(rand.val))));
                break;
        }
    }
    
    //Conditional
    private void rule8()
    {
        beta b = (beta)control.pop();
        ControlItem c = runStack.pop();
        
        if (c.sType != SecondaryControlType.BOOLEAN)
        {
            System.out.println("Got a conditional on control stack with non-bool on run stack");
            System.exit(0);
        }
        
        boolean bVal = Boolean.parseBoolean(c.val);
        if (bVal == true)
        {
            b.then.putOnStack(control);
        }
        else
        {
            b.e.putOnStack(control);
        }
    }
    
    // Making tuples
    private void rule9()
    {
        tau t = (tau)control.pop();
        ArrayList<ControlItem> list = new ArrayList<ControlItem>();
        int num = t.n;
        while (num > 0)
        {
            list.add(runStack.pop());
            num--;
        }
        tuple tup = new tuple(list);
        runStack.push(tup);
    }
    
    // tuple selection
    private void rule10()
    {
        tuple t = (tuple)runStack.pop();
        ControlItem it = runStack.pop();
        
        if (it.sType != SecondaryControlType.INT)
        {
            System.out.println("Got a non int parameter for tuple selection");
            System.exit(0);
        }
        
        int i = Integer.parseInt(it.val);
        runStack.push(t.storage.get(i-1));
    }
    
    private void is(FunctionTypes type)
    {
        runStack.pop();
        ControlItem tr = new ControlItem(ControlType.GEN, SecondaryControlType.BOOLEAN, "true");
        ControlItem fa = new ControlItem(ControlType.GEN, SecondaryControlType.BOOLEAN, "false");
        
        ControlItem item = runStack.pop();
        switch(type)
        {
            case ISDUMMY:
                if(item.sType == SecondaryControlType.DUMMY)
                    runStack.push(tr);
                else
                    runStack.push(fa);
                break;
                
            case ISINTEGER:
                if(item.sType == SecondaryControlType.INT)
                    runStack.push(tr);
                else
                    runStack.push(fa);
                break;
                
            case ISFUNCTION:
                if(item.type == ControlType.ETA || item.type == ControlType.LAMBDA)
                    runStack.push(tr);
                else
                    runStack.push(fa);
                break;
                
            case NULL:
                if (item.sType == SecondaryControlType.NIL)
                    runStack.push(tr);
                else
                    runStack.push(fa);
                break;
                
            case ISTUPPLE:
                if (item.type == ControlType.TUPLE)
                    runStack.push(tr);
                else
                    runStack.push(fa);
                break;
                
            case ISTRUTHVALUE:
                if (item.sType == SecondaryControlType.BOOLEAN)
                    runStack.push(tr);
                else
                    runStack.push(fa);
                break;
                
            case ISSTRING:
                if (item.sType == SecondaryControlType.STRING)
                    runStack.push(tr);
                else
                    runStack.push(fa);
                break;       
        }
    }
    
    private void aug()
    {
        control.pop();
        ControlItem item = runStack.pop();
        if (!(item.type == ControlType.TUPLE || item.sType == SecondaryControlType.NIL))
        {
            System.out.println("After aug operator must get either a tuple or nil");
            System.exit(0);
        }
        
        ControlItem it = runStack.pop();
        tuple t;
        if (item.type == ControlType.TUPLE)
        {
            t = (tuple)item;
            t.storage.add(it);
        }
        else
        {
            ArrayList<ControlItem> ls = new ArrayList<ControlItem>();
            ls.add(it);
            t = new tuple(ls);
        }
        
        runStack.push(t);
    }
    
    private void conc()
    {
       runStack.pop();
       String s1 = runStack.pop().val;
       String s2 = runStack.pop().val;
       
       runStack.push(new ControlItem(ControlType.GEN, SecondaryControlType.STRING, s1+s2));
    }
    
    private void stern()
    {
        runStack.pop();
        runStack.push(new ControlItem(ControlType.GEN, SecondaryControlType.STRING, runStack.pop().val.substring(1)));
    }
    
    private void stem()
    {
        runStack.pop();
        runStack.push(new ControlItem(ControlType.GEN, SecondaryControlType.STRING, runStack.pop().val.substring(0,1)));
    }
    
    private void itos()
    {
        runStack.pop();
        ControlItem i = runStack.pop();
        if (i.sType != SecondaryControlType.INT)
        {
            System.out.println("No Int after ITOS");
            System.exit(0);
        }
        i.sType = SecondaryControlType.STRING;
        runStack.push(i);
    }
    
    private void order()
    {
        runStack.pop();
        ControlItem item = runStack.pop();
        ControlItem i;
        if (item.sType == SecondaryControlType.NIL)
        {
            i = new ControlItem(ControlType.GEN, SecondaryControlType.INT, "0");
        }
        else if (item.type == ControlType.TUPLE)
        {
            i = new ControlItem(ControlType.GEN, SecondaryControlType.INT, Integer.toString(((tuple)item).storage.size()));
        }
        else
        {
            System.out.append("order function not followed by a nil or tuple");
            System.exit(0);
            i = new ControlItem();
        }
        runStack.push(i);
    }
    
    // encountered Ystar
    private void rule12()
    {
        // get ystar
        runStack.pop();
        
        // after ystar there should be a lambda
        lambda l = (lambda)runStack.pop();
        
        // create new eta
        eta e = new eta(l);
        runStack.push(e);
    }
    
    // applying ystar
    private void rule13()
    {
        ControlItem g = new ControlItem(ControlType.GAMMA, "");
        eta e = (eta)runStack.peek();
        lambda l = e.l;
        
        control.push(g);
        control.push(g);
        runStack.push(l);        
    }
}