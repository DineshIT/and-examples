package com.totsp.testing.test;

import android.app.Activity;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.totsp.testing.Main;

import junit.framework.Assert;

public class MainTest extends ActivityInstrumentationTestCase2<Main> {

   public MainTest() {
      super("com.totsp.testing", Main.class);
   }
   
   private EditText input;
   private TextView output;
   private Button run;
   
   protected void setUp() throws Exception {
      Activity main = this.getActivity();
      input = (EditText) main.findViewById(com.totsp.testing.R.id.f_of_x);
      output = (TextView) main.findViewById(com.totsp.testing.R.id.output);
      run = (Button) main.findViewById(com.totsp.testing.R.id.run);
      
      //input.setText("10");
   }
   
   @SmallTest
   public void testSomething() {
      
      
      SystemClock.sleep(1000);
      TouchUtils.tapView(this, run);
      Assert.assertEquals("blah", output.getText().toString());
      
      
   }
   
}
