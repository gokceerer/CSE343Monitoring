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
import org.json.JSONArray;


public class MonitoringService {
	private static String ZABBIX_API_URL = "http://10.1.90.58/zabbix/api_jsonrpc.php"; 
	private static HttpClient client = new HttpClient();
	private static String auth , hostid, interfaceid;
	private static String itemcpuid, itemmemid;
	
	
	public static String connect(@Payload String input) throws HttpException, IOException {
		//System.out.println(input);
		JSONObject inputObj = new JSONObject(input);
		login();
		if("deploy".equals(inputObj.get("method"))){
			//create_host("127.0.0.1", inputObj.getString("app_name"));  // assume that the ip is the host name 
			get_host( "127.0.0.1" ,inputObj.getString("app_name"));
			get_interface();
			createItems(inputObj.getString("app_name"));
			createGraphs(inputObj.getString("app_name"));
		}
		else{
			delete_host(inputObj.getString("app_name"));
			//System.out.println("Undeploy is not implemented yet!");
		}
		
		return "done";
	
	}
	@SuppressWarnings("deprecation")
	public static void login(){
		try{
			//client = new HttpClient();
			JSONObject obj = get_response(zabbixapi.login("Admin","zabbix"));
			auth = new String(obj.getString("result"));
			//System.out.println(auth);
		}catch (Exception e) {
			e.printStackTrace();
		}			
	}

	public static void create_host(String ip, String name){
		try{	
			get_response(zabbixapi.creeate_host(ip, name, auth));
	    }catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void delete_host(String name){
		try{
			JSONObject response = get_response(zabbixapi.get_host(name,auth));
			JSONArray result = response.getJSONArray("result");
	    	if (!(result.isEmpty())){
	    		hostid = (String) result.getJSONObject(0).get("hostid");
	    		get_response(zabbixapi.delete_host(hostid, auth));
	    	}	
	    }catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void get_host(String ip , String name){
		try{	
			JSONObject response = get_response(zabbixapi.get_host(name,auth));
	    	JSONArray result = response.getJSONArray("result");
	    	if (result.isEmpty()){
	    		create_host(ip, name);
	    		response = get_response(zabbixapi.get_host(name,auth));
	    		result = response.getJSONArray("result");
	    	}
	    	hostid = (String) result.getJSONObject(0).get("hostid");
	    }catch (Exception e) {
			e.printStackTrace();
		}	
	}
	public static void get_interface(){
		try{	
			JSONObject response = get_response(zabbixapi.get_interfaceid(hostid, auth));
	    	JSONArray result = response.getJSONArray("result");
	    	interfaceid = (String) result.getJSONObject(0).get("interfaceid");
	    }catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void createItems(String name){
		try{	
		   // zabbixapi.create_item_cpu(name, hostid , interfaceid ,null, "30", auth );
			
		    //CPU
	    	JSONObject response = get_response(zabbixapi.create_item_cpu(name, hostid , interfaceid ,null, "30", auth ));
	    	JSONObject result = response.getJSONObject("result");
	    	itemcpuid = result.getJSONArray("itemids").getString(0);
	    	//MEM
	    	response = get_response(zabbixapi.create_item_mem(name, hostid , interfaceid ,null, "30", auth ));
	    	result = response.getJSONObject("result");
	    	itemmemid = result.getJSONArray("itemids").getString(0);
	    }catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void createGraphs(String name){
		try{	    
		    //CPU
	    	get_response(zabbixapi.create_graph(name + " CPU Usage Graph" , auth , itemcpuid));
	    	
	    	//MEM
			get_response(zabbixapi.create_graph(name + " Memory Usage Graph" , auth , itemmemid));
	    
	    }catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JSONObject get_response(InputStream request){
		PutMethod putMethod = new PutMethod(ZABBIX_API_URL);
	    putMethod.setRequestHeader("Content-Type", "application/json-rpc");
	    putMethod.setRequestBody(request);
	    client.executeMethod(putMethod);
	    return new JSONObject(putMethod.getResponseBodyAsString());
	}
}