package cn.lixing.uilts;

import static cn.lixing.conn.db.uilts.dbAction.setInsertData;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cn.lixing.db.obj.interFaceDataObj;
import static cn.lixing.uilts.PropertiesFileUilt.*;
import static cn.lixing.uilts.ParserHarFileUilt.*;


public class ParserJsonUilt2 {
	private static Map<String, String> map;
	private static Logger logger=Logger.getLogger(ParserJsonUilt2.class);
	public static Map<String, String> ParseJsonDataToMap(String jsonData,String isStora){
		interFaceDataObj dataObj;
		JsonParser parser=new JsonParser();
		JsonObject obj=(JsonObject) parser.parse(jsonData);
		JsonArray entriesElements=obj.get("log").getAsJsonObject().get("entries").getAsJsonArray();
	    //加强for循环遍历JsonArray
	    for (JsonElement user : entriesElements) {
	        //使用GSON，直接转成Bean对象
	    	JsonElement resquestElement=user.getAsJsonObject().get("request");
	    	dataObj=new interFaceDataObj();

    		dataObj.setInterFaceReqMethod(resquestElement.getAsJsonObject().get("method").toString().replace("\"", ""));
    		dataObj.setInterFaceReqUrl(resquestElement.getAsJsonObject().get("url").toString().replace("\"", ""));
    
    		
    		JsonArray headersElements =resquestElement.getAsJsonObject().get("headers").getAsJsonArray();
    		for (int i=0;i<headersElements.size();i++) {
    			
    			for(Object key2:headersElements.get(i).getAsJsonObject().entrySet()) {
    				
    				if(key2.toString().contains("Cookie")) {
    					dataObj.setInterFaceCookie(headersElements.get(i).getAsJsonObject().get("value").toString().replace("\"", ""));

    				}else if(key2.toString().contains("jwttoken")){
    					dataObj.setInterFaceJwtToken(headersElements.get(i).getAsJsonObject().get("value").toString().toString().replace("\\", "").replace("}\"", "}").replace("\"{", "{"));
    				}else {
    					continue;
    				}
    			}
    		}
    		
	    	for(Object key:resquestElement.getAsJsonObject().get("postData").getAsJsonObject().entrySet()) {
	    		
	    		if(key.toString().contains("params")) {
	    			JsonArray paramsArray=resquestElement.getAsJsonObject().get("postData").getAsJsonObject().get("params").getAsJsonArray();
	    			map=new HashMap<String, String>();
	    			for(int i=0;i<paramsArray.size();i++) {
	    				String mapkey=paramsArray.get(i).getAsJsonObject().get("name").toString();
	    				String mapvalue=paramsArray.get(i).getAsJsonObject().get("value").toString();	
	    				
	    				
	    				
	    				if(!mapkey.equals("null")&&!mapvalue.equals("")) {
	    					if(mapkey.contains("activeName")) {
		    					map.put(mapkey, Long.toString(System.currentTimeMillis()));
		    					//System.out.println("==============================================="+mapkey);
		    				}else {
		    					map.put(mapkey, mapvalue);
		    				}
	    					
	    				}
	    				
	    			}
	    			
	    			//logger.info("请求参数：\n"+map.toString().replace("=", ":"));
	    			dataObj.setInterFaceReqParameter(map.toString().replace("=", ":"));
	    			
	    			if(isStora.equals("true")) {
	    				logger.info("执行数据导入操作");
	    				setInsertData(dataObj.getInterFaceReqUrl(), dataObj.getInterFaceReqParameter(), dataObj.getInterFaceReqMethod(),dataObj.getInterFaceCookie(),1,dataObj.getInterFaceJwtToken());
		    			break;
	    			}
	    			break;
	    		}else {
	    			
	    			dataObj.setInterFaceReqParameter(resquestElement.getAsJsonObject().get("postData").getAsJsonObject().get("text").toString().replace("\\", "").replace("}\"", "}").replace("\"{", "{"));
	    			if(isStora.equals("true")) {
	    				logger.info("执行数据导入操作");
	    				setInsertData(dataObj.getInterFaceReqUrl(), dataObj.getInterFaceReqParameter(), dataObj.getInterFaceReqMethod(),dataObj.getInterFaceCookie(),0,dataObj.getInterFaceJwtToken());
		    			break;
	    			}
	    			break;
	    		}
	    	}
	    }
	    return map;
	}
	
	public static void main(String[] args) {
		Map<String, String>map=ParseJsonDataToMap(readToString(getData("harFilePath", "\\configFile\\http")),getData("isStor", "\\configFile\\http"));
		
	}
}
