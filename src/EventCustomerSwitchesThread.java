
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

class EventCustomerSwitchesThread implements Runnable {
	private int kiotUserMappingId;
	private String kiotUserId;
	private String bearerToken;
	private String kiotDeviceId;
	private String customData;
	private String connectionString;
	private int eventId;
	private int customerId;
	private int deviceTypeId;
	private String deviceName;
	
	public EventCustomerSwitchesThread(String kiotDeviceId,int kiotUserMappingId,String kiotUserId, String bearerToken
			, String customData,String connectionString, int eventId, int customerId, int deviceTypeId,String deviceName) {
		this.kiotUserMappingId = kiotUserMappingId;
		this.kiotUserId = kiotUserId;
		this.bearerToken = bearerToken;
		this.kiotDeviceId = kiotDeviceId;
		this.customData = customData;
		this.connectionString = connectionString;
		this.eventId = eventId;
		this.customerId= customerId;
		this.deviceTypeId = deviceTypeId;
		this.deviceName = deviceName;
	}

	public void run() {
		try {
			System.out.println("EventCustomerSwitchesThread");
		//	getTxData();
			//Invoke node Api
			//
			if (deviceTypeId == 1) {
					operateAC();
			} else {
				operateSwitch();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		finally {
			if (ScheduleDAO.con != null) {
//				try {
//			//		ScheduleDAO.con.close();  Close later
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
				}
		System.out.println(Thread.currentThread().getName() + " (End)");// prints thread name
	}

	private void processmessage() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void operateSwitch() throws ClassNotFoundException, SQLException, JSONException, IOException {
		ScheduleDAO scd= new ScheduleDAO();
		double meterReading = 0;
		DBHelper dbhelper = new DBHelper();
		HttpConnectorHelper httpconnectorhelper= new HttpConnectorHelper();
		JSONObject input= new JSONObject();
		JSONObject payload= new JSONObject();
		JSONObject params = new JSONObject();
		params.put("_state", false);
		JSONObject device = new JSONObject();
		device.put("id", this.kiotDeviceId);
		JSONObject customData = new JSONObject(this.customData);
//		customData.put("switch_no","" );
//		customData.put("ud_id", "");
		device.put("customData", customData);
		payload.put("params", params);
		payload.put("command", "action.devices.commands.OnOff");
		payload.put("device", device);
		input.put("payload", payload);
		input.put("intent", "action.entities.EXEC");
		int responseFromDevice = httpconnectorhelper
				.sendPostWithToken("https://api.kiot.io/integrations/ctp/go",bearerToken, input);
		System.out.println("Response from KIOT "+ responseFromDevice);
		if(responseFromDevice == 0) {
			dbhelper.updateEventCustomer(eventId,customerId,12);
		} else {
			dbhelper.updateEventCustomerToLive(eventId, customerId);
		}
		
	}
	
	public void operateAC() throws ClassNotFoundException, SQLException, JSONException, IOException {
		ScheduleDAO scd= new ScheduleDAO();
		double meterReading = 0;
		DBHelper dbhelper = new DBHelper();
		HttpConnectorHelper httpconnectorhelper= new HttpConnectorHelper();
		JSONObject input= new JSONObject();
		JSONObject payload= new JSONObject();
		JSONObject params = new JSONObject();
		params.put("temperature", 27);
		params.put("ir_type", 1);
		JSONObject device = new JSONObject();
		device.put("id", this.kiotDeviceId);
		JSONObject customData = new JSONObject(this.customData);
//		customData.put("switch_no","" );
//		customData.put("ud_id", "");
		device.put("customData", customData);
		payload.put("params", params);
		payload.put("command", "action.devices.commands.SetTemperature");
		payload.put("device", device);
		input.put("payload", payload);
		input.put("intent", "action.entities.EXEC");
		int responseFromDevice = httpconnectorhelper
				.sendPostWithToken("https://api.kiot.io/integrations/ctp/go",bearerToken, input);
		if(responseFromDevice == 0) {
			dbhelper.updateEventCustomer(eventId,customerId,12);
		} else {
			dbhelper.updateEventCustomerToLive(eventId, customerId);
		}
		
	}
	
	public void switchAC() throws ClassNotFoundException, SQLException, JSONException, IOException {
		ScheduleDAO scd= new ScheduleDAO();
		double meterReading = 0;
		DBHelper dbhelper = new DBHelper();
		HttpConnectorHelper httpconnectorhelper= new HttpConnectorHelper();
		JSONObject input= new JSONObject();
		JSONObject payload= new JSONObject();
		JSONObject params = new JSONObject();
		params.put("_state", false);
		params.put("ir_type", 1);
		JSONObject device = new JSONObject();
		device.put("id", this.kiotDeviceId);
		JSONObject customData = new JSONObject(this.customData);
//		customData.put("switch_no","" );
//		customData.put("ud_id", "");
		device.put("customData", customData);
		payload.put("params", params);
		payload.put("command", "action.devices.commands.IROnOff");
		payload.put("device", device);
		input.put("payload", payload);
		input.put("intent", "action.entities.EXEC");
		int responseFromDevice = httpconnectorhelper
				.sendPostWithToken("https://api.kiot.io/integrations/ctp/go",bearerToken, input);
		if(responseFromDevice == 0) {
			dbhelper.updateEventCustomer(eventId,customerId,12);
		} else {
			dbhelper.updateEventCustomerToLive(eventId, customerId);
		}
		
	}
	
}