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
@Table(name = "obgyn_encounter_data")
public class OBGYNEncounterData extends BaseEntity implements Serializable {
  private static final long serialVersionUID = -8119723615984914088L;

  private Integer encounterId;
  private Integer patientId;
  private Integer clinicianId;
  private String g;
  private String p;
  private String t;
  private String a;
  private String l;
  private String pregStatus;
  private String breastfeeding;
  private String breastfeedingMonths;
  private String lastPeriod;
  private String ageFirstPeriod;
  private String papSmearStatus;
  private String birthControlStatus;
  private String birthControlType;
  private String history;
  private String historyOther;
  private Date date;

  public OBGYNEncounterData() {
  }

  @Column(name = "encounter_id")
  public Integer getEncounterId() {
    return encounterId;
  }

  public void setEncounterId(Integer encounterId) {
    this.encounterId = encounterId;
  }

  @Column(name = "patient_id")
  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  @Column(name = "clinician_id")
  public Integer getClinicianId() {
    return clinicianId;
  }

  public void setClinicianId(Integer clinicianId) {
    this.clinicianId = clinicianId;
  }

  @Column(name = "date")
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @Column(name = "g")
  public String getG() {
    return g;
  }

  public void setG(String g) {
    this.g = g;
  }

  @Column(name = "p")
  public String getP() {
    return p;
  }

  public void setP(String p) {
    this.p = p;
  }

  @Column(name = "t")
  public String getT() {
    return t;
  }

  public void setT(String t) {
    this.t = t;
  }

  @Column(name = "a")
  public String getA() {
    return a;
  }

  public void setA(String a) {
    this.a = a;
  }

  @Column(name = "l")
  public String getL() {
    return l;
  }

  public void setL(String l) {
    this.l = l;
  }

  @Column(name = "preg_status")
  public String getPregStatus() {
    return pregStatus;
  }

  public void setPregStatus(String pregStatus) {
    this.pregStatus = pregStatus;
  }

  @Column(name = "breastfeeding")
  public String getBreastfeeding() {
    return breastfeeding;
  }

  public void setBreastfeeding(String breastfeeding) {
    this.breastfeeding = breastfeeding;
  }

  @Column(name = "breastfeeding_months")
  public String getBreastfeedingMonths() {
    return breastfeedingMonths;
  }

  public void setBreastfeedingMonths(String breastfeedingMonths) {
    this.breastfeedingMonths = breastfeedingMonths;
  }

  @Column(name = "last_period")
  public String getLastPeriod() {
    return lastPeriod;
  }

  public void setLastPeriod(String lastPeriod) {
    this.lastPeriod = lastPeriod;
  }

  @Column(name = "age_first_period")
  public String getAgeFirstPeriod() {
    return ageFirstPeriod;
  }

  public void setAgeFirstPeriod(String ageFirstPeriod) {
    this.ageFirstPeriod = ageFirstPeriod;
  }

  @Column(name = "pap_smear_status")
  public String getPapSmearStatus() {
    return papSmearStatus;
  }

  public void setPapSmearStatus(String papSmearStatus) {
    this.papSmearStatus = papSmearStatus;
  }

  @Column(name = "birth_control_status")
  public String getBirthControlStatus() {
    return birthControlStatus;
  }

  public void setBirthControlStatus(String birthControlStatus) {
    this.birthControlStatus = birthControlStatus;
  }

  @Column(name = "birth_control_type")
  public String getBirthControlType() {
    return birthControlType;
  }

  public void setBirthControlType(String birthControlType) {
    this.birthControlType = birthControlType;
  }

  @Column(name = "history")
  public String getHistory() {
    return history;
  }

  public void setHistory(String history) {
    this.history = history;
  }

  @Column(name = "history_other")
  public String getHistoryOther() {
    return historyOther;
  }

