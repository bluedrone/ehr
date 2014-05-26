/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

import java.util.Date;

public class PatientDTO {

  public static final Integer STATUS_AUTHORIZED = 1;
  public static final Integer STATUS_NOT_FOUND = 0;
  public static final Integer STATUS_INVALID_PASSWORD = -1;
  public static final Integer STATUS_INACTIVE = -2;

  public static final int ACCESS_LEVEL_LIMITED = 0;
  public static final int ACCESS_LEVEL_FULL = 1;

  private CredentialsDTO cred;
  private DemographicsDTO demo;
  private PFSHDTO pfshDTO;
  private MedicalHistoryDTO hist;
  private Integer currentEncounterId;

  public PatientDTO() {
  }

  public CredentialsDTO getCred() {
    return cred;
  }

  public void setCred(CredentialsDTO cred) {
    this.cred = cred;
  }

  public DemographicsDTO getDemo() {
    return demo;
  }

  public void setDemo(DemographicsDTO demo) {
    this.demo = demo;
  }

  public PFSHDTO getPfshDTO() {
    return pfshDTO;
  }

  public void setPfshDTO(PFSHDTO pfshDTO) {
    this.pfshDTO = pfshDTO;
  }

  public MedicalHistoryDTO getHist() {
    return hist;
  }

  public void setHist(MedicalHistoryDTO hist) {
    this.hist = hist;
  }

  public Integer getCurrentEncounterId() {
    return currentEncounterId;
  }

  public void setCurrentEncounterId(Integer currentEncounterId) {
    this.currentEncounterId = currentEncounterId;
  }

  @Override
  public String toString() {
    return "PatientDTO [cred=" + cred + ", demo=" + demo + ", pfshDTO=" + pfshDTO + ", hist=" + hist
        + ", currentEncounterId=" + currentEncounterId + "]";
  }

}
