//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.06.22 at 09:24:21 AM EDT 
//


package com.tremolosecurity.config.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Configuration of a chain of authentication mechanisms
 * 			
 * 
 * <p>Java class for authChainType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="authChainType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="compliance" type="{http://www.tremolosecurity.com/tremoloConfig}authLockoutType" minOccurs="0"/&gt;
 *         &lt;element name="authMech" type="{http://www.tremolosecurity.com/tremoloConfig}authMechType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="level" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="finishOnRequiredSucess" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="root" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "authChainType", propOrder = {
    "compliance",
    "authMech"
})
public class AuthChainType {

    protected AuthLockoutType compliance;
    @XmlElement(required = true)
    protected List<AuthMechType> authMech;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "level")
    protected Integer level;
    @XmlAttribute(name = "finishOnRequiredSucess")
    protected Boolean finishOnRequiredSucess;
    @XmlAttribute(name = "root")
    protected String root;

    /**
     * Gets the value of the compliance property.
     * 
     * @return
     *     possible object is
     *     {@link AuthLockoutType }
     *     
     */
    public AuthLockoutType getCompliance() {
        return compliance;
    }

    /**
     * Sets the value of the compliance property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthLockoutType }
     *     
     */
    public void setCompliance(AuthLockoutType value) {
        this.compliance = value;
    }

    /**
     * Gets the value of the authMech property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the authMech property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthMech().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AuthMechType }
     * 
     * 
     */
    public List<AuthMechType> getAuthMech() {
        if (authMech == null) {
            authMech = new ArrayList<AuthMechType>();
        }
        return this.authMech;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLevel(Integer value) {
        this.level = value;
    }

    /**
     * Gets the value of the finishOnRequiredSucess property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isFinishOnRequiredSucess() {
        if (finishOnRequiredSucess == null) {
            return false;
        } else {
            return finishOnRequiredSucess;
        }
    }

    /**
     * Sets the value of the finishOnRequiredSucess property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFinishOnRequiredSucess(Boolean value) {
        this.finishOnRequiredSucess = value;
    }

    /**
     * Gets the value of the root property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoot() {
        return root;
    }

    /**
     * Sets the value of the root property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoot(String value) {
        this.root = value;
    }

}
