package com.wdeanmedical.ehr.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wdeanmedical.ehr.entity.dto.ClinicianScheduleDTO;
import com.wdeanmedical.ehr.entity.dto.CredentialsDTO;
import com.wdeanmedical.ehr.entity.dto.DemographicsDTO;
import com.wdeanmedical.ehr.entity.dto.GenderDTO;
import com.wdeanmedical.ehr.entity.dto.LabReviewDTO;
import com.wdeanmedical.ehr.entity.dto.PatientDTO;
import com.wdeanmedical.ehr.entity.dto.PatientMessageDTO;
import com.wdeanmedical.ehr.entity.dto.PatientMessageTypeDTO;
import com.wdeanmedical.ehr.entity.dto.PatientStatusDTO;
import com.wdeanmedical.ehr.entity.dto.ProgressNoteDTO;
import com.wdeanmedical.ehr.entity.dto.ToDoNoteDTO;
import com.wdeanmedical.ehr.entity.ClinicianSchedule;
import com.wdeanmedical.ehr.entity.LabReview;
import com.wdeanmedical.ehr.entity.PatientMessage;
import com.wdeanmedical.ehr.entity.ProgressNote;
import com.wdeanmedical.ehr.entity.ToDoNote;

public class DashboardUtility {

  public static List<PatientMessageDTO> getPatientMessageDTOList(List<PatientMessage> patientMessageList) {

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
        credentialsDTO.setFirstName(patientMessage.getPatient().getCred().getFirstName());
        credentialsDTO.setMiddleName(patientMessage.getPatient().getCred().getMiddleName());
        credentialsDTO.setLastName(patientMessage.getPatient().getCred().getLastName());
        credentialsDTO.setAdditionalName(patientMessage.getPatient().getCred().getAdditionalName());
      }
      patientDTO.setCred(credentialsDTO);
      patientMessageDTO.setPatientDTO(patientDTO);
      PatientMessageTypeDTO patientMessageTypeDTO = new PatientMessageTypeDTO();
      patientMessageTypeDTO.setName(patientMessage.getPatientMessageType().getName());
      patientMessageDTO.setPatientMessageTypeDTO(patientMessageTypeDTO);
      patientMessageDTOList.add(patientMessageDTO);

    }

    return patientMessageDTOList;

  }

  public static List<ProgressNoteDTO> getProgressNoteDTOList(List<ProgressNote> progressNoteList) {

    List<ProgressNoteDTO> progressNoteDTOList = new ArrayList<ProgressNoteDTO>();

    for (ProgressNote progressNote : progressNoteList) {

      ProgressNoteDTO progressNoteDTO = new ProgressNoteDTO();
      progressNoteDTO.setDate(progressNote.getDate());
      progressNoteDTO.setSubject(progressNote.getSubject());
      progressNoteDTO.setContent(progressNote.getContent());
      progressNoteDTO.setCompleted(progressNote.getCompleted());
      PatientDTO patientDTO = new PatientDTO();
      CredentialsDTO credentialsDTO = new CredentialsDTO();
      if (progressNote.getPatient() != null && progressNote.getPatient().getCred() != null) {
        credentialsDTO.setPatientId(progressNote.getPatient().getCred().getPatientId());
        credentialsDTO.setMrn(progressNote.getPatient().getCred().getMrn());
        credentialsDTO.setFirstName(progressNote.getPatient().getCred().getFirstName());
        credentialsDTO.setMiddleName(progressNote.getPatient().getCred().getMiddleName());
        credentialsDTO.setLastName(progressNote.getPatient().getCred().getLastName());
        credentialsDTO.setAdditionalName(progressNote.getPatient().getCred().getAdditionalName());
      }
      patientDTO.setCred(credentialsDTO);
      progressNoteDTO.setPatient(patientDTO);
      progressNoteDTOList.add(progressNoteDTO);

    }

    return progressNoteDTOList;

  }

  public static List<ToDoNoteDTO> getToDoNoteDTOList(List<ToDoNote> toDoNoteList) {

    List<ToDoNoteDTO> toDoNoteDTOList = new ArrayList<ToDoNoteDTO>();

    for (ToDoNote toDoNote : toDoNoteList) {
      ToDoNoteDTO toDoNoteDTO = new ToDoNoteDTO();
      toDoNoteDTO.setDate(toDoNote.getDate());
      toDoNoteDTO.setSubject(toDoNote.getSubject());
      toDoNoteDTO.setContent(toDoNote.getContent());
      PatientDTO patientDTO = new PatientDTO();
      CredentialsDTO credentialsDTO = new CredentialsDTO();
      if (toDoNote.getPatient() != null && toDoNote.getPatient().getCred() != null) {
        credentialsDTO.setPatientId(toDoNote.getPatient().getCred().getPatientId());
        credentialsDTO.setMrn(toDoNote.getPatient().getCred().getMrn());
        credentialsDTO.setFirstName(toDoNote.getPatient().getCred().getFirstName());
        credentialsDTO.setMiddleName(toDoNote.getPatient().getCred().getMiddleName());
        credentialsDTO.setLastName(toDoNote.getPatient().getCred().getLastName());
        credentialsDTO.setAdditionalName(toDoNote.getPatient().getCred().getAdditionalName());
      }
      patientDTO.setCred(credentialsDTO);
      toDoNoteDTO.setPatient(patientDTO);
      toDoNoteDTOList.add(toDoNoteDTO);
    }

    return toDoNoteDTOList;

  }
  
  public static List<LabReviewDTO> getLabReviewDTOList(List<LabReview> labReviewList) {

    List<LabReviewDTO> labReviewDTOList = new ArrayList<LabReviewDTO>();

    for (LabReview labReview : labReviewList) {
      LabReviewDTO labReviewDTO = new LabReviewDTO();
      labReviewDTO.setDate(labReview.getDate());
      labReviewDTO.setName(labReview.getName());
      labReviewDTO.setValue(labReview.getValue());
      PatientDTO patientDTO = new PatientDTO();
      CredentialsDTO credentialsDTO = new CredentialsDTO();
      if (labReview.getPatient() != null && labReview.getPatient().getCred() != null) {
        credentialsDTO.setPatientId(labReview.getPatient().getCred().getPatientId());
        credentialsDTO.setMrn(labReview.getPatient().getCred().getMrn());
        credentialsDTO.setFirstName(labReview.getPatient().getCred().getFirstName());
        credentialsDTO.setMiddleName(labReview.getPatient().getCred().getMiddleName());
        credentialsDTO.setLastName(labReview.getPatient().getCred().getLastName());
        credentialsDTO.setAdditionalName(labReview.getPatient().getCred().getAdditionalName());
      }     
      patientDTO.setCred(credentialsDTO);
      labReviewDTO.setPatient(patientDTO);
      labReviewDTOList.add(labReviewDTO);
    }

    return labReviewDTOList;

  }
  
  public static List<ClinicianScheduleDTO> getClinicianScheduleDTOList(List<ClinicianSchedule> clinicianScheduleList) {

    List<ClinicianScheduleDTO> clinicianScheduleDTOList = new ArrayList<ClinicianScheduleDTO>();

    for (ClinicianSchedule clinicianSchedule : clinicianScheduleList) {
      ClinicianScheduleDTO clinicianScheduleDTO = new ClinicianScheduleDTO();
      clinicianScheduleDTO.setDate(clinicianSchedule.getDate());
      clinicianScheduleDTO.setLength(clinicianSchedule.getLength());
      clinicianScheduleDTO.setAge(clinicianSchedule.getAge());     
      clinicianScheduleDTO.setReason(clinicianSchedule.getReason());
      clinicianScheduleDTO.setComments(clinicianSchedule.getComments());
      clinicianScheduleDTO.setStatus(clinicianSchedule.getStatus());
      clinicianScheduleDTO.setPatientLocation(clinicianSchedule.getPatientLocation());
      clinicianScheduleDTO.setRoom(clinicianSchedule.getRoom());
      clinicianScheduleDTO.setCheckedIn(clinicianSchedule.isCheckedIn());
      clinicianScheduleDTO.setWaitTime(clinicianSchedule.getWaitTime());
      clinicianScheduleDTO.setProgressNoteStatus(clinicianSchedule.getProgressNoteStatus());
      PatientDTO patientDTO = new PatientDTO();
      CredentialsDTO credentialsDTO = new CredentialsDTO();
      DemographicsDTO demographicsDTO = new DemographicsDTO();
      if (clinicianSchedule.getPatient() != null) {
        if(clinicianSchedule.getPatient().getCred() != null){
          credentialsDTO.setPatientId(clinicianSchedule.getPatient().getCred().getPatientId());
          credentialsDTO.setMrn(clinicianSchedule.getPatient().getCred().getMrn());
          credentialsDTO.setFirstName(clinicianSchedule.getPatient().getCred().getFirstName());
          credentialsDTO.setMiddleName(clinicianSchedule.getPatient().getCred().getMiddleName());
          credentialsDTO.setLastName(clinicianSchedule.getPatient().getCred().getLastName());
          credentialsDTO.setAdditionalName(clinicianSchedule.getPatient().getCred().getAdditionalName());
        }
        if (clinicianSchedule.getPatient().getDemo() != null) {
          GenderDTO genderDTO = new GenderDTO();
          genderDTO.setCode(clinicianSchedule.getPatient().getDemo().getGender().getCode());
          genderDTO.setName(clinicianSchedule.getPatient().getDemo().getGender().getName());
          demographicsDTO.setGender(genderDTO);
        }
      }      
      patientDTO.setDemo(demographicsDTO);
      patientDTO.setCred(credentialsDTO);
      clinicianScheduleDTO.setPatient(patientDTO);
      clinicianScheduleDTOList.add(clinicianScheduleDTO);
    }

    return clinicianScheduleDTOList;

  }

}
