package il.ac.huji.todolist;


import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
/**
 * The main class that manges the activity
 * @author alonaba
 *
 */
public class TodoListManagerActivity extends Activity {
	/*
	 * List of items in list of todo notes
	 */
	private List<String> items=new ArrayList<String>();
	/*
	 * Adapter to display list of items
	 */
	private MyAdapter adapter;
	ListView listOfItems;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		//Set background
		View general=findViewById(R.id.theMain);
        general.setBackgroundResource(R.drawable.bg5);
        //set opacity of background
        general.getBackground().setAlpha(60);
        // the ListView
		listOfItems=(ListView)findViewById(R.id.lstTodoItems);
		// attach adapter to ListView
		adapter=new MyAdapter(this,items);
		listOfItems.setAdapter(adapter);
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
			return addElement();
		case R.id.menuItemDelete:
			return removeElement();
		}
		return true;
	}
	/**
	 * Add element to List
	 * @return true, if element added, false otherwise
	 */
	private boolean addElement(){
		EditText itemToAdd=(EditText)findViewById(R.id.edtNewItem);
		String value=itemToAdd.getText().toString();
		// check if text box isn't empty, if is empty, don't add 
		if(!value.trim().equals("")){
			adapter.add(value);
			//clear the EdittText box after inserting value
			itemToAdd.setText("");
			return true;
		}
		return false; 
	}
	/**
	 * Remove selected element from list
	 * @return true, if element removed, false otherwise
	 */
	private boolean removeElement(){
		// get selected item
		String valueToDelete=(String)listOfItems.getSelectedItem();
		// if nothing selected don't delete thing
		if(valueToDelete!= "" && valueToDelete!=null){
			adapter.remove(valueToDelete);
			return true;
		}
		return false;
	}
}
