/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.entity.dto;

public class EncounterMedicationDTO {

  private String medication;
  private String dose;
  private String frequency;
  private int patientId;

  public EncounterMedicationDTO() {
  }

  public String getMedication() {
    return medication;
  }

  public void setMedication(String medication) {
    this.medication = medication;
  }

  public String getDose() {
    return dose;
  }

  public void setDose(String dose) {
    this.dose = dose;
  }

  public String getFrequency() {
    return frequency;
  }

  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  public int getPatientId() {
    return patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  @Override
  public String toString() {
    return "EncounterMedicationDTO [medication=" + medication + ", dose=" + dose + ", frequency=" + frequency
        + ", patientId=" + patientId + "]";
  }

}
