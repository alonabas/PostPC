package il.ac.huji.todolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The MyAdapter class defined to change the look of list
 * @author alonaba
 *
 */
public class MyAdapter extends ArrayAdapter<ITodoItem>{
	/**
	 * Constructor
	 * @param activity - the activity
	 * @param todoItems - the items in ListView
	 */
	public MyAdapter(TodoListManagerActivity activity, List<ITodoItem> todoItems) {
		super(activity,android.R.layout.simple_list_item_1 ,todoItems);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ToDoItem item = (ToDoItem) getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.one_item, null);
		// The title
		TextView itemName = (TextView)view.findViewById(R.id.txtTodoTitle);
		// The date
		TextView itemDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		// set Items
		itemName.setText(item.getTitle());
		itemName.setTextSize(25);
		// get today's day to compare
		Date curDate=new Date();
		SimpleDateFormat _sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("il"));
		String strDate=_sdf.format(curDate);
		try {
			curDate=_sdf.parse(strDate);
		} catch (ParseException e) {
		}
		if(item.getDueDate()==null){
			itemDate.setText("No due date");
		}
		else{
			itemDate.setText(item.getDateString());
			if(item.getDueDate().before(curDate)){
				// set color of text to red if date of item before then today
				itemName.setTextColor(Color.RED);
				itemDate.setTextColor(Color.RED);
			}
		}
		itemDate.setTextSize(25);
		return view;
	}
}
