/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */

function Clinician() {
  this.id = 0;
  this.firstName = null;
  this.middleName = null;
  this.lastName = null;
  this.active = null;
  this.previousLoginTime = null;
  this.sessionId = 'NO_SESSION';
}

function Patient() {
  this.id = 0;
  this.firstName = undefined;
  this.middleName = undefined;
  this.lastName = undefined;
  this.gender = undefined;
  this.age = undefined;
  this.patientEncounterGroupId = undefined;
}

function Demographics() {
}

function Credentials() {
}

function PatientEncounterGroup() {
  this.id = 0;
  this.name = undefined;
  this.sortOrder = 0;
  this.encounterList = [];
}

function Gender() {
  this.name = undefined;
  this.code = undefined;
}

function Encounter() {
  this.demo = true;
  this.triage = undefined;
  this.patient = undefined;
  this.encounter = undefined;
  this.notes = undefined;
  this.checkIn = undefined;
  this.encounter = undefined;
  this.missing = undefined;
  this.completed = undefined;
  this.consultLocation = undefined;
}
