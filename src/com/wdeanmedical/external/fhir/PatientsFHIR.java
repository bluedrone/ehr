package com.wdeanmedical.external.fhir;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="patients")
public class PatientsFHIR {
  
  private List<PatientFHIR> patients = new ArrayList<PatientFHIR>();

  public List<PatientFHIR> getPatients() {
    return patients;
  }

  public void setPatients(List<PatientFHIR> patients) {
    this.patients = patients;
  }  
  
}