  public void setHistoryOther(String historyOther) {
    this.historyOther = historyOther;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((a == null) ? 0 : a.hashCode());
    result = prime * result
        + ((ageFirstPeriod == null) ? 0 : ageFirstPeriod.hashCode());
    result = prime
        * result
        + ((birthControlStatus == null) ? 0 : birthControlStatus
            .hashCode());
    result = prime
        * result
        + ((birthControlType == null) ? 0 : birthControlType.hashCode());
    result = prime * result
        + ((breastfeeding == null) ? 0 : breastfeeding.hashCode());
    result = prime
        * result
        + ((breastfeedingMonths == null) ? 0 : breastfeedingMonths
            .hashCode());
    result = prime * result
        + ((clinicianId == null) ? 0 : clinicianId.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result
        + ((encounterId == null) ? 0 : encounterId.hashCode());
    result = prime * result + ((g == null) ? 0 : g.hashCode());
    result = prime * result + ((history == null) ? 0 : history.hashCode());
    result = prime * result
        + ((historyOther == null) ? 0 : historyOther.hashCode());
    result = prime * result + ((l == null) ? 0 : l.hashCode());
    result = prime * result
        + ((lastPeriod == null) ? 0 : lastPeriod.hashCode());
    result = prime * result + ((p == null) ? 0 : p.hashCode());
    result = prime * result
        + ((papSmearStatus == null) ? 0 : papSmearStatus.hashCode());
    result = prime * result
        + ((patientId == null) ? 0 : patientId.hashCode());
    result = prime * result
        + ((pregStatus == null) ? 0 : pregStatus.hashCode());
    result = prime * result + ((t == null) ? 0 : t.hashCode());
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
    OBGYNEncounterData other = (OBGYNEncounterData) obj;
    if (a == null) {
      if (other.a != null)
        {return false;}
    } else if (!a.equals(other.a))
      {return false;}
    if (ageFirstPeriod == null) {
      if (other.ageFirstPeriod != null)
        {return false;}
    } else if (!ageFirstPeriod.equals(other.ageFirstPeriod))
      {return false;}
    if (birthControlStatus == null) {
      if (other.birthControlStatus != null)
        {return false;}
    } else if (!birthControlStatus.equals(other.birthControlStatus))
      {return false;}
    if (birthControlType == null) {
      if (other.birthControlType != null)
        {return false;}
    } else if (!birthControlType.equals(other.birthControlType))
      {return false;}
    if (breastfeeding == null) {
      if (other.breastfeeding != null)
        {return false;}
    } else if (!breastfeeding.equals(other.breastfeeding))
      {return false;}
    if (breastfeedingMonths == null) {
      if (other.breastfeedingMonths != null)
        {return false;}
    } else if (!breastfeedingMonths.equals(other.breastfeedingMonths))
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
    if (encounterId == null) {
      if (other.encounterId != null)
        {return false;}
    } else if (!encounterId.equals(other.encounterId))
      {return false;}
    if (g == null) {
      if (other.g != null)
        {return false;}
    } else if (!g.equals(other.g))
      {return false;}
    if (history == null) {
      if (other.history != null)
        {return false;}
    } else if (!history.equals(other.history))
      {return false;}
    if (historyOther == null) {
      if (other.historyOther != null)
        {return false;}
    } else if (!historyOther.equals(other.historyOther))
      {return false;}
    if (l == null) {
      if (other.l != null)
        {return false;}
    } else if (!l.equals(other.l))
      {return false;}
    if (lastPeriod == null) {
      if (other.lastPeriod != null)
        {return false;}
    } else if (!lastPeriod.equals(other.lastPeriod))
      {return false;}
    if (p == null) {
      if (other.p != null)
        {return false;}
    } else if (!p.equals(other.p))
      {return false;}
    if (papSmearStatus == null) {
      if (other.papSmearStatus != null)
        {return false;}
    } else if (!papSmearStatus.equals(other.papSmearStatus))
      {return false;}
    if (patientId == null) {
      if (other.patientId != null)
        {return false;}
    } else if (!patientId.equals(other.patientId))
      {return false;}
    if (pregStatus == null) {
      if (other.pregStatus != null)
        {return false;}
    } else if (!pregStatus.equals(other.pregStatus))
      {return false;}
    if (t == null) {
      if (other.t != null)
        {return false;}
    } else if (!t.equals(other.t))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "OBGYNEncounterData [encounterId=" + encounterId
        + ", patientId=" + patientId + ", clinicianId=" + clinicianId
        + ", g=" + g + ", p=" + p + ", t=" + t + ", a=" + a + ", l="
        + l + ", pregStatus=" + pregStatus + ", breastfeeding="
        + breastfeeding + ", breastfeedingMonths="
        + breastfeedingMonths + ", lastPeriod=" + lastPeriod
        + ", ageFirstPeriod=" + ageFirstPeriod + ", papSmearStatus="
        + papSmearStatus + ", birthControlStatus=" + birthControlStatus
        + ", birthControlType=" + birthControlType + ", history="
        + history + ", historyOther=" + historyOther + ", date=" + date
        + "]";
  }

}
