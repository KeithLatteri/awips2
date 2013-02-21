//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.01.10 at 10:39:12 AM CST 
//


package org.isotc211._2005.gmx;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import net.opengis.gml._3.GeodeticDatumType;


/**
 * <p>Java class for ML_GeodeticDatum_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ML_GeodeticDatum_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/gml/3.2}GeodeticDatumType">
 *       &lt;sequence>
 *         &lt;element name="alternativeExpression" type="{http://www.isotc211.org/2005/gmx}DatumAlt_PropertyType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ML_GeodeticDatum_Type", propOrder = {
    "alternativeExpression"
})
public class MLGeodeticDatumType
    extends GeodeticDatumType
{

    @XmlElement(required = true)
    protected List<DatumAltPropertyType> alternativeExpression;

    /**
     * Gets the value of the alternativeExpression property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alternativeExpression property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlternativeExpression().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DatumAltPropertyType }
     * 
     * 
     */
    public List<DatumAltPropertyType> getAlternativeExpression() {
        if (alternativeExpression == null) {
            alternativeExpression = new ArrayList<DatumAltPropertyType>();
        }
        return this.alternativeExpression;
    }

}
