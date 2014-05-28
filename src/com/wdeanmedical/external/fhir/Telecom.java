package com.wdeanmedical.external.fhir;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Telecom {
  
  private String system;
  private String value;
  private String use;
  private Period period;
  
  public String getSystem() {
    return system;
  }
  public void setSystem(String system) {
    this.system = system;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  public String getUse() {
    return use;
  }
  public void setUse(String use) {
    this.use = use;
  }
  public Period getPeriod() {
    return period;
  }
  public void setPeriod(Period period) {
    this.period = period;
  }

}
