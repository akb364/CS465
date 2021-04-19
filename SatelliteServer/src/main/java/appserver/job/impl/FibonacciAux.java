/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appserver.job.impl;

/**
 *
 * @author Bryan
 */
public class FibonacciAux {
    
    
    public FibonacciAux() {
    }
    
    public Integer getResult(Integer num) 
    {
        if (num <= 1)
        {
            return num;
        }
        
        return getResult(num-1) + getResult(num-2);
    }
}
