package il.ac.huji.todolist;

import java.util.Date;
/**
 * Interface for todo Item
 * @author alonaba
 *
 */
public interface ITodoItem {
	/**
	 * Get title of item
	 * @return the title
	 */
	 String getTitle();
	 /**
	  * Get date of item
	  * @return the date
	  */
	 Date getDueDate();
	 /**
	  * Get the date in String format
	  * @return the date
	  */
	 //String getDateString();
	 /**
	  * Set new date
	  * @param date the date to set
	  */
	 //void setDueDate(Date date);
}