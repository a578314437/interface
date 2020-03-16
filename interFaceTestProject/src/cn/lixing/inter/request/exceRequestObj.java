package cn.lixing.inter.request;

import static cn.lixing.conn.db.uilts.dbAction.getDbSelectData;
import static cn.lixing.conn.db.uilts.dbAction.setDbUpdateData;
import static cn.lixing.uilts.JsonToMapUilt.json2map;
import static cn.lixing.uilts.PropertiesFileUilt.getData;
import static cn.lixing.uilts.TimeUilt.stringToTime;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import cn.lixing.db.obj.interFaceDataObj;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class exceRequestObj{
	private static String responseData;
	private static long requestTime;
	private static long startReqTime;
	private static long endReqTime;
	private static String [] responseDatas=new String[2];
	private static String sep=System.getProperty("file.separator");
	private static String requestMethod=getData("requestMethod", sep+"configFile"+sep+"http");
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static OkHttpClient okHttpClient = null;
	private static Request request = null;
	private static Logger logger=Logger.getLogger(exceRequestObj.class);
	
	
	public static String [] OkHttpGet(String url,String cookiesValue,String jwttoken) {
		logger.info("接口:"+url+":使用方法OkHttpGet,正在执行...");
	    okHttpClient = new OkHttpClient.Builder()
	            .connectTimeout(5000, TimeUnit.MILLISECONDS)
	            .readTimeout(5000,TimeUnit.MILLISECONDS)
	            .writeTimeout(5000,TimeUnit.MILLISECONDS)
	            .build();
	    Response response=null;
        request = new Request.Builder()
        		.addHeader("Accept", getData("Accept", sep+"configFile"+sep+"http"))
                .addHeader("User-Agent", getData("UserAgent", sep+"configFile"+sep+"http"))
                .addHeader("Cookie", cookiesValue)
                .addHeader("jwttoken",jwttoken)
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
	
	public static String [] OkHttpPost(String url,String json,String cookiesValue,String jwttoken) {
		logger.info("接口:"+url+":使用方法OkHttpPost,正在执行...");
		
		okHttpClient = new OkHttpClient.Builder()
	            .connectTimeout(5000, TimeUnit.MILLISECONDS)
	            .readTimeout(5000,TimeUnit.MILLISECONDS)
	            .writeTimeout(5000,TimeUnit.MILLISECONDS)
	            .build();
		
		logger.info("接口:"+url+"\n参数:"+json);
		RequestBody body = RequestBody.create(JSON,json);
		if(cookiesValue==null) {
			cookiesValue="";
		}
		Request request = new Request.Builder()
				.addHeader("Accept", getData("Accept",sep+"configFile"+sep+"http"))
                .addHeader("User-Agent", getData("UserAgent",sep+"configFile"+sep+"http"))
                .addHeader("Cookie", cookiesValue)
                .addHeader("User-Tenant", getData("User-Tenant",sep+"configFile"+sep+"http"))
                .addHeader("Content-Type", getData("Content-Type",sep+"configFile"+sep+"http"))
                .addHeader("X-Requested-With",getData("X-Requested-With",sep+"configFile"+sep+"http"))
                .addHeader("jwttoken",jwttoken)
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
	
	public static String [] OkHttpPostParam(String url,String json,String cookiesValue,String jwttoken,Map<String, String> params) {
		logger.info("接口:"+url+":使用方法OkHttpPostParam,正在执行...");
		okHttpClient = new OkHttpClient.Builder()
	            .connectTimeout(5000, TimeUnit.MILLISECONDS)
	            .readTimeout(5000,TimeUnit.MILLISECONDS)
	            .writeTimeout(5000,TimeUnit.MILLISECONDS)
	            .build();
		FormBody.Builder formBodyBuilder = new FormBody.Builder();
		Set<String> keySet = params.keySet();
		if (jwttoken.equals("")) {
			jwttoken="jwttoken";
		}
        for(String key:keySet) {
        	//logger.info("key = "+key+" value = "+params.get(key));
        	String value="";
        	if(key.contains("activeName")) {
        		value = Long.toString(System.currentTimeMillis());
				
			}else {
				value = params.get(key);
	            
			}
        	formBodyBuilder.add(key,value);
        }
		FormBody formBody = formBodyBuilder.build();
		Request request = new Request.Builder()
				.addHeader("Accept", getData("Accept",sep+"configFile"+sep+"http"))
                .addHeader("User-Agent", getData("UserAgent",sep+"configFile"+sep+"http"))
                .addHeader("Cookie", cookiesValue)
                .addHeader("User-Tenant", getData("User-Tenant",sep+"configFile"+sep+"http"))
                .addHeader("Content-Type", getData("Content-Type",sep+"configFile"+sep+"http"))
                .addHeader("X-Requested-With",getData("X-Requested-With",sep+"configFile"+sep+"http"))
                .addHeader("jwttoken",jwttoken)
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
					logger.info("执行的方法是================OkHttpGet================");
					objects=OkHttpGet(dataObj.getInterFaceReqUrl(),dataObj.getInterFaceCookie(),dataObj.getInterFaceJwtToken());
					dataObj.setInterFaceResResult(objects[0]);
					dataObj.setInterFaceReturnTime(objects[1]);
				}else if (requestMethod.equals("post")&&dataObj.getInterFaceIsJson()==0) {
					logger.info("执行的方法是================OkHttpPost================");
					objects=OkHttpPost(dataObj.getInterFaceReqUrl(),dataObj.getInterFaceReqParameter(),dataObj.getInterFaceCookie(),dataObj.getInterFaceJwtToken());
					dataObj.setInterFaceResResult(objects[0]);
					dataObj.setInterFaceReturnTime(objects[1]);
				}else if(requestMethod.equals("post")&&dataObj.getInterFaceIsJson()==1){
					logger.info("执行的方法是================OkHttpPostParam================");
					Map<String, String>map=json2map(dataObj.getInterFaceReqParameter());
					
					objects=OkHttpPostParam(dataObj.getInterFaceReqUrl(),dataObj.getInterFaceReqParameter(),dataObj.getInterFaceCookie(),dataObj.getInterFaceJwtToken(),map);
					dataObj.setInterFaceResResult(objects[0]);
					dataObj.setInterFaceReturnTime(objects[1]);
				}else {
					System.out.println("Parameter configuration error!");
				}
				setDbUpdateData(dataObj.getInterFaceResResult(),dataObj.getInterFaceReturnTime(),stringToTime(),dataObj.getId());
			}
		}
		
	}
//	public static void main(String[] args) throws ParseException {
//		//StorageResData("interFaceReqMethod",requestMethod);
//		Map<String, String>map=new HashMap<>();
//		String[] response=OkHttpPostParam("http://httpbin.org/post","{name:lixing,password:123}","",map);
//		System.out.println(response[0]);
//		System.out.println(response[1]);
//	}
}
