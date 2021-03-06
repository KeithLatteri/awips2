/**
* This software was developed and / or modified by Raytheon Company,
* pursuant to Contract DG133W-05-CQ-1067 with the US Government.
* 
* U.S. EXPORT CONTROLLED TECHNICAL DATA
* This software product contains export-restricted data whose
* export/transfer/disclosure is restricted by U.S. law. Dissemination
* to non-U.S. persons whether in the United States or abroad requires
* an export license or other authorization.
* 
* Contractor Name:        Raytheon Company
* Contractor Address:     6825 Pine Street, Suite 340
*                         Mail Stop B8
*                         Omaha, NE 68106
*                         402.291.0100
* 
* See the AWIPS II Master Rights File ("Master Rights File.pdf") for
* further licensing information.
**/
package com.raytheon.uf.common.dataplugin.shef.tables;
// default package
// Generated Oct 17, 2008 2:22:17 PM by Hibernate Tools 3.2.2.GA

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Rwradarresult generated by hbm2java
 * 
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Oct 17, 2008                        Initial generation by hbm2java
 * Aug 19, 2011      10672     jkorman Move refactor to new project
 * Oct 07, 2013       2361     njensen Removed XML annotations
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.1
 */
@Entity
@Table(name = "rwradarresult")
@com.raytheon.uf.common.serialization.annotations.DynamicSerialize
public class Rwradarresult extends com.raytheon.uf.common.dataplugin.persist.PersistableDataObject implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private RwradarresultId id;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Short numGages;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String radAvail;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double rwBiasValUsed;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double memSpanUsed;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String editBias;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String ignoreRadar;

    public Rwradarresult() {
    }

    public Rwradarresult(RwradarresultId id) {
        this.id = id;
    }

    public Rwradarresult(RwradarresultId id, Short numGages, String radAvail,
            Double rwBiasValUsed, Double memSpanUsed, String editBias,
            String ignoreRadar) {
        this.id = id;
        this.numGages = numGages;
        this.radAvail = radAvail;
        this.rwBiasValUsed = rwBiasValUsed;
        this.memSpanUsed = memSpanUsed;
        this.editBias = editBias;
        this.ignoreRadar = ignoreRadar;
    }

    @EmbeddedId
    @AttributeOverrides( {
            @AttributeOverride(name = "radid", column = @Column(name = "radid", nullable = false, length = 3)),
            @AttributeOverride(name = "obstime", column = @Column(name = "obstime", nullable = false, length = 29)) })
    public RwradarresultId getId() {
        return this.id;
    }

    public void setId(RwradarresultId id) {
        this.id = id;
    }

    @Column(name = "num_gages")
    public Short getNumGages() {
        return this.numGages;
    }

    public void setNumGages(Short numGages) {
        this.numGages = numGages;
    }

    @Column(name = "rad_avail", length = 1)
    public String getRadAvail() {
        return this.radAvail;
    }

    public void setRadAvail(String radAvail) {
        this.radAvail = radAvail;
    }

    @Column(name = "rw_bias_val_used", precision = 17, scale = 17)
    public Double getRwBiasValUsed() {
        return this.rwBiasValUsed;
    }

    public void setRwBiasValUsed(Double rwBiasValUsed) {
        this.rwBiasValUsed = rwBiasValUsed;
    }

    @Column(name = "mem_span_used", precision = 17, scale = 17)
    public Double getMemSpanUsed() {
        return this.memSpanUsed;
    }

    public void setMemSpanUsed(Double memSpanUsed) {
        this.memSpanUsed = memSpanUsed;
    }

    @Column(name = "edit_bias", length = 1)
    public String getEditBias() {
        return this.editBias;
    }

    public void setEditBias(String editBias) {
        this.editBias = editBias;
    }

    @Column(name = "ignore_radar", length = 1)
    public String getIgnoreRadar() {
        return this.ignoreRadar;
    }

    public void setIgnoreRadar(String ignoreRadar) {
        this.ignoreRadar = ignoreRadar;
    }

}
