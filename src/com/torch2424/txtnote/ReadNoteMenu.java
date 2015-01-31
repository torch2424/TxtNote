package com.torch2424.txtnote;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ReadNoteMenu extends Activity 
{
	public final static String EXTRA_MESSAGE = "com.torch2424.txtnote.MESSAGE";
	
	//creating context globally for long click
	Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_note_menu);
		// Show the Up button in the action bar.
		setupActionBar();
		//creating a list view
		final ListView listView = (ListView) findViewById(R.id.ListView1);
	     //path to the notes folder
		File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TxtNote");
		//putting the list of file names into an array
		String[] fileNames = directory.list();
		//setting up adapter and putting in the list view
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileNames);		
		listView.setAdapter(adapter);
		
		// listener for when someone clicks a file
		OnItemClickListener listclick = new OnItemClickListener() 
		{
		    public void onItemClick(AdapterView parent, View v, int position, long id) 
		    {
		    	 String  selectedFile    = (String) listView.getItemAtPosition(position);
		    	 Intent intent = new Intent(ReadNoteMenu.this, ReadNote.class);
		    	 intent.putExtra(EXTRA_MESSAGE, selectedFile);
	        	 startActivity(intent);
		    }
		};

		listView.setOnItemClickListener(listclick); 
		
		//have to create final alert dialog builder out here for long click
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//create another for delete note
		final AlertDialog.Builder delete = new AlertDialog.Builder(this);
		//listener for long click
		OnItemLongClickListener longClick = new OnItemLongClickListener() {
			  public boolean onItemLongClick(AdapterView parent, View view, final int position, long id) 
			  {
				  //setting up alert dialog
		            builder.setMessage(R.string.longConfirm)
		                   .setPositiveButton(R.string.editNote, new DialogInterface.OnClickListener() 
		                   {
		                       public void onClick(DialogInterface dialog, int id) 
		                       {
		                    	//get file title
		                    	String  fileTitle    = (String) listView.getItemAtPosition(position);
		                    	//find file in directory
		                   		File fileLocation =new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TxtNote" + "/" + fileTitle);
		                   		//create a Scanner with our file location
		                   		Scanner sc; 
		                   		//put every line into newline, then cacatanate that line to total text while adding a newline symbol to the end
		                   		String newLine = "";
		                   		String totalText = "";
		                   		try 
		                   		{
		                   			sc = new Scanner(fileLocation);
		                   			while (sc.hasNextLine() == true)
		                   			{
		                   				newLine = sc.nextLine();
		                   				totalText = totalText + newLine + "\n";
		                   			}
		                   		} 
		                   		catch (FileNotFoundException e) 
		                   		{
		                   			// TODO Auto-generated catch block
		                   			e.printStackTrace();
		                   		}
		                   		
		                   		//transfering files to intent
		                   		Intent editIntent = new Intent(context, EditNote.class);
		                		//putting note text into intent
		                		//first part of put extra is what to name fetching of intent so creating 
		                		editIntent.putExtra("TEXTBODY", totalText);
		                		editIntent.putExtra("TEXTTITLE", fileTitle);
		                		startActivity(editIntent);
		                       }
		                   })
		                   .setNegativeButton(R.string.deleteNote, new DialogInterface.OnClickListener() 
		                   {
		                       public void onClick(DialogInterface dialog, int id) 
		                       {
		                           delete.setMessage(R.string.deleteConfirm)
		                           .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		                               public void onClick(DialogInterface dialog, int id) {
		                            	 //getting file name from intent
		                            	 String  fileTitle = (String) listView.getItemAtPosition(position);
		                           		//locating file
		                           		File fileLocation =new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TxtNote" + "/" + fileTitle);
		                           		//deleting file
		                           		fileLocation.delete();
		                           		//toast to confirm
		                           		Context context = getApplicationContext();
		                               	CharSequence text = "Note deleted!";
		                               	int duration = Toast.LENGTH_LONG;

		                               	Toast toast = Toast.makeText(context, text, duration);
		                               	toast.show();
		                            	//finish this activity
		                            	finish();
		                            	//use this to refresh/restart activity within itself
		                            	startActivity(getIntent());
		                               }
		                           })
		                           .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		                               public void onClick(DialogInterface dialog, int id) {
		                                   // User cancelled the dialog
		                               }
		                           });
		                    // Create the Alert and return true for the case
		                    delete.create();
		                    delete.show();
		                       }
		                   });
		            // Create the Alert and return true for the case
		            builder.create();
		            builder.show();
		        	return true;
			  }
			};
			listView.setOnItemLongClickListener(longClick);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_note_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
