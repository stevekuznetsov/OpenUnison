/*******************************************************************************
 * Copyright 2018 Tremolo Security, Inc.
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
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.02 at 08:54:26 AM EST 
//


package com.tremolosecurity.config.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Determines if the workflow should reload the Unison user
 * 				object after the workflow is executed. This is useful
 * 				for Unison policies that rely on the outcome of the
 * 				workflow
 * 			
 * 
 * <p>Java class for resyncType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resyncType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tremolosecurity.com/tremoloConfig}workflowTaskType">
 *       &lt;attribute name="keepExternalAttrs" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="changeRoot" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="newRoot" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resyncType")
public class ResyncType
    extends WorkflowTaskType
{

    @XmlAttribute(name = "keepExternalAttrs")
    protected Boolean keepExternalAttrs;
    @XmlAttribute(name = "changeRoot")
    protected Boolean changeRoot;
    @XmlAttribute(name = "newRoot")
    protected String newRoot;

    /**
     * Gets the value of the keepExternalAttrs property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isKeepExternalAttrs() {
        return keepExternalAttrs;
    }

    /**
     * Sets the value of the keepExternalAttrs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setKeepExternalAttrs(Boolean value) {
        this.keepExternalAttrs = value;
    }

    /**
     * Gets the value of the changeRoot property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isChangeRoot() {
        if (changeRoot == null) {
            return false;
        } else {
            return changeRoot;
        }
    }

    /**
     * Sets the value of the changeRoot property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setChangeRoot(Boolean value) {
        this.changeRoot = value;
    }

    /**
     * Gets the value of the newRoot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewRoot() {
        return newRoot;
    }

    /**
     * Sets the value of the newRoot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewRoot(String value) {
        this.newRoot = value;
    }

}
