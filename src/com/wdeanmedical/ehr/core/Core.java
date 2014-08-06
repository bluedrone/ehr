/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.core;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;

import com.wdeanmedical.ehr.util.ClinicianSessionData;
import com.wdeanmedical.ehr.core.Core;

public class Core {

  public static ServletContext servletContext;
  public static String timeZone;
  public static Log logger;
  public static String sendMail;
  public static String mailUserName;
  public static String mailPassword;
  public static String mailFrom;
  public static String smtphost;
  public static String smtpport;
  public static String debug;
  public static String smtpauth;
  public static String factport;
  public static String factclass;
  public static String fallback;
  public static String starttls;
  public static String appBaseDir;
  public static String appDefaultHeadshot;
  public static String filesHome;  
  public static String patientDirPath;
  public static String imageMagickHome;
  public static String imagesDir;
  public static Integer appSessionTimeout;
  public static Map<String, ClinicianSessionData> clinicianSessionMap = Collections.synchronizedMap(new TreeMap<String, ClinicianSessionData>());
  public static Map<String, boolean[]> clinicianPermissionsMap = new TreeMap<String, boolean[]>();
  
  public static void buildUserPermissionsMap() {
  
    clinicianPermissionsMap.put("/app/getAppointment",                   new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/getAppointments",                  new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/getAppointmentsByClinician",       new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/getClinicianDashboard",            new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/getClinicianMessage",              new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/getClinicianMessages",             new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/getClinicians",                    new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/getPatientChart",                  new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/getPatientChartSummary",           new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/getPatientHealthIssues",           new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/getPatientSearchTypeAheads",       new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/getRecentPatients",                new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/park",                             new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/patientSearch",                    new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/searchCPT",                        new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/searchICD10",                      new boolean[] {true,true});
    clinicianPermissionsMap.put("/app/unpark",                           new boolean[] {true,true});
    
    clinicianPermissionsMap.put("/admin/activateClinician",              new boolean[] {true,true});
    clinicianPermissionsMap.put("/admin/deactivateClinician",            new boolean[] {true,true});
    clinicianPermissionsMap.put("/admin/purgeClinician",                 new boolean[] {true,true});
    clinicianPermissionsMap.put("/admin/save    newClinician",           new boolean[] {true,true});
    clinicianPermissionsMap.put("/admin/updateClinician",                new boolean[] {true,true});
    
    clinicianPermissionsMap.put("/ext/auth",                             new boolean[] {true,true});
    clinicianPermissionsMap.put("/ext/getPatient",                       new boolean[] {true,true});
    clinicianPermissionsMap.put("/ext/getPatientFullRecord",             new boolean[] {true,true});
    clinicianPermissionsMap.put("/ext/getPatients",                      new boolean[] {true,true});
    clinicianPermissionsMap.put("/ext/importPatients",                   new boolean[] {true,true});
    clinicianPermissionsMap.put("/ext/updatePatient",                    new boolean[] {true,true});
    
    clinicianPermissionsMap.put("/patient/acquirePatient",               new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/addEncounterMedication",       new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/addEncounterQuestion",         new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/closeEncounter",               new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/closePatientNote",             new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/createBasicInfo",              new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/createCC",                     new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/createFollowUp",               new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/createHist",                   new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/createExam",                   new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/createSOAPNote",               new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/createOBGYN",                  new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/createPatientAndEncounter",    new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/createPFSH",                   new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/createSupp",                   new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/createVitals",                 new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/deletePatient",                new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/getCurrentPatientEncounter",   new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/getPatientEncounters",         new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/getPatientProfileImage",       new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/getPatientVitalSigns",         new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/getProgressNotes",             new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/getEncounter",                 new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/getSOAPNotes",                 new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/newProgressNote",              new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/overridePatient",              new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/patientSearch",                new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/releasePatient",               new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/updateEncounterMedication",    new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/updateEncounterQuestion",      new boolean[] {true,true});
    clinicianPermissionsMap.put("/patient/uploadProfileImage",           new boolean[] {true ,true});
    clinicianPermissionsMap.put("/patient/updateProgressNote",           new boolean[] {true,true});
 }

  
}
