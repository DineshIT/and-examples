package com.totsp.database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashSet;

public class Main extends Activity {
    
    public static final String LOG_TAG = "Database";
   
    private static final int MENU_BOOK_ADD = 0;
    private static final int MENU_MANAGE_DATA = 1;
    
    private TextView output;
    
    private MyApplication application;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.application = (MyApplication) this.getApplication();
        
        this.output = (TextView) this.findViewById(R.id.output);
        
        // TODO listview and SimpleCursorAdapter
        this.showDataTemp();
    }
    
    private void showDataTemp() {
       HashSet<Book> books = this.application.getDataHelper().selectAllBooks();
       StringBuilder sb = new StringBuilder();
       for (Book b : books) {
          sb.append("book: " + b.getTitle() + " (" + b.getAuthors() + ")\n");
       }
       this.output.setText(sb.toString());
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
          this.startActivity(new Intent(Main.this, BookAdd.class));
          return true;       
       case MENU_MANAGE_DATA:
          this.startActivity(new Intent(Main.this, ManageData.class));
          return true;
       default:
          return super.onOptionsItemSelected(item);
       }
    }
}