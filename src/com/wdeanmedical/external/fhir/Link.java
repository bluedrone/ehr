package com.wdeanmedical.external.fhir;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Link {
  
  private ResourceReference other;
  private String type;
  
  public ResourceReference getOther() {
    return other;
  }
  public void setOther(ResourceReference other) {
    this.other = other;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

}
