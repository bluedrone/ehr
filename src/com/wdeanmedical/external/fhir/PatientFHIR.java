package com.wdeanmedical.external.fhir;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="patient")
public class PatientFHIR {
	 
  private List<Identifier> identifier = new ArrayList<Identifier>();
  private List<HumanName> name = new ArrayList<HumanName>();
  private List<Telecom> telecom = new ArrayList<Telecom>();
  private Gender gender;
  private Date birthDate;
  private Boolean deceasedBoolean;
  private Date deceasedDate;
  private List<Address> address = new ArrayList<Address>();
  private MaritalStatus maritalStatus;
  private Boolean multipleBirthBoolean;
  private Integer multipleBirthInteger;
//  private List<Attachment> photo = new ArrayList<Attachment>();
  private List<Contact> contact =  new ArrayList<Contact>();
  private List<CodeableConcept> communication = new ArrayList<CodeableConcept>();
  private List<ResourceReference> careProvider = new ArrayList<ResourceReference>();
  private ResourceReference managingOrganization;
  private List<Link> link = new ArrayList<Link>();
  private Boolean active;
  
  public MaritalStatus getMaritalStatus() {
    return maritalStatus;
  }
  public void setMaritalStatus(MaritalStatus maritalStatus) {
    this.maritalStatus = maritalStatus;
  }
  public List<Telecom> getTelecom() {
    return telecom;
  }
  public void setTelecom(List<Telecom> telecom) {
    this.telecom = telecom;
  }
  public List<Address> getAddress() {
    return address;
  }
  public void setAddress(List<Address> address) {
    this.address = address;
  }
  public List<Contact> getContact() {
    return contact;
  }
  public void setContact(List<Contact> contact) {
    this.contact = contact;
  }
  public List<Link> getLink() {
    return link;
  }
  public void setLink(List<Link> link) {
    this.link = link;
  }
  public List<Identifier> getIdentifier() {
    return identifier;
  }
  public void setIdentifier(List<Identifier> identifier) {
    this.identifier = identifier;
  }
  public List<HumanName> getName() {
    return name;
  }
  public void setName(List<HumanName> name) {
    this.name = name;
  }
  public Gender getGender() {
    return gender;
  }
  public void setGender(Gender gender) {
    this.gender = gender;
  }
  public Date getBirthDate() {
    return birthDate;
  }
  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }
  public Boolean getDeceasedBoolean() {
    return deceasedBoolean;
  }
  public void setDeceasedBoolean(Boolean deceasedBoolean) {
    this.deceasedBoolean = deceasedBoolean;
  }
  public Date getDeceasedDate() {
    return deceasedDate;
  }
  public void setDeceasedDate(Date deceasedDate) {
    this.deceasedDate = deceasedDate;
  }
  public Boolean getMultipleBirthBoolean() {
    return multipleBirthBoolean;
  }
  public void setMultipleBirthBoolean(Boolean multipleBirthBoolean) {
    this.multipleBirthBoolean = multipleBirthBoolean;
  }
  public Integer getMultipleBirthInteger() {
    return multipleBirthInteger;
  }
  public void setMultipleBirthInteger(Integer multipleBirthInteger) {
    this.multipleBirthInteger = multipleBirthInteger;
  }
  public List<CodeableConcept> getCommunication() {
    return communication;
  }
  public void setCommunication(List<CodeableConcept> communication) {
    this.communication = communication;
  }
  public List<ResourceReference> getCareProvider() {
    return careProvider;
  }
  public void setCareProvider(List<ResourceReference> careProvider) {
    this.careProvider = careProvider;
  }
  public ResourceReference getManagingOrganization() {
    return managingOrganization;
  }
  public void setManagingOrganization(ResourceReference managingOrganization) {
    this.managingOrganization = managingOrganization;
  }
  public Boolean getActive() {
    return active;
  }
  public void setActive(Boolean active) {
    this.active = active;
  }
  
}
