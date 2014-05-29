package com.wdeanmedical.external.fhir;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Coding {
  
  private String system;
  private String version;
  private String code;
  private String display;
  private Boolean primary;
  private ResourceReference valueSet;
  
  public String getSystem() {
    return system;
  }
  public void setSystem(String system) {
    this.system = system;
  }
  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public String getDisplay() {
    return display;
  }
  public void setDisplay(String display) {
    this.display = display;
  }
  public Boolean getPrimary() {
    return primary;
  }
  public void setPrimary(Boolean primary) {
    this.primary = primary;
  }
  public ResourceReference getValueSet() {
    return valueSet;
  }
  public void setValueSet(ResourceReference valueSet) {
    this.valueSet = valueSet;
  }

}
