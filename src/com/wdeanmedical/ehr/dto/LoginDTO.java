package com.wdeanmedical.ehr.dto;

public class LoginDTO  {
  private String username;
  private String password;
  private String email;


  public LoginDTO() {
  }


  public String getUsername() {
    return this.username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getPassword() {
    return this.password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  
  
  
  

}