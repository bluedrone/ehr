/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
 package com.wdeanmedical.ehr.persistence;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.wdeanmedical.ehr.entity.Activity;
import com.wdeanmedical.ehr.entity.ActivityLog;
import com.wdeanmedical.ehr.entity.BaseEntity;
import com.wdeanmedical.ehr.entity.ChiefComplaint;
import com.wdeanmedical.ehr.entity.Credentials;
import com.wdeanmedical.ehr.entity.Demographics;
import com.wdeanmedical.ehr.entity.Encounter;
import com.wdeanmedical.ehr.entity.Exam;
import com.wdeanmedical.ehr.entity.MedicalHistory;
import com.wdeanmedical.ehr.entity.OBGYNEncounterData;
import com.wdeanmedical.ehr.entity.PFSH;
import com.wdeanmedical.ehr.entity.PatientFollowUp;
import com.wdeanmedical.ehr.entity.ProgressNote;
import com.wdeanmedical.ehr.entity.SuppQuestions;
import com.wdeanmedical.ehr.entity.VitalSigns;

@Transactional
public class ActivityLogDAO  extends SiteDAO{
  
      private static final Logger log = Logger.getLogger(ActivityLogDAO.class);
    
      private SessionFactory sessionFactory;

      public ActivityLogDAO() {
        super();
      }

      public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
      }

      @Override
      protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
      }
      
      public void create(ActivityLog activityLog, Integer activityId) throws Exception {
           Session session = this.getSession();
        Activity activity = (Activity)this.findById(Activity.class, activityId);
        activityLog.setActivity(activity);
         session.save(activityLog);       
    }
      //ProgressNote
      public BaseEntity getOldEntity(BaseEntity newEntity) throws Exception{
        BaseEntity oldEntity = null;
        if(newEntity instanceof Demographics){
          oldEntity = this.findById(Demographics.class, newEntity.getId());
      }else if(newEntity instanceof Credentials){
          oldEntity = this.findById(Credentials.class, newEntity.getId());
      }else if(newEntity instanceof VitalSigns){
          oldEntity = this.findById(VitalSigns.class, newEntity.getId());
      }else if(newEntity instanceof ChiefComplaint){
          oldEntity = this.findById(ChiefComplaint.class, newEntity.getId());
      }else if(newEntity instanceof OBGYNEncounterData){
          oldEntity = this.findById(OBGYNEncounterData.class, newEntity.getId());
      }else if(newEntity instanceof PFSH){
          oldEntity = this.findById(PFSH.class, newEntity.getId());
      }else if(newEntity instanceof SuppQuestions){
          oldEntity = this.findById(SuppQuestions.class, newEntity.getId());
      }else if(newEntity instanceof Exam){
          oldEntity = this.findById(Exam.class, newEntity.getId());
      }else if(newEntity instanceof MedicalHistory){
          oldEntity = this.findById(MedicalHistory.class, newEntity.getId());
      }else if(newEntity instanceof Encounter){
          oldEntity = this.findById(Encounter.class, newEntity.getId());
      }else if(newEntity instanceof ProgressNote){
          oldEntity = this.findById(ProgressNote.class, newEntity.getId());
      }
        return oldEntity;
      }

}
