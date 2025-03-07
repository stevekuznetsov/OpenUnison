//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.06.22 at 09:24:21 AM EDT 
//


package com.tremolosecurity.config.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Defines an approval step that must be completed before
 * 				executing sub tasks
 * 
 * <p>Java class for approvalType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="approvalType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.tremolosecurity.com/tremoloConfig}workflowChoiceTaskType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="emailTemplate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="approvers" type="{http://www.tremolosecurity.com/tremoloConfig}azRulesType"/&gt;
 *         &lt;element name="mailAttr" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="failureEmailSubject" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="failureEmailMsg" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="escalationPolicy" type="{http://www.tremolosecurity.com/tremoloConfig}escalationPolicyType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "approvalType", propOrder = {
    "emailTemplate",
    "approvers",
    "mailAttr",
    "failureEmailSubject",
    "failureEmailMsg",
    "escalationPolicy"
})
public class ApprovalType
    extends WorkflowChoiceTaskType
{

    @XmlElement(required = true)
    protected String emailTemplate;
    @XmlElement(required = true)
    protected AzRulesType approvers;
    @XmlElement(required = true)
    protected String mailAttr;
    @XmlElement(required = true)
    protected String failureEmailSubject;
    @XmlElement(required = true)
    protected String failureEmailMsg;
    protected EscalationPolicyType escalationPolicy;
    @XmlAttribute(name = "label")
    protected String label;

    /**
     * Gets the value of the emailTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailTemplate() {
        return emailTemplate;
    }

    /**
     * Sets the value of the emailTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailTemplate(String value) {
        this.emailTemplate = value;
    }

    /**
     * Gets the value of the approvers property.
     * 
     * @return
     *     possible object is
     *     {@link AzRulesType }
     *     
     */
    public AzRulesType getApprovers() {
        return approvers;
    }

    /**
     * Sets the value of the approvers property.
     * 
     * @param value
     *     allowed object is
     *     {@link AzRulesType }
     *     
     */
    public void setApprovers(AzRulesType value) {
        this.approvers = value;
    }

    /**
     * Gets the value of the mailAttr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailAttr() {
        return mailAttr;
    }

    /**
     * Sets the value of the mailAttr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailAttr(String value) {
        this.mailAttr = value;
    }

    /**
     * Gets the value of the failureEmailSubject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailureEmailSubject() {
        return failureEmailSubject;
    }

    /**
     * Sets the value of the failureEmailSubject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailureEmailSubject(String value) {
        this.failureEmailSubject = value;
    }

    /**
     * Gets the value of the failureEmailMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailureEmailMsg() {
        return failureEmailMsg;
    }

    /**
     * Sets the value of the failureEmailMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailureEmailMsg(String value) {
        this.failureEmailMsg = value;
    }

    /**
     * Gets the value of the escalationPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link EscalationPolicyType }
     *     
     */
    public EscalationPolicyType getEscalationPolicy() {
        return escalationPolicy;
    }

    /**
     * Sets the value of the escalationPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link EscalationPolicyType }
     *     
     */
    public void setEscalationPolicy(EscalationPolicyType value) {
        this.escalationPolicy = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

}
