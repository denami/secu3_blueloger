package org.secu3.secu3_blueloger;

import android.util.Log;

public class SECU3Packet {
	
	private static final String TAG = "SECU3PacketLog";
		
	private String PacketStr;
	private Packets Packet; 
	
	public enum Packets {
		// String packet symbol, int min lenght, int max lenght, Boolean isOnlyTransfer
		CHANGEMODE 	 ("h",1,1,true ),		//!< change mode (type of default packet) //no data need, only a descriptor
		BOOTLOADER 	 ("i",0,0,true ),		//!< start boot loader  //no data need
		TEMPER_PAR 	 ("j",12,12,false),		//!< temperature parameters (coolant sensor, engine cooling etc)
		CARBUR_PAR 	 ("k",26,26,false),		//!< carburetor's parameters
		IDLREG_PAR 	 ("l",30,30,false),		//!< idling regulator parameters
		ANGLES_PAR 	 ("m",22,22,false),		//!< advance angle (ign. timing) parameters
		FUNSET_PAR 	 ("n",29,29,false),		//!< parametersrelated to set of functions (lookup tables)
		STARTR_PAR 	 ("o",9,9,false),		//!< engine start parameters //STARTR_PAR example string @o0258028A
		FNNAME_DAT 	 ("p",21,21,false),		//!< used for transfering of names of set of functions (lookup tables)
		SENSOR_DAT 	 ("q",47,47,false),		//!< used for transfering of sensors data //SENSOR_DAT example string @q0000030012CC02650000000000000013 	
		ADCCOR_PAR 	 ("r",73,73,false),		//!< parameters related to ADC corrections
		ADCRAW_DAT 	 ("s",29,29,false),		//!< used for transfering 'raw" values directly from ADC
		CKPS_PAR	 ("t",14,14,false),		//!< CKP sensor parameters
		OP_COMP_NC 	 ("u",5,5,false),		//!< used to indicate that specified (suspended) operation completed
		CE_ERR_CODES ("v",5,5,false),		//!< used for transfering of CE codes
		KNOCK_PAR 	 ("w",32,32,false),		//!< parameters related to knock detection and knock chip
		CE_SAVED_ERR ("x",5,5,false),		//!< used for transfering of CE codes stored in the EEPROM
		FWINFO_DAT 	 ("y",1,1,false),		//!< used for transfering information about firmware  //TODO  UPDATE
		MISCEL_PAR 	 ("z",16,16,false),		//!< miscellaneous parameters
		EDITAB_PAR 	 ("{",7,37,false),		//!< used for transferring of data for realtime tables editing
		ATTTAB_PAR 	 ("}",5,35,false),		//!< used for transferring of attenuator map (knock detection related)
		DBGVAR_DAT 	 (":",17,17,false),		//!< for watching of firmware variables (used for debug purposes)
		DIAGINP_DAT  ("=",35,35,false),		//!< diagnostics: send input values (analog & digital values)
		DIAGOUT_DAT  ("^",1,1,true),		//!< diagnostics: receive output states (bits)
		CHOKE_PAR 	 ("%",6,6,false);		//!< parameters  related to choke control
		
	    private final int packetLengthMin;   //Минисмальный размер пакета без сигнального символа, дескриптора
	    private final int packetLengthMax;   //Максимальный размер пакета без сигнального символа, дескриптора
	    private final String packetCodeSymbols; // Символ типа пакета	
	    private final Boolean pachetIsOnlyTransfer; //Предназначен пакет только для отправки
	    
	    Packets(String packetCodeSymbols, int packetLengthMin, int packetLengthMax, Boolean pachetIsOnlyTransfer) {
	        this.packetLengthMin = packetLengthMin;
	        this.packetLengthMax = packetLengthMax;
	        this.packetCodeSymbols = packetCodeSymbols;
	        this.pachetIsOnlyTransfer = pachetIsOnlyTransfer;
	    }
		
	    private int packetLengthMin() 		{ return packetLengthMin; }
	    private int packetLengthMax() 		{ return packetLengthMax; }
	    private String packetCodeSymbols() 	{return packetCodeSymbols; }
	    private Boolean isOnlyTransfer() 	{return pachetIsOnlyTransfer; }
		
	}
	
	SECU3Packet(Packets p) {
		Packet=p;
	}
	
	SECU3Packet() {
	}
	
