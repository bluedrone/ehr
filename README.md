# WDean Medical EHR
# version 0.8.10  31-JUL-2014

Improvements:
Updated FHIR Interface:

Method List:

POST /ext/{json/xml}/auth
Authorize a service client by credentials submitted via POST.

GET /ext/{json/xml}/getPatient/{patientMRN}
Get a patient by ID

GET /ext/{json/xml}/getPatients
Get all patients

GET /ext/{json/xml}/getPatientFullRecord/{patientMRN}
Get the patient's full record, including all encounters

POST /ext/{json/xml}/updatePatient/{patientMRN}
Update a patient by ID with FHIR formatted patient data in POST header.

POST /ext/{json/xml}/importPatients
Import patients from FHIR formatted patient data in POST header.

Bug Fixes:


wdeanmedical.com/ehr