/*
 * 20172129 Yesol Park
 * 20-2 Design Pattern
 * Must extend function
 * #2. XML exporter
 */

package com.holub.database;

import java.io.*;
import java.util.*;


public class XMLExporter implements Table.Exporter
{	
	private final Writer out;
	public String[]	columnHeads;
	public int iter = 0;
	
	public XMLExporter( Writer out )
	{	this.out = out;
	}

	public void storeMetadata( String tableName,
							   int width,
							   int height,
							   Iterator columnNames ) throws IOException

	{	
		columnHeads = new String[width];
		int columnIndex = 0;
		while( columnNames.hasNext() )
			columnHeads[columnIndex++] = columnNames.next().toString();
		out.write("<root>\n");
		out.write("<title>"+ tableName +"</title>\n");
		storeRow( columnNames );
	}
	

	public void storeRow( Iterator data ) throws IOException
	{	
		int i = 0;
		if(iter != 0) 
			out.write("<data>");
		while( data.hasNext() )
		{	
			Object datum = data.next();
			
			if( datum != null ){
				out.write("<"+columnHeads[i]+">");
	    		out.write(datum.toString());
	    		out.write("</"+columnHeads[i]+">");
	    		i++;
			}
		}
		if(iter != 0) 
			out.write("</data>\n");
		iter++;
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
			
			String path = "C:/DP2020Project/people.xml";
			Writer out = new FileWriter(path);
			
			people.export(new XMLExporter(out));
			out.write("</root>");
			out.close();
		}
	}
}
