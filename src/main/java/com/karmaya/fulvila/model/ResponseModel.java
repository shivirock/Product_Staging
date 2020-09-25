package com.karmaya.fulvila.model;

import org.springframework.stereotype.Component;

@Component
public class ResponseModel {
	private Object object;
	private int responseCode;
	private String responseMessage;
	private String timeStamp;
	
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

	@Override
	public String toString() {
		return "ResponseModel{" +
				"object=" + object +
				", responseCode=" + responseCode +
				", responseMessage='" + responseMessage + '\'' +
				", timeStamp='" + timeStamp + '\'' +
				'}';
	}
}
