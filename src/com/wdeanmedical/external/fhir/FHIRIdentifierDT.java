package com.wdeanmedical.external.fhir;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="identifier")
public class FHIRIdentifierDT {
  
  private String use;
  private String label;
  private String system;
  private String value;
  private Period period;
  private FHIRAssignerDT assigner;
  
  public String getUse() {
    return use;
  }
  public void setUse(String use) {
    this.use = use;
  }
  public String getLabel() {
    return label;
  }
  public void setLabel(String label) {
    this.label = label;
  }
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
  public Period getPeriod() {
    return period;
  }
  public void setPeriod(Period period) {
    this.period = period;
  }
  public FHIRAssignerDT getAssigner() {
    return assigner;
  }
  public void setAssigner(FHIRAssignerDT assigner) {
    this.assigner = assigner;
  }

}
