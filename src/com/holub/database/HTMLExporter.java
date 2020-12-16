/*
 * 20172129 Yesol Park
 * 20-2 Design Pattern
 * Must extend function
 * #1. HTML exporter
 */

package com.holub.database;

import java.io.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class HTMLExporter implements Table.Exporter
{	
	private final Writer out;
	private 	  int	 width;
	
	public HTMLExporter( Writer out )
	{	this.out = out;
	}

	public void storeMetadata( String tableName,
							   int width,
							   int height,
							   Iterator columnNames ) throws IOException

	{	
		out.write("<html>");
		out.write("<head><title>DP2020Project - HTML Exporter</head></title>");
		out.write("<body>");
		out.write("<table border=\"1\" bordercolor=\"blue\" center\">");
			
		out.write("<tr>");
		while( columnNames.hasNext()) {
			out.write("<td><b>");
			out.write(columnNames.next().toString());
			out.write("</b></td>");
		}
		out.write("</tr>");
		
		storeRow( columnNames );
	}
	

	public void storeRow( Iterator data ) throws IOException
	{	
		int i = width;
		
		while( data.hasNext() )
		{	
			Object datum = data.next();
			if( datum != null ) {
				out.write("<td>");
	    		out.write(datum.toString());
	    		out.write("</td>");
			}
		}
		out.write("<tr>");
	}
	

	public void startTable() throws IOException {/*nothing to do*/}
	public void endTable()   throws IOException {/*nothing to do*/}
	
	public static class Test
	{ 	public static void main( String[] args ) throws IOException
		{	
			Table people = TableFactory.create( "people",
						   new String[]{ "First", "Last"		} );
			people.insert( new String[]{ "Allen",	"Holub" 	} );
			people.insert( new String[]{ "Ichabod",	"Crane" 	} );
			people.insert( new String[]{ "Rip",		"VanWinkle" } );
			people.insert( new String[]{ "Goldie",	"Locks" 	} );
			
			String path = "C:/DP2020Project/people.html";
			Writer out = new FileWriter(path);
			people.export(new HTMLExporter(out));
			out.close();
		}
	}
}
