/*******************************************************************************
 * Copyright (c) 2021 Tremolo Security, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.31 at 03:16:02 PM EST 
//


package com.tremolosecurity.config.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * List of all applications configured on this Unison
 * 				instance
 * 
 * <p>Java class for applicationsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="applicationsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="errorPage" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}int" default="0" /&gt;
 *                 &lt;attribute name="location" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="dynamicApplications" type="{http://www.tremolosecurity.com/tremoloConfig}dynamicPortalUrlsType" minOccurs="0"/&gt;
 *         &lt;element name="application" type="{http://www.tremolosecurity.com/tremoloConfig}applicationType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="openSessionCookieName" type="{http://www.w3.org/2001/XMLSchema}string" default="unisonOpenSession" /&gt;
 *       &lt;attribute name="openSessionTimeout" type="{http://www.w3.org/2001/XMLSchema}int" default="900" /&gt;
 *       &lt;attribute name="openSessionSecure" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="openSessionHttpOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="hsts" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="hstsTTL" type="{http://www.w3.org/2001/XMLSchema}int" default="0" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "applicationsType", propOrder = {
    "errorPage",
    "dynamicApplications",
    "application"
})
public class ApplicationsType {

    protected List<ApplicationsType.ErrorPage> errorPage;
    protected DynamicPortalUrlsType dynamicApplications;
    protected List<ApplicationType> application;
    @XmlAttribute(name = "openSessionCookieName")
    protected String openSessionCookieName;
    @XmlAttribute(name = "openSessionTimeout")
    protected Integer openSessionTimeout;
    @XmlAttribute(name = "openSessionSecure")
    protected Boolean openSessionSecure;
    @XmlAttribute(name = "openSessionHttpOnly")
    protected Boolean openSessionHttpOnly;
    @XmlAttribute(name = "hsts")
    protected Boolean hsts;
    @XmlAttribute(name = "hstsTTL")
    protected Integer hstsTTL;

    /**
     * Gets the value of the errorPage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errorPage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrorPage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ApplicationsType.ErrorPage }
     * 
     * 
     */
    public List<ApplicationsType.ErrorPage> getErrorPage() {
        if (errorPage == null) {
            errorPage = new ArrayList<ApplicationsType.ErrorPage>();
        }
        return this.errorPage;
    }

    /**
     * Gets the value of the dynamicApplications property.
     * 
     * @return
     *     possible object is
     *     {@link DynamicPortalUrlsType }
     *     
     */
    public DynamicPortalUrlsType getDynamicApplications() {
        return dynamicApplications;
    }

    /**
     * Sets the value of the dynamicApplications property.
     * 
     * @param value
     *     allowed object is
     *     {@link DynamicPortalUrlsType }
     *     
     */
    public void setDynamicApplications(DynamicPortalUrlsType value) {
        this.dynamicApplications = value;
    }

    /**
     * Gets the value of the application property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the application property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApplication().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ApplicationType }
     * 
     * 
     */
    public List<ApplicationType> getApplication() {
        if (application == null) {
            application = new ArrayList<ApplicationType>();
        }
        return this.application;
    }

    /**
     * Gets the value of the openSessionCookieName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenSessionCookieName() {
        if (openSessionCookieName == null) {
            return "unisonOpenSession";
        } else {
            return openSessionCookieName;
        }
    }

    /**
     * Sets the value of the openSessionCookieName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenSessionCookieName(String value) {
        this.openSessionCookieName = value;
    }

    /**
     * Gets the value of the openSessionTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getOpenSessionTimeout() {
        if (openSessionTimeout == null) {
            return  900;
        } else {
            return openSessionTimeout;
        }
    }

    /**
     * Sets the value of the openSessionTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOpenSessionTimeout(Integer value) {
        this.openSessionTimeout = value;
    }

    /**
     * Gets the value of the openSessionSecure property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOpenSessionSecure() {
        if (openSessionSecure == null) {
            return false;
        } else {
            return openSessionSecure;
        }
    }

    /**
     * Sets the value of the openSessionSecure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOpenSessionSecure(Boolean value) {
        this.openSessionSecure = value;
    }

    /**
     * Gets the value of the openSessionHttpOnly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOpenSessionHttpOnly() {
        if (openSessionHttpOnly == null) {
            return false;
        } else {
            return openSessionHttpOnly;
        }
    }

    /**
     * Sets the value of the openSessionHttpOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOpenSessionHttpOnly(Boolean value) {
        this.openSessionHttpOnly = value;
    }

    /**
     * Gets the value of the hsts property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isHsts() {
        if (hsts == null) {
            return false;
        } else {
            return hsts;
        }
    }

    /**
     * Sets the value of the hsts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHsts(Boolean value) {
        this.hsts = value;
    }

    /**
     * Gets the value of the hstsTTL property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getHstsTTL() {
        if (hstsTTL == null) {
            return  0;
        } else {
            return hstsTTL;
        }
    }

    /**
     * Sets the value of the hstsTTL property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHstsTTL(Integer value) {
        this.hstsTTL = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}int" default="0" /&gt;
     *       &lt;attribute name="location" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class ErrorPage {

        @XmlAttribute(name = "code")
        protected Integer code;
        @XmlAttribute(name = "location")
        protected String location;

        /**
         * Gets the value of the code property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public int getCode() {
            if (code == null) {
                return  0;
            } else {
                return code;
            }
        }

        /**
         * Sets the value of the code property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCode(Integer value) {
            this.code = value;
        }

        /**
         * Gets the value of the location property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLocation() {
            return location;
        }

        /**
         * Sets the value of the location property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLocation(String value) {
            this.location = value;
        }

    }

}
