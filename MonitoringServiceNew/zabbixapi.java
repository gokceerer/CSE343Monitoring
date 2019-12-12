import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class zabbixapi {

    public static String login(String username,String password){
        String login = "{\"jsonrpc\":\"2.0\",\"method\":\"user.login\",\"params\":{\"user\":\""+username+"\",\"password\":\""+password+"\"},\"id\":1}";
        return login;
        //return fromString(login);
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

    public static String create_item_cpu(String app , String hostid , String interfaceid , String[] application , String  delay , String auth ){
        return create_item(app+" CPU_usage" , "proc.cpu.util["+app+"]" , hostid , interfaceid , application , delay ,auth);
    }

    public static String create_item_mem(String app , String hostid , String interfaceid , String[] application , String  delay , String auth ){
        return create_item(app+" mem_usage" , "proc.mem["+app+"]" , hostid , interfaceid , application , delay ,auth);
    }

    public static String get_interfaceid (String hostid , String auth){
        String getinterface = "{ \"jsonrpc\": \"2.0\" , \"method\" : \"hostinterface.get\",  \"params\": { \"output\": \"extend\", \"hostids\": \""+hostid+"\"},";
        getinterface += "\"auth\": \""+auth+"\",\"id\": 1} ";
        return getinterface;
        //return fromString(getinterface);
    }

    public static String create_graph(String name , String auth , String itemid){
        String creategraph = "{ \"jsonrpc\": \"2.0\", \"method\": \"graph.create\",\"params\": {\"name\": \""+name+"\",\"gitems\":";
        creategraph += "[ {\"itemid\": \""+itemid+"\",\"color\": \"00AA00\" }]},\"auth\": \""+auth+"\",\"id\": 1 }";
        return creategraph ;
        //return fromString(creategraph);
    }

    public static String creeate_host(String ip , String auth){ // the ip represents the host name here  
        return create_host(ip,auth,ip,groups("1"),interfaces("127.0.0.1","10050"));
        //return fromString(create_host(ip,auth,groups(),interfaces(ip)));
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
