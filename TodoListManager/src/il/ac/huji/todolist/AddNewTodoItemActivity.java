package il.ac.huji.todolist;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The add Item activity
 * @author alonaba
 *
 */
public class AddNewTodoItemActivity extends Activity {
	/*
	 * The title of activity window
	 */
	private final String add="Add New Item";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// define custom title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_add_new_todo_item);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		// set title of header
		TextView textView = (TextView)findViewById(R.id.header);
        textView.setText(add);
        // on CANCEL button click action
        findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				//do nothing
				setResult(RESULT_CANCELED);					
				finish();			
			}
		});
        // on OK button click action
        findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				EditText newItem = (EditText)findViewById(R.id.edtNewItem);
				String newItemName = newItem.getText().toString();
				// get date and translate to Date format
				DatePicker dateOfItem=(DatePicker)findViewById(R.id.datePicker);
				Calendar cal= Calendar.getInstance();
				cal.set(dateOfItem.getYear(), dateOfItem.getMonth(), dateOfItem.getDayOfMonth());
				Date date=cal.getTime();
				// if nothing written in EditText line, just return
				if (newItemName == null || "".equals(newItemName)) {					
					setResult(RESULT_CANCELED);					
					finish();				
				} else {
					//return to main activity and put title and date 
					Intent resultIntent = new Intent();
					resultIntent.putExtra("title", newItemName);
					resultIntent.putExtra("dueDate", date);
					setResult(RESULT_OK, resultIntent);					
					finish();
				}			
			}
		});
	}
}
