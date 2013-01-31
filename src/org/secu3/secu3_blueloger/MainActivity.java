package org.secu3.secu3_blueloger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "secu3_blueloger";
	//Кнопка для очистки
	Button btnConnect;
	TextView txtLod;    
	
	Handler h;	
	private BluetoothAdapter btAdapter = null;  
	private BluetoothSocket btSocket = null;
	
	// Статус для Handler
	// Status  for Handler
	final int RECIEVE_MESSAGE = 1;
	
	
	// SPP UUID service
	// SPP UUID сервиса
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	
	// MAC-address of Bluetooth module (you must edit this line)
	// MAC-адрес Bluetooth модуля
	private static String address = "00:12:03:30:00:23";
	
	private ConnectedThread mConnectedThread;
		
	
	private StringBuilder sb = new StringBuilder();


	  
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "...In onPause()...");
		try     {
			btSocket.close();
		} catch (IOException e2) {
			errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
		}  
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnConnect = (Button) findViewById(R.id.buttonconnect);
		
		
	    btnConnect.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        txtLod.setText("");
	      	mConnectedThread.write("1");	// Send "1" via Bluetooth
	          //Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
	        }
	      });
		
		// for display the received data from the Secu3
		// для вывода текста, полученного от Secu3
		txtLod = (TextView) findViewById(R.id.textViewLog);
		txtLod.setText("Data from SECU: " ); 
	    h = new Handler() {
	    	public void handleMessage(android.os.Message msg) {
	    		switch (msg.what) {
	            case RECIEVE_MESSAGE:													// if receive massage / если приняли сообщение в Handler
	            	byte[] readBuf = (byte[]) msg.obj;
	            	String strIncom = new String(readBuf, 0, msg.arg1);					// create string from bytes array / Создаем строку из байт в буфере
	            	sb.append(strIncom);												// append string  / формируем строку 
	            	int endOfLineIndex = sb.indexOf("@");								// determine the end-of-line / определяем символ конца строки 
	            	if (endOfLineIndex > 0) { 											// if end-of-line,  / если встречаем конец строки,
	            		String sbprint = sb.substring(0, endOfLineIndex);				// extract string / то извлекаем строку
	                    sb.delete(0, sb.length());										// and clear / и очищаем sb
	                	txtLod.setText(sbprint); 	       								// update TextView / Обновляем TextView с логом
	                	//btnConnect.setEnabled(true); 
	                	
	                }
	            	//Log.d(TAG, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
	            	Log.d(TAG, "...Byte:" + msg.arg1 + "...");
	            	if (sb.length()>25) {
	            		sb.delete(0, sb.length());										// and clear / и очищаем sb
	            	}
	            	break;
	    		}
	        };
		};
		btAdapter = BluetoothAdapter.getDefaultAdapter();								// get Bluetooth adapter / получаем локальный Bluetooth адаптер
		checkBTState();																	// Check Bluetooth / Проверяем наличие Bluetooth адаптера
	}

	@Override  
	public void onResume() {
		super.onResume();
		Log.d(TAG, "...onResume - try connect...");
		// Set up a pointer to the remote node using it's address.
		BluetoothDevice device = btAdapter.getRemoteDevice(address);
		// Two things are needed to make a connection:
		//   A MAC address, which we got above.
		//   A Service ID or UUID.  In this case we are using the 
		//     UUID for SPP. 
		try {
			btSocket = createBluetoothSocket(device);
		} catch (IOException e) {
			errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + "."); 
		}
		btAdapter.cancelDiscovery();
		// Establish the connection.  This will block until it connects.
		Log.d(TAG, "...Соединяемся...");
		try {
			btSocket.connect();
			Log.d(TAG, "...Соединение установлено и готово к передачи данных...");   
		} catch (IOException e) { 
			try {
				btSocket.close(); 
			} catch (IOException e2) {
				errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + "."); 
			}    
		}
		     
		// Create a data stream so we can talk to server.
		Log.d(TAG, "...Создание Socket...");
		mConnectedThread = new ConnectedThread(btSocket);
		mConnectedThread.start();
	}
	 
	private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
		if(Build.VERSION.SDK_INT >= 10){
			try {
				final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
				return (BluetoothSocket) m.invoke(device, MY_UUID);
			} catch (Exception e) {
				Log.e(TAG, "Could not create Insecure RFComm Connection",e);
			}
		}
		return  device.createRfcommSocketToServiceRecord(MY_UUID); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void checkBTState() {
	    // Check for Bluetooth support and then check to make sure it is turned on
	    // Emulator doesn't support Bluetooth and will return null
	    if(btAdapter==null) { 
	      errorExit("Fatal Error", "Bluetooth not support");
	    } else {
	      if (btAdapter.isEnabled()) {
	        Log.d(TAG, "...Bluetooth ON...");
	      } else {
	        //Prompt user to turn on Bluetooth
	        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	        startActivityForResult(enableBtIntent, 1);
	      }
	    }
	}
	  
	private void errorExit(String title, String message){
		Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();    
		finish();  
	}
	  
	private class ConnectedThread extends Thread {
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;    
		public ConnectedThread(BluetoothSocket socket) {
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) { }
			mmInStream = tmpIn;
			mmOutStream = tmpOut;    
		}
		 
		public void run() {
			byte[] buffer = new byte[256];  // buffer store for the stream
			int bytes; // bytes returned from read()
			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					// Read from the InputStrea
					bytes = mmInStream.read(buffer);		// Get number of bytes and message in "buffer"
					h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();		// Send to message queue Handler     
				} catch (IOException e) {
					break;
				}  
			}    
		}
		   
		/* Call this from the main activity to send data to the remote device */
		public void write(String message) {
			Log.d(TAG, "...Data to send: " + message + "...");
			byte[] msgBuffer = message.getBytes();
			try {
				mmOutStream.write(msgBuffer);
			} catch (IOException e) {
				Log.d(TAG, "...Error data send: " + e.getMessage() + "...");     
			}
		}
	}
}