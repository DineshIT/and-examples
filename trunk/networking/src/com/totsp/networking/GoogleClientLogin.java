package com.totsp.networking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.totsp.networking.data.GoogleContactHandler;
import com.totsp.networking.data.HttpHelper;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Android Apache HTTP example demonstrating using the ClientLogin feature of the Google Data APIs
 * (obtain a token, and send it as a header with subsequent requests).
 * 
 * TODO does *NOT* properly respond to Captcha authentication redirects, needs to.
 * TODO most Google APIs use OAuth now also, if possible, that is preferred
 * 
 * @author ccollins
 * 
 */
public class GoogleClientLogin extends Activity {

   private static final String URL_GET_GTOKEN = "https://www.google.com/accounts/ClientLogin";
   private static final String URL_GET_CONTACTS_PREFIX = "http://www.google.com/m8/feeds/contacts/"; // user%40gmail.com
   private static final String URL_GET_CONTACTS_SUFFIX = "/full";
   private static final String GTOKEN_AUTH_HEADER_NAME = "Authorization";
   private static final String GTOKEN_AUTH_HEADER_VALUE_PREFIX = "GoogleLogin auth=";
   private static final String PARAM_ACCOUNT_TYPE = "accountType";
   private static final String PARAM_ACCOUNT_TYPE_VALUE = "HOSTED_OR_GOOGLE";
   private static final String PARAM_EMAIL = "Email";
   private static final String PARAM_PASSWD = "Passwd";
   private static final String PARAM_SERVICE = "service";
   private static final String PARAM_SERVICE_VALUE = "cp"; // google contacts
   private static final String PARAM_SOURCE = "source";
   private static final String PARAM_SOURCE_VALUE = "and-examples-networking-1.0";

   private String tokenValue;

   private EditText emailAddress;
   private EditText password;
   private Button getContacts;
   private Button getToken;
   private Button clearToken;
   private TextView tokenText;

   private ListView listView;
   private ArrayAdapter<String> adapter;

