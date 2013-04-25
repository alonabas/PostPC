/**
 * 
 */
package il.ac.huji.todolist;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The tweet class, that defines tweet object
 * @author alonaba
 *
 */
public class Tweet {
	/*
	 * Tweet ID
	 */
	private long _id;
	@SuppressLint("SimpleDateFormat")
	/*
	 * Date format
	 */
	private SimpleDateFormat _sdf = new SimpleDateFormat("dd/MM/yyyy");
	/*
	 * The date tweet was created
	 */
	private Date _date;
	/*
	 * The title of tweet
	 */
	private String _title;
	/**
	 * Constructor of Tweet
	 * @param id - the id of tweet
	 * @param date - the date tweet was created
	 * @param title - the title of the tweet
	 */
	@SuppressWarnings("deprecation")
	public Tweet(long id,String date, String title){
		_id=id;
		_date=new Date(date);
		_title=title;
	}
	/**
	 * Get id of the tweet
	 * @return id
	 */
	public long getId(){
		return _id;
	}
	/**
	 * Get tweet title
	 * @return the title
	 */
	public String getTitle(){
		return _title;
	}
	/**
	 * Get date of the tweet
	 * @return the date
	 */
	public Date getDate(){
		return _date;
	}
	/**
	 * Get date of tweet in long
	 * @return long(Date of tweet)
	 */
	public long getDateFomat(){
		return _date.getTime();
	}
	/**
	 * Get date of tweet in String format
	 * @return String (Date of tweet)
	 */
	public String getDateString(){
		return _sdf.format(_date);
	}
}
