/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.dto;
 
public class DTO {

  private int total;
  private int size;
  private int start;
  private String errorMsg;
  private boolean success;
  private int returnCode;
  
  

  public DTO() {
  }

  public int getTotal() { return total; }
  public void setTotal(int total) { this.total = total; }

  public int getSize() { return size; }
  public void setSize(int size) { this.size = size; }

  public int getStart() { return start; }
  public void setStart(int start) { this.start = start; }
  
  public String getErrorMsg() { return errorMsg; }
  public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }

  public boolean isSuccess() { return success; }
  public void setSuccess(boolean success) { this.success = success; }

  public int getReturnCode() { return returnCode; }
  public void setReturnCode(int returnCode) { this.returnCode = returnCode; }
  
  
  
}