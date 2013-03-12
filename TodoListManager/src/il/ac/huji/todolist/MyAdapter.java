package il.ac.huji.todolist;

import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The MyAdapter class defined to change the look of list
 * @author alonaba
 *
 */
public class MyAdapter extends ArrayAdapter<String>{
	/**
	 * Constructor
	 * @param activity - the activity
	 * @param todoItems - the items in ListView
	 */
	public MyAdapter(TodoListManagerActivity activity, List<String> todoItems) {
		super(activity,android.R.layout.simple_spinner_item ,todoItems);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		TextView view=(TextView) super.getView(position, convertView, parent);
		// if position is odd the color of text is blue, otherwise, red
		if(position%2==0){
			view.setTextColor(Color.RED);
		}
		else{
			view.setTextColor(Color.BLUE);
		}
		view.setTextSize(25);
		return view;
	}
}
