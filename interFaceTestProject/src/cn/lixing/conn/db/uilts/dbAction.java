package cn.lixing.conn.db.uilts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import static cn.lixing.conn.db.ConnectDataBase.*;
import static cn.lixing.uilts.ParserHarFileUilt.readToString;
import static cn.lixing.uilts.ParserJsonUilt2.ParseJsonDataToMap;
import static cn.lixing.uilts.PropertiesFileUilt.getData;

import cn.lixing.db.obj.interFaceDataObj;
public class dbAction {
	private static Connection connection;
	private static List<interFaceDataObj>resultData;
	private static HashMap<Object, List<interFaceDataObj>>resultMap;
	private static String sql;
	private static PreparedStatement pmt;
	private static Logger logger=Logger.getLogger(dbAction.class);
	private static String sep=System.getProperty("file.separator");
	
	public static HashMap<Object, List<interFaceDataObj>> getDbSelectData(String colName,Object colValue){
		connection=getConnect();
		resultData=new ArrayList<>();
		resultMap=new HashMap<>();
		interFaceDataObj dataObj = null;
		PreparedStatement pmt = null;
		ResultSet result;
		
		sql="select * from t_interface_info where "+colName+" =?";
		try {
			pmt=connection.prepareStatement(sql);
			pmt.setObject(1,colValue);
			result=pmt.executeQuery();
			logger.info("执行的sql："+sql);
			logger.info("请求的parameter："+colValue);
			logger.info("执行sql："+"select * from t_interface_info where "+colName+" ="+colValue+"");
			while(result.next()) {
				dataObj=new interFaceDataObj();
				dataObj.setId(result.getInt("id"));
				dataObj.setInterFaceMode(result.getString("interFaceMode"));
				dataObj.setInterFaceReqParameter(result.getString("interFaceReqParameter"));
				dataObj.setInterFaceReqUrl(result.getString("interFaceReqUrl"));
				dataObj.setInterFaceReqMethod(result.getString("interFaceReqMethod"));
				dataObj.setInterFaceResResult(result.getString("interFaceResResult"));
				dataObj.setInterFaceReturnTime(result.getString("interFaceReturnTime"));
				dataObj.setInterFaceCookie(result.getString("interFaceCookie"));
				dataObj.setUpdateTime(result.getTimestamp("updateTime"));
				dataObj.setInterFaceIsJson(result.getInt("interFaceIsJson"));
				dataObj.setInterFaceThreadNum(result.getInt("interFaceThreadNum"));
				dataObj.setInterFaceJwtToken(result.getString("interFaceJwtToken"));
				resultData.add(dataObj);
			}
			try {
				resultMap.put(dataObj.getId(), resultData);
			} catch (NullPointerException e) {
				logger.info("数据为空，进行导入操作...");
				ParseJsonDataToMap(readToString(getData("harFilePath", sep+"configFile"+sep+"http")),"true");
			}
		} catch (SQLException e) {
			try {
				pmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return resultMap;
	}
	
	public static int setDbUpdateData(String val1,String val2,Timestamp val3,int keyVal) {
		connection=getConnect();
		int updateRowNum =0;
		sql="update t_interface_info set interFaceResResult=?, interFaceReturnTime=?,updateTime=? where id =?";
		try {
			pmt=connection.prepareStatement(sql);
			pmt.setString(1,val1);
			pmt.setString(2, val2);
			pmt.setTimestamp(3, val3);
			pmt.setInt(4, keyVal);
			logger.info("执行的sql："+sql);
			updateRowNum=pmt.executeUpdate();
		} catch (SQLException e) {
			try {
				pmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return updateRowNum;
	}
	
	public static int setInsertData(String url,String parame,String method,String cookies,int IsJson,String jwttoken) {
		connection=getConnect();
		int updateRowNum =0;
		sql="INSERT INTO t_interface_info (interFaceReqUrl,interFaceReqParameter,interFaceReqMethod,interFaceCookie,interFaceIsJson,interFaceJwtToken)values(?,?,?,?,?,?)";
		try {
			pmt=connection.prepareStatement(sql);
			pmt.setString(1, url);
			pmt.setString(2, parame);
			pmt.setString(3, method);
			pmt.setString(4, cookies);
			pmt.setInt(5, IsJson);
			pmt.setString(6, jwttoken);
			logger.info("执行的sql："+sql);
			logger.info("请求的parameter："+url+","+parame+","+method+","+cookies+","+IsJson+","+jwttoken);
			updateRowNum=pmt.executeUpdate();
			logger.info("更新了"+updateRowNum+"行记录");
			
		} catch (SQLException e) {
			try {
				pmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return updateRowNum;
	}
//	public static void main(String[] args) {
//		//int isSucceed=setDbUpdateData("sssss","9ss",1);
//		//System.out.println(isSucceed);
//		//System.out.println(setInsertData("2","3","4","4","4","2"));
//		HashMap<Object, List<interFaceDataObj>> hashMap=getDbSelectData("interFaceReqMethod","post");
//		for(Object obj:hashMap.keySet()) {
//			List<interFaceDataObj> dataObjs=hashMap.get(obj);
//			for(interFaceDataObj dataObj :dataObjs) {
//				System.out.println(dataObj.getId());
//				System.out.println(dataObj.getInterFaceMode());
//				System.out.println(dataObj.getInterFaceReqParameter());
//				System.out.println(dataObj.getInterFaceReqUrl());
//				System.out.println(dataObj.getInterFaceResResult());
//				System.out.println(dataObj.getInterFaceReturnTime());
//				System.out.println(dataObj.getInterFaceCookie());
//				System.out.println("===============================");
//			}
//		}
//
//	}
	
}
