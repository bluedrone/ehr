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
import com.wdeanmedical.ehr.entity.ClinicianSchedule;
import com.wdeanmedical.ehr.entity.LabReview;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.PatientMessage;
import com.wdeanmedical.ehr.entity.ProgressNote;
import com.wdeanmedical.ehr.entity.ToDoNote;


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
    else if ("ToDoNote".equals(className)) {
      ToDoNote note = (ToDoNote)obj;
      note.setClinician(null);
    }
    else if ("ProgressNote".equals(className)) {
      ProgressNote note = (ProgressNote)obj;
      note.setClinician(null);
    }
    else if ("LabReview".equals(className)) {
      LabReview lr = (LabReview)obj;
      lr.setClinician(null);
    }
    else if ("ClinicianSchedule".equals(className)) {
      ClinicianSchedule cs = (ClinicianSchedule)obj;
      cs.setClinician(null);
    }
    
  }

  public ExcludedObjects() {
    list.put("Credentials",   new String[] {"password","salt"});
 }
 
}
