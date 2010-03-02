package com.totsp.database;


public final class Book {

   private long id;
   private String title = "";
   private String authors = "";

   public Book() {
   }
   
   public Book(String title, String authors) {
      this.id = 0L;
      this.title = title;  
      this.authors = authors;
   }
   
   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }
   
   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getAuthors() {
      return this.authors;
   }

   public void setAuthors(String authors) {
      this.authors = authors;
   }
   
   public String toString() {
      return this.title;
   }

}