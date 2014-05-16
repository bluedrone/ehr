/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass()
public class BaseEntity {
  private Integer id;
  private Date lastAccessed;
  private Date lastUpdated;
  private Date createdDate;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Column(name = "last_accessed")
  public Date getLastAccessed() {
    return lastAccessed;
  }

  public void setLastAccessed(Date lastAccessed) {
    this.lastAccessed = lastAccessed;
  }

  @Column(name = "last_updated")
  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @Column(name = "created_date")
  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((lastAccessed == null) ? 0 : lastAccessed.hashCode());
    result = prime * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj == null) { return false; }
    if (getClass() != obj.getClass()) { return false; }
    BaseEntity other = (BaseEntity) obj;
    if (createdDate == null) {
      if (other.createdDate != null) { return false; }
    } 
    else if (!createdDate.equals(other.createdDate)) { return false; }
    if (id == null) {
      if (other.id != null) { return false; }
    } 
    else if (!id.equals(other.id)) { return false; }
    if (lastAccessed == null) {
      if (other.lastAccessed != null) { return false; }
    } 
    else if (!lastAccessed.equals(other.lastAccessed)) { return false; }
    if (lastUpdated == null) {
      if (other.lastUpdated != null) { return false; }
    } 
    else if (!lastUpdated.equals(other.lastUpdated)) { return false; }
    return true;
  }

  @Override
  public String toString() {
    return "BaseEntity [id=" + id + ", lastAccessed=" + lastAccessed
        + ", lastUpdated=" + lastUpdated + ", createdDate="
        + createdDate + "]";
  }

}
