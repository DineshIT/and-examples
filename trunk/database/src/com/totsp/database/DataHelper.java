package com.totsp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Oversimplified Android DB example.
 * Includes SQLite foreign keys and unique constraints - though contrived example.
 * 
 * @author ccollins
 *
 */
public class DataHelper {

   private static final String DATABASE_NAME = "sample.db";
   private static final int DATABASE_VERSION = 1;

   private static final String BOOK_TABLE = "book";
   private static final String AUTHOR_TABLE = "author";
   private static final String BOOKAUTHOR_TABLE = "bookauthor";

   private Context context;
   private SQLiteDatabase db;

   // can use precompiled statements - put em here, then init in ctor (this.db.compileStatement(stmt))

   public DataHelper(Context context) {
      this.context = context;
      OpenHelper openHelper = new OpenHelper(this.context);
      this.db = openHelper.getWritableDatabase();

      if (openHelper.isDbCreated()) {
         // insert default data here if needed
      }
   }

   public SQLiteDatabase getDb() {
      return this.db;
   }

   public void resetDbConnection() {
      Log.i(Main.LOG_TAG, "resetting database connection (close and re-open).");
      this.cleanup();
      this.db =
               SQLiteDatabase.openDatabase("/data/data/com.totsp.database/databases/sample.db", null,
                        SQLiteDatabase.OPEN_READWRITE);
   }

   public void cleanup() {
      if (this.db != null && this.db.isOpen()) {
         this.db.close();
      }
   }

   //
   // DB methods
   //

   // book      
   public long insertBook(Book b) {
      long bookId = 0L;
      if (b != null && b.getTitle() != null) {

         Book bookExists = this.selectBook(b.getTitle());
         if (bookExists != null) {
            return bookExists.getId();
         }

         // begin transaction
         this.db.beginTransaction();

         try {
            // insert authors as needed
            ArrayList<Long> authorIds = new ArrayList<Long>();
            if (b.getAuthors() != null) {
               String[] names = StringUtil.expandComma(b.getAuthors());
               for (String name : names) {
                  Author authorExists = this.selectAuthor(name);
                  if (authorExists == null) {
                     authorIds.add(this.insertAuthor(new Author(name)));
                  } else {
                     authorIds.add(authorExists.getId());
                  }
               }
            }

            // insert book
            ContentValues values = new ContentValues();
            values.put(DataConstants.TITLE, b.getTitle());
            bookId = db.insert(BOOK_TABLE, null, values);

            // insert bookauthors
            this.insertBookAuthorData(bookId, authorIds);

            // set transaction success
            db.setTransactionSuccessful();
         } catch (SQLException e) {
            Log.e(Main.LOG_TAG, "Error inserting book", e);
         } finally {
            this.db.endTransaction();
         }
      } else {
         throw new IllegalArgumentException("Error, book cannot be null, and must have a unique title");
      }
      return bookId;
   }

   public Book selectBook(long id) {
      Book b = null;
      Cursor c =
               this.db.query(BOOK_TABLE, new String[] { DataConstants.TITLE }, DataConstants.BOOKID + " = ?",
                        new String[] { String.valueOf(id) }, null, null, null, "1");
      if (c.moveToFirst()) {
         b = new Book();
         b.setId(id);
         b.setTitle(c.getString(0));
      }
      if (c != null && !c.isClosed()) {
         c.close();
      }

      // include authors with sep query (hack - should do a join/group for this)
      if (b != null) {
         this.appendAuthors(b);
      }

      return b;
   }

   public Book selectBook(String title) {
      Book b = null;
      Cursor c =
               this.db.query(BOOK_TABLE, new String[] { DataConstants.BOOKID }, DataConstants.TITLE + " = ?",
                        new String[] { title }, null, null, null, "1");
      if (c.moveToFirst()) {
         b = this.selectBook(c.getLong(0));
      }
      if (c != null && !c.isClosed()) {
         c.close();
      }
      return b;
   }

   public ArrayList<Book> selectAllBooks() {
      ArrayList<Book> list = new ArrayList<Book>();
      Cursor c =
               this.db.query(BOOK_TABLE, new String[] { DataConstants.BOOKID, DataConstants.TITLE }, null, null, null,
                        null, DataConstants.TITLE + " desc", null);
      if (c.moveToFirst()) {
         do {
            Book b = new Book();
            b.setId(c.getLong(0));
            b.setTitle(c.getString(1));
            list.add(b);
         } while (c.moveToNext());
      }
      if (c != null && !c.isClosed()) {
         c.close();
      }

      // add authors (again, a hack)
      for (Book b : list) {
         this.appendAuthors(b);
      }
      return list;
   }

   private void appendAuthors(Book b) {
      ArrayList<Author> authors = this.selectAuthorsByBook(b.getId());
      String[] names = new String[authors.size()];
      int i = 0;
      for (Author a : authors) {
         names[i] = a.getName();
         i++;
      }
      b.setAuthors(StringUtil.contractComma(names));
   }

   // book-author data
   public void insertBookAuthorData(long bookId, ArrayList<Long> authorIds) {
      for (Long authorId : authorIds) {
         ContentValues values = new ContentValues();
         values.put(DataConstants.BOOKID, bookId);
         values.put(DataConstants.AUTHORID, authorId);
         db.insert(BOOKAUTHOR_TABLE, null, values);
      }
   }

   // author
   public long insertAuthor(Author a) {
      long authorId = 0L;
      if (a != null && a.getName() != null) {

         Author authorExists = this.selectAuthor(a.getName());
         if (authorExists != null) {
            return authorExists.getId();
         }

         ContentValues values = new ContentValues();
         values.put(DataConstants.NAME, a.getName());
         authorId = db.insert(AUTHOR_TABLE, null, values);
      }
      return authorId;
   }

