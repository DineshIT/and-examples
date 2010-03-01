package com.totsp.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import java.util.List;

public class Main extends Activity {

   private LinearLayout layout;
   private LayoutParams layoutParams;

   TextView out1;
   TextView out2;
   TextView out3;
   TextView out4;
   TextView out5;
   TextView out6;
   TextView out7;
   TextView out8;
   
   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.setContentView(R.layout.main);
      this.layout = (LinearLayout) this.findViewById(R.id.mainlayout);
      this.layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
      this.layoutParams.setMargins(0, 10, 0, 0);

      this.out1 = new TextView(this);
      this.out2 = new TextView(this);
      this.out3 = new TextView(this);
      this.out4 = new TextView(this);
      this.out5 = new TextView(this);
      this.out6 = new TextView(this);
      this.out7 = new TextView(this);
      this.out8 = new TextView(this);      
      
      this.crawlSensors();
   }

   private void crawlSensors() {

      SensorManager sm = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

      List<Sensor> sensorList = sm.getSensorList(Sensor.TYPE_ALL);
      for (Sensor sensor : sensorList) {
         sensor.getType();
         TextView tv = new TextView(this);
         this.layout.addView(tv, layoutParams);

         switch (sensor.getType()) {
         case Sensor.TYPE_ACCELEROMETER:
            tv.setText("Sensor present (ACCELEROMETER): \n" + sensor.getName());
            sm.registerListener(this.getSensorListener(this.out1), sensor, SensorManager.SENSOR_DELAY_UI);
            this.layout.addView(this.out1);
            break;
         case Sensor.TYPE_GYROSCOPE:
            tv.setText("Sensor present (GYROSCOPE): \n" + sensor.getName());
            sm.registerListener(this.getSensorListener(this.out2), sensor, SensorManager.SENSOR_DELAY_UI);
            this.layout.addView(this.out2);
            break;
         case Sensor.TYPE_LIGHT:
            tv.setText("Sensor present (LIGHT): \n" + sensor.getName());
            sm.registerListener(this.getSensorListener(this.out3), sensor, SensorManager.SENSOR_DELAY_UI);
            this.layout.addView(this.out3);
            break;
         case Sensor.TYPE_MAGNETIC_FIELD:
            tv.setText("Sensor present (MAGNETIC_FIELD): \n" + sensor.getName());
            sm.registerListener(this.getSensorListener(this.out4), sensor, SensorManager.SENSOR_DELAY_UI);
            this.layout.addView(this.out4);
            break;
         case Sensor.TYPE_ORIENTATION:
            tv.setText("Sensor present (ORIENTATION): \n" + sensor.getName());
            sm.registerListener(this.getSensorListener(this.out5), sensor, SensorManager.SENSOR_DELAY_UI);
            this.layout.addView(this.out5);
            break;
         case Sensor.TYPE_PRESSURE:
            tv.setText("Sensor present (PRESSURE): \n" + sensor.getName());
            sm.registerListener(this.getSensorListener(this.out6), sensor, SensorManager.SENSOR_DELAY_UI);
            this.layout.addView(this.out6);
            break;
         case Sensor.TYPE_PROXIMITY:
            tv.setText("Sensor present (PROXIMITY): \n" + sensor.getName());
            sm.registerListener(this.getSensorListener(this.out7), sensor, SensorManager.SENSOR_DELAY_UI);
            this.layout.addView(this.out7);
            break;
         case Sensor.TYPE_TEMPERATURE:
            tv.setText("Sensor present (TEMPERATURE): \n" + sensor.getName());
            sm.registerListener(this.getSensorListener(this.out8), sensor, SensorManager.SENSOR_DELAY_UI);
            this.layout.addView(this.out8);
            break;
         default:
            break;
         }

         
      }
   }
   
   private SensorEventListener getSensorListener(final TextView out) {
      return new SensorEventListener() {
         public void onSensorChanged(SensorEvent e) {
            float[] vals = e.values;
            StringBuilder sb = new StringBuilder();
            for (float f : vals) {
               sb.append("val:" + f + " ");
            }
            out.setText(sb.toString());
         }
         public void onAccuracyChanged(Sensor s, int i) {
            // TODO
         }         
      };
   }   
}