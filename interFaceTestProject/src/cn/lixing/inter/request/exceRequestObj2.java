package cn.lixing.inter.request;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.lixing.db.obj.interFaceDataObj;
import static cn.lixing.conn.db.uilts.dbAction.*;
import static cn.lixing.uilts.PropertiesFileUilt.*;
import static cn.lixing.uilts.TimeUilt.*;
import static cn.lixing.uilts.JsonToMapUilt.*;


public class exceRequestObj2{
	private static String responseData;
	private static long requestTime;
	private static long startReqTime;
	private static long endReqTime;
	private static String [] responseDatas=new String[2];
	private static String requestMethod=getData("requestMethod", "\\configFile\\http");
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static OkHttpClient okHttpClient = null;
	private static Request request = null;
	private static Logger logger=Logger.getLogger(exceRequestObj2.class);
	
	
	
	public static String [] OkHttpGet(String url,String cookiesValue) {
		logger.info("接口:"+url+":使用方法get,正在执行...");
	    okHttpClient = new OkHttpClient();
	    Response response=null;
        request = new Request.Builder()
        		.addHeader("Accept", getData("Accept", "\\configFile\\http"))
                .addHeader("User-Agent", getData("UserAgent", "\\configFile\\http"))
                .addHeader("Cookie", cookiesValue)
                .get()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            response = call.execute();
            responseData=response.body().string();
            logger.info("接口:"+url+"\n返回结果:\n"+responseData);
            startReqTime=response.receivedResponseAtMillis();
            endReqTime=response.sentRequestAtMillis();
            requestTime=startReqTime-endReqTime;
            logger.info("接口:"+url+"\n接口耗时:"+requestTime+"ms");

            responseDatas[0]=responseData;
            responseDatas[1]=requestTime+"ms";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseDatas;
    }
	
	public static String [] OkHttpPost(String url,String json,String cookiesValue) {
		logger.info("接口:"+url+":使用方法post,正在执行...");
		okHttpClient = new OkHttpClient();
		if(json==null) {
			json="";
		}
		logger.info("接口:"+url+"\n参数:"+json);
		RequestBody body = RequestBody.create(JSON,json);
		if(cookiesValue==null) {
			cookiesValue="";
		}
		Request request = new Request.Builder()
				.addHeader("Accept", getData("Accept","\\configFile\\http"))
                .addHeader("User-Agent", getData("UserAgent","\\configFile\\http"))
                .addHeader("Cookie", cookiesValue)
                .addHeader("User-Tenant", getData("User-Tenant","\\configFile\\http"))
                .addHeader("Content-Type", getData("Content-Type","\\configFile\\http"))
                .addHeader("X-Requested-With",getData("X-Requested-With","\\configFile\\http"))
				.url(url)
				.post(body)
				.build();
		try {
			Response response = okHttpClient.newCall(request).execute();
			if (response.isSuccessful()) {
				responseData=response.body().string();
				logger.info("接口:"+url+"\n返回结果:\n"+responseData);
				startReqTime=response.receivedResponseAtMillis();
	            endReqTime=response.sentRequestAtMillis();
	            requestTime=startReqTime-endReqTime;
	            logger.info("接口:"+url+"\n接口耗时:"+requestTime+"ms");

	            responseDatas[0]=responseData;
	            responseDatas[1]=requestTime+"ms";
		        return responseDatas;
		    } else {
		        throw new IOException("Unexpected code " + response);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String [] OkHttpPostParam(String url,String json,String cookiesValue,Map<String, String> params) {
		logger.info("接口:"+url+":使用方法post,正在执行...");
		okHttpClient = new OkHttpClient();
		if(json==null) {
			json="";
		}
		logger.info("接口:"+url+"\n参数:"+json);
		FormBody.Builder formBodyBuilder = new FormBody.Builder();
		Set<String> keySet = params.keySet();
		
        for(String key:keySet) {
            String value = params.get(key);
            formBodyBuilder.add(key,value);
        }
		FormBody formBody = formBodyBuilder.build();
		Request request = new Request.Builder()
				.addHeader("Accept", getData("Accept","\\configFile\\http"))
                .addHeader("User-Agent", getData("UserAgent","\\configFile\\http"))
                .addHeader("Cookie", cookiesValue)
                .addHeader("User-Tenant", getData("User-Tenant","\\configFile\\http"))
                .addHeader("Content-Type", getData("Content-Type","\\configFile\\http"))
                .addHeader("X-Requested-With",getData("X-Requested-With","\\configFile\\http"))
				.url(url)
				.post(formBody)
				.build();
		try {
			Response response = okHttpClient.newCall(request).execute();
			if (response.isSuccessful()) {
				responseData=response.body().string();
				logger.info("接口:"+url+"\n返回结果:\n"+responseData);
				startReqTime=response.receivedResponseAtMillis();
	            endReqTime=response.sentRequestAtMillis();
	            requestTime=startReqTime-endReqTime;
	            logger.info("接口:"+url+"\n接口耗时:"+requestTime+"ms");

	            responseDatas[0]=responseData;
	            responseDatas[1]=requestTime+"ms";
		        return responseDatas;
		    } else {
		        throw new IOException("Unexpected code " + response);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void StorageResData(String colName,Object colValue) throws ParseException {
		String[] objects;
		HashMap<Object, List<interFaceDataObj>> dbDataHashMap=getDbSelectData(colName,colValue);
		for(Object obj:dbDataHashMap.keySet()) {
			List<interFaceDataObj> dataObjs=dbDataHashMap.get(obj);
			for(interFaceDataObj dataObj :dataObjs) {
				if(requestMethod.equals("get")) {
					objects=OkHttpGet(dataObj.getInterFaceReqUrl(),dataObj.getInterFaceCookie());
					dataObj.setInterFaceResResult(objects[0]);
					dataObj.setInterFaceReturnTime(objects[1]);
				}else if (requestMethod.equals("post")&&dataObj.getInterFaceIsJson()==0) {
					objects=OkHttpPost(dataObj.getInterFaceReqUrl(),dataObj.getInterFaceReqParameter(),dataObj.getInterFaceCookie());
					dataObj.setInterFaceResResult(objects[0]);
					dataObj.setInterFaceReturnTime(objects[1]);
				}else if(requestMethod.equals("post")&&dataObj.getInterFaceIsJson()==1){
					Map<String, String>map=json2map(dataObj.getInterFaceReqParameter());
					objects=OkHttpPostParam(dataObj.getInterFaceReqUrl(),dataObj.getInterFaceReqParameter(),dataObj.getInterFaceCookie(),map);
					dataObj.setInterFaceResResult(objects[0]);
					dataObj.setInterFaceReturnTime(objects[1]);
				}else {
					System.out.println("Parameter configuration error!");
				}
				setDbUpdateData(dataObj.getInterFaceResResult(),dataObj.getInterFaceReturnTime(),stringToTime(),dataObj.getId());
			}
		}
		
	}
	public static void main(String[] args) throws ParseException {
		StorageResData("interFaceReqMethod",requestMethod);
		//Map<String, String>map=new HashMap<>();
		//String[] response=OkHttpPostParam("http://httpbin.org/post","{name:lixing,password:123}","",map);
		//System.out.println(response[0]);
		//System.out.println(response[1]);
	}
}
