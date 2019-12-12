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
	private static HttpClient client ;
	private static String auth , hostid, interfaceid;
	private static String itemcpuid, itemmemid;
	
	
	public static String connect(@Payload String input) throws HttpException, IOException {
		//System.out.println(input);
		JSONObject inputObj = new JSONObject(input);
		login();
		create_host("127.0.0.1", inputObj.getString("app_name"));  // assume that the ip is the host name 
		get_host( inputObj.getString("app_name"));
		get_interface();
		createItems(inputObj.getString("app_name"));
		createGraphs(inputObj.getString("app_name"));
		
		System.out.println(hostid);
		System.out.println(interfaceid);
		System.out.print(itemcpuid);
		System.out.print(itemmemid);
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
	public static void get_interface(){
		try{	
			PutMethod putMethod_getInterface = new PutMethod(ZABBIX_API_URL);
	    	putMethod_getInterface.setRequestHeader("Content-Type", "application/json-rpc");
			putMethod_getInterface.setRequestBody(zabbixapi.get_interfaceid(hostid, auth)); // the ip is the host name 
	    	client.executeMethod(putMethod_getInterface);
	    	JSONObject response = new JSONObject(putMethod_getInterface.getResponseBodyAsString());
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
			PutMethod putMethod_cpu = new PutMethod(ZABBIX_API_URL);
	    	putMethod_cpu.setRequestHeader("Content-Type", "application/json-rpc");
			putMethod_cpu.setRequestBody(zabbixapi.create_item_cpu(name, hostid , interfaceid ,null, "30", auth )); // the ip is the host name 
	    	client.executeMethod(putMethod_cpu);
	    	JSONObject response = new JSONObject(putMethod_cpu.getResponseBodyAsString());
	    	JSONObject result = response.getJSONObject("result");
	    	itemcpuid = result.getJSONArray("itemids").getString(0);
	    
			PutMethod putMethod_mem = new PutMethod(ZABBIX_API_URL);
	    	putMethod_mem.setRequestHeader("Content-Type", "application/json-rpc");
			putMethod_mem.setRequestBody(zabbixapi.create_item_mem(name, hostid , interfaceid ,null, "30", auth )); // the ip is the host name 
	    	client.executeMethod(putMethod_mem);
	    	response = new JSONObject(putMethod_mem.getResponseBodyAsString());
	    	result = response.getJSONObject("result");
	    	itemmemid = result.getJSONArray("itemids").getString(0);
	    }catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void createGraphs(String name){
		try{	
		    zabbixapi.create_graph(name + " CPU Usage Graph" , auth , itemcpuid);
			
		    //CPU
			PutMethod putMethod_cpu = new PutMethod(ZABBIX_API_URL);
	    	putMethod_cpu.setRequestHeader("Content-Type", "application/json-rpc");
			putMethod_cpu.setRequestBody(zabbixapi.create_graph(name + " CPU Usage Graph" , auth , itemcpuid)); // the ip is the host name 
	    	client.executeMethod(putMethod_cpu);
	    
			PutMethod putMethod_mem = new PutMethod(ZABBIX_API_URL);
	    	putMethod_mem.setRequestHeader("Content-Type", "application/json-rpc");
			putMethod_mem.setRequestBody(zabbixapi.create_graph(name + " Memory Usage Graph" , auth , itemmemid)); // the ip is the host name 
	    	client.executeMethod(putMethod_mem);
	    }catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static InputStream fromString(String str) throws UnsupportedEncodingException {
		byte[] bytes = str.getBytes("UTF-8");
		return new ByteArrayInputStream(bytes);
	}
}