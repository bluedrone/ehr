//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.12 at 12:19:07 PM EDT 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CarePlanActivityStatus-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CarePlanActivityStatus-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="not started"/>
 *     &lt;enumeration value="scheduled"/>
 *     &lt;enumeration value="in progress"/>
 *     &lt;enumeration value="on hold"/>
 *     &lt;enumeration value="completed"/>
 *     &lt;enumeration value="cancelled"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CarePlanActivityStatus-list")
@XmlEnum
public enum CarePlanActivityStatusList {


    /**
     * IOTActivity is planned but no action has yet been taken.
     * 
     */
    @XmlEnumValue("not started")
    NOT_STARTED("not started"),

    /**
     * Appointment or other booking has occurred but activity has not yet begun.
     * 
     */
    @XmlEnumValue("scheduled")
    SCHEDULED("scheduled"),

    /**
     * IOTActivity has been started but is not yet complete.
     * 
     */
    @XmlEnumValue("in progress")
    IN_PROGRESS("in progress"),

    /**
     * IOTActivity was started but has temporarily ceased with an expectation of resumption at a future time.
     * 
     */
    @XmlEnumValue("on hold")
    ON_HOLD("on hold"),

    /**
     * The activities have been completed (more or less) as planned.
     * 
     */
    @XmlEnumValue("completed")
    COMPLETED("completed"),

    /**
     * The activities have been ended prior to completion (perhaps even before they were started).
     * 
     */
    @XmlEnumValue("cancelled")
    CANCELLED("cancelled");
    private final java.lang.String value;

    CarePlanActivityStatusList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static CarePlanActivityStatusList fromValue(java.lang.String v) {
        for (CarePlanActivityStatusList c: CarePlanActivityStatusList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
