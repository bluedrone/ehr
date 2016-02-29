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

import org.hibernate.annotations.Cascade;

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
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  public PFSH getPfsh() { return pfsh; }
  public void setPfsh(PFSH pfsh) { this.pfsh = pfsh; }

  @JoinColumn(name = "patient_medical_history", referencedColumnName = "id")
  @ManyToOne(optional = true)
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  public MedicalHistory getHist() { return hist; }
  public void setHist(MedicalHistory hist) { this.hist = hist; }

  @Column(name = "current_encounter_id")
  public Integer getCurrentEncounterId() { return currentEncounterId; }
  public void setCurrentEncounterId(Integer currentEncounterId) { this.currentEncounterId = currentEncounterId; }
  
  @Transient
  public Boolean isEncrypted() { return encrypted; }
  public void setEncrypted(Boolean encrypted) { this.encrypted = encrypted; }

}
