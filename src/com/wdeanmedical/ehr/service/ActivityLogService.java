/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.service;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.entity.Activity;
import com.wdeanmedical.ehr.entity.ActivityLog;
import com.wdeanmedical.ehr.entity.BaseEntity;
import com.wdeanmedical.ehr.entity.Module;
import com.wdeanmedical.ehr.persistence.ActivityLogDAO;

public class ActivityLogService {

  private static Log log = LogFactory.getLog(ActivityLogService.class);
  private ServletContext context;
  private WebApplicationContext wac;
  private ActivityLogDAO activityLogDAO;

  public ActivityLogService() {
    context = Core.servletContext;
    wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
    activityLogDAO = (ActivityLogDAO) wac.getBean("activityLogDAO");
  }

  public void logLogin(Integer userId) throws Exception {
    ActivityLog activityLog = new ActivityLog();
    activityLog.setCreatedDate(new Date());
    activityLog.setTimePerformed(new Date());
    activityLog.setUserId(userId);
    activityLogDAO.create(activityLog, Activity.LOGIN, Module.EHR);
    log.info("======= Audit logged login for userId: " + userId);
  }

  public void logLogout(Integer userId) throws Exception {
    ActivityLog activityLog = new ActivityLog();
    activityLog.setCreatedDate(new Date());
    activityLog.setTimePerformed(new Date());
    activityLog.setUserId(userId);
    activityLogDAO.create(activityLog, Activity.LOGOUT, Module.EHR);
    log.info("======= Audit logged logout for userId: " + userId);

  }

  public void logViewPatient(Integer userId, Integer patientId, Integer clinicianId) throws Exception {
    ActivityLog activityLog = new ActivityLog();
    activityLog.setCreatedDate(new Date());
    activityLog.setTimePerformed(new Date());
    activityLog.setUserId(userId);
    activityLog.setPatientId(patientId);
    activityLog.setClinicianId(clinicianId);
    activityLogDAO.create(activityLog, Activity.VIEW_PATIENT, Module.EHR);
    log.info("======= Audit logged view patient for userId: " + userId);

  }

  public void logNewEncounter(Integer userId, Integer patientId, Integer clinicianId, Integer encounterId)
      throws Exception {
    ActivityLog activityLog = new ActivityLog();
    activityLog.setCreatedDate(new Date());
    activityLog.setTimePerformed(new Date());
    activityLog.setUserId(userId);
    activityLog.setPatientId(patientId);
    activityLog.setClinicianId(clinicianId);
    activityLog.setEncounterId(encounterId);
    activityLogDAO.create(activityLog, Activity.CREATE_ENCOUNTER, Module.EHR);
    log.info("======= Audit logged new encounter for userId: " + userId);

  }

  public void logViewEncounter(Integer userId, Integer patientId, Integer clinicianId, Integer encounterId)
      throws Exception {
    ActivityLog activityLog = new ActivityLog();
    activityLog.setCreatedDate(new Date());
    activityLog.setTimePerformed(new Date());
    activityLog.setUserId(userId);
    activityLog.setPatientId(patientId);
    activityLog.setClinicianId(clinicianId);
    activityLog.setEncounterId(encounterId);
    activityLogDAO.create(activityLog, Activity.VIEW_PATIENT_ENCOUNTER, Module.EHR);
    log.info("======= Audit logged view encounter for userId: " + userId);

  }

  public void logEditPatient(Integer userId, Integer patientId, Integer clinicianId, Integer encounterId,
      Collection<String> fieldNames) throws Exception {
    for (String fieldName : fieldNames) {
      ActivityLog activityLog = new ActivityLog();
      activityLog.setCreatedDate(new Date());
      activityLog.setTimePerformed(new Date());
      activityLog.setUserId(userId);
      activityLog.setPatientId(patientId);
      activityLog.setClinicianId(clinicianId);
      activityLog.setEncounterId(encounterId);
      activityLog.setFieldName(fieldName);
      activityLogDAO.create(activityLog, Activity.EDIT_PATIENT_FIELD, Module.EHR);
    }
    log.info("======= Audit logged edit patient for userId: " + userId);

  }

