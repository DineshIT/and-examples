package com.totsp.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

      crawlProviders();
   }

   private void crawlProviders() {
      LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

      TextView divider = new TextView(this);
      divider.setText("-------------------------------------------------------");
      layout.addView(divider);

      List<String> providers = lm.getAllProviders();
      for (String p : providers) {
         TextView tv = new TextView(this);
         layout.addView(tv, layoutParams);
         tv.setText("Provider present (enabled= " + lm.isProviderEnabled(p) + "): " + p);

         TextView lastKnown = new TextView(this);
         TextView updates = new TextView(this);

         lastKnown.setText("last known location - " + lm.getLastKnownLocation(p));
         layout.addView(lastKnown, layoutParams);

         lm.requestLocationUpdates(p, 500, 5, getLocationListener(updates));
         layout.addView(updates, layoutParams);

         TextView div = new TextView(this);
         div.setText("-------------------------------------------------------");
         layout.addView(div);
      }
   }

   private LocationListener getLocationListener(final TextView tv) {
      return new LocationListener() {
         public void onLocationChanged(final Location l) {
            tv.setText("update: provider LOCATION CHANGED " + l.getLatitude() + " | " + l.getLongitude());
         }

         public void onProviderDisabled(final String s) {
            tv.setText("update: provider DISABLED");
         }

         public void onProviderEnabled(final String s) {
            tv.setText("update: provider ENABLED");
         }

         public void onStatusChanged(final String s, final int i, final Bundle b) {
            tv.setText("update: provider STATUS CHANGED " + s);
         }
      };
   }
}