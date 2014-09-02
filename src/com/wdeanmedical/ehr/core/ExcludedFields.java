/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.core;

import java.util.Map;
import java.util.TreeMap;

import com.wdeanmedical.ehr.entity.BaseEntity;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.User;


public class ExcludedFields {
  public static String CREDENTIALS = "password, salt";
   
  public static Map<String, String[]> list = new TreeMap<String, String[]>();
  
  public static void excludeFields(Object obj) {
    if (obj == null) {
      return;
    }
    String className = obj.getClass().getSimpleName();
    
    ((BaseEntity)obj).setLastAccessed(null);
    ((BaseEntity)obj).setLastUpdated(null);
    ((BaseEntity)obj).setCreatedDate(null);
    
    if ("Patient".equals(className)) {
      Patient patient = (Patient)obj;
      patient.getCred().setPassword(null);
      patient.getCred().setSalt(null);
      excludeFields(patient.getCred());
      excludeFields(patient.getDemo());
    }
    else if ("Clinician".equals(className)) {
      Clinician clinician = (Clinician)obj;
      clinician.setPassword(null);
      clinician.setSalt(null);
    }
    else if ("User".equals(className)) {
      User user = (User)obj;
      user.setPassword(null);
      user.setSalt(null);
    }
    
  }
  
  
    
  public ExcludedFields() {
    list.put("Credentials",   new String[] {"password","salt"});
    list.put("BaseEntity",    new String[] {"lastAccessed","lastUpdated", "createdDate"});
 }
}
