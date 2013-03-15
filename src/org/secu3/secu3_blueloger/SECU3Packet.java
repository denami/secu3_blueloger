package org.secu3.secu3_blueloger;

import android.content.res.Resources;

public class SECU3Packet {
	
	public enum PacketCode {
		CHANGEMODE,						//!< change mode (type of default packet)
		BOOTLOADER,						//!< start boot loader
		TEMPER_PAR,						//!< temperature parameters (coolant sensor, engine cooling etc)
		CARBUR_PAR,						//!< carburetor's parameters
		IDLREG_PAR,						//!< idling regulator parameters
		ANGLES_PAR,						//!< advance angle (ign. timing) parameters
		FUNSET_PAR,						//!< parametersrelated to set of functions (lookup tables)
		STARTR_PAR,						//!< engine start parameters
		FNNAME_DAT,						//!< used for transfering of names of set of functions (lookup tables)
		SENSOR_DAT,              		//!< used for transfering of sensors data
		ADCCOR_PAR,						//!< parameters related to ADC corrections
		ADCRAW_DAT,						//!< used for transfering 'raw" values directly from ADC
		CKPS_PAR,						//!< CKP sensor parameters
		OP_COMP_NC,						//!< used to indicate that specified (suspended) operation completed
		CE_ERR_CODES,					//!< used for transfering of CE codes
		KNOCK_PAR,						//!< parameters related to knock detection and knock chip
		CE_SAVED_ERR,					//!< used for transfering of CE codes stored in the EEPROM
		FWINFO_DAT,						//!< used for transfering information about firmware
		MISCEL_PAR,						//!< miscellaneous parameters
		EDITAB_PAR,						//!< used for transferring of data for realtime tables editing
		ATTTAB_PAR,						//!< used for transferring of attenuator map (knock detection related)
		DBGVAR_DAT,						//!< for watching of firmware variables (used for debug purposes)
		DIAGINP_DAT,					//!< diagnostics: send input values (analog & digital values)
		DIAGOUT_DAT,					//!< diagnostics: receive output states (bits)
		CHOKE_PAR;						//!< parameters  related to choke control
	}
	
//	private static final String CHANGEMODE = "h";   //!< change mode (type of default packet)
//	private static final String BOOTLOADER = "i";   //!< start boot loader
//	private static final String TEMPER_PAR = "j";   //!< temperature parameters (coolant sensor, engine cooling etc)
//	private static final String CARBUR_PAR = "k";   //!< carburetor's parameters
//	private static final String IDLREG_PAR = "l";   //!< idling regulator parameters
//	private static final String ANGLES_PAR = "m";   //!< advance angle (ign. timing) parameters
//	private static final String FUNSET_PAR = "n";   //!< parametersrelated to set of functions (lookup tables)
//	private static final String STARTR_PAR = "o";   //!< engine start parameters
//	private static final String FNNAME_DAT = "p";   //!< used for transfering of names of set of functions (lookup tables)
//	private static final String SENSOR_DAT = "q";   //!< used for transfering of sensors data
//	private static final String ADCCOR_PAR = "r";   //!< parameters related to ADC corrections
//	private static final String ADCRAW_DAT = "s";   //!< used for transfering 'raw" values directly from ADC
//	private static final String CKPS_PAR   = "t";   //!< CKP sensor parameters
//	private static final String OP_COMP_NC = "u";   //!< used to indicate that specified (suspended) operation completed
//	private static final String CE_ERR_CODES = "v";   //!< used for transfering of CE codes
//	private static final String KNOCK_PAR  = "w";   //!< parameters related to knock detection and knock chip
//	private static final String CE_SAVED_ERR = "x";   //!< used for transfering of CE codes stored in the EEPROM
//	private static final String FWINFO_DAT = "y";   //!< used for transfering information about firmware
//	private static final String MISCEL_PAR = "z";   //!< miscellaneous parameters
//	private static final String EDITAB_PAR = "{";   //!< used for transferring of data for realtime tables editing
//	private static final String ATTTAB_PAR = "}";   //!< used for transferring of attenuator map (knock detection related)
//	private static final String DBGVAR_DAT = ":";  //!< for watching of firmware variables (used for debug purposes)
//	private static final String DIAGINP_DAT = "=";  //!< diagnostics: send input values (analog & digital values)
//	private static final String DIAGOUT_DAT = "^";  //!< diagnostics: receive output states (bits)
//	private static final String CHOKE_PAR   = "%";   //!< parameters  related to choke control

	
//	SENSOR_DAT example string @q0000030012CC02650000000000000013 	
	
	private String PacketStr;
	
	private PacketCode packetCode;
	
