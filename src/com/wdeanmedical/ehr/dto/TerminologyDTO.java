package com.wdeanmedical.ehr.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wdeanmedical.ehr.entity.Appointment;
import com.wdeanmedical.ehr.entity.CPT;
import com.wdeanmedical.ehr.entity.Clinician;
import com.wdeanmedical.ehr.entity.ICD10;
import com.wdeanmedical.ehr.entity.PatientMessage;

public class TerminologyDTO extends AuthorizedDTO {
  private String searchText;
  private List<ICD10> icd10List;
  private List<CPT> cptList;

  public TerminologyDTO() {
  }
  
  public String getSearchText() { return searchText; }
  public void setSearchText(String searchText) { this.searchText = searchText; }

  public List<ICD10> getIcd10List() { return icd10List; }
  public void setIcd10List(List<ICD10> icd10List) { this.icd10List = icd10List; }

  public List<CPT> getCptList() { return cptList; }
  public void setCptList(List<CPT> cptList) { this.cptList = cptList; }
  
}
