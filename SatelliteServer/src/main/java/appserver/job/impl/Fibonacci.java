/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appserver.job.impl;

/**
 *
 * @author Kevyn
 */
public class Fibonacci {
    
    public static int fib(int num)
    {
        if (num <= 1)
        {
            return num;
        }
        
        return fib(num-1) + fib(num-2);
    }
}
