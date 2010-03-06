package com.totsp.testing.test;

import android.test.AndroidTestCase;

import com.totsp.testing.Fibonacci;

import java.math.BigInteger;

import junit.framework.Assert;

public class FibonacciTest extends AndroidTestCase {

   // Android test case is a "unit" level test case 
   // it won't launch an activity but you can get an injected context (getContext)
   // good for testing non-UI portions of your app, data, logic, XML parsing, etc

   public void testFibonacci() {
      Fibonacci fib = new Fibonacci();
      Assert.assertEquals(new BigInteger("55"), fib.fibFast(10));
   }

}
