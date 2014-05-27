package com.wdeanmedical.external.fhir;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CodeableConcept {
  
  private List<Coding> coding = new ArrayList<Coding>();
  private String text;
  
  public List<Coding> getCoding() {
    return coding;
  }
  public void setCoding(List<Coding> coding) {
    this.coding = coding;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }

}
