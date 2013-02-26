package org.secu3.secu3_blueloger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	private static final String TAG = "secu3_blueloger";
	private static final int REQUEST_CODE = 10;
	
	//clean Button 
	// Кнопка для очистки
	Button btnConnect;
	TextView txtLod;    
	
	Handler h;	
	private BluetoothAdapter btAdapter = null;  
	private BluetoothSocket btSocket = null;
	
	// Статус для Handler
	// Status  for Handler
	final int RECIEVE_MESSAGE = 1;
	
	private static final int REQUEST_CONNECT_DEVICE = 1;
	
	// SPP UUID service
	// SPP UUID сервиса
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	// MAC-address of Bluetooth module (you must edit this line)
	// MAC-адрес Bluetooth модуля
	private static String address = "00:12:03:30:00:23";
	
	private ConnectedThread mConnectedThread;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT ).show();
		switch (item.getItemId()) {
		case 5:
			// ChangeLogFilenameSelected
			Intent i = new Intent(this, changePathToLogFileActivity.class);
			startActivityForResult(i, REQUEST_CODE);
			
			break;
		case 4:
			//Select Bluetooth Device 
			break;
		case 6:
			Log.d(TAG, "Exit from application");
			//Exit from application
			//Выход из приложения (сворачивание приложения)
			finish();
			break;
		default:
			return false;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("lofFilePath")) {
				//Toast.makeText(this, data.getExtras().getString("lofFilePath"), Toast.LENGTH_SHORT).show();
				String state = Environment.getExternalStorageState();
				File pathToWorkFolder= Environment.getExternalStorageDirectory();
				 File path = Environment.getExternalStoragePublicDirectory(
				            Environment.DIRECTORY_DOWNLOADS);

				txtLod = (TextView) findViewById(R.id.textViewFilePath);
				txtLod.setText( path.toString()+"/"+data.getExtras().getString("lofFilePath") ); 
			}
		}
	}

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
	            	String strIncom = (String)msg.obj;							        // extract string / то извлекаем строку												
	            	txtLod.setText(strIncom); 	       								// update TextView / Обновляем TextView с логом
	            	Log.d(TAG, "...strIncom:" + strIncom + "...");
	            	break;
	    		}
	    	}
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
		
		SubMenu AdaptorMenu = menu.addSubMenu(1,//group
											  4,//id
											  2,//order
											  getString(R.string.choseAdaptorButtonText)); //text
		AdaptorMenu.add("Dev1");
		AdaptorMenu.add("Dev2");
		menu.add(1, //group
				 5, //id
				 2, //order
				 getString(R.string.changePathToLogFile)); //text
		menu.add(2,
				 6,
				 4,
				 getString(R.string.exitButtonText));
		
	    return super.onCreateOptionsMenu(menu);
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
		private StringBuilder sbInThread = new StringBuilder(); // StringBuilder для отправки TextView
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
		File toLogFile = new File (getString(R.string._sdcard_test_log));
		
		public void run() {
			byte[] buffer = new byte[256];  // buffer store for the stream
			int bytes; // bytes returned from read()
			ArrayList<String> resultingList = new ArrayList<String>();
			String container;
			//Create one element array 
			// Создание масива из одного байта
			byte[] t = new byte [1]; 
			
			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					if (!toLogFile.exists()){
						toLogFile.createNewFile();
					}
					FileWriter wrt = new FileWriter(toLogFile, true);
					// Read from the InputStrea
					bytes = mmInStream.read(buffer);		// Get number of bytes and message in "buffer"					
					for (int i = 0; i < bytes; i++) {
						t[0]=buffer[i]; //Set array element from buffer / Устанавливаем заничение элемента значением из buffer 
						container = new String(t);  // create string from bytes array / Создаем строку из байт в буфере
						//Log.d(TAG, "Buffer elemrnt: "+i+"-"+buffer[i]);
						sbInThread.append(container);
						if (buffer[i]==13) { // determine the end-of-line / определяем символ конца строки
							// if end-of-line,  
							// если встречаем конец строки,
							Log.d(TAG, "New message"); 
							//get current time
							// получаем текущее время
						    final Calendar c = Calendar.getInstance();
							resultingList.add(c.getTime().toString()+": "+sbInThread.toString()); //Add string to ArrayList / Добовляем строку в ArrayList 
							sbInThread = new StringBuilder(); //clear StringBuilder / и очищаем StringBuilder
						}
					}
					for (String strToPush: resultingList) {
						wrt.append(strToPush);
						// Send to message queue Handler
						h.obtainMessage(RECIEVE_MESSAGE, strToPush).sendToTarget(); 
					}
					//clear ArrayList  
					//очищаем ArrayList
					resultingList.clear();
					wrt.flush();
					wrt.close();     
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