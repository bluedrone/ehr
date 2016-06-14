package com.wdeanmedical.ehr.entity.dell;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.wdeanmedical.ehr.dto.DeviceInfo;
import com.wdeanmedical.ehr.entity.BaseEntity;

public class HIEDeviceData extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 5583816940173806003L;
  
  public String mrn = "ABC123";
  public String patientLastName = "Doe";
  public String patientFirstName = "John";
  public String patientMiddleName = "";
  public String patientDOB = "10/10/1962";
  public String patientSex = "M";
  public String patientClass = "O";
  public String attendingPhysicianCode = "XYZ789";
  public String attendingLastName = "St. James";
  public String attendingPhysicianFirstName = "Maria";
  public Long datetime = new Date().getTime();
  public String orderingPhysicianCode = "XYZ123";
  public String orderingLastName = "Gonzales";
  public String orderingPhysicianFirstName = "Francis";
  public String orderNumber = "012345556";
  public String resultNumber = "1465497598";
  public DeviceInfo data = new DeviceInfo();
  

  public HIEDeviceData() {
  }

}
