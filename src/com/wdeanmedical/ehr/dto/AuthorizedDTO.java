/*
 * WDean Medical is distributed under the
 * GNU Lesser General Public License (GNU LGPL).
 * For details see: http://www.wdeanmedical.com
 * copyright 2013-2014 WDean Medical
 */
 
package com.wdeanmedical.ehr.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "authorizedDTO")
public class AuthorizedDTO extends BooleanResultDTO {

    private String sessionId;
    private boolean authenticated = true;


    public AuthorizedDTO() {
    }

    public String getSessionId() {
      return sessionId;
    }

    public void setSessionId(String sessionId) {
      this.sessionId = sessionId;
    }

    public boolean getAuthenticated() {
      return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
      this.authenticated = authenticated;
    }

}
