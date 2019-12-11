import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class zabbixapi {

    public static String login(String username,String password){
        String login = "{\"jsonrpc\":\"2.0\",\"method\":\"user.login\",\"params\":{\"user\":\""+username+"\",\"password\":\""+password+"\"},\"id\":1}";
        return login;
        //return fromString(login);
    }

    public static String creeate_host(String ip , String auth){
        return create_host(ip,auth,"host name here",groups("1"),interfaces(ip,"10051"));
        //return fromString(create_host(ip,auth,groups(),interfaces(ip)));
    }

    public static String get_host(String host , String auth){
        String gethost = "{ \"jsonrpc\" : \"2.0\" , \"method\" : \"host.get\", \"params\" : { \"filter\" : { \"host\" : [ \""+host+"\"]}},\"auth\": \""+auth+"\",\"id\" : 1}";
        return gethost ;
        //return fromString(gethost);
    }
    public static String create_item(String app , String key , String hostid , String interfaceid , String[] application , String  delay , String auth){
        String createitem = "{\"jsonrpc\" : \"2.0\" , \"method\" : \"item.create\",\"params\": { \"name\": \""+app+"\",";
        createitem += "\"key_\": \""+key+"\", \"hostid\" : \""+hostid+"\", \"type\": 0 ,\"value_type\": 3,\"interfaceid\": \""+interfaceid+"\", ";
        if(application != null){
            createitem += "\"applications\": [ " ;
            for (int i = 0 ; i < application.length ; i++){
                if((application.length - i ) > 1){
                    createitem += "\""+application[i]+"\" ," ;
                }
                else{
                    createitem += "\""+application[i]+"\"]," ;
                }
            }
        }
        createitem += "\"delay\": 30 },";
        createitem += "\"auth\": \""+auth+"\",\"id\": 1}";
        return createitem ;
        //return fromString(createitem);
    }


    private static String create_host( String IP , String auth ,String host, String group , String interfaces ){
        String create_host = "{ \"jsonrpc\": \"2.0\", \"method\": \"host.create\", \"params\": { \"host\": \""+host+"\",";
        create_host += interfaces + "," + group + ",";
        create_host += "\"auth\": \""+auth+"\",\"id\":\"1\"}}";
        return create_host ;
    }
    private static String interfaces(String ip , String port){
        String interfaces = "\"interfaces\":[{ \"type\" : 1 , \"main\" : 1 , \"useip\" : 1 , \"ip\" : \""+ip+"\" , \"dns\" : \"\" , \"port\" : \""+port+"\" }]";
        return interfaces;
    }
    private static String groups (String groupid){
        String groups = "\"groups\": [{ \"groupid\" : \""+groupid+"\" }]";
        return groups ;
    }

    private static InputStream fromString(String str){
        byte[] bytes ;
        try {
            bytes = str.getBytes("UTF-8");
            return new ByteArrayInputStream(bytes);
        }catch (UnsupportedEncodingException e){
            System.out.println(e);
        }
        return null;
    }
}
