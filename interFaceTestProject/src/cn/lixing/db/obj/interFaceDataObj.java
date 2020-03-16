package cn.lixing.db.obj;

import java.sql.Timestamp;

public class interFaceDataObj {
	private int id;
	private String interFaceMode;
	private String interFaceResResult;
	private String interFaceReturnTime;
	
	private String interFaceCookie="222";
	private String interFaceAccept;
	private String interFaceUserAgent;
	private String interFaceReqUrl;
	private String interFaceReqParameter;
	private String interFaceReqMethod;
	private int interFaceIsJson;
	private int interFaceThreadNum;
	private String interFaceJwtToken;
	
	public String getInterFaceJwtToken() {
		return interFaceJwtToken;
	}
	public void setInterFaceJwtToken(String interFaceJwtToken) {
		this.interFaceJwtToken = interFaceJwtToken;
	}
	private Timestamp updateTime;
	
	public String getInterFaceCookie() {
		return interFaceCookie;
	}
	public void setInterFaceCookie(String interFaceCookie) {
		this.interFaceCookie = interFaceCookie;
	}
	public String getInterFaceAccept() {
		return interFaceAccept;
	}
	public void setInterFaceAccept(String interFaceAccept) {
		this.interFaceAccept = interFaceAccept;
	}
	public String getInterFaceUserAgent() {
		return interFaceUserAgent;
	}
	public void setInterFaceUserAgent(String interFaceUserAgent) {
		this.interFaceUserAgent = interFaceUserAgent;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInterFaceMode() {
		return interFaceMode;
	}
	public void setInterFaceMode(String interFaceMode) {
		this.interFaceMode = interFaceMode;
	}
	public String getInterFaceReqUrl() {
		return interFaceReqUrl;
	}
	public void setInterFaceReqUrl(String interFaceReqUrl) {
		this.interFaceReqUrl = interFaceReqUrl;
	}
	public String getInterFaceReqParameter() {
		return interFaceReqParameter;
	}
	public void setInterFaceReqParameter(String interFaceReqParameter) {
		this.interFaceReqParameter = interFaceReqParameter;
	}
	public String getInterFaceResResult() {
		return interFaceResResult;
	}
	public void setInterFaceResResult(String interFaceResResult) {
		this.interFaceResResult = interFaceResResult;
	}
	public String getInterFaceReturnTime() {
		return interFaceReturnTime;
	}
	public void setInterFaceReturnTime(String interFaceReturnTime) {
		this.interFaceReturnTime = interFaceReturnTime;
	}
	public String getInterFaceReqMethod() {
		return interFaceReqMethod;
	}
	public void setInterFaceReqMethod(String interFaceReqMethod) {
		this.interFaceReqMethod = interFaceReqMethod;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public int getInterFaceIsJson() {
		return interFaceIsJson;
	}
	public void setInterFaceIsJson(int interFaceIsJson) {
		this.interFaceIsJson = interFaceIsJson;
	}
	public int getInterFaceThreadNum() {
		return interFaceThreadNum;
	}
	public void setInterFaceThreadNum(int interFaceThreadNum) {
		this.interFaceThreadNum = interFaceThreadNum;
	}
}
