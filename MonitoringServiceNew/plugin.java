package MonitoringPackage;

import org.mule.api.annotations.param.Payload;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PutMethod;
import org.json.JSONException;
import org.json.JSONObject;

public class MonitoringService {
	private static String ZABBIX_API_URL = "http://10.1.90.58/zabbix/api_jsonrpc.php"; 
	private static HttpClient client ;
	private static String auth , hostid ;
	
	public static String connect(@Payload String input) throws HttpException, IOException {
		//System.out.println(input);
		JSONObject inputObj = new JSONObject(input);
		login();
		create_host("127.0.0.1", inputObj.getString("app_name"));  // assume that the ip is the host name 
		get_host( inputObj.getString("app_name"));

		System.out.println(hostid);
		return "done";
	
	}
	@SuppressWarnings("deprecation")
	public static void login(){
		try{
			client = new HttpClient();
			PutMethod putMethod = new PutMethod(ZABBIX_API_URL);
			putMethod.setRequestHeader("Content-Type", "application/json-rpc"); 
			putMethod.setRequestBody(zabbixapi.login("Admin","zabbix")); 
			client.executeMethod(putMethod);
			JSONObject obj = new JSONObject(putMethod.getResponseBodyAsString()); 
			auth = new String(obj.getString("result"));
			System.out.println(auth);
		}catch (Exception e) {
			e.printStackTrace();
		}			
	}

	public static void create_host(String ip, String name){
		try{	
			PutMethod putMethod_createhost = new PutMethod(ZABBIX_API_URL);
	    	putMethod_createhost.setRequestHeader("Content-Type", "application/json-rpc");
			putMethod_createhost.setRequestBody(zabbixapi.creeate_host(ip, name, auth)); 
	    	client.executeMethod(putMethod_createhost);
	    }catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void get_host(String name){
		try{	
			PutMethod putMethod_gethost = new PutMethod(ZABBIX_API_URL);
	    	putMethod_gethost.setRequestHeader("Content-Type", "application/json-rpc");
			putMethod_gethost.setRequestBody(zabbixapi.get_host(name,auth)); // the ip is the host name 
	    	client.executeMethod(putMethod_gethost);
	    	JSONObject response = new JSONObject(putMethod_gethost.getResponseBodyAsString());
	    	JSONArray result = response.getJSONArray("result");
	    	hostid = (String) result.getJSONObject(0).get("hostid");
	    }catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static InputStream fromString(String str) throws UnsupportedEncodingException {
		byte[] bytes = str.getBytes("UTF-8");
		return new ByteArrayInputStream(bytes);
	}

}