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
import com.wdeanmedical.ehr.entity.ActivityLog;
import com.wdeanmedical.ehr.entity.Appointment;
import com.wdeanmedical.ehr.entity.BaseEntity;
import com.wdeanmedical.ehr.entity.ChiefComplaint;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.ClinicianSchedule;
import com.wdeanmedical.ehr.entity.ClinicianSession;
import com.wdeanmedical.ehr.entity.Credential;
import com.wdeanmedical.ehr.entity.Demographics;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.EncounterType;
import com.wdeanmedical.ehr.entity.Exam;
import com.wdeanmedical.ehr.entity.Gender;
import com.wdeanmedical.ehr.entity.EncounterMedication;
import com.wdeanmedical.ehr.entity.EncounterQuestion;
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
import com.wdeanmedical.ehr.entity.Role;
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
  
  public Clinician findClinicianBySessionId(String sessionId ) throws Exception {
    Session session = this.getSession();
    Criteria crit = session.createCriteria(ClinicianSession.class);
    crit.add(Restrictions.eq("sessionId", sessionId));
    ClinicianSession clinicianSession = (ClinicianSession) crit.uniqueResult();
    return (Clinician) this.findById(Clinician.class, clinicianSession.getClinician().getId());
  }

  public List<ActivityLog> getActivityLog(Integer clinicianId) {
    Session session = this.getSession();
    Query activityLogQuery = session.createQuery("SELECT al FROM ActivityLog al WHERE al.clinicianId = '" + clinicianId + "' ORDER BY al.createdDate DESC");
    activityLogQuery.setMaxResults(200);
    return activityLogQuery.list();
  }

}
