package com.wdeanmedical.external.fhir;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Gender {
  
  private Coding coding = new Coding();
  private String text;
  
  public Coding getCoding() {
    return coding;
  }
  public void setCoding(Coding coding) {
    this.coding = coding;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }
  

}
