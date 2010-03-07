package com.totsp.testing.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.totsp.testing.Main;

import junit.framework.Assert;

public class MainTest extends ActivityInstrumentationTestCase2<Main> {

   public MainTest() {
      super("com.totsp.testing", Main.class);
   }

   private Activity activity;
   private EditText input;
   private TextView output;
   private Button run;

   protected void setUp() throws Exception {
      this.activity = this.getActivity();
      this.input = (EditText) this.activity.findViewById(com.totsp.testing.R.id.f_of_x);
      this.output = (TextView) this.activity.findViewById(com.totsp.testing.R.id.output);
      this.run = (Button) this.activity.findViewById(com.totsp.testing.R.id.run);
   }

   // @SmallTest @MediumTest @LargeTest (can be run separately - e size small \ medium \ large)

   public void testRun() {
      
      ViewAsserts.assertOnScreen(input.getRootView(), input);
      ViewAsserts.assertOnScreen(output.getRootView(), output);
      ViewAsserts.assertOnScreen(run.getRootView(), run);
      
      // simulate keys and dpad
      this.sendKeys(KeyEvent.KEYCODE_5);
      this.sendKeys(KeyEvent.KEYCODE_5);
      this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
      
      //this.sendKeys(KeyEvent.KEYCODE_ENTER);
      
      // can also use handy "TouchUtils" for touch screen simul
      TouchUtils.tapView(this, this.run);      

      this.getInstrumentation().waitForIdleSync();

      Assert.assertEquals("139583862445", this.output.getText().toString());
   }

}
