package com.wdeanmedical.external.fhir.encounter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.wdeanmedical.external.fhir.Coding;
import com.wdeanmedical.external.fhir.Identifier;
import com.wdeanmedical.external.fhir.Period;
import com.wdeanmedical.external.fhir.ResourceReference;

@XmlRootElement(name="hospitalization")
public class Hospitalization {
  
  private Identifier preAdmissionIdentifier;
  private ResourceReference origin;
  private Coding admitSource;
  private Period period;
//  private Duration length;
  private List<Accomodation> accomodation = new ArrayList<Accomodation>();
  private Coding diet;
  private Coding specialCourtesy;
  private Coding specialArrangement;
  private ResourceReference destination;
  private Coding dischargeDisposition;
  private ResourceReference dischargeDiagnosis;
  private Boolean reAdmission;
  
  public Identifier getPreAdmissionIdentifier() {
    return preAdmissionIdentifier;
  }
  public void setPreAdmissionIdentifier(Identifier preAdmissionIdentifier) {
    this.preAdmissionIdentifier = preAdmissionIdentifier;
  }
  public ResourceReference getOrigin() {
    return origin;
  }
  public void setOrigin(ResourceReference origin) {
    this.origin = origin;
  }
  public Coding getAdmitSource() {
    return admitSource;
  }
  public void setAdmitSource(Coding admitSource) {
    this.admitSource = admitSource;
  }
  public Period getPeriod() {
    return period;
  }
  public void setPeriod(Period period) {
    this.period = period;
  }
  public List<Accomodation> getAccomodation() {
    return accomodation;
  }
  public void setAccomodation(List<Accomodation> accomodation) {
    this.accomodation = accomodation;
  }
  public Coding getDiet() {
    return diet;
  }
  public void setDiet(Coding diet) {
    this.diet = diet;
  }
  public Coding getSpecialCourtesy() {
    return specialCourtesy;
  }
  public void setSpecialCourtesy(Coding specialCourtesy) {
    this.specialCourtesy = specialCourtesy;
  }
  public Coding getSpecialArrangement() {
    return specialArrangement;
  }
  public void setSpecialArrangement(Coding specialArrangement) {
    this.specialArrangement = specialArrangement;
  }
  public ResourceReference getDestination() {
    return destination;
  }
  public void setDestination(ResourceReference destination) {
    this.destination = destination;
  }
  public Coding getDischargeDisposition() {
    return dischargeDisposition;
  }
  public void setDischargeDisposition(Coding dischargeDisposition) {
    this.dischargeDisposition = dischargeDisposition;
  }
  public ResourceReference getDischargeDiagnosis() {
    return dischargeDiagnosis;
  }
  public void setDischargeDiagnosis(ResourceReference dischargeDiagnosis) {
    this.dischargeDiagnosis = dischargeDiagnosis;
  }
  public Boolean getReAdmission() {
    return reAdmission;
  }
  public void setReAdmission(Boolean reAdmission) {
    this.reAdmission = reAdmission;
  }

}
