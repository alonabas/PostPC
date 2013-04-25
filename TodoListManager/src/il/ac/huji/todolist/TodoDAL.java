package il.ac.huji.todolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TodoDAL {
	private SQLiteDatabase _db;
	private SimpleDateFormat _sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("il"));
	/**
	 * Constructor of the Object of DB and Parse 
	 * @param context- the current context
	 */
	public TodoDAL(Context context) {
		MyDb dataBase = new MyDb(context);
		_db = dataBase.getWritableDatabase();
		Parse.initialize(context,context.getString(R.string.parseApplication), context.getString(R.string.clientKey));
		ParseUser.enableAutomaticUser();
	}
	/**
	 * Insert item to local db and to Parse cloud
	 * @param todoItem - the item to insert
	 * @return true if the operation succeed
	 */
	public boolean insert(ITodoItem todoItem) {
		ContentValues todo = new ContentValues();
		ParseObject parseObject = new ParseObject("todo");
		todo.put("title", todoItem.getTitle());
		parseObject.put("title",todoItem.getTitle());
		if(todoItem.getDueDate()!=null){
			todo.put("due",todoItem.getDueDate().getTime());
			parseObject.put("due", todoItem.getDueDate().getTime());
		}
		if(_db.insert("todo", null, todo)==-1){
			return false;
		}			
		parseObject.saveInBackground();
		return true;
	}
	/**
	 * Update the item date
	 * @param todoItem the item with new date
	 * @return true if update successful
	 */
	public boolean update(ITodoItem todoItem) {
		ContentValues args = new ContentValues();
		args.put("title", todoItem.getTitle());
		if(todoItem.getDueDate()!=null){
			args.put("due", todoItem.getDueDate().getTime());
		}
        _db.update("todo", args, "title='" + todoItem.getTitle()+"'", null);
		ParseQuery query = new ParseQuery("todo").whereContains("title", todoItem.getTitle());
		try {
			ParseObject object=query.getFirst();
			if(todoItem.getDueDate()!=null){
				object.put("due", todoItem.getDueDate().getTime());
			}
			object.saveInBackground();
		} catch (com.parse.ParseException e) {
			return false;
		}
		return true;
	}
	/**
	 * Delete item from local DB and parse
	 * @param todoItem the item to delete
	 * @return true if item deleted successfully and not otherwise
	 */
	public boolean delete(ITodoItem todoItem) {
		try{
			_db.delete("todo", "title='"+todoItem.getTitle()+"'", null);
		}catch(Exception e){}
		ParseQuery query = new ParseQuery("todo").whereContains("title", todoItem.getTitle());
		try {
			ParseObject object=query.getFirst();
			object.deleteInBackground();
		} catch (com.parse.ParseException e) {
			return false;
		}
		return true;
	}
	/**
	 * Get the list of all items in DB and 
	 * @return true if retrieve of items was successful, false otherwise
	 */
	@SuppressLint("NewApi")
	public List<ITodoItem> all() {
		List<ITodoItem> myList=new ArrayList<ITodoItem>();
		Cursor item=_db.rawQuery("select * from todo", null);
		if (item.moveToFirst()) {
			do{
				String title=item.getString(1);
				Date itemDate=null;
				if(item.getType(2)!=Cursor.FIELD_TYPE_NULL){
					long date=item.getLong(2);
					String itemDateStr;
					itemDate=new Date(date);
					itemDateStr=_sdf.format(itemDate);
					try {
						itemDate=_sdf.parse(itemDateStr);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				myList.add(new ToDoItem(title,itemDate));
			} while(item.moveToNext());
		}
		return myList;
		/*
		 * Get from Parse
		 */
		/*ParseQuery query=new ParseQuery("todo");
		try {
			List<ParseObject> arr=query.find();
			for (ParseObject i:arr){
				String title=i.get("title").toString();
				long date=(Long) i.get("due");
				Date itemDate=new Date(date);
				String itemDateStr=_sdf.format(itemDate);
				try {
					itemDate=_sdf.parse(itemDateStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				myList.add(new ToDoItem(title,itemDate));
			}
		} catch (com.parse.ParseException e) {
		}
		return myList;*/
	}
}