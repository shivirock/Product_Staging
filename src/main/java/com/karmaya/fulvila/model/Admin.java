package com.karmaya.fulvila.model;

import com.mongodb.lang.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "admin")
public class Admin {
	private static final Logger LOGGER = LoggerFactory.getLogger(Admin.class);

	@Transient
	public static final String SEQUENCE_NAME = "admin_sequence";
	@Id
	private long id;
	@NonNull
	private String name;
	@NonNull
	@Indexed(name = "admin_email",unique = true)
	private String email;
	@NonNull
	private String password;
	private String primaryContact;
	private List<String> otherContact;
	private String aboutAdmin;
	private String designation;
	private String linkdinProfileURL;
	public static enum RegistrationType{
		surfer,
		builder,
		vendor,
		architect
	}
	private List<RegistrationType> registrationType;
	public static enum  AuthProvider implements Serializable {
		local,
		facebook,
		google
	}
	private AuthProvider authProvider;
	private String timeStamp;
	private String lastUpdateTime;
	public static enum ActivationStatus{
		Active,
		Inactive
	}
	private ActivationStatus activationStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NonNull
	public String getName() {
		return name;
	}

	public void setName(@NonNull String name) {
		this.name = name;
	}

	@NonNull
	public String getEmail() {
		return email;
	}

	public void setEmail(@NonNull String email) {
		this.email = email;
	}

	@NonNull
	public String getPassword() {
		return password;
	}

	public void setPassword(@NonNull String password) {
		this.password = password;
	}

	public String getPrimaryContact() {
		return primaryContact;
	}

	public void setPrimaryContact(String primaryContact) {
		this.primaryContact = primaryContact;
	}

	public List<String> getOtherContact() {
		return otherContact;
	}

	public void setOtherContact(List<String> otherContact) {
		this.otherContact = otherContact;
	}

	public String getAboutAdmin() {
		return aboutAdmin;
	}

	public void setAboutAdmin(String aboutAdmin) {
		this.aboutAdmin = aboutAdmin;
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

	public List<RegistrationType> getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(List<RegistrationType> registrationType) {
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

	public AuthProvider getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(AuthProvider authProvider) {
		this.authProvider = authProvider;
	}

	public ActivationStatus getActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(ActivationStatus activationStatus) {
		this.activationStatus = activationStatus;
	}

	@Override
	public String toString() {
		return "Admin{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", primaryContact='" + primaryContact + '\'' +
				", otherContact=" + otherContact +
				", aboutAdmin='" + aboutAdmin + '\'' +
				", designation='" + designation + '\'' +
				", linkdinProfileURL='" + linkdinProfileURL + '\'' +
				", registrationType=" + registrationType +
				", authProvider=" + authProvider +
				", timeStamp='" + timeStamp + '\'' +
				", lastUpdateTime='" + lastUpdateTime + '\'' +
				", activationStatus=" + activationStatus +
				'}';
	}
}
