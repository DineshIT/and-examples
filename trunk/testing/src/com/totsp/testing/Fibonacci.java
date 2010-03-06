package com.totsp.testing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Fibonacci {

   // polynomial algorithm (n^2)
   // linear in n
   // .17 seconds for fib1(30000)
   public BigInteger fibFast(int n) {      
      if (n == 0) {
         return BigInteger.ZERO;
      } else if (n == 1) {
         return BigInteger.ONE;
      } else {
         List<BigInteger> fibs = new ArrayList<BigInteger>();
         fibs.add(BigInteger.ZERO);
         fibs.add(BigInteger.ONE);
         for (int i = 2; i <= n; i++) {
            BigInteger fib2 = fibs.get(fibs.size() - 2);
            BigInteger fib1 = fibs.get(fibs.size() - 1);
            fibs.add(fib1.add(fib2));
         }
         return fibs.get(n);
      }
   }

   // exponential algorithm (2^n)
   // exponential in n
   // .27 seconds for fib2(30)
   // 62 seconds for fib2(45)   
   public BigInteger fibSlow(int n) {
      if (n == 0) {
         return BigInteger.ZERO;
      } else if (n == 1) {
         return BigInteger.ONE;
      } else {
         return fibSlow(n - 1).add(fibSlow(n - 2));
      }
   }   
}
