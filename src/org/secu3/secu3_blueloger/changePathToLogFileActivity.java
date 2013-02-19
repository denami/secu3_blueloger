package org.secu3.secu3_blueloger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * 
 */

/**
 * @author Andrei_Laptsinski
 *
 */
public class changePathToLogFileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.changepathtologfilelayout);
	}

}
