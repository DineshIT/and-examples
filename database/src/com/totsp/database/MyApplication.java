package com.totsp.database;

import android.app.Application;

public class MyApplication extends Application {

   private DataHelper dataHelper;   
   private Book selectedBook;
   
   @Override
   public void onCreate() {
      super.onCreate();
      this.dataHelper = new DataHelper(this);      
   }   
   
   @Override
   public void onTerminate() {
      // NOTE - this method is not guaranteed to be called
      this.dataHelper.cleanup(); 
      this.selectedBook = null;
      super.onTerminate();      
   }

   public DataHelper getDataHelper() {
      return this.dataHelper;
   }

   public void setDataHelper(DataHelper dataHelper) {
      this.dataHelper = dataHelper;
   }

   public Book getSelectedBook() {
      return this.selectedBook;
   }

   public void setSelectedBook(Book selectedBook) {
      this.selectedBook = selectedBook;
   }   
   
   // so that onSaveInstanceState/onRestoreInstanceState can use with just saved title
   public void establishSelectedBook(String title) {
      this.selectedBook = this.dataHelper.selectBook(title);
   }
}