  public void logEditEncounter(Integer userId, Integer patientId, Integer clinicianId, Integer encounterId,
      Collection<String> fieldNames) throws Exception {
    for (String fieldName : fieldNames) {
      ActivityLog activityLog = new ActivityLog();
      activityLog.setCreatedDate(new Date());
      activityLog.setTimePerformed(new Date());
      activityLog.setUserId(userId);
      activityLog.setPatientId(patientId);
      activityLog.setClinicianId(clinicianId);
      activityLog.setEncounterId(encounterId);
      activityLog.setFieldName(fieldName);
      activityLogDAO.create(activityLog, Activity.EDIT_PATIENT_ENCOUNTER_FIELD, Module.EHR);
    }
    log.info("======= Audit logged edit encounter for userId: " + userId);

  }

  public void logDeletePatient(Integer userId, Integer patientId, Integer clinicianId, Integer encounterId)
      throws Exception {
    ActivityLog activityLog = new ActivityLog();
    activityLog.setCreatedDate(new Date());
    activityLog.setTimePerformed(new Date());
    activityLog.setUserId(userId);
    activityLog.setPatientId(patientId);
    activityLog.setClinicianId(clinicianId);
    activityLog.setEncounterId(encounterId);
    activityLogDAO.create(activityLog, Activity.DELETE_PATIENT, Module.EHR);
    log.info("======= Audit logged delete patient for userId: " + userId);

  }

  public Set<String> getListOfChangedFields(BaseEntity newEntity) throws Exception {
    return getChangedFields(activityLogDAO.getOldEntity(newEntity), newEntity);
  }

  private static Set<String> getChangedFields(Object oldEntity, Object newEntity) throws Exception {
    Set<String> hashSet = new HashSet<String>();
    Method[] oldEntityMethods = null;
    if (oldEntity != null) {
      oldEntityMethods = oldEntity.getClass().getMethods();
    }
    Method[] newEntityMethods = newEntity.getClass().getMethods();
    if (oldEntityMethods == null) {
      for (Method newEntityMethod : newEntityMethods) {
        if (newEntityMethod.getName().startsWith("get") || newEntityMethod.getName().startsWith("is")) {
          Object newEntityReturnedValue = newEntityMethod.invoke(newEntity);
          if (newEntityReturnedValue == null) {
            continue;
          } else if (newEntityMethod.getName().startsWith("get")) {
            hashSet.add(newEntityMethod.getName().substring(3));
          } else if (newEntityMethod.getName().startsWith("is")) {
            hashSet.add(newEntityMethod.getName().substring(2));
          }
        }
      }
    } else {
      outer: for (Method oldEntityMethod : oldEntityMethods) {
        Object oldEntityReturnedValue = null;
        Object newEntityReturnedValue = null;
        String oldEntityMethodName = null;
        String newEntityMethodName = null;
        if (oldEntityMethod.getName().startsWith("get") || oldEntityMethod.getName().startsWith("is")) {
          oldEntityMethodName = oldEntityMethod.getName();
          oldEntityReturnedValue = oldEntityMethod.invoke(oldEntity);
          if (oldEntityReturnedValue == null) {
            continue;
          }
          for (Method newEntityMethod : newEntityMethods) {

            if (newEntityMethod.getName().startsWith("get") || newEntityMethod.getName().startsWith("is")) {
              newEntityMethodName = newEntityMethod.getName();
              if (oldEntityMethodName.equals(newEntityMethodName)) {
                newEntityReturnedValue = newEntityMethod.invoke(newEntity);
                if (newEntityReturnedValue == null) {
                  continue outer;
                }
                if (oldEntityReturnedValue.equals(newEntityReturnedValue)) {
                  continue outer;
                } else if (newEntityMethod.getName().startsWith("get")) {
                  hashSet.add(newEntityMethod.getName().substring(3));
                } else if (newEntityMethod.getName().startsWith("is")) {
                  hashSet.add(newEntityMethod.getName().substring(2));
                }
                continue outer;
              }
            }
          }

        }
      }
    }
    return hashSet;
  }

}
