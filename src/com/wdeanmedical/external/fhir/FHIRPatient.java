package com.wdeanmedical.external.fhir;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="patient")
public class FHIRPatient {
	 
  private List<FHIRIdentifierDT> identifier = new ArrayList<FHIRIdentifierDT>();
  private List<FHIRHumanNameDT> name = new ArrayList<FHIRHumanNameDT>();
//  private List<Contact> telecom = new ArrayList<Contact>();
  private FHIRCodeableConceptDT gender;
  private Date birthDate;
  private Boolean deceasedBoolean;
  private Date deceasedDate;
//  private List<Address> address = new ArrayList<Address>();
  private Boolean multipleBirthBoolean;
  private Integer multipleBirthInteger;
//  private List<Attachment> photo = new ArrayList<Attachment>();
//  private List<Contact> contact =  new ArrayList<Contact>();
  private List<FHIRCodeableConceptDT> communication = new ArrayList<FHIRCodeableConceptDT>();
  private List<FHIRResourceReferenceDT> careProvider = new ArrayList<FHIRResourceReferenceDT>();
  private FHIRResourceReferenceDT managingOrganization;
//  private List<FHIRLinkDT> link = new ArrayList<Link>();
  private Boolean active;
  
  public List<FHIRIdentifierDT> getIdentifier() {
    return identifier;
  }
  public void setIdentifier(List<FHIRIdentifierDT> identifier) {
    this.identifier = identifier;
  }
  public List<FHIRHumanNameDT> getName() {
    return name;
  }
  public void setName(List<FHIRHumanNameDT> name) {
    this.name = name;
  }
  public FHIRCodeableConceptDT getGender() {
    return gender;
  }
  public void setGender(FHIRCodeableConceptDT gender) {
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
  public List<FHIRCodeableConceptDT> getCommunication() {
    return communication;
  }
  public void setCommunication(List<FHIRCodeableConceptDT> communication) {
    this.communication = communication;
  }
  public List<FHIRResourceReferenceDT> getCareProvider() {
    return careProvider;
  }
  public void setCareProvider(List<FHIRResourceReferenceDT> careProvider) {
    this.careProvider = careProvider;
  }
  public FHIRResourceReferenceDT getManagingOrganization() {
    return managingOrganization;
  }
  public void setManagingOrganization(FHIRResourceReferenceDT managingOrganization) {
    this.managingOrganization = managingOrganization;
  }
  public Boolean getActive() {
    return active;
  }
  public void setActive(Boolean active) {
    this.active = active;
  }
  
}