   @Override
   public void onCreate(final Bundle icicle) {
      super.onCreate(icicle);
      setContentView(R.layout.google_client_login);

      emailAddress = (EditText) findViewById(R.id.gclient_email);
      password = (EditText) findViewById(R.id.gclient_password);

      getToken = (Button) findViewById(R.id.gclientgettoken_button);
      clearToken = (Button) findViewById(R.id.gclientcleartoken_button);
      getContacts = (Button) findViewById(R.id.gclientgetcontacts_button);
      tokenText = (TextView) findViewById(R.id.gclient_token);

      listView = (ListView) findViewById(R.id.contactslistview);
      listView.setEmptyView(findViewById(R.id.empty));

      getToken.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            GoogleClientLogin.this.bindListView(null);

            if ((GoogleClientLogin.this.tokenValue == null) || GoogleClientLogin.this.tokenValue.equals("")) {
               Log.d(Constants.LOG_TAG, "token NOT set, getting it");
               GoogleClientLogin.this.getToken(GoogleClientLogin.this.emailAddress.getText().toString(),
                        GoogleClientLogin.this.password.getText().toString());
            } else {
               Toast.makeText(GoogleClientLogin.this,
                        "token already set, not re-getting it (clear it first if you want a new one)",
                        Toast.LENGTH_LONG).show();
            }
         }
      });

      clearToken.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            GoogleClientLogin.this.bindListView(null);
            GoogleClientLogin.this.tokenText.setText("");
            GoogleClientLogin.this.tokenValue = null;
         }
      });

      getContacts.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            if ((GoogleClientLogin.this.tokenValue != null) && !GoogleClientLogin.this.tokenValue.equals("")) {
               GoogleClientLogin.this.getContacts(GoogleClientLogin.this.emailAddress.getText().toString(),
                        GoogleClientLogin.this.tokenValue);
            } else {
               Toast.makeText(GoogleClientLogin.this, "cannot get contacts without token (get token first)",
                        Toast.LENGTH_LONG).show();
            }
         }
      });
   };

   private void bindListView(ArrayList<String> titles) {
      // if you have a lot of data you will want to use a CursorAdapter (or other custom adapter)
      // for this simple case, not much data, can get away with ArrayAdapter
      if (titles == null) {
         titles = new ArrayList<String>(0);
      }
      adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
      listView.setAdapter(adapter);
   }

   private class RequestBean {
      String url;
      HashMap<String, String> headers;
      HashMap<String, String> params;
   }

   private void getContacts(final String email, final String token) {
      HashMap<String, String> headers = new HashMap<String, String>();
      headers.put(GoogleClientLogin.GTOKEN_AUTH_HEADER_NAME, GoogleClientLogin.GTOKEN_AUTH_HEADER_VALUE_PREFIX + token);

      String encEmail = email;
      try {
         encEmail = URLEncoder.encode(encEmail, "UTF-8");
      } catch (UnsupportedEncodingException e) {
         Log.e(Constants.LOG_TAG, e.getMessage(), e);
      }
      String url = GoogleClientLogin.URL_GET_CONTACTS_PREFIX + encEmail + GoogleClientLogin.URL_GET_CONTACTS_SUFFIX;

      RequestBean requestBean = new RequestBean();
      requestBean.url = url;
      requestBean.headers = headers;
      new GetContactsTask().execute(requestBean);
   }

   private void getToken(final String email, final String pass) {
      HashMap<String, String> params = new HashMap<String, String>();
      params.put(GoogleClientLogin.PARAM_ACCOUNT_TYPE, GoogleClientLogin.PARAM_ACCOUNT_TYPE_VALUE);
      params.put(GoogleClientLogin.PARAM_EMAIL, email);
      params.put(GoogleClientLogin.PARAM_PASSWD, pass);
      params.put(GoogleClientLogin.PARAM_SERVICE, GoogleClientLogin.PARAM_SERVICE_VALUE);
      params.put(GoogleClientLogin.PARAM_SOURCE, GoogleClientLogin.PARAM_SOURCE_VALUE);

      RequestBean requestBean = new RequestBean();
      requestBean.url = GoogleClientLogin.URL_GET_GTOKEN;
      requestBean.params = params;

      new GetTokenTask().execute(requestBean);
   }

   // AsyncTask
   private class GetContactsTask extends AsyncTask<RequestBean, Void, ArrayList<String>> {
      private final ProgressDialog dialog = new ProgressDialog(GoogleClientLogin.this);

      private final HttpHelper httpHelper = new HttpHelper();

      // can use UI thread here
      @Override
      protected void onPreExecute() {
         dialog.setMessage("Performing Get Contacts request...");
         dialog.show();
      }

      // automatically done on worker thread (separate from UI thread)
      @Override
      protected ArrayList<String> doInBackground(final RequestBean... args) {
         RequestBean bean = args[0];
         String result = httpHelper.performGet(bean.url, null, null, bean.headers);
         GoogleContactHandler handler = new GoogleContactHandler();
         try {
            Xml.parse(new ByteArrayInputStream(result.getBytes("UTF-8")), Xml.Encoding.UTF_8, handler);
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
         return handler.getTitles();
      }

      // can use UI thread here
      @Override
      protected void onPostExecute(final ArrayList<String> result) {
         if (dialog.isShowing()) {
            dialog.dismiss();
         }

         GoogleClientLogin.this.bindListView(result);
      }
   }

   // AsyncTask
   private class GetTokenTask extends AsyncTask<RequestBean, Void, String> {
      private final ProgressDialog dialog = new ProgressDialog(GoogleClientLogin.this);

      private final HttpHelper httpHelper = new HttpHelper();

      // can use UI thread here
      @Override
      protected void onPreExecute() {
         dialog.setMessage("Performing Get Token request...");
         dialog.show();
      }

      // automatically done on worker thread (separate from UI thread)
      @Override
      protected String doInBackground(final RequestBean... args) {
         RequestBean requestBean = args[0];
         return httpHelper.performPost(HttpHelper.MIME_FORM_ENCODED, requestBean.url, null, null, null,
                  requestBean.params);
      }

      // can use UI thread here
      @Override
      protected void onPostExecute(final String result) {
         if (dialog.isShowing()) {
            dialog.dismiss();
         }

         String authToken = result;
         if ((authToken != null) && authToken.contains("Auth=")) {
            authToken = authToken.substring(authToken.indexOf("Auth=") + 5, authToken.length()).trim();
         } else {
            Toast.makeText(GoogleClientLogin.this, "Unable to obtain ClientLogin token, invalid response.",
                     Toast.LENGTH_LONG);
         }

         Log.d(Constants.LOG_TAG, "authToken - " + authToken);

         GoogleClientLogin.this.tokenValue = authToken;
         GoogleClientLogin.this.tokenText.setText(authToken);
      }
   }
}
