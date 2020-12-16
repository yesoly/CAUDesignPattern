package com.holub.database;

import com.holub.tools.ArrayIterator;

import java.io.*;
import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class XMLImporter implements Table.Importer
{	private BufferedReader in;			// null once end-of-file reached
	private String[]        columnNames;
	private String          tableName;
	String line = "";
	// column 이름 : 데이터 순으로 저장
	

	public XMLImporter( Reader in ) throws IOException
	{	
		this.in = new BufferedReader(in);
	}
	
	public String[] parseTagXML(String data)
	{	  
		  Document doc = Jsoup.parse(data);
		  Elements items = doc.getElementsByTag("data");
		  String[] result = null;
		  
		  for(Element item : items) {
			  result = new String[item.childrenSize()];
			  for(int i = 0; i<item.childrenSize(); i++) {
				  Element child_item = item.child(i);
				  result[i] = child_item.nodeName();
			  }
		  }
		  return result;
	}
	
	public Object[] parseDataXML(String data, String tag)
	{	  
		  Document doc = Jsoup.parse(data);
		  Elements items = doc.getElementsByTag(tag);
		  
		  Object[] result = null;

		  for(Element item : items) {
			  if (item.childrenSize() == 0) {
				  result =  new Object[1];
				  result[0] = item.text();
				  return result;
			  }
			  else {
				  result =  new Object[item.childrenSize()];
				  for(int i = 0; i<item.childrenSize(); i++) {
					  Element child_item = item.child(i);
					  result[i] = child_item.text();
			  	}
			  }
		  }
		  return result;
	}
	
	
	public void startTable()			throws IOException
	{	
		Object[] 	item = null;
		while((line = in.readLine()) != null ) {
			if(line.contains("<title")){
				tableName = parseDataXML(line, "title")[0].toString();
			}
			else if(line.contains("<data")){
				columnNames = parseTagXML(line);
				break;
			}
		}
	}
	
	public String loadTableName()		throws IOException
	{	
		return tableName;
	}
	public int loadWidth()			    throws IOException
	{	
		return columnNames.length;
	}
	public Iterator loadColumnNames()	throws IOException
	{	return new ArrayIterator(columnNames);  //{=CSVImporter.ArrayIteratorCall}
	}

	public Iterator loadRow()			throws IOException	
	{	
		Iterator row = null;
		if(line != null) {
			if(line.contains("<data")){
				Object[] item = parseDataXML(line, "data");
				row = new ArrayIterator(item);
			}
			line = in.readLine();
		}
		return row;
	}

	public void endTable() throws IOException {}
	
	public static class Test
	{ 	public static void main( String[] args ) throws IOException
		{		
			String path = "C:/DP2020Project/people.xml";
			Reader in = new FileReader(path);  
			Table people = new ConcreteTable(new XMLImporter(in));
			System.out.println(people.toString());
			in.close();
		}
	}
}


