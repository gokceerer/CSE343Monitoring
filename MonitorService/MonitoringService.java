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
	private static String auth ;
	
	public static String connect(@Payload String input) throws HttpException, IOException {
		System.out.println(input);
		
		login();
		create_host("ip");
		System.out.println(auth);
		return "done";
	
	}
	@SuppressWarnings("deprecation")
	public static void login() throws HttpException, IOException, JSONException {
	
		client = new HttpClient();
		
		PutMethod putMethod = new PutMethod(ZABBIX_API_URL);
		putMethod.setRequestHeader("Content-Type", "application/json-rpc"); 
		JSONObject jsonObj=new JSONObject("{\"jsonrpc\":\"2.0\",\"method\":\"user.login\",\"params\":{\"user\":\"Admin\",\"password\":\"zabbix\"},\"id\":2}");
		putMethod.setRequestBody(fromString(jsonObj.toString())); 
		try {
			client.executeMethod(putMethod);
			JSONObject obj = new JSONObject(putMethod.getResponseBodyAsString()); 
			auth = new String(obj.getString("result"));
			System.out.println(auth);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
			
	}

	public static create_host(String ip)throws FileNotFoundException, UnsupportedEncodingException, ParseException, HttpException, IOException{
		PutMethod putMethod_createhost = new PutMethod(ZABBIX_API_URL);
    	putMethod_createhost.setRequestHeader("Content-Type", "application/json-rpc");
		putMethod_createhost.setRequestBody(zabbixapi.create_host(ip,auth)); 
    	client.executeMethod(putMethod_createhost);
	}
	
	public static InputStream fromString(String str) throws UnsupportedEncodingException {
		byte[] bytes = str.getBytes("UTF-8");
		return new ByteArrayInputStream(bytes);
	}

}