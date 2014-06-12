package com.wdeanmedical.external.fhir.encounter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.wdeanmedical.external.fhir.Coding;
import com.wdeanmedical.external.fhir.ResourceReference;

@XmlRootElement(name="participant")
public class Participant {
  
  private List<Coding> type =  new ArrayList<Coding>();
  private ResourceReference individual;
  
  public List<Coding> getType() {
    return type;
  }
  public void setType(List<Coding> type) {
    this.type = type;
  }
  public ResourceReference getIndividual() {
    return individual;
  }
  public void setIndividual(ResourceReference individual) {
    this.individual = individual;
  }

}
