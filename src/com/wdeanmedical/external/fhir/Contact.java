package com.wdeanmedical.external.fhir;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="telecom")
public class Contact {
  
  private Relationship relationship;
  private HumanName name;
  private Telecom telecom;
  private Address address;
  private String gender;
  private ResourceReference organization;

  public Relationship getRelationship() {
    return relationship;
  }
  public void setRelationship(Relationship relationship) {
    this.relationship = relationship;
  }
  public HumanName getName() {
    return name;
  }
  public void setName(HumanName name) {
    this.name = name;
  }
  public Telecom getTelecom() {
    return telecom;
  }
  public void setTelecom(Telecom telecom) {
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
