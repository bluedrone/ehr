/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.core;

import java.util.Map;
import java.util.TreeMap;

import com.wdeanmedical.ehr.entity.Patient;


public class ExcludedFields {
  public static String CREDENTIALS = "password, salt";
   
  public static Map<String, String[]> list = new TreeMap<String, String[]>();
  
  public static void excludeFields(Object obj) {
    String className = obj.getClass().getSimpleName();
    
    if ("Patient".equals(className)) {
      Patient patient = (Patient)obj;
      patient.setLastAccessed(null);
      patient.setLastUpdated(null);
      patient.setCreatedDate(null);
      patient.getCred().setPassword(null);
      patient.getCred().setSalt(null);
    }
    
  }
  
  
    
  public ExcludedFields() {
    list.put("Credentials",   new String[] {"password","salt"});
    list.put("BaseEntity",    new String[] {"lastAccessed","lastUpdated", "createdDate"});
 }
}
