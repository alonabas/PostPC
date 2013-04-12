package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The ToDoItem object, that contain title and date 
 * @author alonaba
 *
 */
public class ToDoItem implements ITodoItem{
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
	
	public String getDateString(){
		try{
			return _sdf.format(_date);
		}catch(Exception e){
			return null;
		}
	}
	@Override
	public String getTitle() {
		return _item;
	}
	@Override
	public Date getDueDate() {
		return _date;
	}
	
	public void setDueDate(Date date) {
		_date=date;
	}
}
