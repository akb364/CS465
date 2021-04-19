/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appserver.job.impl;
import appserver.job.Tool;
/**
 *
 * @author Kevyn
 */
public class Fibonacci implements Tool {
    
    FibonacciAux helper = null;
    
    @Override
    public Object go(Object parameters)
    {
        helper = new FibonacciAux();
        return helper.getResult((Integer) parameters);
        
    }
}