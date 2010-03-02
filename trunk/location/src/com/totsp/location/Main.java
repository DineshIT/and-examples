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
      this.setContentView(R.layout.main);
      this.layout = (LinearLayout) this.findViewById(R.id.mainlayout);
      this.layoutParams =
               new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
      this.layoutParams.setMargins(0, 10, 0, 0);

      this.crawlProviders();
   }

   private void crawlProviders() {
      LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

      TextView divider = new TextView(this);
      divider.setText("-------------------------------------------------------");
      this.layout.addView(divider);
      
      List<String> providers = lm.getAllProviders();
      for (String p : providers) {
         TextView tv = new TextView(this);
         this.layout.addView(tv, this.layoutParams);
         tv.setText("Provider present (enabled= " + lm.isProviderEnabled(p) + "): " + p);

         TextView lastKnown = new TextView(this);
         TextView updates = new TextView(this);

         lastKnown.setText("last known location - " + lm.getLastKnownLocation(p));
         this.layout.addView(lastKnown, this.layoutParams);

         lm.requestLocationUpdates(p, 500, 5, this.getLocationListener(updates));
         this.layout.addView(updates, this.layoutParams);
         
         TextView div = new TextView(this);
         div.setText("-------------------------------------------------------");
         this.layout.addView(div);
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