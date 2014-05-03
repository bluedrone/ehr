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
  public Integer getId() {return id;}
  public void setId(Integer id) {this.id = id;}
    
  @Column(name = "last_accessed")
  public Date getLastAccessed() { return lastAccessed; }
  public void setLastAccessed(Date lastAccessed) { this.lastAccessed = lastAccessed; }
  
  @Column(name = "last_updated")
  public Date getLastUpdated() { return lastUpdated; }
  public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
  
  @Column(name = "created_date")
  public Date getCreatedDate() { return createdDate; }
  public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
   
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }
}
