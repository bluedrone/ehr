package com.wdeanmedical.ehr.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="coding")
public class FHIRCodingDT {
  
  private String system;
  private String version;
  private String code;
  private String display;
  private Boolean primary;
  private FHIRResourceReferenceDT valueSet;

}
