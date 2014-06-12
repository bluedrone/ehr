package com.wdeanmedical.external.fhir.encounter;

import javax.xml.bind.annotation.XmlRootElement;

import com.wdeanmedical.external.fhir.Period;
import com.wdeanmedical.external.fhir.ResourceReference;

@XmlRootElement(name="accomodation")
public class Accomodation {
  
  private ResourceReference bed;
  private Period period;
  
  public ResourceReference getBed() {
    return bed;
  }
  public void setBed(ResourceReference bed) {
    this.bed = bed;
  }
  public Period getPeriod() {
    return period;
  }
  public void setPeriod(Period period) {
    this.period = period;
  }

}
