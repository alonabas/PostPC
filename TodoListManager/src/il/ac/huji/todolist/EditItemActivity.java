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
import android.widget.TextView;

/**
 * The edit Item date activity
 * @author alonaba
 *
 */
public class EditItemActivity extends Activity {
	/*
	 * The title of activity window
	 */
	private String add;
	private int position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// define custom title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_adit_todo_item);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_edit);
		Intent intent = getIntent();
		add = intent.getStringExtra("item");
		position = intent.getIntExtra("position", -1);
		// set title of header
		TextView textView = (TextView)findViewById(R.id.header);
        textView.setText(add);
        // on CANCEL button click action
        findViewById(R.id.btnCancelEdit).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				//do nothing
				setResult(RESULT_CANCELED);					
				finish();			
			}
		});
        // on OK button click action
        findViewById(R.id.btnOKEdit).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				// get date and translate to Date format
				DatePicker dateOfItem=(DatePicker)findViewById(R.id.datePickerEdit);
				Calendar cal= Calendar.getInstance();
				cal.set(dateOfItem.getYear(), dateOfItem.getMonth(), dateOfItem.getDayOfMonth());
				Date date=cal.getTime();
				// if nothing written in EditText line, just return
				Intent resultIntent = new Intent();
				resultIntent.putExtra("newDueDate", date);
				resultIntent.putExtra("title", add);
				resultIntent.putExtra("position", position);
				setResult(RESULT_OK, resultIntent);					
				finish();		
			}
		});
	}
}
