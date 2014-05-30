package com.wdeanmedical.external.fhir;

public class Enums {
  
  public enum IdentifierUse {
    usual, official, temp, secondary
  }
  public enum Nameuse {
    usual, official, temp, nickname, anonymous, old, maiden
  }
  public enum AddressUse {
    home, work, temp, old
  }
  public enum ContactSystem {
    phone, fax, email, url
  }
  public enum ContactUse {
    home, work, temp, old, mobile
  }
  public enum AdministrativeGender {
    Female("F"), Male("M"), Undifferentiated("Un"), unknown("UNK");
    private String value;
    private AdministrativeGender(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }
  }
  public enum MaritalStatus {
    Annulled("A"), Divorced("D"), Interlocutory("I"), LegallySeparated("L"), 
    Married("M"), Polygamous("P"), NeverMarried("S"), DomesticPartner("T"), 
    Widowed("W"), unknown("UNK");
    private String value;
    private MaritalStatus(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }
  }
  public enum PatientContactRelationship {
    Emergency("emergency"), Family("family"), Guardian("guardian"), Friend("friend"), 
    Partner("partner"), Work("work"), Caregiver("caregiver"), Agent("agent"), 
    Guarantor("guarantor"), Ownerofanimal("owner"), Parent("parent");
    private String value;
    private PatientContactRelationship(String value) {
      this.value = value;
    }
    public String getValue() {
      return value;
    }
  }

}
