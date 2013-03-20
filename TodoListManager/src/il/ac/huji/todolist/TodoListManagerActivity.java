package il.ac.huji.todolist;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
/**
 * The main class that manages the activity
 * @author alonaba
 *
 */
public class TodoListManagerActivity extends Activity {
	/*
	 * List of items in list of todo notes
	 */
	private List<ToDoItem> _items=new ArrayList<ToDoItem>();
	/*
	 * Adapter to display list of items
	 */
	private MyAdapter _adapter;
	/*
	 * Application title
	 */
	private final String _title="ToDo List Manager";
	ListView listOfItems;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_todo_list_manager);
		// set custom title
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		TextView textView = (TextView)findViewById(R.id.header_main);
        textView.setText(_title);
        textView.setTextSize(22);
        textView.setTypeface(null, Typeface.BOLD);
		//Set background
		View general=findViewById(R.id.theMain);
        general.setBackgroundResource(R.drawable.bg5);
        //set opacity of background
        general.getBackground().setAlpha(60);
        // the ListView
		listOfItems=(ListView)findViewById(R.id.lstTodoItems);
		// attach adapter to ListView
		_adapter=new MyAdapter(this,_items);
		listOfItems.setAdapter(_adapter);
		registerForContextMenu(listOfItems);
	}
	@Override	
	public void onCreateContextMenu(ContextMenu menu, View view ,ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.context_menu, menu);
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    // get the string written in layout for title
	    TableLayout currentView =(TableLayout)info.targetView;
	    // first child of TableLayot is row 
	    TableRow row=(TableRow)currentView.getChildAt(0);
	    // first child of TableRaw is TextView with title
	    TextView text=(TextView)row.getChildAt(0);
	    String currentText=text.getText().toString();
	    menu.setHeaderTitle(currentText);
	    MenuItem callItem=menu.findItem(R.id.menuItemCall);
	    // if title of item doesn't contain "Call: ", then hide this option
	    if(currentText.contains("Call ")){
	    	callItem.setTitle(currentText);
	    }
	    else{
	    	menu.removeItem(R.id.menuItemCall);
		}
	}
	@Override
	public boolean onContextItemSelected (MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int selectedItemIndex = info.position;		
		switch (item.getItemId()){
			case R.id.menuItemDelete:
				// delete item
				_adapter.remove(_adapter.getItem(selectedItemIndex));			
				break;
			case R.id.menuItemCall:
				ToDoItem currentItem=_adapter.getItem(selectedItemIndex);
				//define string to forward to ACTION_DIAL
				String textItem=currentItem.getItem().replace("Call: ", "");
				textItem=textItem.trim();
				textItem="tel:"+textItem;
				makeCall(textItem);
				break;
		}	
		return true;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}
	/**
	 * The method to take action on section of menu
	 */
	public boolean onOptionsItemSelected(MenuItem item){
		// switch on items selected
		switch(item.getItemId()){
		case R.id.menuItemAdd:
			// if add pressed open new activity and wait for result
			Intent intent = new Intent(this, AddNewTodoItemActivity.class);
			startActivityForResult(intent,0);
		}
		return true;
	}
	/**
	 * Treat the result of previous activity, if is was "Add",
	 * so add the ToDoItem to List
	 */
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if (reqCode == 0 && resCode == RESULT_OK) {
			// get extras
			String title=data.getStringExtra("title");
			Date date=null;
			if(data.getExtras().get("dueDate")!=null){
				date = (Date) data.getExtras().get("dueDate");
			}
			// add item
			_adapter.add(new ToDoItem(title,date));
		}
	}
	/**
	 * Method that calls the ACTION_DIAL activity, with given number
	 * @param number - the number to call
	 */
	private void makeCall(String number){
		Intent call=new Intent(Intent.ACTION_DIAL,Uri.parse(number));
		startActivity(call);
	}
}
