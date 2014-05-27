package com.wdeanmedical.external.fhir;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="telecom")
public class Contact {
  
  private String system;
  private String value;
  private String use;
  private Period period;
  private String relationship;
  private HumanName name;
  private Contact telecom;
  private Address address;
  private String gender;
  private ResourceReference organization;
  
  public String getSystem() {
    return system;
  }
  public void setSystem(String system) {
    this.system = system;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  public String getUse() {
    return use;
  }
  public void setUse(String use) {
    this.use = use;
  }
  public Period getPeriod() {
    return period;
  }
  public void setPeriod(Period period) {
    this.period = period;
  }
  public String getRelationship() {
    return relationship;
  }
  public void setRelationship(String relationship) {
    this.relationship = relationship;
  }
  public HumanName getName() {
    return name;
  }
  public void setName(HumanName name) {
    this.name = name;
  }
  public Contact getTelecom() {
    return telecom;
  }
  public void setTelecom(Contact telecom) {
    this.telecom = telecom;
  }
  public Address getAddress() {
    return address;
  }
  public void setAddress(Address address) {
    this.address = address;
  }
  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.gender = gender;
  }
  public ResourceReference getOrganization() {
    return organization;
  }
  public void setOrganization(ResourceReference organization) {
    this.organization = organization;
  }

}
