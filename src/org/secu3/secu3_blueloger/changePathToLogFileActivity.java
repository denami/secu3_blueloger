package org.secu3.secu3_blueloger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 */

/**
 * @author Andrei_Laptsinski
 *
 */
public class changePathToLogFileActivity extends Activity {
	Button btnConnect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.changepathtologfilelayout);
		btnConnect = (Button) findViewById(R.id.buttonChangePathToLogFileApplyButtonText);
	    btnConnect.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        finish();
	        }
	      });
	}
	@Override
	public void finish() {
		EditText text1 = (EditText) findViewById(R.id.editTextPathToLogFile);
		
		String pathToFile=text1.getText().toString();
		Intent data = new Intent();
		data.putExtra("lofFilePath", pathToFile);
		setResult(RESULT_OK, data);
		super.finish();
	}
	

}