   public Author selectAuthor(long id) {
      Author a = null;
      Cursor c =
               this.db.query(AUTHOR_TABLE, new String[] { DataConstants.NAME }, DataConstants.AUTHORID + " = ?",
                        new String[] { String.valueOf(id) }, null, null, null, "1");
      if (c.moveToFirst()) {
         a = new Author();
         a.setId(id);
         a.setName(c.getString(0));
      }
      if (c != null && !c.isClosed()) {
         c.close();
      }
      return a;
   }

   public Author selectAuthor(String name) {
      Author a = null;
      Cursor c =
               this.db.query(AUTHOR_TABLE, new String[] { DataConstants.AUTHORID }, DataConstants.NAME + " = ?",
                        new String[] { name }, null, null, null, "1");
      if (c.moveToFirst()) {
         a = this.selectAuthor(c.getLong(0));
      }
      if (c != null && !c.isClosed()) {
         c.close();
      }
      return a;
   }

   public ArrayList<Author> selectAllAuthors() {
      ArrayList<Author> list = new ArrayList<Author>();
      Cursor c =
               this.db.query(AUTHOR_TABLE, new String[] { DataConstants.AUTHORID, DataConstants.NAME }, null, null,
                        null, null, DataConstants.NAME + " desc", null);
      if (c.moveToFirst()) {
         do {
            Author a = new Author();
            a.setId(c.getLong(0));
            a.setName(c.getString(1));
            list.add(a);
         } while (c.moveToNext());
      }
      if (c != null && !c.isClosed()) {
         c.close();
      }
      return list;
   }

   public ArrayList<Author> selectAuthorsByBook(long bookId) {
      ArrayList<Author> authors = new ArrayList<Author>();
      Cursor c =
               this.db.query(BOOKAUTHOR_TABLE, new String[] { DataConstants.AUTHORID }, DataConstants.BOOKID + " = ?",
                        new String[] { String.valueOf(bookId) }, null, null, null);
      if (c.moveToFirst()) {
         do {
            Author a = this.selectAuthor(c.getLong(0));
            authors.add(a);
         } while (c.moveToNext());
      }
      if (c != null && !c.isClosed()) {
         c.close();
      }
      return authors;
   }

   // super delete - clears all tables
   public void deleteAllDataYesIAmSure() {
      Log.i(Main.LOG_TAG, "deleting all data from database - deleteAllYesIAmSure invoked");
      this.db.beginTransaction();
      try {
         this.db.delete(AUTHOR_TABLE, null, null);
         this.db.delete(BOOKAUTHOR_TABLE, null, null);
         this.db.delete(BOOK_TABLE, null, null);
         this.db.setTransactionSuccessful();
      } finally {
         this.db.endTransaction();
      }
      this.db.execSQL("vacuum");
   }

   //
   // end DB methods
   //  

   //
   // SQLiteOpenHelper   
   //

   private static class OpenHelper extends SQLiteOpenHelper {

      private boolean dbCreated;

      OpenHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

      @Override
      public void onCreate(SQLiteDatabase db) {

         // using StringBuilder here because it is easier to read/reuse lines
         StringBuilder sb = new StringBuilder();

         // book table
         sb.append("CREATE TABLE " + BOOK_TABLE + " (");
         sb.append(DataConstants.BOOKID + " INTEGER PRIMARY KEY, ");
         sb.append(DataConstants.TITLE + " TEXT");
         sb.append(");");
         db.execSQL(sb.toString());

         // author table
         sb.setLength(0);
         sb.append("CREATE TABLE " + AUTHOR_TABLE + " (");
         sb.append(DataConstants.AUTHORID + " INTEGER PRIMARY KEY, ");
         sb.append(DataConstants.NAME + " TEXT");
         sb.append(");");
         db.execSQL(sb.toString());

         // bookauthor join table
         sb.setLength(0);
         sb.append("CREATE TABLE " + BOOKAUTHOR_TABLE + " (");
         sb.append(DataConstants.BOOKAUTHORID + " INTEGER PRIMARY KEY, ");
         sb.append(DataConstants.BOOKID + " INTEGER, ");
         sb.append(DataConstants.AUTHORID + " INTEGER, ");
         sb.append("FOREIGN KEY(" + DataConstants.BOOKID + ") REFERENCES " + BOOK_TABLE + "(" + DataConstants.BOOKID
                  + "), ");
         sb.append("FOREIGN KEY(" + DataConstants.AUTHORID + ") REFERENCES " + AUTHOR_TABLE + "("
                  + DataConstants.AUTHORID + ") ");
         sb.append(");");
         db.execSQL(sb.toString());

         // constraints 
         db.execSQL("CREATE UNIQUE INDEX uidxBookTitle ON " + BOOK_TABLE + "(" + DataConstants.TITLE
                  + " COLLATE NOCASE)");
         db.execSQL("CREATE UNIQUE INDEX uidxAuthorName ON " + AUTHOR_TABLE + "(" + DataConstants.NAME
                  + " COLLATE NOCASE)");

         this.dbCreated = true;
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         Log.i(Main.LOG_TAG, "SQLiteOpenHelper onUpgrade - oldVersion:" + oldVersion + " newVersion:" + newVersion);
         // export old data first, then upgrade, then import
         db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE);
         db.execSQL("DROP TABLE IF EXISTS " + AUTHOR_TABLE);
         db.execSQL("DROP TABLE IF EXISTS " + BOOKAUTHOR_TABLE);
         onCreate(db);
      }

      public boolean isDbCreated() {
         return this.dbCreated;
      }
   }
}