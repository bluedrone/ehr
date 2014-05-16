/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "encounter_question")
public class EncounterQuestion extends BaseEntity implements Serializable {

  private static final long serialVersionUID = -5235097606726399045L;
  private String question = "&nbsp;";
  private String response = "&nbsp;";
  private int encounterId;

  public EncounterQuestion() {
  }

  @Column(name = "question")
  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  @Column(name = "response")
  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  @Column(name = "encounter_id")
  public int getEncounterId() {
    return encounterId;
  }

  public void setEncounterId(int encounterId) {
    this.encounterId = encounterId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + encounterId;
    result = prime * result
        + ((question == null) ? 0 : question.hashCode());
    result = prime * result
        + ((response == null) ? 0 : response.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      {return false;}
    if (getClass() != obj.getClass())
      {return false;}
    EncounterQuestion other = (EncounterQuestion) obj;
    if (encounterId != other.encounterId)
      {return false;}
    if (question == null) {
      if (other.question != null)
        {return false;}
    } else if (!question.equals(other.question))
      {return false;}
    if (response == null) {
      if (other.response != null)
        {return false;}
    } else if (!response.equals(other.response))
      {return false;}
    return true;
  }

  @Override
  public String toString() {
    return "EncounterQuestion [question=" + question + ", response="
        + response + ", encounterId=" + encounterId + "]";
  }

}
