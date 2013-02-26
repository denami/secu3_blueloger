/**
 * 
 */
package org.secu3.secu3_blueloger;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass.Device;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Andrei_Laptsinski
 *
 */
public class changeBluetoothDeviceActivity extends Activity {

	private String bluDevName="null-00:00:00:00:00";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_bluetooth_device_layout);
		ArrayList<String> listDevBlue = new ArrayList<String>();
		BluetoothAdapter mBA = BluetoothAdapter.getDefaultAdapter();
		for (BluetoothDevice device : mBA.getBondedDevices()) {
			listDevBlue.add(device.getName()+"_"+device.getAddress());
		}
		ListView lv = (ListView)findViewById(R.id.bluetoothDevicelistView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listDevBlue);
		lv.setAdapter(adapter);		
        OnItemClickListener l = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View itemClicked, int position,
					long id) {
				bluDevName=(String) ((TextView) itemClicked).getText();
				finish();
				//Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
				//         Toast.LENGTH_SHORT).show();
			}
		};
		lv.setOnItemClickListener(l);
	}
	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("bluDevName", bluDevName);
		setResult(RESULT_OK, data);
		super.finish();
	}
}
