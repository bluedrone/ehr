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
@Table(name = "chief_complaint")
public class ChiefComplaint extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1462745762564975233L;

  private Date date;
  private Integer encounterId;
  private Integer patientId;
  private Integer clinicianId;
  private String description;
  private String occursWhen;
  private String specificLocation;
  private Integer hoursSince;
  private Integer daysSince;
  private Integer weeksSince;
  private Integer monthsSince;
  private Integer yearsSince;
  private String howLongOther;
  private Integer painXHour;
  private Integer painXDay;
  private Integer painXWeek;
  private Integer painXMonth;
  private String painDuration;
  private Integer painScale;
  private String painType;
  private String denies;
  private String deniesOther;

  public ChiefComplaint() {
  }

  @Column(name = "encounter_id")
  public Integer getEncounterId() {
    return encounterId;
  }

  public void setEncounterId(Integer encounterId) {
    this.encounterId = encounterId;
  }

  @Column(name = "date")
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
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

  @Column(name = "description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Column(name = "occurs_when")
  public String getOccursWhen() {
    return occursWhen;
  }

  public void setOccursWhen(String occursWhen) {
    this.occursWhen = occursWhen;
  }

  @Column(name = "specific_location")
  public String getSpecificLocation() {
    return specificLocation;
  }

  public void setSpecificLocation(String specificLocation) {
    this.specificLocation = specificLocation;
  }

  @Column(name = "hours_since")
  public Integer getHoursSince() {
    return hoursSince;
  }

  public void setHoursSince(Integer hoursSince) {
    this.hoursSince = hoursSince;
  }

  @Column(name = "days_since")
  public Integer getDaysSince() {
    return daysSince;
  }

  public void setDaysSince(Integer daysSince) {
    this.daysSince = daysSince;
  }

  @Column(name = "weeks_since")
  public Integer getWeeksSince() {
    return weeksSince;
  }

  public void setWeeksSince(Integer weeksSince) {
    this.weeksSince = weeksSince;
  }

  @Column(name = "months_since")
  public Integer getMonthsSince() {
    return monthsSince;
  }

  public void setMonthsSince(Integer monthsSince) {
    this.monthsSince = monthsSince;
  }

  @Column(name = "years_since")
  public Integer getYearsSince() {
    return yearsSince;
  }

  public void setYearsSince(Integer yearsSince) {
    this.yearsSince = yearsSince;
  }

  @Column(name = "how_long_other")
  public String getHowLongOther() {
    return howLongOther;
  }

  public void setHowLongOther(String howLongOther) {
    this.howLongOther = howLongOther;
  }

  @Column(name = "pain_x_hour")
  public Integer getPainXHour() {
    return painXHour;
  }

  public void setPainXHour(Integer painXHour) {
    this.painXHour = painXHour;
  }

  @Column(name = "pain_x_day")
  public Integer getPainXDay() {
    return painXDay;
  }

  public void setPainXDay(Integer painXDay) {
    this.painXDay = painXDay;
  }

  @Column(name = "pain_x_week")
  public Integer getPainXWeek() {
    return painXWeek;
  }

  public void setPainXWeek(Integer painXWeek) {
    this.painXWeek = painXWeek;
  }

  @Column(name = "pain_x_month")
  public Integer getPainXMonth() {
    return painXMonth;
  }

  public void setPainXMonth(Integer painXMonth) {
    this.painXMonth = painXMonth;
  }

  @Column(name = "pain_duration")
  public String getPainDuration() {
    return painDuration;
  }

  public void setPainDuration(String painDuration) {
    this.painDuration = painDuration;
  }

  @Column(name = "pain_scale")
  public Integer getPainScale() {
    return painScale;
  }

  public void setPainScale(Integer painScale) {
    this.painScale = painScale;
  }

  @Column(name = "pain_type")
  public String getPainType() {
    return painType;
  }

  public void setPainType(String painType) {
    this.painType = painType;
  }

  @Column(name = "denies")
  public String getDenies() {
    return denies;
  }

  public void setDenies(String denies) {
    this.denies = denies;
  }

  @Column(name = "denies_other")
  public String getDeniesOther() {
    return deniesOther;
  }

  public void setDeniesOther(String deniesOther) {
    this.deniesOther = deniesOther;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((clinicianId == null) ? 0 : clinicianId.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((daysSince == null) ? 0 : daysSince.hashCode());
    result = prime * result + ((denies == null) ? 0 : denies.hashCode());
    result = prime * result + ((deniesOther == null) ? 0 : deniesOther.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((encounterId == null) ? 0 : encounterId.hashCode());
    result = prime * result + ((hoursSince == null) ? 0 : hoursSince.hashCode());
    result = prime * result + ((howLongOther == null) ? 0 : howLongOther.hashCode());
    result = prime * result + ((monthsSince == null) ? 0 : monthsSince.hashCode());
    result = prime * result + ((occursWhen == null) ? 0 : occursWhen.hashCode());
    result = prime * result + ((painDuration == null) ? 0 : painDuration.hashCode());
    result = prime * result + ((painScale == null) ? 0 : painScale.hashCode());
    result = prime * result + ((painType == null) ? 0 : painType.hashCode());
    result = prime * result + ((painXDay == null) ? 0 : painXDay.hashCode());
    result = prime * result + ((painXHour == null) ? 0 : painXHour.hashCode());
    result = prime * result + ((painXMonth == null) ? 0 : painXMonth.hashCode());
    result = prime * result + ((painXWeek == null) ? 0 : painXWeek.hashCode());
    result = prime * result + ((patientId == null) ? 0 : patientId.hashCode());
    result = prime * result + ((specificLocation == null) ? 0 : specificLocation.hashCode());
    result = prime * result + ((weeksSince == null) ? 0 : weeksSince.hashCode());
    result = prime * result + ((yearsSince == null) ? 0 : yearsSince.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (!super.equals(obj)) { return false; }
    if (getClass() != obj.getClass()) { return false; }
    ChiefComplaint other = (ChiefComplaint) obj;
    if (clinicianId == null) {
      if (other.clinicianId != null) { return false; }
    } 
    else if (!clinicianId.equals(other.clinicianId)) { return false; }
    if (date == null) {
      if (other.date != null) { return false; }
    } 
    else if (!date.equals(other.date)) { return false; }
    if (daysSince == null) {
      if (other.daysSince != null) { return false; }
    } 
    else if (!daysSince.equals(other.daysSince)) {
      return false;
    }
    if (denies == null) {
      if (other.denies != null) { return false; }
    } 
    else if (!denies.equals(other.denies)) { return false; }
    if (deniesOther == null) {
      if (other.deniesOther != null) { return false; }
    } 
    else if (!deniesOther.equals(other.deniesOther)) { return false; }
    if (description == null) {
      if (other.description != null) { return false; }
    } 
    else if (!description.equals(other.description)) { return false; }
    if (encounterId == null) {
      if (other.encounterId != null) { return false; }
    } 
    else if (!encounterId.equals(other.encounterId)) { return false; }
    if (hoursSince == null) {
      if (other.hoursSince != null) { return false; }
    } 
    else if (!hoursSince.equals(other.hoursSince)) { return false; }
    if (howLongOther == null) {
      if (other.howLongOther != null) { return false; }
    } 
    else if (!howLongOther.equals(other.howLongOther)) { return false; }
    if (monthsSince == null) {
      if (other.monthsSince != null) { return false; }
    } 
    else if (!monthsSince.equals(other.monthsSince)) { return false; }
    if (occursWhen == null) {
      if (other.occursWhen != null) { return false; }
    } 
    else if (!occursWhen.equals(other.occursWhen)) { return false; }
    if (painDuration == null) {
      if (other.painDuration != null) { return false; }
    } 
    else if (!painDuration.equals(other.painDuration)) { return false; }
    if (painScale == null) {
      if (other.painScale != null) { return false; }
    } else if (!painScale.equals(other.painScale))
       {return false;}
    if (painType == null) {
      if (other.painType != null)
         {return false;}
    } else if (!painType.equals(other.painType))
       {return false;}
    if (painXDay == null) {
      if (other.painXDay != null)
         {return false;}
    } else if (!painXDay.equals(other.painXDay))
       {return false;}
    if (painXHour == null) {
      if (other.painXHour != null)
         {return false;}
    } else if (!painXHour.equals(other.painXHour))
       {return false;}
    if (painXMonth == null) {
      if (other.painXMonth != null)
         {return false;}
    } else if (!painXMonth.equals(other.painXMonth))
       {return false;}
    if (painXWeek == null) {
      if (other.painXWeek != null)
         {return false;}
    } else if (!painXWeek.equals(other.painXWeek))
       {return false;}
    if (patientId == null) {
      if (other.patientId != null)
         {return false;}
    } else if (!patientId.equals(other.patientId))
       {return false;}
    if (specificLocation == null) {
      if (other.specificLocation != null)
         {return false;}
    } else if (!specificLocation.equals(other.specificLocation))
       {return false;}
    if (weeksSince == null) {
      if (other.weeksSince != null)
         {return false;}
    } else if (!weeksSince.equals(other.weeksSince))
       {return false;}
    if (yearsSince == null) {
      if (other.yearsSince != null)
         {return false;}
    } else if (!yearsSince.equals(other.yearsSince))
       {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "ChiefComplaint [date=" + date + ", encounterId=" + encounterId
        + ", patientId=" + patientId + ", clinicianId=" + clinicianId
        + ", description=" + description + ", occursWhen=" + occursWhen
        + ", specificLocation=" + specificLocation + ", hoursSince="
        + hoursSince + ", daysSince=" + daysSince + ", weeksSince="
        + weeksSince + ", monthsSince=" + monthsSince + ", yearsSince="
        + yearsSince + ", howLongOther=" + howLongOther
        + ", painXHour=" + painXHour + ", painXDay=" + painXDay
        + ", painXWeek=" + painXWeek + ", painXMonth=" + painXMonth
        + ", painDuration=" + painDuration + ", painScale=" + painScale
        + ", painType=" + painType + ", denies=" + denies
        + ", deniesOther=" + deniesOther + "]";
  }

}
