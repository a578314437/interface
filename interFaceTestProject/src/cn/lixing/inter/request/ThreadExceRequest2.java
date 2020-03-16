package cn.lixing.inter.request;

import static cn.lixing.conn.db.uilts.dbAction.getDbSelectData;
import static cn.lixing.conn.db.uilts.dbAction.setDbUpdateData;
import static cn.lixing.uilts.JsonToMapUilt.json2map;
import static cn.lixing.uilts.ParserHarFileUilt.readToString;
import static cn.lixing.uilts.PropertiesFileUilt.getData;
import static cn.lixing.uilts.TimeUilt.stringToTime;
import static cn.lixing.uilts.ParserJsonUilt2.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.lixing.db.obj.interFaceDataObj;

import static cn.lixing.inter.request.exceRequestObj.*;

public class ThreadExceRequest2 extends Thread {
	private static String sep=System.getProperty("file.separator");
	private String requestMethod=getData("requestMethod", sep+"configFile"+sep+"http");
	private String colName=getData("colName", sep+"configFile"+sep+"http");
	private String colValue=getData("colValue", sep+"configFile"+sep+"http");
	private String[] objects;
	private static Logger logger=Logger.getLogger(ThreadExceRequest2.class);
	
	public int getThreadNum() {
		int threadNum=0;
		HashMap<Object, List<interFaceDataObj>> dbDataHashMap=getDbSelectData(colName,colValue);
		for(Object obj:dbDataHashMap.keySet()) {
			List<interFaceDataObj> dataObjs=dbDataHashMap.get(obj);
			for(interFaceDataObj dataObj :dataObjs) {
				threadNum=dataObj.getInterFaceThreadNum();
			}
		}
		return threadNum;
	}
	public void exceInterFaceTest() {
		HashMap<Object, List<interFaceDataObj>> dbDataHashMap=getDbSelectData(colName,colValue);
		for(Object obj:dbDataHashMap.keySet()) {
			List<interFaceDataObj> dataObjs=dbDataHashMap.get(obj);
			for(interFaceDataObj dataObj :dataObjs) {
				if(requestMethod.equals("get")) {
					logger.info("执行的方法是 ================OkHttpGet================");
					objects=OkHttpGet(dataObj.getInterFaceReqUrl(),dataObj.getInterFaceCookie(),dataObj.getInterFaceJwtToken());
					dataObj.setInterFaceResResult(objects[0]);
					dataObj.setInterFaceReturnTime(objects[1]);
				}else if (requestMethod.equals("post")&&dataObj.getInterFaceIsJson()==0) {
					logger.info("执行的方法是 ================OkHttpPost================");
					objects=OkHttpPost(dataObj.getInterFaceReqUrl(),dataObj.getInterFaceReqParameter(),dataObj.getInterFaceCookie(),dataObj.getInterFaceJwtToken());
					dataObj.setInterFaceResResult(objects[0]);
					dataObj.setInterFaceReturnTime(objects[1]);
				}else if(requestMethod.equals("post")&&dataObj.getInterFaceIsJson()==1){
					logger.info("执行的方法是 ================OkHttpPostParam================");
					Map<String, String>map=json2map(dataObj.getInterFaceReqParameter());
					logger.info("参数转map前的字符串："+dataObj.getInterFaceReqParameter());
					logger.info("参数转map后的字符串："+map.toString());
					objects=OkHttpPostParam(dataObj.getInterFaceReqUrl(),dataObj.getInterFaceReqParameter(),dataObj.getInterFaceCookie(),dataObj.getInterFaceJwtToken(),map);
					dataObj.setInterFaceResResult(objects[0]);
					dataObj.setInterFaceReturnTime(objects[1]);
				}else {
					logger.info("Parameter configuration error!");
				}
				try {
					setDbUpdateData(dataObj.getInterFaceResResult(),dataObj.getInterFaceReturnTime(),stringToTime(),dataObj.getId());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void run() {
		HashMap<Object, List<interFaceDataObj>> dbDataHashMap=getDbSelectData(colName,colValue);
		for(Object obj:dbDataHashMap.keySet()) {
			List<interFaceDataObj> dataObjs=dbDataHashMap.get(obj);
			for(interFaceDataObj dataObj :dataObjs) {
				if(dataObj.getInterFaceThreadNum()>0) {
					logger.info("线程数为："+dataObj.getInterFaceThreadNum()+" 开始执行");
					for(int i=0;i<dataObj.getInterFaceThreadNum();i++) {
						logger.info(Thread.currentThread().getName()+" run ---"+i);
						if(requestMethod.equals("get")) {
							logger.info("=====================使用 OkHttpGet 方法=========================");
							objects=OkHttpGet(dataObj.getInterFaceReqUrl(),dataObj.getInterFaceCookie(),dataObj.getInterFaceJwtToken());
							dataObj.setInterFaceResResult(objects[0]);
							dataObj.setInterFaceReturnTime(objects[1]);
						}else if (requestMethod.equals("post")&&dataObj.getInterFaceIsJson()==0) {
							logger.info("=====================使用 OkHttpPost 方法=========================");
							objects=OkHttpPost(dataObj.getInterFaceReqUrl(),dataObj.getInterFaceReqParameter(),dataObj.getInterFaceCookie(),dataObj.getInterFaceJwtToken());
							dataObj.setInterFaceResResult(objects[0]);
							dataObj.setInterFaceReturnTime(objects[1]);
						}else if(requestMethod.equals("post")&&dataObj.getInterFaceIsJson()==1){
							Map<String, String>map=json2map(dataObj.getInterFaceReqParameter());
							logger.info("参数转map前的字符串："+dataObj.getInterFaceReqParameter());
							logger.info("参数转map后的字符串："+map.toString());
							logger.info("=====================使用 OkHttpPostParam 方法=========================");
							objects=OkHttpPostParam(dataObj.getInterFaceReqUrl(),dataObj.getInterFaceReqParameter(),dataObj.getInterFaceCookie(),dataObj.getInterFaceJwtToken(),map);
							dataObj.setInterFaceResResult(objects[0]);
							dataObj.setInterFaceReturnTime(objects[1]);
						}else {
							logger.info("Parameter configuration error!");
						}
						try {
							setDbUpdateData(dataObj.getInterFaceResResult(),dataObj.getInterFaceReturnTime(),stringToTime(),dataObj.getId());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}	
				}
				
			}
		}
		//exceInterFaceTest();
	}
	public void MainRun() throws InterruptedException {
		
		ParseJsonDataToMap(readToString(getData("harFilePath", System.getProperty("file.separator")+"configFile"+System.getProperty("file.separator")+"http")),getData("isStor", System.getProperty("file.separator")+"configFile"+System.getProperty("file.separator")+"http"));
		ThreadExceRequest2 exceRequest=new ThreadExceRequest2();
		exceRequest.start();
		
	}
	public static void main(String[] args) throws InterruptedException {
		ThreadExceRequest2 exceRequest=new ThreadExceRequest2();
		exceRequest.MainRun();

	}
}
