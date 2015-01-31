package com.torch2424.txtnote;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EnterNote extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_note);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.enter_note, menu);
		return true;
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        case R.id.ReadNoteMenu:
        	//checking if there is an sd card. need to implement internal saving as an option, got equals from stack overflow
        	if(Environment.getExternalStorageDirectory().canWrite() == false)
        	{
        		Toast.makeText(this, "TxtNote requires an SD card to save Notes. Please insert an external storage.", Toast.LENGTH_LONG).show();
        	}
        	else
        	{
        	//getting txtnote directory
        	File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TxtNote");
        	if(directory.exists())
        	{
        	 Intent intent = new Intent(this, ReadNoteMenu.class);
        	 startActivity(intent);
        	}
        	else
        	{
        		Context context = getApplicationContext();
            	CharSequence text = "Txtnote directory doesn't exist! You have to save a note before you read one!";
            	int duration = Toast.LENGTH_LONG;

            	Toast toast = Toast.makeText(context, text, duration);
            	toast.show();
        	}
        	
        	}
        	 return true;
        	 
        }
        return false;
    }
        	 
	
	//save the note to a file on the sd card
    public void saveNote (View view)
    {
    	//checking if there is an sd card. need to implement internal saving as an option, got equals from stack overflow
    	if(Environment.getExternalStorageDirectory().canWrite() == false)
    	{
    		Toast.makeText(this, "TxtNote requires an SD card to save Notes. Please insert an external storage.", Toast.LENGTH_LONG).show();
    	}
    	else
    	{
    	//note title into a string
    	EditText notetitle = (EditText) findViewById(R.id.editText2);
    	String title = (notetitle.getText().toString().replaceAll("\\n", "<br />")) + ".txt";
    	//to avoid overwriting files
    	File noOverwrite = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TxtNote/" + title) ;
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
    	//looking for duplicate directories which would overwrite old notes
    	else if (noOverwrite.exists() == true)
    	{
    		Context context = getApplicationContext();
        	CharSequence text = "You cannot use the same title as your other notes! Try another title!";
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
    	
    	//clear text field
    	notetext.setText("");
    	notetitle.setText("");
    	
    	//toast informing note was saved
    	Context context = getApplicationContext();
    	CharSequence text = "Note Saved! Please go to Read Notes from the menu button, or check /TxtNote on the root of your SD card to find it!";
    	int duration = Toast.LENGTH_LONG;

    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    	}
    }
    //else bracket from external sd check
    }

}

