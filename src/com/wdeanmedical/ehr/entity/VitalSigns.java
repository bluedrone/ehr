/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "vital_signs")
public class VitalSigns extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1342327774068897447L;

  private Integer encounterId;
  private Date date;
  private Integer patientId;
  private Integer clinicianId;
  private Float height;
  private Float weight;
  private Float bmi;
  private Float ofc;
  private Float temperature;
  private Integer pulse;
  private Integer respiration;
  private Integer systolic;
  private Integer diastolic;
  private Float oximetry;

  public VitalSigns() {
  }

  @Column(name = "encounter_id")
  public Integer getEncounterId() { return encounterId; }
  public void setEncounterId(Integer encounterId) { this.encounterId = encounterId; }

  @Column(name = "patient_id")
  public Integer getPatientId() { return patientId; }
  public void setPatientId(Integer patientId) { this.patientId = patientId; }

  @Column(name = "clinician_id")
  public Integer getClinicianId() { return clinicianId; }
  public void setClinicianId(Integer clinicianId) { this.clinicianId = clinicianId; }

  @Column(name = "date")
  public Date getDate() { return date; }
  public void setDate(Date date) { this.date = date; }

  @Column(name = "height")
  public Float getHeight() { return height; }
  public void setHeight(Float height) { this.height = height; }

  @Column(name = "weight")
  public Float getWeight() { return weight; }
  public void setWeight(Float weight) { this.weight = weight; }

  @Column(name = "bmi")
  public Float getBmi() { return bmi; }
  public void setBmi(Float bmi) { this.bmi = bmi; }

  @Column(name = "ofc")
  public Float getOfc() { return ofc; }
  public void setOfc(Float ofc) { this.ofc = ofc; }

  @Column(name = "temperature")
  public Float getTemperature() { return temperature; }
  public void setTemperature(Float temperature) { this.temperature = temperature; }

  @Column(name = "pulse")
  public Integer getPulse() { return pulse; }
  public void setPulse(Integer pulse) { this.pulse = pulse; }

  @Column(name = "respiration")
  public Integer getRespiration() { return respiration; }
  public void setRespiration(Integer respiration) { this.respiration = respiration; }

  @Column(name = "systolic")
  public Integer getSystolic() { return systolic; }
  public void setSystolic(Integer systolic) { this.systolic = systolic; }

  @Column(name = "diastolic")
  public Integer getDiastolic() { return diastolic; }
  public void setDiastolic(Integer diastolic) { this.diastolic = diastolic; }

  @Column(name = "oximetry")
  public Float getOximetry() { return oximetry; }
  public void setOximetry(Float oximetry) { this.oximetry = oximetry; }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((bmi == null) ? 0 : bmi.hashCode());
    result = prime * result
        + ((clinicianId == null) ? 0 : clinicianId.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result
        + ((diastolic == null) ? 0 : diastolic.hashCode());
    result = prime * result
        + ((encounterId == null) ? 0 : encounterId.hashCode());
    result = prime * result + ((height == null) ? 0 : height.hashCode());
    result = prime * result + ((ofc == null) ? 0 : ofc.hashCode());
    result = prime * result
        + ((oximetry == null) ? 0 : oximetry.hashCode());
    result = prime * result + ((patientId == null) ? 0 : patientId.hashCode());
    result = prime * result + ((pulse == null) ? 0 : pulse.hashCode());
    result = prime * result
        + ((respiration == null) ? 0 : respiration.hashCode());
    result = prime * result
        + ((systolic == null) ? 0 : systolic.hashCode());
    result = prime * result
        + ((temperature == null) ? 0 : temperature.hashCode());
    result = prime * result + ((weight == null) ? 0 : weight.hashCode());
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
    VitalSigns other = (VitalSigns) obj;
    if (bmi == null) {
      if (other.bmi != null)
        {return false;}
    } else if (!bmi.equals(other.bmi))
      {return false;}
    if (clinicianId == null) {
      if (other.clinicianId != null)
        {return false;}
    } else if (!clinicianId.equals(other.clinicianId))
      {return false;}
    if (date == null) {
      if (other.date != null)
        {return false;}
    } else if (!date.equals(other.date))
      {return false;}
    if (diastolic == null) {
      if (other.diastolic != null)
        {return false;}
    } else if (!diastolic.equals(other.diastolic))
      {return false;}
    if (encounterId == null) {
      if (other.encounterId != null)
        {return false;}
    } else if (!encounterId.equals(other.encounterId))
      {return false;}
    if (height == null) {
      if (other.height != null)
        {return false;}
    } else if (!height.equals(other.height))
      {return false;}
    if (ofc == null) {
      if (other.ofc != null)
        {return false;}
    } else if (!ofc.equals(other.ofc))
      {return false;}
    if (oximetry == null) {
      if (other.oximetry != null)
        {return false;}
    } else if (!oximetry.equals(other.oximetry))
      {return false;}
    if (patientId == null) {
      if (other.patientId != null)
        {return false;}
    } else if (!patientId.equals(other.patientId))
      {return false;}
    if (pulse == null) {
      if (other.pulse != null)
        {return false;}
    } else if (!pulse.equals(other.pulse))
      {return false;}
    if (respiration == null) {
      if (other.respiration != null)
        {return false;}
    } else if (!respiration.equals(other.respiration))
      {return false;}
    if (systolic == null) {
      if (other.systolic != null)
        {return false;}
    } else if (!systolic.equals(other.systolic))
      {return false;}
    if (temperature == null) {
      if (other.temperature != null)
        {return false;}
    } else if (!temperature.equals(other.temperature))
      {return false;}
    if (weight == null) {
      if (other.weight != null)
        {return false;}
    } else if (!weight.equals(other.weight))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "VitalSigns [encounterId=" + encounterId + ", date=" + date
        + ", patientId=" + patientId + ", clinicianId=" + clinicianId
        + ", height=" + height + ", weight=" + weight + ", bmi=" + bmi
        + ", ofc=" + ofc + ", temperature=" + temperature + ", pulse="
        + pulse + ", respiration=" + respiration + ", systolic="
        + systolic + ", diastolic=" + diastolic + ", oximetry="
        + oximetry + "]";
  }

}
