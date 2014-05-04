package com.wdeanmedical.ehr.persistence;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.wdeanmedical.ehr.entity.BaseEntity;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

public abstract class SiteDAO {

    protected abstract Session getSession();

    protected void createEntity(BaseEntity entity) throws Exception {
      Session session = this.getSession();
      entity.setLastAccessed(new Date());
      entity.setLastUpdated(new Date());
      session.save(entity);
    }

    protected void updateEntity(BaseEntity entity) throws Exception {
      Session session = this.getSession();
      entity.setLastAccessed(new Date());
      entity.setLastUpdated(new Date());
      session.update(entity);
    }

    protected void deleteEntity(BaseEntity entity) throws Exception {
      Session session = this.getSession();
      session.delete(entity);
    }

    protected BaseEntity findById(Class entityClass, Integer id) throws Exception {
      Session session = this.getSession();
      Criteria crit = session.createCriteria(entityClass);
      crit.add(Restrictions.eq("id", id));
      BaseEntity item = (BaseEntity) crit.uniqueResult();
      return item;
    }

    protected List findAll(Class entityClass) throws Exception {
      Session session = this.getSession();
      Criteria crit = session.createCriteria(entityClass);
      List list = crit.list();
      return list;
    }
    
    /**
     * Get a list of Objects that hold the data based on the Query passed.
     * @param sqlQuery
     * @return a list of Objects 
     * @throws Exception
     */
    protected List<Object[]> findAllByQuery(String sqlQuery, int start, int limit) throws Exception {
    	Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(sqlQuery);
        query.setFirstResult(start);
        query.setMaxResults(limit);
        List<Object[]> list = query.list();
        return list;
      }
    /**
     * Get total number of records based on sql passed
     * @param sqlQuery
     * @return
     * @throws Exception
     */
    protected int findTotalByQuery(String sqlQuery) throws Exception {
    	
    	Session session = this.getSession();
    	SQLQuery query = session.createSQLQuery(sqlQuery);
        BigInteger bi = (BigInteger)query.uniqueResult();
        return bi.intValue();
      }
  
    protected int findTotalCount(Class entityClass) throws Exception {
      Session session = this.getSession();
      return ((Long)session.createQuery("select count(*) from " + entityClass.getName()).iterate().next()).intValue();
    }
  
    
}
