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


public class ExcludedObjects {

  public static String AppDAO_getPatientMessagesByClinician = "password, salt";
  
  public static Map<String, String[]> list = new TreeMap<String, String[]>();
  
  public static void excludeObjects(Object obj) {
    String className = obj.getClass().getSimpleName();
    
    if ("Patient".equals(className)) {
      Patient patient = (Patient)obj;
      patient.setPfsh(null);
      patient.setHist(null);
    }
    
  }

  public ExcludedObjects() {
    list.put("Credentials",   new String[] {"password","salt"});
 }
 
}
