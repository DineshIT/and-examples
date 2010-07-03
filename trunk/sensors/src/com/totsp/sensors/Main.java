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

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      layout = (LinearLayout) findViewById(R.id.mainlayout);
      layoutParams =
               new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
      layoutParams.setMargins(0, 10, 0, 0);

      crawlSensors();
   }

   private void crawlSensors() {

      SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

      List<Sensor> sensorList = sm.getSensorList(Sensor.TYPE_ALL);
      for (Sensor sensor : sensorList) {
         sensor.getType();
         TextView tv = new TextView(this);
         layout.addView(tv, layoutParams);

         TextView out = new TextView(this);

         switch (sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
               tv.setText("Sensor present (ACCELEROMETER): \n" + sensor.getName());
               sm.registerListener(getSensorListener(out), sensor, SensorManager.SENSOR_DELAY_UI);
               layout.addView(out);
               break;
            case Sensor.TYPE_GYROSCOPE:
               tv.setText("Sensor present (GYROSCOPE): \n" + sensor.getName());
               sm.registerListener(getSensorListener(out), sensor, SensorManager.SENSOR_DELAY_UI);
               layout.addView(out);
               break;
            case Sensor.TYPE_LIGHT:
               tv.setText("Sensor present (LIGHT): \n" + sensor.getName());
               sm.registerListener(getSensorListener(out), sensor, SensorManager.SENSOR_DELAY_UI);
               layout.addView(out);
               break;
            case Sensor.TYPE_MAGNETIC_FIELD:
               tv.setText("Sensor present (MAGNETIC_FIELD): \n" + sensor.getName());
               sm.registerListener(getSensorListener(out), sensor, SensorManager.SENSOR_DELAY_UI);
               layout.addView(out);
               break;
            case Sensor.TYPE_ORIENTATION:
               tv.setText("Sensor present (ORIENTATION): \n" + sensor.getName());
               sm.registerListener(getSensorListener(out), sensor, SensorManager.SENSOR_DELAY_UI);
               layout.addView(out);
               break;
            case Sensor.TYPE_PRESSURE:
               tv.setText("Sensor present (PRESSURE): \n" + sensor.getName());
               sm.registerListener(getSensorListener(out), sensor, SensorManager.SENSOR_DELAY_UI);
               layout.addView(out);
               break;
            case Sensor.TYPE_PROXIMITY:
               tv.setText("Sensor present (PROXIMITY): \n" + sensor.getName());
               sm.registerListener(getSensorListener(out), sensor, SensorManager.SENSOR_DELAY_UI);
               layout.addView(out);
               break;
            case Sensor.TYPE_TEMPERATURE:
               tv.setText("Sensor present (TEMPERATURE): \n" + sensor.getName());
               sm.registerListener(getSensorListener(out), sensor, SensorManager.SENSOR_DELAY_UI);
               layout.addView(out);
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