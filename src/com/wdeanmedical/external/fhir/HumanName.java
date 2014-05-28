package com.wdeanmedical.external.fhir;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="name")
public class HumanName {
  
  private String use;
  private String text;
  private List<String> family = new ArrayList<String>();
  private List<String> given = new ArrayList<String>();
  private List<String> prefix = new ArrayList<String>();
  private List<String> suffix = new ArrayList<String>();
  private Period period;
  
  public String getUse() {
    return use;
  }
  public void setUse(String use) {
    this.use = use;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }
  public List<String> getFamily() {
    return family;
  }
  public void setFamily(List<String> family) {
    this.family = family;
  }
  public List<String> getGiven() {
    return given;
  }
  public void setGiven(List<String> given) {
    this.given = given;
  }
  public List<String> getPrefix() {
    return prefix;
  }
  public void setPrefix(List<String> prefix) {
    this.prefix = prefix;
  }
  public List<String> getSuffix() {
    return suffix;
  }
  public void setSuffix(List<String> suffix) {
    this.suffix = suffix;
  }
  public Period getPeriod() {
    return period;
  }
  public void setPeriod(Period period) {
    this.period = period;
  }

}
