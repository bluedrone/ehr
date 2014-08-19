/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "patient")
@Inheritance(strategy = InheritanceType.JOINED)
public class Patient extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 5963957101514207030L;

  public static final Integer STATUS_AUTHORIZED = 1;
  public static final Integer STATUS_NOT_FOUND = 0;
  public static final Integer STATUS_INVALID_PASSWORD = -1;
  public static final Integer STATUS_INACTIVE = -2;

  public static final int ACCESS_LEVEL_LIMITED = 0;
  public static final int ACCESS_LEVEL_FULL = 1;

  private Credentials cred;
  private Demographics demo;
  private PFSH pfsh;
  private MedicalHistory hist;
  private Integer currentEncounterId;
  private Boolean encrypted = true;

  public Patient() {
  }

  @JoinColumn(name = "credentials", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Credentials getCred() { return cred; }
  public void setCred(Credentials cred) { this.cred = cred; }

  @JoinColumn(name = "demographics", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public Demographics getDemo() { return demo; }
  public void setDemo(Demographics demo) { this.demo = demo; }

  @JoinColumn(name = "pfsh", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public PFSH getPfsh() { return pfsh; }
  public void setPfsh(PFSH pfsh) { this.pfsh = pfsh; }

  @JoinColumn(name = "patient_medical_history", referencedColumnName = "id")
  @ManyToOne(optional = true)
  public MedicalHistory getHist() { return hist; }
  public void setHist(MedicalHistory hist) { this.hist = hist; }

  @Column(name = "current_encounter_id")
  public Integer getCurrentEncounterId() { return currentEncounterId; }
  public void setCurrentEncounterId(Integer currentEncounterId) { this.currentEncounterId = currentEncounterId; }


  @Transient
  public Boolean isEncrypted() { return encrypted; }
  public void setEncrypted(Boolean encrypted) { this.encrypted = encrypted; }




  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((cred == null) ? 0 : cred.hashCode());
    result = prime
        * result
        + ((currentEncounterId == null) ? 0 : currentEncounterId
            .hashCode());
    result = prime * result + ((demo == null) ? 0 : demo.hashCode());
    result = prime * result + ((hist == null) ? 0 : hist.hashCode());
    result = prime * result + ((pfsh == null) ? 0 : pfsh.hashCode());
    return result;
  }



@Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      {return false;}
    if (getClass() != obj.getClass())
      {return false;}
    Patient other = (Patient) obj;
    if (cred == null) {
      if (other.cred != null)
        {return false;}
    } else if (!cred.equals(other.cred))
      {return false;}
    if (currentEncounterId == null) {
      if (other.currentEncounterId != null)
        {return false;}
    } else if (!currentEncounterId.equals(other.currentEncounterId))
      {return false;}
    if (demo == null) {
      if (other.demo != null)
        {return false;}
    } else if (!demo.equals(other.demo))
      {return false;}
    if (hist == null) {
      if (other.hist != null)
        {return false;}
    } else if (!hist.equals(other.hist))
      {return false;}
    if (pfsh == null) {
      if (other.pfsh != null)
        {return false;}
    } else if (!pfsh.equals(other.pfsh))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "Patient [cred=" + cred + ", demo=" + demo + ", pfsh=" + pfsh
        + ", hist=" + hist + ", currentEncounterId="
        + currentEncounterId + "]";
  }

}
