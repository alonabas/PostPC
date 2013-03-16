package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The ToDoItem object, that contain title and date 
 * @author alonaba
 *
 */
public class ToDoItem {
	/*
	 * Translate date to string
	 */
	private SimpleDateFormat _sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("il"));
	/*
	 * The title of item
	 */
	private String _item;
	/*
	 * The date of item
	 */
	private Date _date;
	/**
	 * Constuctor
	 * @param item - the item title
	 * @param date - the daye of item
	 */
	public ToDoItem(String item,Date date){
		_item=item;
		_date=date;
	}
	/**
	 * Get date in String format
	 * @return the date
	 */
	public String getDateString(){
		try{
			return _sdf.format(_date);
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * Get title of ToDoItem
	 * @return title
	 */
	public String getItem(){
		return _item;
	}
	/**
	 * Get date of ToDoItem
	 * @return the date of item
	 */
	public Date getDate(){
		return _date;
	}
}
