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
import com.wdeanmedical.ehr.entity.Demographics;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.EncounterType;
import com.wdeanmedical.ehr.entity.Exam;
import com.wdeanmedical.ehr.entity.Gender;
import com.wdeanmedical.ehr.entity.IntakeMedication;
import com.wdeanmedical.ehr.entity.IntakeQuestion;
import com.wdeanmedical.ehr.entity.Lab;
import com.wdeanmedical.ehr.entity.LabReview;
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
import com.wdeanmedical.ehr.entity.SuppQuestions;
import com.wdeanmedical.ehr.entity.VitalSigns;
import com.wdeanmedical.ehr.entity.ProgressNote;
import com.wdeanmedical.ehr.entity.ToDoNote;
import com.wdeanmedical.ehr.persistence.SiteDAO;
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
  
  public List<IntakeQuestion> getIntakeQuestionsByEncounter(int encounterId) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(IntakeQuestion.class);
    crit.add(Restrictions.eq("encounterId", encounterId));
    crit.addOrder(Order.asc("id"));
    List<IntakeQuestion> list = crit.list();
    return list;
  }
  
  public List<IntakeMedication> getIntakeMedicationsByPatient(int patientId) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(IntakeMedication.class);
    crit.add(Restrictions.eq("patientId", patientId));
    crit.addOrder(Order.asc("id"));
    List<IntakeMedication> list = crit.list();
    return list;
  }
  
  
  public List<Encounter> getEncountersByGroupId(int patientIntakeGroupId) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Encounter.class);
    crit.add(Restrictions.eq("patientIntakeGroupId", patientIntakeGroupId));
    crit.add(Restrictions.eq("completed", false));
    crit.addOrder(Order.asc("id"));
    List<Encounter> list = crit.list();
    for (Encounter encounter : list) {
      encounter.getSupp().setIntakeQuestionList(getIntakeQuestionsByEncounter(encounter.getId()));
      Patient patient = encounter.getPatient();
      patient.getHist().setIntakeMedicationList(getIntakeMedicationsByPatient(patient.getId()));
    }
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
    return encounter;
  }
    
    
  public  List<Encounter> findEncountersByPatient(Integer patientId) throws Exception {
    Patient patient = findPatientById(patientId);
    Session session = this.getSession();
    Criteria crit = session.createCriteria(Encounter.class);
    crit.add(Restrictions.eq("patient", patient));
    crit.addOrder(Order.desc("date"));
    List<Encounter> list = crit.list();
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

  
  public void updateIntakeMedication(IntakeMedication intakeMedication) throws Exception {
    Session session = this.getSession();
    session.update(intakeMedication);
  }
  
  public void updateIntakeQuestion(IntakeQuestion intakeQuestion) throws Exception {
    Session session = this.getSession();
    session.update(intakeQuestion);
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
    encounter.setPatient(patient);
    session.save(encounter);
    
    ChiefComplaint cc = new ChiefComplaint();
    cc.setPatientId(patient.getId());
    cc.setEncounterId(encounter.getId());
    session.save(cc);
    encounter.setCc(cc);
    
    VitalSigns vitals = new VitalSigns();
    vitals.setPatient(patient);
    vitals.setClinician(clinician);
    vitals.setEncounterId(encounter.getId());
    session.save(vitals);
    encounter.setVitals(vitals);
    
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
    return (Encounter)crit.uniqueResult();
  }
  
  public EncounterType findEncounterTypeById(int id) throws Exception {
    EncounterType et = (EncounterType) this.findById(EncounterType.class, id);
    return et;
  }
  
  public Encounter findEncounterById(int id) throws Exception {
    Encounter encounter = (Encounter) this.findById(Encounter.class, id);
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
  
  public IntakeMedication findIntakeMedicationById(int id) throws Exception {
    IntakeMedication intakeMedication = (IntakeMedication) this.findById(IntakeMedication.class, id);
    return intakeMedication;
  }
  
  public IntakeQuestion findIntakeQuestionById(int id) throws Exception {
    IntakeQuestion intakeQuestion = (IntakeQuestion) this.findById(IntakeQuestion.class, id);
    return intakeQuestion;
  }
  
  public Gender findGenderByCode(String code) throws Exception {
    Session session = getSession();
    Criteria crit = session.createCriteria(Gender.class);
    crit.add(Restrictions.eq("code", code));
    return (Gender)crit.uniqueResult();
  }

}
