/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.persistence;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.wdeanmedical.ehr.core.Core;
import com.wdeanmedical.ehr.entity.Appointment;
import com.wdeanmedical.ehr.entity.BaseEntity;
import com.wdeanmedical.ehr.entity.CPT;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.ClinicianSchedule;
import com.wdeanmedical.ehr.entity.ClinicianSession;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.ICD10;
import com.wdeanmedical.ehr.entity.LabReview;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.PatientAllergen;
import com.wdeanmedical.ehr.entity.PatientClinician;
import com.wdeanmedical.ehr.entity.PatientHealthIssue;
import com.wdeanmedical.ehr.entity.PatientHealthTrendReport;
import com.wdeanmedical.ehr.entity.PatientImmunization;
import com.wdeanmedical.ehr.entity.PatientLetter;
import com.wdeanmedical.ehr.entity.PatientMedicalProcedure;
import com.wdeanmedical.ehr.entity.PatientMedicalTest;
import com.wdeanmedical.ehr.entity.PatientMedication;
import com.wdeanmedical.ehr.entity.PatientMessage;
import com.wdeanmedical.ehr.entity.VitalSigns;
import com.wdeanmedical.ehr.entity.ProgressNote;
import com.wdeanmedical.ehr.entity.ToDoNote;
import com.wdeanmedical.ehr.persistence.SiteDAO;
import com.wdeanmedical.ehr.util.DashboardUtility;
import com.wdeanmedical.ehr.util.OneWayPasswordEncoder;
import com.wdeanmedical.ehr.entity.PatientStatus;
import com.wdeanmedical.ehr.entity.dto.ClinicianScheduleDTO;
import com.wdeanmedical.ehr.entity.dto.PatientMessageDTO;
import com.wdeanmedical.ehr.entity.dto.ProgressNoteDTO;
import com.wdeanmedical.ehr.entity.dto.ToDoNoteDTO;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AppDAO extends SiteDAO {

  private static final Logger log = Logger.getLogger(AppDAO.class);

  private SessionFactory sessionFactory;

  public AppDAO() {
    super();
  }

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  protected Session getSession() {
    return this.sessionFactory.getCurrentSession();
  }
  
  public void create(BaseEntity item) throws Exception {
    item.setLastUpdated(new Date());
    this.createEntity(item);
  }
  
  public void update(BaseEntity item) throws Exception {
    item.setLastUpdated(new Date());
    this.updateEntity(item);
  }
  
  public void delete(BaseEntity item) throws Exception {
    this.deleteEntity(item);
  }
  
  
  public  List<ClinicianScheduleDTO> getClinicianSchedule(Clinician clinician) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(ClinicianSchedule.class);
    crit.add(Restrictions.eq("clinician", clinician));
    crit.addOrder(Order.desc("date"));
    List<ClinicianSchedule> list =  crit.list();
    return DashboardUtility.getClinicianScheduleDTOList(list);
  }
  
  public  List<LabReview> getLabReview(Clinician clinician) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(LabReview.class);
    crit.add(Restrictions.eq("clinician", clinician));
    crit.addOrder(Order.desc("date"));
    List<LabReview> list =  crit.list();
    return list;
  }
  
  public  List<ToDoNoteDTO> getToDoNotes(Clinician clinician) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(ToDoNote.class);
    crit.add(Restrictions.eq("clinician", clinician));
    crit.addOrder(Order.desc("date"));
    List<ToDoNote> list =  crit.list();
    return DashboardUtility.getToDoNoteDTOList(list);
  }
  
  public  List<Clinician> getClinicians() throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Clinician.class);
    crit.add(Restrictions.eq("purged", false));
    List<Clinician> list =  crit.list();
    return list;
  }
  
  
  public  List<ICD10> searchICD10(String searchText) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(ICD10.class);
    crit.add(Restrictions.ilike("description", searchText, MatchMode.ANYWHERE));
    List<ICD10> list =  crit.list();
    return list;
  }
  
  public  List<CPT> searchCPT(String searchText) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(CPT.class);
    crit.add(Restrictions.ilike("description", searchText, MatchMode.ANYWHERE));
    List<CPT> list =  crit.list();
    return list;
  }
  
  public  List<ProgressNoteDTO> getProgressNotes(Clinician clinician) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(ProgressNote.class);
    crit.add(Restrictions.eq("clinician", clinician));
    crit.addOrder(Order.desc("date"));
    List<ProgressNote> list =  crit.list();
    return DashboardUtility.getProgressNoteDTOList(list);
  }
  
  public List<PatientMessageDTO> getPatientMessagesByClinician(Clinician clinician) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientMessage.class);
    crit.add(Restrictions.eq("clinician", clinician));
    crit.add(Restrictions.eq("fromClinician", false));
    crit.addOrder(Order.desc("date"));
    List<PatientMessage> list =  crit.list();
    return DashboardUtility.getPatientMessageDTOList(list);
  }
  
  public List<PatientAllergen> getPatientAllergens(Patient patient) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientAllergen.class);
    crit.add(Restrictions.eq("patient", patient));
    List<PatientAllergen> list =  crit.list();
    return list;
  }
  
  public List<PatientMedication> getPatientMedications(Patient patient) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientMedication.class);
    crit.add(Restrictions.eq("patient", patient));
    List<PatientMedication> list =  crit.list();
    return list;
  }
  
  public List<PatientImmunization> getPatientImmunizations(Patient patient) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientImmunization.class);
    crit.add(Restrictions.eq("patient", patient));
    List<PatientImmunization> list =  crit.list();
    return list;
  }
  
  public List<PatientHealthIssue> getPatientHealthIssues(Patient patient) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientHealthIssue.class);
    crit.add(Restrictions.eq("patient", patient));
    List<PatientHealthIssue> list =  crit.list();
    return list;
  }
  
  public List<PatientMedicalTest> getPatientMedicalTests(Patient patient) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientMedicalTest.class);
    crit.add(Restrictions.eq("patient", patient));
    List<PatientMedicalTest> list =  crit.list();
    return list;
  }
  
  public List<PatientMedicalProcedure> getPatientMedicalProcedures(Patient patient) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientMedicalProcedure.class);
    crit.add(Restrictions.eq("patient", patient));
    List<PatientMedicalProcedure> list =  crit.list();
    return list;
  }
  
  public List<PatientHealthTrendReport> getPatientHealthTrendReports(Patient patient) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientHealthTrendReport.class);
    crit.add(Restrictions.eq("patient", patient));
    List<PatientHealthTrendReport> list =  crit.list();
    return list;
  }
  
  public List<PatientLetter> getPatientLetters(Patient patient) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientLetter.class);
    crit.add(Restrictions.eq("patient", patient));
    List<PatientLetter> list =  crit.list();
    return list;
  }
  
  public List<PatientMessage> getClinicianMessages(Clinician clinician, Boolean fromClinician) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientMessage.class);
    crit.add(Restrictions.eq("clinician", clinician));
    crit.add(Restrictions.eq("fromClinician", fromClinician));
    List<PatientMessage> list =  crit.list();
    return list;
  }
  
  public PatientMessage findClinicianMessageById(int id) throws Exception {
    PatientMessage patientMessage = (PatientMessage) this.findById(PatientMessage.class, id);
    patientMessage.setReadByRecipient(true);
    update(patientMessage);
    return patientMessage;
  }
  
  public List<PatientMessage> getPatientMessages(Patient patient, Boolean fromClinician) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientMessage.class);
    crit.add(Restrictions.eq("patient", patient));
    crit.add(Restrictions.eq("fromClinician", fromClinician));
    List<PatientMessage> list =  crit.list();
    return list;
  }

  public List <VitalSigns> getPatientVitalSigns(Patient patient) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(VitalSigns.class);
    crit.add(Restrictions.eq("patient", patient));
    crit.addOrder(Order.desc("date"));
    List<VitalSigns> list =  crit.list();
    return list;
  }

  public List<Encounter> getEncountersByPatient(Patient patient, Clinician clinician) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Encounter.class);
    crit.add(Restrictions.eq("patient", patient));
    crit.add(Restrictions.eq("clinician", clinician));
    crit.addOrder(Order.desc("date"));
    List<Encounter> list =  crit.list();
    return list;
  }
 
  
  public List<Appointment> getAppointments(Patient patient, boolean isPast) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Appointment.class);
    crit.add(Restrictions.eq("patient", patient));
    if (isPast) {
      crit.add(Restrictions.lt("startTime", new Date()));
    }
    else {
      crit.add(Restrictions.ge("startTime", new Date()));
    }
    List<Appointment> list =  crit.list();
    return list;
  }
  
  
  public Patient findPatientById(int id ) throws Exception {
    return (Patient) this.findById(Patient.class, id);
  }
  
  public Clinician findClinicianById(int id ) throws Exception {
    return (Clinician) this.findById(Clinician.class, id);
  }
  
  public Clinician findClinicianBySessionId(String sessionId ) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(ClinicianSession.class);
    crit.add(Restrictions.eq("sessionId", sessionId));
    ClinicianSession clinicianSession = (ClinicianSession) crit.uniqueResult();
    return (Clinician) this.findById(Clinician.class, clinicianSession.getClinician().getId());
  }
    
  public void deleteClinicianSession(String sessionId) {
    Session session = this.getSession();
    String hql = "delete from ClinicianSession c where c.sessionId = :sessionId";
    Query query = session.createQuery(hql);
    query.setParameter("sessionId", sessionId);
    query.executeUpdate();
  }
  
  public void parkClinicianSession(String sessionId)  throws Exception {
    ClinicianSession clinicianSession = findClinicianSessionBySessionId(sessionId);
    clinicianSession.setParked(true);
    update(clinicianSession);
  }
  
  public void unparkClinicianSession(String sessionId)  throws Exception {
    ClinicianSession clinicianSession = findClinicianSessionBySessionId(sessionId);
    clinicianSession.setParked(false);
    update(clinicianSession);
  }
  
  public void deleteExpiredClinicianSessions() throws Exception {
    if (Core.appSessionTimeout == 0) {
      return;
    }
    Session session = getSession(); 
    Calendar timeoutThreshold = Calendar.getInstance();
    timeoutThreshold.add(Calendar.MINUTE, 0 - Core.appSessionTimeout);
    Date  expireTime = timeoutThreshold.getTime();
    String hql = "delete from ClinicianSession c where c.parked = false and c.lastAccessTime < :expireTime";
    Query query = session.createQuery(hql);
    query.setParameter("expireTime", expireTime);
    query.executeUpdate();
  }
  
  
   public Clinician authenticateClinician(String username, String password) throws Exception {
    log.info("testing username: " + username);
    Clinician clinician = findClinicianByUsername(username);
    if (clinician == null) {
      clinician = new Clinician();
      clinician.setAuthStatus(Clinician.STATUS_NOT_FOUND);
      return clinician;
    }
    String encodedPassword = OneWayPasswordEncoder.getInstance().encode(password, clinician.getSalt());

    Session session = this.getSession();
    Criteria crit = session.createCriteria(Clinician.class);
    crit.add(Restrictions.eq("username", username));
    crit.add(Restrictions.eq("password", encodedPassword));
    clinician = (Clinician) crit.uniqueResult();
    if (clinician != null) {
      clinician.setAuthStatus(Clinician.STATUS_AUTHORIZED);
      if (clinician.getActive() == false) {
        clinician.setAuthStatus(Clinician.STATUS_INACTIVE);
      } 
      else {
        DateFormat df = new SimpleDateFormat("EEE, MMM d, yyyy h:mm a");
        clinician.setPreviousLoginTime(clinician.getLastLoginTime() != null ? df.format(clinician.getLastLoginTime().getTime()) : "");
        clinician.setLastLoginTime(new Date());
        clinician.setSessionId(UUID.randomUUID().toString());
        update(clinician);
      }
    } 
    else {
      clinician = new Clinician();
      clinician.setAuthStatus(Clinician.STATUS_INVALID_PASSWORD);
    }
    return clinician;
  }
  
  public Clinician findClinicianByUsername(String username) throws Exception {
    Session session = this.getSession();
    Clinician clinician = null;
    Criteria crit = session.createCriteria(Clinician.class);
    crit.add(Restrictions.eq("username", username));
    clinician = (Clinician) crit.uniqueResult();
    return clinician;
  }
  
  public ClinicianSession findClinicianSessionBySessionId(String sessionId ) throws Exception {
    Session session = this.getSession();
    ClinicianSession item = null;
    Criteria crit = session.createCriteria(ClinicianSession.class);
    crit.add(Restrictions.eq("sessionId", sessionId));
    item = (ClinicianSession) crit.uniqueResult();
    return item;
  }
  
  public List<PatientClinician> getPatientClinicians(Patient patient) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientClinician.class);
    crit.add(Restrictions.eq("patient", patient));
    List<PatientClinician> list =  crit.list();
    return list;
  }
  
  public List<PatientClinician> getPatientCliniciansByClinician(Clinician clinician) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientClinician.class);
    crit.add(Restrictions.eq("clinician", clinician));
    List<PatientClinician> list =  crit.list();
    return list;
  }
  
  public List<Patient> getFilteredPatients(
    String firstNameFilter,
    String middleNameFilter,
    String lastNameFilter,
    String cityFilter,
    String genderFilter,
    Date dobFilter
  ) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Patient.class, "patient");
    crit.createAlias("patient.cred", "cred"); 
    crit.createAlias("patient.demo", "demo"); 
    crit.createAlias("demo.gender", "gender"); 
    if (firstNameFilter.length() > 0) {crit.add(Restrictions.eq("cred.firstName", firstNameFilter));}
    if (middleNameFilter.length() > 0) {crit.add(Restrictions.eq("cred.middleName", middleNameFilter));}
    if (lastNameFilter.length() > 0) {crit.add(Restrictions.eq("cred.lastName", lastNameFilter));}
    if (cityFilter.length() > 0) {crit.add(Restrictions.eq("demo.city", cityFilter));}
    if (dobFilter != null) {crit.add(Restrictions.eq("demo.dob", dobFilter));}
    if (genderFilter.length() > 0) {crit.add(Restrictions.eq("gender.code", genderFilter));}
    List<Patient> list =  crit.list();
    return list;
  }
  
  public List<Patient> getPatients() throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Patient.class);
    List<Patient> list =  crit.list();
    return list;
  }
  
  public List<Patient> getRecentPatients(int limit) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Patient.class);
    crit.addOrder(Order.desc("lastAccessed"));
    crit.setMaxResults(limit);
    List<Patient> list =  crit.list();
    return list;
  }
  
  public List<Patient> getRecentPatientsByClinician(Clinician clinician, int limit) throws Exception {
    List<PatientClinician> patientClinicianList = getPatientCliniciansByClinician(clinician);
    List<Integer> patientIds = new ArrayList<Integer>();
    for (PatientClinician pc : patientClinicianList) {
      Integer id = pc.getPatient().getId();       
      patientIds.add(id);
    }
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Patient.class);
    crit.add(Restrictions.in("id", patientIds));
    crit.addOrder(Order.desc("lastAccessed"));
    crit.setMaxResults(limit);
    List<Patient> list =  crit.list();
    return list;
  }
  
  public PatientStatus findPatientStatusById(int id) throws Exception {
    return (PatientStatus) this.findById(PatientStatus.class, id);
  }
  
  
  
  public List<Appointment> getAllAppointments() throws Exception {
    return this.findAll(Appointment.class);
  }
  
  
  public Appointment findAppointmentById(int id) throws Exception {
    return (Appointment) this.findById(Appointment.class, id);
  }
  
  

}
