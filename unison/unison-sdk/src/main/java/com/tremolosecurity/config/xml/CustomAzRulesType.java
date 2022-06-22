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
import javax.xml.bind.annotation.XmlType;


/**
 * List of custom authorization implementations
 * 			
 * 
 * <p>Java class for customAzRulesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customAzRulesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dynamicCustomAuthorizations" type="{http://www.tremolosecurity.com/tremoloConfig}dynamicPortalUrlsType" minOccurs="0"/&gt;
 *         &lt;element name="azRule" type="{http://www.tremolosecurity.com/tremoloConfig}customAzRuleType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customAzRulesType", propOrder = {
    "dynamicCustomAuthorizations",
    "azRule"
})
public class CustomAzRulesType {

    protected DynamicPortalUrlsType dynamicCustomAuthorizations;
    protected List<CustomAzRuleType> azRule;

    /**
     * Gets the value of the dynamicCustomAuthorizations property.
     * 
     * @return
     *     possible object is
     *     {@link DynamicPortalUrlsType }
     *     
     */
    public DynamicPortalUrlsType getDynamicCustomAuthorizations() {
        return dynamicCustomAuthorizations;
    }

    /**
     * Sets the value of the dynamicCustomAuthorizations property.
     * 
     * @param value
     *     allowed object is
     *     {@link DynamicPortalUrlsType }
     *     
     */
    public void setDynamicCustomAuthorizations(DynamicPortalUrlsType value) {
        this.dynamicCustomAuthorizations = value;
    }

    /**
     * Gets the value of the azRule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the azRule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAzRule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomAzRuleType }
     * 
     * 
     */
    public List<CustomAzRuleType> getAzRule() {
        if (azRule == null) {
            azRule = new ArrayList<CustomAzRuleType>();
        }
        return this.azRule;
    }

}
