package com.wdeanmedical.external.fhir;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="coding")
public class Coding {
  
  private String system;
  private String version;
  private String code;
  private String display;
  private Boolean primary;
  private ResourceReference valueSet;

}
