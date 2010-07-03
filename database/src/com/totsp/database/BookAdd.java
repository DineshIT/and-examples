package com.totsp.database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.totsp.database.data.Book;

public class BookAdd extends Activity {

   private MyApplication application;

   private EditText title;
   private EditText authors;
   private Button insertButton;

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.bookadd);

      application = (MyApplication) getApplication();

      title = (EditText) findViewById(R.id.title);
      authors = (EditText) findViewById(R.id.authors);

      insertButton = (Button) findViewById(R.id.insertbutton);
      insertButton.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            Toast.makeText(BookAdd.this, BookAdd.this.title.getText() + " " + BookAdd.this.authors.getText(),
                     Toast.LENGTH_LONG).show();
            Book b = new Book(BookAdd.this.title.getText().toString(), BookAdd.this.authors.getText().toString());
            BookAdd.this.application.getDataHelper().insertBook(b);
            BookAdd.this.startActivity(new Intent(BookAdd.this, Main.class));
         }
      });
   }
}