package com.wdeanmedical.external.fhir;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="assigner")
public class Assigner {
  
  private ResourceReference referenceResource;

  public ResourceReference getReferenceResource() {
    return referenceResource;
  }

  public void setReferenceResource(ResourceReference referenceResource) {
    this.referenceResource = referenceResource;
  }

}
