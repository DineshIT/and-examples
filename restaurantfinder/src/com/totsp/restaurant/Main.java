package com.totsp.restaurant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Main extends Activity {

   private static final int MENU_GET_REVIEWS = Menu.FIRST;

   private Spinner cuisine;
   private Button grabReviews;
   private EditText location;

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      this.setContentView(R.layout.main);

      this.location = (EditText) this.findViewById(R.id.location);
      this.cuisine = (Spinner) this.findViewById(R.id.cuisine);
      this.grabReviews = (Button) this.findViewById(R.id.get_reviews_button);

      ArrayAdapter<String> cuisines =
               new ArrayAdapter<String>(this, R.layout.spinner_view, this.getResources().getStringArray(
                        R.array.cuisines));
      cuisines.setDropDownViewResource(R.layout.spinner_view_dropdown);
      this.cuisine.setAdapter(cuisines);

      this.grabReviews.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            Main.this.handleGetReviews();
         }
      });
   }

   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      super.onCreateOptionsMenu(menu);
      menu.add(0, Main.MENU_GET_REVIEWS, 0, R.string.menu_get_reviews).setIcon(android.R.drawable.ic_menu_more);
      return true;
   }

   @Override
   public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
      switch (item.getItemId()) {
      case MENU_GET_REVIEWS:
         this.handleGetReviews();
         return true;
      }
      return super.onMenuItemSelected(featureId, item);
   }

   private void handleGetReviews() {
      if (!this.validate()) {
         return;
      }

      // use the "Application" to store global state (can go beyond primitives and Strings -
      // beyond extras - if needed)
      RestaurantFinderApplication application = (RestaurantFinderApplication) this.getApplication();
      application.setReviewCriteriaCuisine(this.cuisine.getSelectedItem().toString());
      application.setReviewCriteriaLocation(this.location.getText().toString());

      // call next Activity, VIEW_LIST
      Intent intent = new Intent(this, ReviewList.class);
      this.startActivity(intent);
   }

   // validate form fields
   private boolean validate() {
      boolean valid = true;
      StringBuilder validationText = new StringBuilder();
      if ((this.location.getText() == null) || this.location.getText().toString().equals("")) {
         validationText.append(this.getResources().getString(R.string.location_not_supplied_message));
         valid = false;
      }
      if (!valid) {
         new AlertDialog.Builder(this).setTitle(this.getResources().getString(R.string.alert_label)).setMessage(
                  validationText.toString()).setPositiveButton("Continue",
                  new android.content.DialogInterface.OnClickListener() {
                     public void onClick(final DialogInterface dialog, final int arg1) {
                        // in this case, don't need to do anything other than close alert
                     }
                  }).show();
         validationText = null;
      }
      return valid;
   }
}
