package com.wdeanmedical.external.fhir;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Relationship {
  
  private CodeableConcept coding;

  public CodeableConcept getCoding() {
    return coding;
  }

  public void setCoding(CodeableConcept coding) {
    this.coding = coding;
  }
  
}
