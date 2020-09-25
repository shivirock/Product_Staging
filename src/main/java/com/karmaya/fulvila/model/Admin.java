package com.karmaya.fulvila.model;

import com.mongodb.lang.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin")
public class Admin {
	private static final Logger LOGGER = LoggerFactory.getLogger(Admin.class);

	@Transient
	public static final String SEQUENCE_NAME = "admin_sequence";
	@Id
	private long id;
	@NonNull
	private String adminName;
	@NonNull
	@Indexed(name = "admin_email",unique = true)
	private String adminEmail;
	@NonNull
	private String adminPassword;
	private String adminContact;
	private String designation;
	private String linkdinProfileURL;
	private String registrationType;
	private String timeStamp;
	private String lastUpdateTime;

	public static Logger getLOGGER() {
		return LOGGER;
	}

	public static String getSequenceName() {
		return SEQUENCE_NAME;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NonNull
	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(@NonNull String adminName) {
		this.adminName = adminName;
	}

	@NonNull
	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(@NonNull String adminEmail) {
		this.adminEmail = adminEmail;
	}

	@NonNull
	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(@NonNull String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getAdminContact() {
		return adminContact;
	}

	public void setAdminContact(String adminContact) {
		this.adminContact = adminContact;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getLinkdinProfileURL() {
		return linkdinProfileURL;
	}

	public void setLinkdinProfileURL(String linkdinProfileURL) {
		this.linkdinProfileURL = linkdinProfileURL;
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String toString() {
		return "Admin{" +
				"id=" + id +
				", adminName='" + adminName + '\'' +
				", adminEmail='" + adminEmail + '\'' +
				", adminPassword='" + adminPassword + '\'' +
				", adminContact='" + adminContact + '\'' +
				", designation='" + designation + '\'' +
				", linkdinProfileURL='" + linkdinProfileURL + '\'' +
				", registrationType='" + registrationType + '\'' +
				", timeStamp='" + timeStamp + '\'' +
				", lastUpdateTime='" + lastUpdateTime + '\'' +
				'}';
	}
}
