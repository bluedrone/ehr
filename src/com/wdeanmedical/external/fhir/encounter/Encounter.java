package com.wdeanmedical.external.fhir.encounter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wdeanmedical.external.fhir.*;

@XmlRootElement(name="Encounter")
public class Encounter {
  
  private String status;
  private String clazz;
  private List<Coding> type = new ArrayList<Coding>();
  private ResourceReference subject;
  private List<Participant> participant = new ArrayList<Participant>();
  private Coding reason;
  private ResourceReference indication;
  private Coding priority;
  private List<Location> location =  new ArrayList<Location>();
  private ResourceReference serviceProvider;
  private ResourceReference partOf;

  public List<Coding> getType() {
    return type;
  }

  public void setType(List<Coding> type) {
    this.type = type;
  }

  public ResourceReference getSubject() {
    return subject;
  }

  public void setSubject(ResourceReference subject) {
    this.subject = subject;
  }

  public List<Participant> getParticipant() {
    return participant;
  }

  public void setParticipant(List<Participant> participant) {
    this.participant = participant;
  }

  public Coding getReason() {
    return reason;
  }

  public void setReason(Coding reason) {
    this.reason = reason;
  }

  public ResourceReference getIndication() {
    return indication;
  }

  public void setIndication(ResourceReference indication) {
    this.indication = indication;
  }

  public Coding getPriority() {
    return priority;
  }

  public void setPriority(Coding priority) {
    this.priority = priority;
  }

  public List<Location> getLocation() {
    return location;
  }

  public void setLocation(List<Location> location) {
    this.location = location;
  }

  public ResourceReference getServiceProvider() {
    return serviceProvider;
  }

  public void setServiceProvider(ResourceReference serviceProvider) {
    this.serviceProvider = serviceProvider;
  }

  public ResourceReference getPartOf() {
    return partOf;
  }

  public void setPartOf(ResourceReference partOf) {
    this.partOf = partOf;
  }

  @XmlElement(name="class")
  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
