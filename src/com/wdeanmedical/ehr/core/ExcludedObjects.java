/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.core;

import java.util.Map;
import java.util.TreeMap;

import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.PatientMessage;


public class ExcludedObjects {

  public static String AppDAO_getPatientMessagesByClinician = "password, salt";
  
  public static Map<String, String[]> list = new TreeMap<String, String[]>();
  
  public static void excludeObjects(Object obj) {
    if (obj == null) {
      return;
    }
    String className = obj.getClass().getSimpleName();
    
    if ("Patient".equals(className)) {
      Patient patient = (Patient)obj;
      patient.setPfsh(null);
      patient.setHist(null);
    }
    else if ("Clinician".equals(className)) {
      Clinician clinician = (Clinician)obj;
      clinician.setAdminUser(null);
    }
    else if ("PatientMessage".equals(className)) {
      PatientMessage pm = (PatientMessage)obj;
      pm.setClinician(null);
    }
    
  }

  public ExcludedObjects() {
    list.put("Credentials",   new String[] {"password","salt"});
 }
 
}
