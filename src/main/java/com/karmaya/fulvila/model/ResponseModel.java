package com.karmaya.fulvila.model;

import org.springframework.stereotype.Component;

@Component
public class ResponseModel {
	private Object object;
	private int responseCode;
	private String responseMessage;
	private String timeStamp;
	private String path;
	
	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "ResponseModel{" +
				"object=" + object +
				", responseCode=" + responseCode +
				", responseMessage='" + responseMessage + '\'' +
				", timeStamp='" + timeStamp + '\'' +
				", path='" + path + '\'' +
				'}';
	}

	public ResponseModel buildResponse(Object obj, String msg, int code, String timeStamp, String path){
		this.object = obj;
		this.responseMessage = msg;
		this.responseCode = code;
		this.timeStamp = timeStamp;
		this.path = path;
		return this;
	}
}
