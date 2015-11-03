package weico.gyx.org.person_comment_client.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gyx on 2015/9/5.
 */
public class JsonUtils{

    private static JsonUtils jsonUtils = null;
    private Gson gson;
    private HttpURLConnection con = null;
    private URL url = null;


    public JsonUtils(){
        gson = new Gson();
    }
    //获取实例
    public static JsonUtils getInstance(){
        if(jsonUtils == null){
            synchronized (JsonUtils.class){
                if(jsonUtils == null){
                    jsonUtils = new JsonUtils();
                }
            }
        }
        return jsonUtils;
    }

    public String _getHttpToGetJsonData(String jsonUrl){
        StringBuffer sb = new StringBuffer();
        try {

            url = new URL(jsonUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(3000);
            if(con.getResponseCode() == 200){
                Log.e("TAG", "doInBackground " + "SUCCESS 200");
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                con.getInputStream(),"UTF-8"));
                String s;
                while((s = reader.readLine()) != null){
                    sb.append(s);
                }
                reader.close();
                return sb.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            con.disconnect();
        }
        return null;
    }

    public String _postHttpToGetJsonData(String jsonUrl){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] jsonData = new byte[1024];
        int len = 0;

        try {

            url = new URL(jsonUrl);
            con = (HttpURLConnection)
                    url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "text/html");
            con.setRequestProperty("Accept-Charset", "utf-8");
            con.setRequestProperty("contentType", "utf-8");
            con.setConnectTimeout(3000);
            if(con.getResponseCode() == 200){
                Log.e("TAG", "doInBackground " + "SUCCESS 200");
                InputStream inputStream = con.getInputStream();
                while((len = inputStream.read(jsonData)) != -1){
                    outputStream.write(jsonData,0,len);
                }
                inputStream.close();
                return new String(outputStream.toByteArray());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            con.disconnect();
        }
        return null;

    }


    //GSON解析json文件  返回数据
    public <T>List<T> parseCityDatasJson(String jsonString,List<T> data) {
        Log.e("TAG", "JSON is" + jsonString);
        Type type = new TypeToken<LinkedList<T>>(){}.getType();
        LinkedList<T> t = gson.fromJson(jsonString, type);

        List<T> list = data;
        for(Iterator<T> iterator = t.iterator();iterator.hasNext();){
            T c = iterator.next();
            list.add(c);
        }
        return list;
    }
}
