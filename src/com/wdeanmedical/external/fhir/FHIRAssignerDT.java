package com.wdeanmedical.external.fhir;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="assigner")
public class FHIRAssignerDT {
  
  private FHIRResourceReferenceDT referenceResource;

  public FHIRResourceReferenceDT getReferenceResource() {
    return referenceResource;
  }

  public void setReferenceResource(FHIRResourceReferenceDT referenceResource) {
    this.referenceResource = referenceResource;
  }

}
