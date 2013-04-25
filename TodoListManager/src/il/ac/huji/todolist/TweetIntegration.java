/**
 * 
 */
package il.ac.huji.todolist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * The Async task to syncronize with twitter
 * @author alonaba
 *
 */
public class TweetIntegration extends AsyncTask<Void, Void, String>{
	/*
	 * The current hash tag 
	 */
	private String _hashtag;
	/*
	 * format of URL
	 */
	private String _format="http://search.twitter.com/search.json?q=%s&rpp=100";
	/*
	 * The URL of twitter
	 */
	private URL _URI;
	/*
	 * The class to update the ToDo application database and parse with new items
	 */
	private TodoDAL _myUpdate;
	/*
	 * The adapter to update the view
	 */
	private MyAdapter _adapter;
	/*
	 * The context
	 */
	private Context _context;
	/*
	 * The dialog to run during communication with twitter
	 */
	private ProgressDialog _progressDialog;
	/*
	 * The local database to save the tweets that already have been seen by user
	 */
	private SQLiteDatabase _tweetsSeen;
	/**
	 * The constructor
	 * @param hashtag - the current hash tag, as defined in preferences or todo if nothing defined
	 * @param context - the current context
	 * @param updateClass - the class to update database and parse with new items
	 * @param update - the adapter to update the view
	 * @throws MalformedURLException - if URL illegal
	 */
	@SuppressWarnings("deprecation")
	public TweetIntegration(String hashtag,Context context, TodoDAL updateClass, MyAdapter update) throws MalformedURLException{
		_hashtag=hashtag;
		_context=context;
		_adapter=update;
		TweetsDB helper=new TweetsDB(_context);
		_tweetsSeen=helper.getWritableDatabase();
		_myUpdate=updateClass;
		_URI=new URL(String.format(_format, URLEncoder.encode("#"+_hashtag)));
	}
	/**
	 * Set new hashtag
	 * @param hashtag - the new hashtag
	 * @throws MalformedURLException
	 */
	@SuppressWarnings("deprecation")
	public void setHashtag(String hashtag) throws MalformedURLException{
		_hashtag=hashtag;
		_URI=new URL(String.format(_format, URLEncoder.encode("#"+_hashtag)));
	}
	/**
	 * Get currently defined hash tag
	 * @return the hashtag
	 */
	public String getHashtag(){
		return _hashtag;
	}
	/**
	 * Read the data received in stream
	 * @param in - the stream 
	 * @return String of all received data
	 */
	private String readStream(InputStream in){
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));		
		StringBuffer buffer = new StringBuffer();
		String line="33";
		try {
			while((line=reader.readLine())!=null){
				buffer.append(line);
				buffer.append('\n');
			}
		} catch (Exception e) {
			return line;
		}
		return buffer.toString();
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// display text at connection to twitter
		_progressDialog = new ProgressDialog(_context);		
		_progressDialog.setTitle("Connecting to tweeter");
		_progressDialog.setIcon(R.drawable.network_icon);
		_progressDialog.setMessage("Retrieving tweets from Twitter...");		
		_progressDialog.setCancelable(false);
		_progressDialog.show();
		
	}
	@Override	
	protected void onPostExecute(String result) {
		_progressDialog.dismiss();
		try {
			// parse the object as JSON
			JSONObject obj=new JSONObject(result);
			JSONArray arr=obj.getJSONArray("results");
			// cjeck for item that wasn't seen before
			ArrayList<Tweet> mayAdd=checkForNewItems(arr);
			// ask to add items 
			displayDialog(mayAdd);
		} catch (JSONException e) {
			// if format isn't JSON, print the message
			Toast toast = Toast.makeText(_context,result, Toast.LENGTH_LONG);
			toast.show();
			return;
		}
	}
	/**
	 * Check if there are new items in returned list, items 
	 * that user still didn't see
	 * @param arr - the JSON array of tweet items
	 * @return the array of tweet items to add
	 */
	private ArrayList<Tweet> checkForNewItems(JSONArray arr){
		ArrayList<Tweet> mayAdd=new ArrayList<Tweet>();
		String due;
	   	long id;
	   	String title;
	   	Tweet current;
	   	// run over each entry and check if is exists in database
	   	try {
	   		for(int i=0; i<arr.length();i++){
	   			// dont add the hash tag defined
	   			title=arr.getJSONObject(i).getString("text").replace("#"+_hashtag, "");
	   			due=arr.getJSONObject(i).getString("created_at");
				id=arr.getJSONObject(i).getLong("id");
				if(!alreadySeen(id)){
					// add to db
					addToTweetsSeen(id, title);
					current=new Tweet(id,due,title);
					mayAdd.add(current);
				}
	   		}
	   	} catch (JSONException e) {
		}
	   	return mayAdd;
	}
	/**
	 * Ask to add found items to todo list
	 * @param arr
	 */
	private void displayDialog(final ArrayList<Tweet> arr){
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
		// if there are items,display message that asks to add  found items 
		if(arr.size()!=0){
			builder.setMessage("There are "+arr.size()+" items with hashtag "+ _hashtag +".\nWould you like to add them?")
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						addItems(arr);
					}
				})
		       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	  return;
		           }
		       });
		}
		// if there aren't items, just notify it
		else{
			builder.setMessage("There are no new items with hashtag "+ _hashtag +".");
		}
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	@Override
	protected String doInBackground(Void... params) {
		HttpURLConnection conn;
		String response="";
		InputStream in=null;
		// open connection
		try {
			conn = (HttpURLConnection) _URI.openConnection();
		} catch (Exception e) {
			return "Error In open connection";
		}
		// get stream
		try {
			in=conn.getInputStream();
		} catch (Exception e) {
			return "Error In read from stream\n"+e.getMessage();
		}
		response = readStream(in);
		// close connection
		try {
			in.close();
			conn.disconnect();
		} catch (IOException e) {
			return "Error: Can't close stream\n";
		}
		return response;
	}
	/**
	 * Add items that was returned from query to twitter
	 * to the database and parse
	 * @param arr - the array of wteets to add
	 */
	private void addItems(ArrayList<Tweet> arr){
	   	ToDoItem item;
	   	for(int i=0; i<arr.size();i++){
			item=new ToDoItem(arr.get(i).getTitle(),arr.get(i).getDate());
			// add to DB and Parse
			_myUpdate.insert(item);
			// add to View
			_adapter.add(item);
   		}
	}
	/**
	 * Add item with given ID to DB of already seen items
	 * @param id - the id of item to add
	 * @param title - the title of item
	 * @return true - if action success
	 */
	private boolean addToTweetsSeen(long id,String title) {
		ContentValues tweet = new ContentValues();
		tweet.put("_id", id);
		tweet.put("title",title);
		if(_tweetsSeen.insert("tweets", null, tweet)==-1){
			return false;
		}
		return false;
		
	}
	/**
	 * Check if item, with given ID exists in DB of items that
	 * user already seen
	 * @param id of item to check
	 * @return true, if tweet with this id are in db, false otherwise
	 */
	private boolean alreadySeen(long id){
		Cursor element = _tweetsSeen.rawQuery("SELECT * FROM tweets WHERE _id ="+ id,null);
		if(element.getCount()==0){
			return false;
		}
		return true;
	}
	/**
	 * Nested class with db helper
	 * @author alonaba
	 *
	 */
	class TweetsDB extends SQLiteOpenHelper{
		/**
		 * Constructor
		 * @param context - the current context
		 */
		public TweetsDB(Context context) {
			super(context, "tweets_db", null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			db.execSQL("create table tweets ( " +
				      	"_id long primary key," +
				      	"title text);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// do nothing
		}
	}
}
