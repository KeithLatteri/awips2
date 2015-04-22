//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.06.17 at 02:02:51 PM EDT 
//


package gov.noaa.nws.ncep.viz.tools.cursor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}ReferenceIndex"/>
 *         &lt;element ref="{}ReferenceName"/>
 *         &lt;element ref="{}CursorName"/>
 *         &lt;element ref="{}CursorColor"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "referenceIndex",
    "referenceName",
    "cursorName",
    "cursorColor"
})
@XmlRootElement(name = "CursorReference")
public class CursorReference {

    @XmlElement(name = "ReferenceIndex")
    protected int referenceIndex;
    @XmlElement(name = "ReferenceName", required = true)
    protected String referenceName;
    @XmlElement(name = "CursorName", required = true)
    protected String cursorName;
    @XmlElement(name = "CursorColor", required = true)
    protected String cursorColor;

    /**
     * Gets the value of the referenceIndex property.
     * 
     */
    public int getReferenceIndex() {
        return referenceIndex;
    }

    /**
     * Sets the value of the referenceIndex property.
     * 
     */
    public void setReferenceIndex(int value) {
        this.referenceIndex = value;
    }

    /**
     * Gets the value of the referenceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceName() {
        return referenceName;
    }

    /**
     * Sets the value of the referenceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceName(String value) {
        this.referenceName = value;
    }

    /**
     * Gets the value of the cursorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCursorName() {
        return cursorName;
    }

    /**
     * Sets the value of the cursorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCursorName(String value) {
        this.cursorName = value;
    }

    /**
     * Gets the value of the cursorColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCursorColor() {
        return cursorColor;
    }

    /**
     * Sets the value of the cursorColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCursorColor(String value) {
        this.cursorColor = value;
    }

}
