package com.totsp.database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.totsp.database.data.Book;

import java.util.ArrayList;

public class Main extends Activity {

   public static final String LOG_TAG = "Database";

   private static final int MENU_BOOK_ADD = 0;
   private static final int MENU_MANAGE_DATA = 1;

   private ListView listView;
   private ArrayAdapter<Book> adapter;
   private MyApplication application;
   private ArrayList<Book> books;

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      application = (MyApplication) getApplication();

      // crude, but works for small data set - just select all 
      books = application.getDataHelper().selectAllBooks();

      // use a ListView to show the data
      listView = (ListView) findViewById(R.id.booklistview);
      listView.setEmptyView(findViewById(R.id.empty));
      listView.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(final AdapterView<?> parent, final View v, final int index, final long id) {
            Toast.makeText(Main.this, "Selected book - " + Main.this.books.get(index).toStringFull(),
                     Toast.LENGTH_SHORT).show();
         }
      });

      bindListView(books);
   }

   private void bindListView(final ArrayList<Book> books) {
      // if you have a lot of data you will want to use a CursorAdapter (or other custom adapter)
      // for this simple case, not much data, can get away with ArrayAdapter
      Log.d(Main.LOG_TAG, "books size - " + books.size());
      adapter = new ArrayAdapter<Book>(this, android.R.layout.simple_list_item_1, books);
      listView.setAdapter(adapter);
   }

   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      menu.add(0, Main.MENU_BOOK_ADD, 0, "Add Book").setIcon(android.R.drawable.ic_menu_add);
      menu.add(0, Main.MENU_MANAGE_DATA, 1, "Manage DB").setIcon(android.R.drawable.ic_menu_manage);
      return super.onCreateOptionsMenu(menu);
   }

   @Override
   public boolean onOptionsItemSelected(final MenuItem item) {
      switch (item.getItemId()) {
         case MENU_BOOK_ADD:
            startActivity(new Intent(Main.this, BookAdd.class));
            return true;
         case MENU_MANAGE_DATA:
            startActivity(new Intent(Main.this, ManageData.class));
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }
}