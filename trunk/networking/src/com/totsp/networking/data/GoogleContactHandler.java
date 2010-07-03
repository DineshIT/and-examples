package com.totsp.networking.data;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Title only SAX DefaultHandler impl for Google Contacts feed.
 * 
 * @author ccollins
 */
public class GoogleContactHandler extends DefaultHandler {

   private static final String ENTRY = "entry";

   private ArrayList<String> titles;

   private boolean inEntry;
   StringBuilder sb;

   public GoogleContactHandler() {
   }

   @Override
   public void startDocument() throws SAXException {
      this.titles = new ArrayList<String>();
      this.sb = new StringBuilder();
   }

   @Override
   public void endDocument() throws SAXException {
   }

   @Override
   public void startElement(final String namespaceURI, final String localName, final String qName, final Attributes atts)
      throws SAXException {

      if (localName.equals(GoogleContactHandler.ENTRY)) {
         this.inEntry = true;
      }

      if (this.inEntry && localName.equals("title")) {
         this.sb = new StringBuilder();
      }
   }

   @Override
   public void endElement(final String namespaceURI, final String localName, final String qName) throws SAXException {
      if (localName.equals(GoogleContactHandler.ENTRY)) {
         if (this.inEntry) {
            this.inEntry = false;
         }
      }

      String bufferContents = sb.toString().replaceAll("\\s+", " ");

      if (this.inEntry && localName.equals("title")) {
         titles.add(bufferContents);
      }
   }

   @Override
   public void characters(final char ch[], final int start, final int length) {
      this.sb.append(new String(ch, start, length));
   }

   /*
   private String getAttributeValue(final String attName, final Attributes atts) {
      String result = null;
      for (int i = 0; i < atts.getLength(); i++) {
         String thisAtt = atts.getLocalName(i);
         if (attName.equals(thisAtt)) {
            result = atts.getValue(i);
            break;
         }
      }
      return result;
   }
   */

   public ArrayList<String> getTitles() {
      return this.titles;
   }
}
