package il.ac.huji.tipcalculator;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The main activity
 * @author alonaba
 *
 */
public class TipCalculatorActivity extends Activity {
	/*
	 * Define percent for tip calculation
	 */
	public final double PERCENT=0.12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);
        //Define Background image
        View general=findViewById(R.id.TipCalculator);
        general.setBackgroundResource(R.drawable.background);
        general.getBackground().setAlpha(70);
        // Button calculate to perform action
        final Button calculate = (Button)findViewById(R.id.btnCalculate);
        calculate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// The bill
				EditText amount=(EditText)findViewById(R.id.edtBillAmount);
				double data;
				// get the double of inserted amount of money
				try{
					data=Double.parseDouble(amount.getText().toString());
				}catch(Exception e){
					data=0;
				}
				// if round the tip result or not
				CheckBox roundBox=(CheckBox)findViewById(R.id.chkRound);
				boolean round=roundBox.isChecked();
				// The tip
				double tip=data*PERCENT;
				String dataToPrint="Tip: $";
				if(round){
					dataToPrint+=String.format("%.0f", tip);
				}
				else{
					dataToPrint+=String.format("%.2f", tip);
				}
				// write result on screen
				TextView result=(TextView)findViewById(R.id.txtTipResult);
				result.setText(dataToPrint);
			}
        	
        	
        	
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tip_calculator, menu);
        return true;
    }
    
}
