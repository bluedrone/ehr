package com.wdeanmedical.ehr.util;

import java.util.ArrayList;
import java.util.List;

import com.wdeanmedical.ehr.entity.dto.CredentialsDTO;
import com.wdeanmedical.ehr.entity.dto.PatientDTO;
import com.wdeanmedical.ehr.entity.dto.PatientMessageDTO;
import com.wdeanmedical.ehr.entity.dto.PatientMessageTypeDTO;
import com.wdeanmedical.ehr.entity.dto.PatientStatusDTO;
import com.wdeanmedical.ehr.entity.PatientMessage;

public class DashboardUtility {

  public static List<PatientMessageDTO> getPatientMessageDTO(List<PatientMessage> patientMessageList) {

    List<PatientMessageDTO> patientMessageDTOList = new ArrayList<PatientMessageDTO>();

    for (PatientMessage patientMessage : patientMessageList) {

      PatientMessageDTO patientMessageDTO = new PatientMessageDTO();
      patientMessageDTO.setSubject(patientMessage.getSubject());
      patientMessageDTO.setDate(patientMessage.getDate());
      patientMessageDTO.setFrom(patientMessage.getFrom());
      patientMessageDTO.setFromClinician(patientMessage.getFromClinician());
      patientMessageDTO.setReadByRecipient(patientMessage.getReadByRecipient());
      patientMessageDTO.setContent(patientMessage.getContent());
      PatientDTO patientDTO = new PatientDTO();
      CredentialsDTO credentialsDTO = new CredentialsDTO();
      if (patientMessage.getPatient() != null && patientMessage.getPatient().getCred() != null) {
        credentialsDTO.setPatientId(patientMessage.getPatient().getCred().getPatientId());
        credentialsDTO.setMrn(patientMessage.getPatient().getCred().getMrn());
        credentialsDTO.setUsername(patientMessage.getPatient().getCred().getUsername());
        credentialsDTO.setPassword(patientMessage.getPatient().getCred().getPassword());
        credentialsDTO.setFirstName(patientMessage.getPatient().getCred().getFirstName());
        credentialsDTO.setMiddleName(patientMessage.getPatient().getCred().getMiddleName());
        credentialsDTO.setLastName(patientMessage.getPatient().getCred().getLastName());
        credentialsDTO.setAdditionalName(patientMessage.getPatient().getCred().getAdditionalName());
        credentialsDTO.setEmail(patientMessage.getPatient().getCred().getEmail());
        credentialsDTO.setSalt(patientMessage.getPatient().getCred().getSalt());
        credentialsDTO.setAuthStatus(patientMessage.getPatient().getCred().getAuthStatus());
        credentialsDTO.setAccessLevel(patientMessage.getPatient().getCred().getAccessLevel());
        credentialsDTO.setSessionId(patientMessage.getPatient().getCred().getSessionId());
        PatientStatusDTO patientStatusDTO = new PatientStatusDTO();
        patientStatusDTO.setName(patientMessage.getPatient().getCred().getStatus().getName());
        credentialsDTO.setStatus(patientStatusDTO);
        credentialsDTO.setGovtId(patientMessage.getPatient().getCred().getGovtId());
        credentialsDTO.setLastLoginTime(patientMessage.getPatient().getCred().getLastLoginTime());
        credentialsDTO.setPreviousLoginTime(patientMessage.getPatient().getCred().getPreviousLoginTime());
        credentialsDTO.setActivationCode(patientMessage.getPatient().getCred().getActivationCode());
        patientDTO.setCred(credentialsDTO);
      }
      patientMessageDTO.setPatientDTO(patientDTO);
      PatientMessageTypeDTO patientMessageTypeDTO = new PatientMessageTypeDTO();
      patientMessageTypeDTO.setName(patientMessage.getPatientMessageType().getName());
      patientMessageDTO.setPatientMessageTypeDTO(patientMessageTypeDTO);
      patientMessageDTOList.add(patientMessageDTO);

    }

    return patientMessageDTOList;

  }

}
