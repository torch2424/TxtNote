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
import android.widget.TextView;
import android.widget.Toast;

public class ReadNote extends Activity {
	
	String totalText = "";
	String fileTitle = "";
	
	//I premade read note menu in xml to put it a scroll view alot easier

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_note);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//getting the intent and file title from read note menu
		Intent intent = getIntent();
		fileTitle = intent.getStringExtra(ReadNoteMenu.EXTRA_MESSAGE);
		
		// inserting text for title
	    TextView titleView = (TextView)findViewById(R.id.noteTitle);
	    titleView.setText(fileTitle);
	    
	    //find file in directory
		File fileLocation =new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TxtNote" + "/" + fileTitle);
		//create a Scanner with our file location
		Scanner sc; 
		//put every line into newline, then cacatanate that line to total text while adding a newline symbol to the end
		String newLine = "";
		totalText = "";
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
		// inserting text for note contents
	    TextView readNote = (TextView)findViewById(R.id.noteText);
	    readNote.setText(totalText);
	    
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
		getMenuInflater().inflate(R.menu.read_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		
		switch (item.getItemId())
        {
        case R.id.deleteNote:
        	//creating alert dialog to confirm delete, gotten from android devs
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.deleteConfirm)
                   .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                    	 //getting file name from intent
                       	Intent intent = getIntent();
                   		String fileTitle = intent.getStringExtra(ReadNoteMenu.EXTRA_MESSAGE);
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
                        //make intent to go back to menu (in case of title change), and clear all previous activities.
                       	//using context cause there in a method
                    	Intent menuIntent = new Intent(context, ReadNoteMenu.class);
                    	menuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    	startActivity(menuIntent);
                    	//finish this activity
                    	finish();
                       }
                   })
                   .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           // User cancelled the dialog
                       }
                   });
            // Create the Alert and return true for the case
            builder.create();
            builder.show();
        	return true;
        }
		
		switch (item.getItemId())
        {
        case R.id.editNote:
    		//file text is total text
        	//file title is file title
    		//creating intent
    		Intent editIntent = new Intent(this, EditNote.class);
    		//putting note text into intent
    		//first part of put extra is what to name fetching of intent so creating 
    		editIntent.putExtra("TEXTBODY", totalText);
    		editIntent.putExtra("TEXTTITLE", fileTitle);
    		startActivity(editIntent);
        	return true;
        }
		
		
		switch (item.getItemId()) {
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
