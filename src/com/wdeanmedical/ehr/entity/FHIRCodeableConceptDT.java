package com.wdeanmedical.ehr.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FHIRCodeableConceptDT {
  
  private List<FHIRCodingDT> coding = new ArrayList<FHIRCodingDT>();
  private String text;
  
  public List<FHIRCodingDT> getCoding() {
    return coding;
  }
  public void setCoding(List<FHIRCodingDT> coding) {
    this.coding = coding;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }

}
