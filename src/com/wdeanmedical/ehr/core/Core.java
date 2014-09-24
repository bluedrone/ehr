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
import com.wdeanmedical.ehr.entity.Patient;

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
  public static String pmHome;
  public static Map<String, ClinicianSessionData> clinicianSessionMap = Collections.synchronizedMap(new TreeMap<String, ClinicianSessionData>());
  public static Map<Integer, Patient> decryptedPatients = Collections.synchronizedMap(new TreeMap<Integer, Patient>());
  
}
