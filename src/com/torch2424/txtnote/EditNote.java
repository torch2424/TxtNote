package com.torch2424.txtnote;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class EditNote extends Activity 
{

	//have to declare out here to delete old file later
	//one and two because we have filtitle with .txt and without
	String fileTitle1 = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_note);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//getting intent and setting up entered text boxes
		Intent intent = getIntent();
		fileTitle1 = intent.getStringExtra("TEXTTITLE");
		//removing .txt, this returns a string it doesnt change it, so you have to make it equal to old string
		String fileTitle2 = fileTitle1.replace(".txt", "");
		String fileText = intent.getStringExtra("TEXTBODY");
		//setting up title edit text
		EditText notetitle = (EditText) findViewById(R.id.editText2);
		notetitle.setText(fileTitle2);
		//setting up text edittext
		EditText notetext = (EditText) findViewById(R.id.editText1);
		notetext.setText(fileText);

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
		getMenuInflater().inflate(R.menu.edit_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
	
	
	
	
	
	
	
	//save the note to a file on the sd card
    public void saveEditNote (View view)
    {
    	//note title into a string
    	EditText notetitle = (EditText) findViewById(R.id.editText2);
    	String title = (notetitle.getText().toString()) + ".txt";
    	//deleting old file
    	File oldFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TxtNote/" + fileTitle1);
    	oldFile.delete();
    	//just incase I have to ban alot of characters from title
    	//String allowedCharacters = "[a-zA-Z.? *(){}[]@!]*";
    	//use title.matches(string, charsequence) to do the abve
    	//bugfix if slashes are in title then note doesnt save
    	if (title.contains("/") == true)
    	{
    		Context context = getApplicationContext();
        	CharSequence text = "Your note title contains a character that is not allowed(slashes). Please fix this!";
        	int duration = Toast.LENGTH_SHORT;

        	Toast toast = Toast.makeText(context, text, duration);
        	toast.show();
    	}
    	else if (title.equals(".txt") == true)
    	{
    		Context context = getApplicationContext();
        	CharSequence text = "You need a title!";
        	int duration = Toast.LENGTH_SHORT;

        	Toast toast = Toast.makeText(context, text, duration);
        	toast.show();
    	}
    	else
    	{
    	//note text into a string
    	EditText notetext = (EditText) findViewById(R.id.editText1);
    	//use replace all to get new lines in actual file
    	String note = notetext.getText().toString();
    	//getting directory
    	File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TxtNote");
    	directory.mkdir();
    	String storedir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TxtNote";
    	//creating file and writing strings into note
    	File file = new File(storedir, title);
    	try {
			FileWriter filewriter = new FileWriter(file);
			filewriter.write(note);
	    	filewriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//toast informing note was saved
    	Context context = getApplicationContext();
    	CharSequence text = "Note Saved! Reselect your note to read it!";
    	int duration = Toast.LENGTH_LONG;

    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    	//make intent to go back to menu (in case of title change), and clear all previous activities.
    	Intent intent = new Intent(this, ReadNoteMenu.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    	//finish this activity
    	finish();
    	}

    }

}
