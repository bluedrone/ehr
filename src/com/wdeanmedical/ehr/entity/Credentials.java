package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "credentials")
public class Credentials extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1462745762564975233L;

	private Integer patientId;
	private String mrn;
	private String username;
	private String password;
	private String firstName;
	private String middleName;
	private String lastName;
	private String additionalName;
	private String email;
	private String salt;
	private int authStatus;
	private int accessLevel;
	private String sessionId;
	private PatientStatus status;
	private String govtId;
	private Date lastLoginTime;
	private String previousLoginTime;
	private String activationCode;

	public Credentials() {
	}

	@Column(name = "patient_id")
	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password")
	@Basic(optional = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "first_name")
	@Basic(optional = false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "middle_name")
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "last_name")
	@Basic(optional = false)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "salt")
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Column(name = "last_login_time")
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Column(name = "access_level")
	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}

	@Column(name = "activation_code")
	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	@JoinColumn(name = "patient_status", referencedColumnName = "id")
	@ManyToOne(optional = false)
	public PatientStatus getStatus() {
		return status;
	}

	public void setStatus(PatientStatus status) {
		this.status = status;
	}

	@Column(name = "mrn")
	public String getMrn() {
		return mrn;
	}

	public void setMrn(String mrn) {
		this.mrn = mrn;
	}

	@Column(name = "additional_name")
	public String getAdditionalName() {
		return additionalName;
	}

	public void setAdditionalName(String additionalName) {
		this.additionalName = additionalName;
	}

	@Column(name = "govt_id")
	public String getGovtId() {
		return govtId;
	}

	public void setGovtId(String govtId) {
		this.govtId = govtId;
	}

	@Transient
	public Integer getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
	}

	@Transient
	public String getPreviousLoginTime() {
		return previousLoginTime;
	}

	public void setPreviousLoginTime(String previousLoginTime) {
		this.previousLoginTime = previousLoginTime;
	}

	@Transient
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + accessLevel;
		result = prime * result
				+ ((activationCode == null) ? 0 : activationCode.hashCode());
		result = prime * result
				+ ((additionalName == null) ? 0 : additionalName.hashCode());
		result = prime * result + authStatus;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((govtId == null) ? 0 : govtId.hashCode());
		result = prime * result
				+ ((lastLoginTime == null) ? 0 : lastLoginTime.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((middleName == null) ? 0 : middleName.hashCode());
		result = prime * result + ((mrn == null) ? 0 : mrn.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((patientId == null) ? 0 : patientId.hashCode());
		result = prime
				* result
				+ ((previousLoginTime == null) ? 0 : previousLoginTime
						.hashCode());
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
		result = prime * result
				+ ((sessionId == null) ? 0 : sessionId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credentials other = (Credentials) obj;
		if (accessLevel != other.accessLevel)
			return false;
		if (activationCode == null) {
			if (other.activationCode != null)
				return false;
		} else if (!activationCode.equals(other.activationCode))
			return false;
		if (additionalName == null) {
			if (other.additionalName != null)
				return false;
		} else if (!additionalName.equals(other.additionalName))
			return false;
		if (authStatus != other.authStatus)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (govtId == null) {
			if (other.govtId != null)
				return false;
		} else if (!govtId.equals(other.govtId))
			return false;
		if (lastLoginTime == null) {
			if (other.lastLoginTime != null)
				return false;
		} else if (!lastLoginTime.equals(other.lastLoginTime))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (middleName == null) {
			if (other.middleName != null)
				return false;
		} else if (!middleName.equals(other.middleName))
			return false;
		if (mrn == null) {
			if (other.mrn != null)
				return false;
		} else if (!mrn.equals(other.mrn))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (patientId == null) {
			if (other.patientId != null)
				return false;
		} else if (!patientId.equals(other.patientId))
			return false;
		if (previousLoginTime == null) {
			if (other.previousLoginTime != null)
				return false;
		} else if (!previousLoginTime.equals(other.previousLoginTime))
			return false;
		if (salt == null) {
			if (other.salt != null)
				return false;
		} else if (!salt.equals(other.salt))
			return false;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Credentials [patientId=" + patientId + ", mrn=" + mrn
				+ ", username=" + username + ", password=" + password
				+ ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", additionalName="
				+ additionalName + ", email=" + email + ", salt=" + salt
				+ ", authStatus=" + authStatus + ", accessLevel=" + accessLevel
				+ ", sessionId=" + sessionId + ", status=" + status
				+ ", govtId=" + govtId + ", lastLoginTime=" + lastLoginTime
				+ ", previousLoginTime=" + previousLoginTime
				+ ", activationCode=" + activationCode + "]";
	}

}