	SECU3Packet(String PacketStrOnCreate) {
		PacketStr=PacketStrOnCreate;
		Log.d(TAG, "...PacketString: " + PacketStr + "...");
		Packets p =  Packets.SENSOR_DAT;
		Log.d(TAG, "...PacketString: " + p.name() + "...");
		Log.d(TAG, "...Packet Length: " + p.packetLengthMin() + "...");
		Log.d(TAG, "...Packet Length Max: " + p.packetLengthMax() + "...");
		Log.d(TAG, "...Packet CodeSymbols: " + p.packetCodeSymbols() + "...");
		Log.d(TAG, "...Packet isOnlyTransfer: " + p. isOnlyTransfer() + "...");
		Packet=Packets.SENSOR_DAT;
	}
	
	public String toString() {
		return PacketStr;
	}
	
	public boolean setString(String PacketStrOnSet ) {
		PacketStr=PacketStrOnSet;
		Packet=Packets.SENSOR_DAT;
		return true;
	}
	
	public Packets getPacketCode() { return Packet; }
	
	public void setPacketCode (Packets pc) { Packet = pc; }

	//Данные из ControlApp.cpp
	//частота вращения двигателя 
	//Parse_SENSOR_DAT
	//TODO add function
	public int getFrequen(){
		if (Packet.equals(Packets.SENSOR_DAT) )
		{
		return 0;
		}
		else return -1;
	}
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//давление во впускном коллекторе
	//TODO add function
	public int getPressure() {
		
		return 0;
	}
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//напряжение бортовой сети
	//TODO add function
	public int getVoltage() {

		return 0;
	}
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Температура охлаждающей жидкости
	//TODO add function
	public int getTemperature() {
		
		return 0;
	}

	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Текущий УОЗ (число со знаком)
	//TODO add function
	public int getAdv_angle() {
		
		return 0;
	}
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Уровень детонации двигателя
	//TODO add function
	public int getKnock_k() {
		
		return 0;
	}
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Корректировка УОЗ при детонации
	//TODO add function
	public int getKnock_retard() {
		
		return 0;
	}
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Расход воздуха
	//TODO add function
	public int getAir_flow() {
		
		return 0;
	}
	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Байт с флажками
	//TODO add function
	public byte getByteFlags() {
		
		return 0;
	}
	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние клапана ЭПХХ
	//ephh_valve
	//TODO add function
	public boolean getEphh_valve() {
		
		return false;
	}
	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние дроссельной заслонки
	//TODO add function
	public boolean getCarb() {
		
		
		return false;
	}
	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние газового клапана
	//TODO add function
	public boolean getGas() {
		
		
		return false;
	}	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние клапана ЭМР
	//TODO add function
	public boolean getEpm_valve() {
		
		
		return false;
	}	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние лампы "CE"
	//TODO add function
	public boolean getCe_state() {
		
		
		return false;
	}	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние вентилятора
	//TODO add function
	public boolean getCool_fan() {
		
		
		return false;
	}	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние реле блокировки стартера
	//TODO add function
	public boolean getSt_block() {
			
			
		return false;
	}
	
	//Parse_SENSOR_DAT
	//TPS sensor
	//TODO add function
	public int getTps() {
		
		return 0;
	}
	
	//Parse_SENSOR_DAT
	//ADD_I1 input
	//TODO add function
	public int getAdd_i1_v() {
		
		
		return 0;
	}
	
	//Parse_SENSOR_DAT
	//ADD_I2 input
	//TODO add function
	public int getAdd_i2_v() {
		
		return 0;
	}
	
	//Parse_SENSOR_DAT
	//Биты ошибок СЕ
	//TODO add function
	public int getCe_errors() {
		
		
		return 0;
	}
	
	//Parse_DBGVAR_DAT
	//переменная 1
	//TODO add function
	public int getDbgvarDat_var1() {
		
		
		return 0;
	}
	
	//Parse_DBGVAR_DAT
	//переменная 2
	//TODO add function
	public int getDbgvarDat_var2() {
		
		return 0;
	}
	
	//Parse_DBGVAR_DAT
	//переменная 3
	//TODO add function
	public int getDbgvarDat_var3() {
		
		return 0;
	}
	
	//Parse_DBGVAR_DAT
	//переменная 4
	//TODO add function
	public int getDbgvarDat_var4() {
		
		return 0;
	}
	
	//Parse_FNNAME_DAT
	//Общее кол-во наборов (семейств характеристик)
	//TODO add function
	public int getFnTables_num() {
		
		return 0;
	}
	
	//Parse_FNNAME_DAT
	//номер этого набора характеристик
	//TODO add function
	public int getFnIndex() {
				
		return 0;
	}
	
	//Parse_FNNAME_DAT
	//имя этого набора характеристик
	//TODO add function
	public String getFnName() {

		return "";
	}
	
	public String getSymbolOfPacketType(Packets pc) { return pc.packetCodeSymbols(); }
		
}
