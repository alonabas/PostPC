/**
 * 
 */
package il.ac.huji.todolist;

import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * The class of preferences of ToDo application
 * @author alonaba
 *
 */
public class EditHashtagActivity extends PreferenceActivity{
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_todo);
	}
}
