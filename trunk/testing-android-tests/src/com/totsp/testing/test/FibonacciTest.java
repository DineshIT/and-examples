package com.totsp.testing.test;

import android.test.AndroidTestCase;

import com.totsp.testing.Fibonacci;

import java.math.BigInteger;

import junit.framework.Assert;

public class FibonacciTest extends AndroidTestCase {

   // Android test case is a "unit" level test case 
   // it won't launch an activity but you can get an injected context (getContext)
   // good for testing non-UI portions of your app, data, logic, XML parsing, etc
   //
   // of course, ideally many of these aspects can be tested without android at all
   // to do that though you need to run a regular JUnit TestCase and not an 
   // AndroidTestCase - that can be done, but you have to remove android.jar from
   // the classpath and add are regular JRE and JUnit 
   // (sometimes easier just to run AndroidTestCase, but it's a lot slower and more overhead)

   public void testFibonacci() {
      Fibonacci fib = new Fibonacci();
      Assert.assertEquals(new BigInteger("55"), fib.fibFast(10));
   }

}
