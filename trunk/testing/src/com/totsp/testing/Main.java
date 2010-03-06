package com.totsp.testing;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;

public class Main extends Activity {

   private EditText input;
   private TextView output;
   private Button run;

   private Fibonacci fib;

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setContentView(R.layout.main);

      this.input = (EditText) this.findViewById(R.id.f_of_x);
      this.output = (TextView) this.findViewById(R.id.output);
      this.run = (Button) this.findViewById(R.id.run);

      this.fib = new Fibonacci();

      this.run.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            BigInteger result = Main.this.fib.fibFast(Integer.parseInt(Main.this.input.getText().toString()));
            Main.this.output.setText(result.toString());
         }
      });
   }
}