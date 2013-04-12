package il.ac.huji.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * The DB class
 * @author alonaba
 *
 */
public class MyDb extends SQLiteOpenHelper {
	/**
	 * The constructor of db
	 * @param context - the context
	 */
	public MyDb(Context context) {
		super(context, "todo_db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table todo ( " +
			      	"_id integer primary key autoincrement," +
			      	"title text, " +
			      	"due long );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// do nothing
	}

}
