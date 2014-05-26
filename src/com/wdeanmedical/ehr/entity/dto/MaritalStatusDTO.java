package com.wdeanmedical.ehr.entity.dto;

public class MaritalStatusDTO {

  private String name;
  private String code;

  public MaritalStatusDTO() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return "MaritalStatusDTO [name=" + name + ", code=" + code + "]";
  }

}
