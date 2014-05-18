package com.wdeanmedical.ehr.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="patient")
public class FHIRPatient {
	 
  public List<String> name = new ArrayList<String>();
  
  public List<Identifier> identifier = new ArrayList<Identifier>();
  
  public List<Identifier> getIdentifiers(){
    return identifier;
  }

  public Identifier getIdentifier(){
    return new Identifier();
  }
  
//  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Identifier {
    private String use;
    
    public void setUse(String use){
      this.use = use;
    }
    
    public String getUse(){
      return this.use;
    }
  }
}