	SECU3Packet() {
		
	}
	
	SECU3Packet(String PacketStrOnCreate) {
		PacketStr=PacketStrOnCreate;
	}
	
	public String toString() {
		return PacketStr;
	}
	
	public boolean setString(String PacketStrOnSet ) {
		PacketStr=PacketStrOnSet;
		return true;
	}
	
	public PacketCode getPacketCode() {
		PacketCode pCode = null ;
		String PacketCodeFromString = PacketStr.substring(1, 2);
		if (PacketCodeFromString== "q")
			pCode = PacketCode.SENSOR_DAT;
		return pCode;
	}
	
	public void setPacketCode (PacketCode pc) {
		packetCode = pc;
	}
	public PacketCode getPacketCode (PacketCode pc) {
		return packetCode;
	}
	
	//Данные из ControlApp.cpp
	//частота вращения двигателя 
	//Parse_SENSOR_DAT
	public int getFrequen(){
		
		if (getPacketCode()==PacketCode.SENSOR_DAT)
		{
		return 0;
		}
		else return -1;
	}
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//давление во впускном коллекторе
	public int getPressure() {
		
		return 0;
	}
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//напряжение бортовой сети
	public int getVoltage() {

		return 0;
	}
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Температура охлаждающей жидкости
	public int getTemperature() {
		
		return 0;
	}

	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Текущий УОЗ (число со знаком)
	public int getAdv_angle() {
		
		return 0;
	}
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Уровень детонации двигателя
	public int getKnock_k() {
		
		return 0;
	}
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Корректировка УОЗ при детонации
	public int getKnock_retard() {
		
		return 0;
	}
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Расход воздуха
	public int getAir_flow() {
		
		return 0;
	}
	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Байт с флажками
	public byte getByteFlags() {
		
		return 0;
	}
	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние клапана ЭПХХ
	//ephh_valve
	public boolean getEphh_valve() {
		
		return false;
	}
	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние дроссельной заслонки
	public boolean getCarb() {
		
		
		return false;
	}
	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние газового клапана
	public boolean getGas() {
		
		
		return false;
	}	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние клапана ЭМР
	public boolean getEpm_valve() {
		
		
		return false;
	}	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние лампы "CE"
	public boolean getCe_state() {
		
		
		return false;
	}	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние вентилятора
	public boolean getCool_fan() {
		
		
		return false;
	}	
	
	//Данные из ControlApp.cpp
	//Parse_SENSOR_DAT
	//Состояние реле блокировки стартера
	public boolean getSt_block() {
			
			
		return false;
	}
	
	//Parse_SENSOR_DAT
	//TPS sensor
	public int getTps() {
		
		return 0;
	}
	
	//Parse_SENSOR_DAT
	//ADD_I1 input
	public int getAdd_i1_v() {
		
		
		return 0;
	}
	
	//Parse_SENSOR_DAT
	//ADD_I2 input
	public int getAdd_i2_v() {
		
		return 0;
	}
	
	//Parse_SENSOR_DAT
	//Биты ошибок СЕ
	public int getCe_errors() {
		
		
		return 0;
	}
	
	//Parse_DBGVAR_DAT
	//переменная 1
	public int getDbgvarDat_var1() {
		
		
		return 0;
	}
	
	//Parse_DBGVAR_DAT
	//переменная 2
	public int getDbgvarDat_var2() {
		
		
		return 0;
	}
	
	//Parse_DBGVAR_DAT
	//переменная 3
	public int getDbgvarDat_var3() {
		
		
		return 0;
	}
	
	//Parse_DBGVAR_DAT
	//переменная 4
	public int getDbgvarDat_var4() {
		
		
		return 0;
	}
	
	//Parse_FNNAME_DAT
	//Общее кол-во наборов (семейств характеристик)
	public int getFnTables_num() {
		
		
		return 0;
	}
	
	//Parse_FNNAME_DAT
	//номер этого набора характеристик
	public int getFnIndex() {
		
		
		return 0;
	}
	
	//Parse_FNNAME_DAT
	//имя этого набора характеристик
	public String getFnName() {

		
		return "";
	}
	
	public String getSymbolOfPacketType(PacketCode pc) {
		int order=pc.ordinal();
		String outputString;
		// загрузка массива строк из res/values/arrays.xml в текстовое поле textStrings
		//Resources secuRes = null;//getResources();	
		String[] names = Resources.getSystem().getStringArray(R.array.packetTypeSymbol);
		//for(int i = 0; i < names.length; i++) {
		//    textStrings.append("Name[" + i + "]: "+ names[i] + "\n");
		//} 
		outputString=names[order];
		
		return outputString;
	}
	
	
}
