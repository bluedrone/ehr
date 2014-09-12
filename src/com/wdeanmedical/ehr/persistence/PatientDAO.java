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
import com.wdeanmedical.ehr.entity.ChiefComplaint;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.ClinicianSchedule;
import com.wdeanmedical.ehr.entity.ClinicianSession;
import com.wdeanmedical.ehr.entity.Country;
import com.wdeanmedical.ehr.entity.Credentials;
import com.wdeanmedical.ehr.entity.Demographics;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.EncounterType;
import com.wdeanmedical.ehr.entity.Ethnicity;
import com.wdeanmedical.ehr.entity.Exam;
import com.wdeanmedical.ehr.entity.Gender;
import com.wdeanmedical.ehr.entity.PatientHistoryMedication;
import com.wdeanmedical.ehr.entity.EncounterQuestion;
import com.wdeanmedical.ehr.entity.Lab;
import com.wdeanmedical.ehr.entity.LabReview;
import com.wdeanmedical.ehr.entity.MaritalStatus;
import com.wdeanmedical.ehr.entity.OBGYNEncounterData;
import com.wdeanmedical.ehr.entity.Patient;
import com.wdeanmedical.ehr.entity.PatientAllergen;
import com.wdeanmedical.ehr.entity.PatientClinician;
import com.wdeanmedical.ehr.entity.PatientFollowUp;
import com.wdeanmedical.ehr.entity.PatientHealthIssue;
import com.wdeanmedical.ehr.entity.PatientHealthTrendReport;
import com.wdeanmedical.ehr.entity.PatientImmunization;
import com.wdeanmedical.ehr.entity.PatientLetter;
import com.wdeanmedical.ehr.entity.MedicalHistory;
import com.wdeanmedical.ehr.entity.PatientMedicalProcedure;
import com.wdeanmedical.ehr.entity.PatientMedicalTest;
import com.wdeanmedical.ehr.entity.PatientMedication;
import com.wdeanmedical.ehr.entity.PatientMessage;
import com.wdeanmedical.ehr.entity.PFSH;
import com.wdeanmedical.ehr.entity.PatientStatus;
import com.wdeanmedical.ehr.entity.Race;
import com.wdeanmedical.ehr.entity.SOAPNote;
import com.wdeanmedical.ehr.entity.SuppQuestions;
import com.wdeanmedical.ehr.entity.USState;
import com.wdeanmedical.ehr.entity.VitalSigns;
import com.wdeanmedical.ehr.entity.ProgressNote;
import com.wdeanmedical.ehr.entity.ToDoNote;
import com.wdeanmedical.ehr.persistence.SiteDAO;
import com.wdeanmedical.ehr.util.DataEncryptor;
import com.wdeanmedical.ehr.util.OneWayPasswordEncoder;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PatientDAO extends SiteDAO {

  private static final Logger log = Logger.getLogger(PatientDAO.class);

  private SessionFactory sessionFactory;
  
  
  public PatientDAO() {
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
  
  public List<EncounterQuestion> getEncounterQuestionsByEncounter(int encounterId) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(EncounterQuestion.class);
    crit.add(Restrictions.eq("encounterId", encounterId));
    crit.addOrder(Order.asc("id"));
    List<EncounterQuestion> list = crit.list();
    return list;
  }
  
  public List<PatientHistoryMedication> getEncounterMedicationsByPatient(int patientId) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(PatientHistoryMedication.class);
    crit.add(Restrictions.eq("patientId", patientId));
    crit.addOrder(Order.asc("id"));
    List<PatientHistoryMedication> list = crit.list();
    return list;
  }
  
  

  public Encounter findCurrentEncounterByPatientId(int patientId) throws Exception {
    Patient patient = findPatientById(patientId);
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Encounter.class);
    crit.add(Restrictions.eq("patient", patient));
    ProjectionList proj = Projections.projectionList();
    proj = proj.add(Projections.max("id"));
    crit = crit.setProjection(proj);
    Integer id = (Integer)crit.uniqueResult();
    crit = session.createCriteria(Encounter.class);
    crit.add(Restrictions.eq("id", id));
    Encounter encounter = (Encounter)crit.uniqueResult();
    encounter.getSupp().setEncounterQuestionList(getEncounterQuestionsByEncounter(encounter.getId()));
    patient.getHist().setEncounterMedicationList(getEncounterMedicationsByPatient(patient.getId()));
    
    return encounter;
  }
    
    
  public  List<Encounter> findEncountersByPatient(Integer patientId) throws Exception {
    Patient patient = findPatientById(patientId);
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Encounter.class);
    crit.add(Restrictions.eq("patient", patient));
    crit.addOrder(Order.desc("date"));
    List<Encounter> list = crit.list();
    for (Encounter encounter : list) {
      encounter.getSupp().setEncounterQuestionList(getEncounterQuestionsByEncounter(encounter.getId()));
      patient.getHist().setEncounterMedicationList(getEncounterMedicationsByPatient(patient.getId()));
    }
    return list;
  }
  
  
  public  List<ProgressNote> findProgressNotesByPatient(Patient patient) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(ProgressNote.class);
    crit.add(Restrictions.eq("patient", patient));
    crit.addOrder(Order.desc("date"));
    List<ProgressNote> list = crit.list();
    return list;
  }
  
  
  
  public  List<ChiefComplaint> findChiefComplaintsByPatientId(Integer patientId) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(ChiefComplaint.class);
    crit.add(Restrictions.eq("patientId", patientId));
    crit.addOrder(Order.desc("date"));
    List<ChiefComplaint> list = crit.list();
    return list;
  }
  
  
  
  public  List<SOAPNote> findSOAPNotesByPatientId(Integer patientId) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(SOAPNote.class);
    crit.add(Restrictions.eq("patientId", patientId));
    crit.addOrder(Order.desc("date"));
    List<SOAPNote> list = crit.list();
    return list;
  }
  

  
  public void updateEncounterMedication(PatientHistoryMedication patientHistoryMedication) throws Exception {
    Session session = this.getSession();
    session.update(patientHistoryMedication);
  }
  
  public void updateEncounterQuestion(EncounterQuestion encounterQuestion) throws Exception {
    Session session = this.getSession();
    session.update(encounterQuestion);
  }
  
  public void createPatient(Patient patient) throws Exception {
    Session session = this.getSession();
    patient.setLastAccessed(new Date());
    session.save(patient);
  }
  
  public ProgressNote createProgressNote(Patient patient, Clinician clinician) throws Exception {
    Session session = this.getSession();
    ProgressNote note = new ProgressNote(); 
    note.setPatient(patient);
    note.setClinician(clinician);
    note.setDate(new Date());
    session.save(note);
    return note;
  }
  
  public Encounter createEncounter(Patient patient, Clinician clinician) throws Exception {
    Session session = this.getSession();
    
    Encounter encounter = new Encounter(); 
    encounter.setEncounterType(findEncounterTypeById(EncounterType.CHECK_UP)); 
    encounter.setLastAccessed(new Date());
    encounter.setCreatedDate(new Date());
    encounter.setDate(new Date());
    encounter.setClinician(clinician);
    encounter.setPatient(patient);
    session.save(encounter);
    
    ChiefComplaint cc = new ChiefComplaint();
    cc.setPatientId(patient.getId());
    cc.setEncounterId(encounter.getId());
    session.save(cc);
    encounter.setCc(cc);
    
    VitalSigns vitals = new VitalSigns();
    vitals.setPatientId(patient.getId());
    vitals.setClinicianId(clinician.getId());
    vitals.setEncounterId(encounter.getId());
    vitals.setDate(encounter.getDate());
    session.save(vitals);
    encounter.setVitals(vitals);
    
    SOAPNote soapNote = new SOAPNote();
    soapNote.setPatientId(patient.getId());
    soapNote.setClinicianId(clinician.getId());
    soapNote.setEncounterId(encounter.getId());
    soapNote.setDate(encounter.getDate());
    session.save(soapNote);
    encounter.setSOAPNote(soapNote);
    
    SuppQuestions supp = new SuppQuestions();
    supp.setPatientId(patient.getId());
    supp.setEncounterId(encounter.getId());
    session.save(supp);
    encounter.setSupp(supp);
    
    Exam exam = new Exam();
    exam.setPatientId(patient.getId());
    exam.setEncounterId(encounter.getId());
    session.save(exam);
    encounter.setExam(exam);
    
    Lab lab = new Lab();
    lab.setPatientId(patient.getId());
    lab.setEncounterId(encounter.getId());
    session.save(lab);
    encounter.setLab(lab);
    
    OBGYNEncounterData obgyn = new OBGYNEncounterData();
    obgyn.setPatientId(patient.getId());
    obgyn.setEncounterId(encounter.getId());
    session.save(obgyn);
    encounter.setObgyn(obgyn);
    
    PatientFollowUp followUp = new PatientFollowUp();
    followUp.setPatientId(patient.getId());
    followUp.setEncounterId(encounter.getId());
    session.save(followUp);
    encounter.setFollowUp(followUp);
    
    session.update(encounter);
    return encounter;
  }

  
  public Encounter findCurrentEncounterByPatient(Patient patient) throws Exception {
    Session session = getSession();
    Criteria crit = session.createCriteria(Encounter.class);
    crit.add(Restrictions.eq("patient", patient));
    crit.add(Restrictions.eq("completed", false));
    Encounter encounter = (Encounter)crit.uniqueResult();
    encounter.getSupp().setEncounterQuestionList(getEncounterQuestionsByEncounter(encounter.getId()));
    patient.getHist().setEncounterMedicationList(getEncounterMedicationsByPatient(patient.getId()));
    return encounter;
  }
  
  public EncounterType findEncounterTypeById(int id) throws Exception {
    EncounterType et = (EncounterType) this.findById(EncounterType.class, id);
    return et;
  }
  
  public Encounter findEncounterById(int id) throws Exception {
    Encounter encounter = (Encounter) this.findById(Encounter.class, id);
    Patient patient = encounter.getPatient();
    encounter.getSupp().setEncounterQuestionList(getEncounterQuestionsByEncounter(encounter.getId()));
    patient.getHist().setEncounterMedicationList(getEncounterMedicationsByPatient(patient.getId()));
    return encounter;
  }
  
  public ProgressNote findProgressNoteById(int id) throws Exception {
    ProgressNote note = (ProgressNote) this.findById(ProgressNote.class, id);
    return note;
  }
  
  public Patient findPatientById(Integer id) throws Exception {
    Patient patient = (Patient) this.findById(Patient.class, id);
    return patient;
  }
  
  public PatientStatus findPatientStatusById(Integer id) throws Exception {
    PatientStatus ps = (PatientStatus) this.findById(PatientStatus.class, id);
    return ps;
  }
  
  public Encounter findEncounterById(Integer id) throws Exception {
    Encounter patientEncounter = (Encounter) this.findById(Encounter.class, id);
    return patientEncounter;
  }
  
  public VitalSigns findVitalSignsByEncounterId(Integer encounterId) throws Exception {
    Session session = getSession();
    Criteria crit = session.createCriteria(VitalSigns.class);
    crit.add(Restrictions.eq("encounterId", encounterId));
    return (VitalSigns)crit.uniqueResult();
  }
  
  public ChiefComplaint findChiefComplaintByEncounterId(Integer encounterId) throws Exception {
    Session session = getSession();
    Criteria crit = session.createCriteria(ChiefComplaint.class);
    crit.add(Restrictions.eq("encounterId", encounterId));
    return (ChiefComplaint)crit.uniqueResult();
  }
  
  public Exam findExamByEncounterId(Integer encounterId) throws Exception {
    Session session = getSession();
    Criteria crit = session.createCriteria(Exam.class);
    crit.add(Restrictions.eq("encounterId", encounterId));
    return (Exam)crit.uniqueResult();
  }
  
  public Lab findLabByEncounterId(Integer encounterId) throws Exception {
    Session session = getSession();
    Criteria crit = session.createCriteria(Lab.class);
    crit.add(Restrictions.eq("encounterId", encounterId));
    return (Lab)crit.uniqueResult();
  }
  
  public void updatePatientProfileImage(Patient patient, String path) throws Exception {
    Session session = this.getSession();
    Demographics d = patient.getDemo();
    d.setProfileImagePath(path);
    session.update(d);
  }
  
  public PatientHistoryMedication findEncounterMedicationById(int id) throws Exception {
    PatientHistoryMedication patientHistoryMedication = (PatientHistoryMedication) this.findById(PatientHistoryMedication.class, id);
    return patientHistoryMedication;
  }
  
  public EncounterQuestion findEncounterQuestionById(int id) throws Exception {
    EncounterQuestion encounterQuestion = (EncounterQuestion) this.findById(EncounterQuestion.class, id);
    return encounterQuestion;
  }
  
  public Gender findGenderByCode(String code) throws Exception {
    Session session = getSession();
    Criteria crit = session.createCriteria(Gender.class);
    crit.add(Restrictions.eq("code", code));
    return (Gender)crit.uniqueResult();
  }
  
  
  public USState findUSStateById(int id ) throws Exception {
    return (USState) this.findById(USState.class, id);
  }
  
    
  public Race findRaceById(int id ) throws Exception {
    return (Race) this.findById(Race.class, id);
  }
  
  
  
  public Ethnicity findEthnicityById(int id ) throws Exception {
    return (Ethnicity) this.findById(Ethnicity.class, id);
  }
  
  
  
  public MaritalStatus findMaritalStatusByCode(String code) throws Exception {
    Session session = getSession();
    Criteria crit = session.createCriteria(MaritalStatus.class);
    crit.add(Restrictions.eq("code", code));
    return (MaritalStatus)crit.uniqueResult();
  }
  
  public Country findCountryByName(String name) throws Exception {
    Session session = getSession();
    Criteria crit = session.createCriteria(Country.class);
    crit.add(Restrictions.eq("name", name));
    return (Country)crit.uniqueResult();
  }
  
  public USState findUSStateByName(String name) throws Exception {
    Session session = getSession();
    Criteria crit = session.createCriteria(USState.class);
    crit.add(Restrictions.eq("name", name));
    return (USState)crit.uniqueResult();
  }
  
  public Patient findPatientByMrn(String mrn) throws Exception {
	 Session session = getSession();
	 Criteria crit = session.createCriteria(Credentials.class);
	 crit.add(Restrictions.eq("mrn", mrn));
	 Credentials cred = (Credentials)crit.uniqueResult();
	 Patient patient = (Patient) this.findById(Patient.class, cred.getPatientId());
	 return patient;
  }

}
