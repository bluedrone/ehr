package com.wdeanmedical.external.fhir.encounter;

import javax.xml.bind.annotation.XmlRootElement;

import com.wdeanmedical.external.fhir.Period;
import com.wdeanmedical.external.fhir.ResourceReference;

@XmlRootElement(name="location")
public class Location {
  
  private ResourceReference location;
  private Period period;
  
  public ResourceReference getLocation() {
    return location;
  }
  public void setLocation(ResourceReference location) {
    this.location = location;
  }
  public Period getPeriod() {
    return period;
  }
  public void setPeriod(Period period) {
    this.period = period;
  }

}
