package com.wdeanmedical.external.fhir;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="patients")
public class PatientsFHIR {
  
  private List<PatientFHIR> patient = new ArrayList<PatientFHIR>();

  public List<PatientFHIR> getPatient() {
    return patient;
  }

  public void setPatient(List<PatientFHIR> patient) {
    this.patient = patient;
  }  
  
}
