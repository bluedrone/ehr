/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

package com.wdeanmedical.ehr.persistence;


import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.wdeanmedical.ehr.entity.Activity;
import com.wdeanmedical.ehr.entity.ActivityLog;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.ClinicianSession;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.Report;
import com.wdeanmedical.ehr.entity.User;
import com.wdeanmedical.ehr.persistence.SiteDAO;
import com.wdeanmedical.ehr.util.DataEncryptor;
import com.wdeanmedical.ehr.util.WDMConstants;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ReportsDAO extends SiteDAO {

  private static final Logger log = Logger.getLogger(ReportsDAO.class);

  private SessionFactory sessionFactory;

  public ReportsDAO() {
    super();
  }

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  protected Session getSession() {
    return this.sessionFactory.getCurrentSession();
  }

  public Clinician findClinicianBySessionId(String sessionId) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(ClinicianSession.class);
    crit.add(Restrictions.eq("sessionId", sessionId));
    ClinicianSession clinicianSession = (ClinicianSession) crit.uniqueResult();
    return (Clinician) this.findById(Clinician.class, clinicianSession.getClinician().getId());
  }

  public List<ActivityLog> getActivityLog(Integer clinicianId) {
    Session session = this.getSession();
    Query activityLogQuery = session.createQuery("SELECT al FROM ActivityLog al WHERE al.clinicianId = '" + clinicianId
        + "' ORDER BY al.createdDate DESC");
    activityLogQuery.setMaxResults(200);
    return activityLogQuery.list();
  }
  
  public Map<Integer, List<ActivityLog>> getActivityLogGroupByPatientId(Integer clinicianId) {
    Session session = this.getSession();
    Criteria criteria = session.createCriteria(ActivityLog.class);
    Query patientIdQuery = session.createQuery("SELECT DISTINCT(al.patientId) FROM ActivityLog al"
        + " WHERE al.clinicianId = '" + clinicianId + "' AND al.patientId IS NOT NULL");
    List<Integer> patientIdList = patientIdQuery.list();
    Map<Integer, List<ActivityLog>> groupedPatients = new TreeMap<Integer, List<ActivityLog>>();
    for(Integer patientId : patientIdList){
      Query activityLogQuery = session.createQuery("SELECT al FROM ActivityLog al WHERE al.clinicianId = '" 
      + clinicianId + "' AND al.patientId = '" + patientId + "'");
      groupedPatients.put(patientId, activityLogQuery.list());
    }
    return groupedPatients;
  }

  public List<Report> getReportList(Integer userId) {
    Session session = this.getSession();
    Query reportListQuery = session.createQuery("SELECT r FROM Report r  ORDER BY r.sortOrder ASC");
    return reportListQuery.list();
  }
  
  public User findUserById(Integer id) throws Exception {
    User user = (User) this.findById(User.class, id);
    return user;
  }
  
  public Patient findPatientById(Integer id) throws Exception {
    Patient patient = (Patient) this.findById(Patient.class, id);
    return patient;
  }
  
  public Clinician findClinicianById(Integer id) throws Exception {
    Clinician clinician = (Clinician) this.findById(Clinician.class, id);
    return clinician;
  }
  
  public List<Clinician> getClinicians() throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Clinician.class);
    List<Clinician> list =  crit.list();
    return list;
  }
  
  public List<Patient> getPatients() throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Patient.class);
    List<Patient> list =  crit.list();
    return list;
  }
  
  public List<Activity> activityLogGetActivity() throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Activity.class);
    List<Activity> list =  crit.list();
    return list;
  }
  
  public List<ActivityLog> getFilteredActivityLog(Date dateFrom, Date dateTo, Integer clinicianId, Activity activity, Integer patientId) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(ActivityLog.class);
    if (dateFrom != null) {crit.add(Restrictions.ge("timePerformed", dateFrom));}
    if (dateFrom != null && dateTo != null) {
      crit.add(Restrictions.le("timePerformed", dateTo));
    }else if(dateFrom != null && dateTo == null){
      crit.add(Restrictions.le("timePerformed", new Date()));
    }
    if (clinicianId != null) {crit.add(Restrictions.eq("clinicianId", clinicianId));}
    if (activity != null) {crit.add(Restrictions.eq("activity", activity));}
    if (patientId != null) {crit.add(Restrictions.eq("patientId", patientId));}
    List<ActivityLog> list =  crit.list();
    return list;
  }
  
  public Map<Integer, List<ActivityLog>> filterGroupByPatientsActivityLog(Date dateFrom, Date dateTo, Integer clinicianId, Activity activity, Integer patientId) {
    Session session = this.getSession();
    StringBuilder distinctPatientQuery = new StringBuilder();
    distinctPatientQuery.append("SELECT DISTINCT(al.patientId) FROM ActivityLog al WHERE al.patientId IS NOT NULL AND al.patientId != 0");
    if(clinicianId != null){
      distinctPatientQuery.append(" AND al.clinicianId = '" + clinicianId + "'");
    }
    if(patientId != null){
      distinctPatientQuery.append(" AND al.patientId  = '" + patientId  + "'");
    }
    
    Query patientIdQuery = session.createQuery(distinctPatientQuery.toString());
    List<Integer> patientIdList = patientIdQuery.list();
    Map<Integer, List<ActivityLog>> groupedPatients = new TreeMap<Integer, List<ActivityLog>>();
    for(Integer distinctPatientId : patientIdList){
      Criteria crit = session.createCriteria(ActivityLog.class);
      if (dateFrom != null) {crit.add(Restrictions.ge("timePerformed", dateFrom));}
      if (dateFrom != null && dateTo != null) {
        crit.add(Restrictions.le("timePerformed", dateTo));
      }else if(dateFrom != null && dateTo == null){
        crit.add(Restrictions.le("timePerformed", new Date()));
      }
      if (clinicianId != null) {crit.add(Restrictions.eq("clinicianId", clinicianId));}
      if (activity != null) {crit.add(Restrictions.eq("activity", activity));}
      if (patientId != null) {crit.add(Restrictions.eq("patientId", distinctPatientId));}
      groupedPatients.put(distinctPatientId, crit.list());
    }
    return groupedPatients;
  }
  
  public Clinician getClinicianByFullName(String clinicianFullName) throws Exception {
    String[] name = clinicianFullName.split(WDMConstants.SINGLE_SPACE);
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Clinician.class);
    if(name.length == 2){
      {crit.add(Restrictions.eq("firstName", name[0]));}
      {crit.add(Restrictions.eq("lastName", name[1]));}
    }else if(name.length == 3){
      {crit.add(Restrictions.eq("firstName", name[0]));}
      {crit.add(Restrictions.eq("middleName", name[1]));}
      {crit.add(Restrictions.eq("lastName", name[2]));}      
    }
    return (Clinician) crit.uniqueResult();
  }
  
  public Patient getPatientByFullName(String patientFullName) throws Exception {
    String[] name = patientFullName.split(WDMConstants.SINGLE_SPACE);
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Patient.class, "patient");
    crit.createAlias("patient.cred", "cred");
    String encryptedFirstNameFilter = null;
    String encryptedMiddleNameFilter = null;
    String encryptedLastNameFilter = null;  
    if(name.length == 2){
      encryptedFirstNameFilter = DataEncryptor.encrypt(capitalize(name[0]));
      encryptedLastNameFilter = DataEncryptor.encrypt(capitalize(name[1]));
      {crit.add(Restrictions.eq("cred.firstName", encryptedFirstNameFilter));}
      {crit.add(Restrictions.eq("cred.lastName", encryptedLastNameFilter));}
    }else if(name.length == 3){
      encryptedFirstNameFilter = DataEncryptor.encrypt(capitalize(name[0]));
      encryptedMiddleNameFilter = DataEncryptor.encrypt(capitalize(name[1]));
      encryptedLastNameFilter = DataEncryptor.encrypt(capitalize(name[2]));
      {crit.add(Restrictions.eq("cred.firstName", encryptedFirstNameFilter));}
      {crit.add(Restrictions.eq("cred.middleName", encryptedMiddleNameFilter));}
      {crit.add(Restrictions.eq("cred.lastName", encryptedLastNameFilter));}      
    }
    return (Patient) crit.uniqueResult();
  }
  
  public Activity findActivityById(Integer id) throws Exception {
    Activity activity = (Activity) this.findById(Activity.class, id);
    return activity;
  }
  
  private String capitalize(String s) {
    return Character.toUpperCase(s.charAt(0)) + s.substring(1); 
  }
  

}
