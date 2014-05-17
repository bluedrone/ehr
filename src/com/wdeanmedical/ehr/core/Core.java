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
  public static String patientDirPath;
  public static String imageMagickHome;
  public static String imagesDir;
  public static Integer appSessionTimeout;
  public static Map<String, ClinicianSessionData> clinicianSessionMap = Collections.synchronizedMap(new TreeMap<String, ClinicianSessionData>());
  public static Map<String, boolean[]> clinicianPermissionsMap = new TreeMap<String, boolean[]>();
  
  public static void buildUserPermissionsMap() {
    clinicianPermissionsMap.put("/acquirePatient",               new boolean[] {true,true});
    clinicianPermissionsMap.put("/activateClinician",            new boolean[] {true,true});
    clinicianPermissionsMap.put("/deactivateClinician",          new boolean[] {true,true});
    clinicianPermissionsMap.put("/addEncounterMedication",          new boolean[] {true,true});
    clinicianPermissionsMap.put("/addEncounterQuestion",            new boolean[] {true,true});
    clinicianPermissionsMap.put("/closeEncounter",               new boolean[] {true,true});
    clinicianPermissionsMap.put("/closePatientNote",             new boolean[] {true,true});
    clinicianPermissionsMap.put("/createPatientAndEncounter",    new boolean[] {true,true});
    clinicianPermissionsMap.put("/createPatientEncounterGroup",     new boolean[] {true,true});
    clinicianPermissionsMap.put("/deletePatient",                new boolean[] {true,true});
    clinicianPermissionsMap.put("/getClinicianDashboard",        new boolean[] {true,true});
    clinicianPermissionsMap.put("/getClinicianMessage",          new boolean[] {true,true});
    clinicianPermissionsMap.put("/getClinicianMessages",         new boolean[] {true,true});
    clinicianPermissionsMap.put("/getClinicians",                new boolean[] {true,true});
    clinicianPermissionsMap.put("/getEncounter",                 new boolean[] {true,true});
    clinicianPermissionsMap.put("/newProgressNote",              new boolean[] {true,true});
    clinicianPermissionsMap.put("/updateProgressNote",           new boolean[] {true,true});
    clinicianPermissionsMap.put("/getCurrentPatientEncounter",   new boolean[] {true,true});
    clinicianPermissionsMap.put("/patientSearch",                new boolean[] {true,true});
    clinicianPermissionsMap.put("/getPatientChart",              new boolean[] {true,true});
    clinicianPermissionsMap.put("/getProgressNotes",             new boolean[] {true,true});
    clinicianPermissionsMap.put("/getPatientEncounters",         new boolean[] {true,true});
    clinicianPermissionsMap.put("/getPatientChartSummary",       new boolean[] {true,true});
    clinicianPermissionsMap.put("/getPatientVitalSigns",         new boolean[] {true,true});
    clinicianPermissionsMap.put("/getRecentPatients",            new boolean[] {true,true});
    clinicianPermissionsMap.put("/getPatientSearchTypeAheads",   new boolean[] {true,true});
    clinicianPermissionsMap.put("/overridePatient",              new boolean[] {true,true});
    clinicianPermissionsMap.put("/park",                         new boolean[] {true,true});
    clinicianPermissionsMap.put("/purgeClinician",               new boolean[] {true,true});
    clinicianPermissionsMap.put("/releasePatient",               new boolean[] {true,true});
    clinicianPermissionsMap.put("/swapSortOrder",                new boolean[] {true,true});
    clinicianPermissionsMap.put("/saveNewClinician",             new boolean[] {true,true});
    clinicianPermissionsMap.put("/unpark",                       new boolean[] {true,true});
    clinicianPermissionsMap.put("/createBasicInfo",              new boolean[] {true,true});
    clinicianPermissionsMap.put("/createCC",                     new boolean[] {true,true});
    clinicianPermissionsMap.put("/createExam",                   new boolean[] {true,true});
    clinicianPermissionsMap.put("/createFamily",                 new boolean[] {true,true});
    clinicianPermissionsMap.put("/createFollowUp",               new boolean[] {true,true});
    clinicianPermissionsMap.put("/createHist",                   new boolean[] {true,true});
    clinicianPermissionsMap.put("/createOBGYN",                  new boolean[] {true,true});
    clinicianPermissionsMap.put("/createPFSH",                   new boolean[] {true,true});
    clinicianPermissionsMap.put("/createSupp",                   new boolean[] {true,true});
    clinicianPermissionsMap.put("/createVitals",                 new boolean[] {true,true});
    clinicianPermissionsMap.put("/searchCPT",                    new boolean[] {true,true});
    clinicianPermissionsMap.put("/searchICD10",                  new boolean[] {true,true});
    clinicianPermissionsMap.put("/updateEncounterMedication",    new boolean[] {true,true});
    clinicianPermissionsMap.put("/updateEncounterQuestion",      new boolean[] {true,true});
    clinicianPermissionsMap.put("/uploadProfileImage",           new boolean[] {true ,true});
    clinicianPermissionsMap.put("/updatePatient",                new boolean[] {true,true});
    clinicianPermissionsMap.put("/updateClinician",              new boolean[] {true,true});
 }
  

  
}
