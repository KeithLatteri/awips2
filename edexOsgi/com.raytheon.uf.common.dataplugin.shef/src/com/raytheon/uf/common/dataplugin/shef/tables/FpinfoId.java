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

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FpinfoId generated by hbm2java
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
@Embeddable
@com.raytheon.uf.common.serialization.annotations.DynamicSerialize
public class FpinfoId extends com.raytheon.uf.common.dataplugin.persist.PersistableDataObject implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String lid;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String name;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String county;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String state;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String hsa;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String primaryBack;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String secondaryBack;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String stream;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double bf;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double wstg;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double fs;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double fq;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double actionFlow;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String pe;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String useLatestFcst;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String proximity;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String reach;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String groupId;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Integer ordinal;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double chgThreshold;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private String recType;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Integer backhrs;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Integer forwardhrs;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double adjustendhrs;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double minorStage;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double moderateStage;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double majorStage;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double minorFlow;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double moderateFlow;

    @com.raytheon.uf.common.serialization.annotations.DynamicSerializeElement
    private Double majorFlow;

    public FpinfoId() {
    }

    public FpinfoId(String lid, String name, String county, String state,
            String hsa, String primaryBack, String secondaryBack,
            String stream, Double bf, Double wstg, Double fs, Double fq,
            Double actionFlow, String pe, String useLatestFcst,
            String proximity, String reach, String groupId, Integer ordinal,
            Double chgThreshold, String recType, Integer backhrs,
            Integer forwardhrs, Double adjustendhrs, Double minorStage,
            Double moderateStage, Double majorStage, Double minorFlow,
            Double moderateFlow, Double majorFlow) {
        this.lid = lid;
        this.name = name;
        this.county = county;
        this.state = state;
        this.hsa = hsa;
        this.primaryBack = primaryBack;
        this.secondaryBack = secondaryBack;
        this.stream = stream;
        this.bf = bf;
        this.wstg = wstg;
        this.fs = fs;
        this.fq = fq;
        this.actionFlow = actionFlow;
        this.pe = pe;
        this.useLatestFcst = useLatestFcst;
        this.proximity = proximity;
        this.reach = reach;
        this.groupId = groupId;
        this.ordinal = ordinal;
        this.chgThreshold = chgThreshold;
        this.recType = recType;
        this.backhrs = backhrs;
        this.forwardhrs = forwardhrs;
        this.adjustendhrs = adjustendhrs;
        this.minorStage = minorStage;
        this.moderateStage = moderateStage;
        this.majorStage = majorStage;
        this.minorFlow = minorFlow;
        this.moderateFlow = moderateFlow;
        this.majorFlow = majorFlow;
    }

    @Column(name = "lid", length = 8)
    public String getLid() {
        return this.lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    @Column(name = "name", length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "county", length = 20)
    public String getCounty() {
        return this.county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Column(name = "state", length = 2)
    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "hsa", length = 3)
    public String getHsa() {
        return this.hsa;
    }

    public void setHsa(String hsa) {
        this.hsa = hsa;
    }

    @Column(name = "primary_back", length = 3)
    public String getPrimaryBack() {
        return this.primaryBack;
    }

    public void setPrimaryBack(String primaryBack) {
        this.primaryBack = primaryBack;
    }

    @Column(name = "secondary_back", length = 3)
    public String getSecondaryBack() {
        return this.secondaryBack;
    }

    public void setSecondaryBack(String secondaryBack) {
        this.secondaryBack = secondaryBack;
    }

    @Column(name = "stream", length = 32)
    public String getStream() {
        return this.stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    @Column(name = "bf", precision = 17, scale = 17)
    public Double getBf() {
        return this.bf;
    }

    public void setBf(Double bf) {
        this.bf = bf;
    }

    @Column(name = "wstg", precision = 17, scale = 17)
    public Double getWstg() {
        return this.wstg;
    }

    public void setWstg(Double wstg) {
        this.wstg = wstg;
    }

    @Column(name = "fs", precision = 17, scale = 17)
    public Double getFs() {
        return this.fs;
    }

    public void setFs(Double fs) {
        this.fs = fs;
    }

    @Column(name = "fq", precision = 17, scale = 17)
    public Double getFq() {
        return this.fq;
    }

    public void setFq(Double fq) {
        this.fq = fq;
    }

    @Column(name = "action_flow", precision = 17, scale = 17)
    public Double getActionFlow() {
        return this.actionFlow;
    }

    public void setActionFlow(Double actionFlow) {
        this.actionFlow = actionFlow;
    }

    @Column(name = "pe", length = 2)
    public String getPe() {
        return this.pe;
    }

    public void setPe(String pe) {
        this.pe = pe;
    }

    @Column(name = "use_latest_fcst", length = 1)
    public String getUseLatestFcst() {
        return this.useLatestFcst;
    }

    public void setUseLatestFcst(String useLatestFcst) {
        this.useLatestFcst = useLatestFcst;
    }

    @Column(name = "proximity", length = 6)
    public String getProximity() {
        return this.proximity;
    }

    public void setProximity(String proximity) {
        this.proximity = proximity;
    }

    @Column(name = "reach", length = 80)
    public String getReach() {
        return this.reach;
    }

    public void setReach(String reach) {
        this.reach = reach;
    }

    @Column(name = "group_id", length = 8)
    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Column(name = "ordinal")
    public Integer getOrdinal() {
        return this.ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Column(name = "chg_threshold", precision = 17, scale = 17)
    public Double getChgThreshold() {
        return this.chgThreshold;
    }

    public void setChgThreshold(Double chgThreshold) {
        this.chgThreshold = chgThreshold;
    }

    @Column(name = "rec_type", length = 3)
    public String getRecType() {
        return this.recType;
    }

    public void setRecType(String recType) {
        this.recType = recType;
    }

    @Column(name = "backhrs")
    public Integer getBackhrs() {
        return this.backhrs;
    }

    public void setBackhrs(Integer backhrs) {
        this.backhrs = backhrs;
    }

    @Column(name = "forwardhrs")
    public Integer getForwardhrs() {
        return this.forwardhrs;
    }

    public void setForwardhrs(Integer forwardhrs) {
        this.forwardhrs = forwardhrs;
    }

    @Column(name = "adjustendhrs", precision = 17, scale = 17)
    public Double getAdjustendhrs() {
        return this.adjustendhrs;
    }

    public void setAdjustendhrs(Double adjustendhrs) {
        this.adjustendhrs = adjustendhrs;
    }

    @Column(name = "minor_stage", precision = 17, scale = 17)
    public Double getMinorStage() {
        return this.minorStage;
    }

    public void setMinorStage(Double minorStage) {
        this.minorStage = minorStage;
    }

    @Column(name = "moderate_stage", precision = 17, scale = 17)
    public Double getModerateStage() {
        return this.moderateStage;
    }

    public void setModerateStage(Double moderateStage) {
        this.moderateStage = moderateStage;
    }

    @Column(name = "major_stage", precision = 17, scale = 17)
    public Double getMajorStage() {
        return this.majorStage;
    }

    public void setMajorStage(Double majorStage) {
        this.majorStage = majorStage;
    }

    @Column(name = "minor_flow", precision = 17, scale = 17)
    public Double getMinorFlow() {
        return this.minorFlow;
    }

    public void setMinorFlow(Double minorFlow) {
        this.minorFlow = minorFlow;
    }

    @Column(name = "moderate_flow", precision = 17, scale = 17)
    public Double getModerateFlow() {
        return this.moderateFlow;
    }

    public void setModerateFlow(Double moderateFlow) {
        this.moderateFlow = moderateFlow;
    }

    @Column(name = "major_flow", precision = 17, scale = 17)
    public Double getMajorFlow() {
        return this.majorFlow;
    }

    public void setMajorFlow(Double majorFlow) {
        this.majorFlow = majorFlow;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof FpinfoId))
            return false;
        FpinfoId castOther = (FpinfoId) other;

        return ((this.getLid() == castOther.getLid()) || (this.getLid() != null
                && castOther.getLid() != null && this.getLid().equals(
                castOther.getLid())))
                && ((this.getName() == castOther.getName()) || (this.getName() != null
                        && castOther.getName() != null && this.getName()
                        .equals(castOther.getName())))
                && ((this.getCounty() == castOther.getCounty()) || (this
                        .getCounty() != null
                        && castOther.getCounty() != null && this.getCounty()
                        .equals(castOther.getCounty())))
                && ((this.getState() == castOther.getState()) || (this
                        .getState() != null
                        && castOther.getState() != null && this.getState()
                        .equals(castOther.getState())))
                && ((this.getHsa() == castOther.getHsa()) || (this.getHsa() != null
                        && castOther.getHsa() != null && this.getHsa().equals(
                        castOther.getHsa())))
                && ((this.getPrimaryBack() == castOther.getPrimaryBack()) || (this
                        .getPrimaryBack() != null
                        && castOther.getPrimaryBack() != null && this
                        .getPrimaryBack().equals(castOther.getPrimaryBack())))
                && ((this.getSecondaryBack() == castOther.getSecondaryBack()) || (this
                        .getSecondaryBack() != null
                        && castOther.getSecondaryBack() != null && this
                        .getSecondaryBack()
                        .equals(castOther.getSecondaryBack())))
                && ((this.getStream() == castOther.getStream()) || (this
                        .getStream() != null
                        && castOther.getStream() != null && this.getStream()
                        .equals(castOther.getStream())))
                && ((this.getBf() == castOther.getBf()) || (this.getBf() != null
                        && castOther.getBf() != null && this.getBf().equals(
                        castOther.getBf())))
                && ((this.getWstg() == castOther.getWstg()) || (this.getWstg() != null
                        && castOther.getWstg() != null && this.getWstg()
                        .equals(castOther.getWstg())))
                && ((this.getFs() == castOther.getFs()) || (this.getFs() != null
                        && castOther.getFs() != null && this.getFs().equals(
                        castOther.getFs())))
                && ((this.getFq() == castOther.getFq()) || (this.getFq() != null
                        && castOther.getFq() != null && this.getFq().equals(
                        castOther.getFq())))
                && ((this.getActionFlow() == castOther.getActionFlow()) || (this
                        .getActionFlow() != null
                        && castOther.getActionFlow() != null && this
                        .getActionFlow().equals(castOther.getActionFlow())))
                && ((this.getPe() == castOther.getPe()) || (this.getPe() != null
                        && castOther.getPe() != null && this.getPe().equals(
                        castOther.getPe())))
                && ((this.getUseLatestFcst() == castOther.getUseLatestFcst()) || (this
                        .getUseLatestFcst() != null
                        && castOther.getUseLatestFcst() != null && this
                        .getUseLatestFcst()
                        .equals(castOther.getUseLatestFcst())))
                && ((this.getProximity() == castOther.getProximity()) || (this
                        .getProximity() != null
                        && castOther.getProximity() != null && this
                        .getProximity().equals(castOther.getProximity())))
                && ((this.getReach() == castOther.getReach()) || (this
                        .getReach() != null
                        && castOther.getReach() != null && this.getReach()
                        .equals(castOther.getReach())))
                && ((this.getGroupId() == castOther.getGroupId()) || (this
                        .getGroupId() != null
                        && castOther.getGroupId() != null && this.getGroupId()
                        .equals(castOther.getGroupId())))
                && ((this.getOrdinal() == castOther.getOrdinal()) || (this
                        .getOrdinal() != null
                        && castOther.getOrdinal() != null && this.getOrdinal()
                        .equals(castOther.getOrdinal())))
                && ((this.getChgThreshold() == castOther.getChgThreshold()) || (this
                        .getChgThreshold() != null
                        && castOther.getChgThreshold() != null && this
                        .getChgThreshold().equals(castOther.getChgThreshold())))
                && ((this.getRecType() == castOther.getRecType()) || (this
                        .getRecType() != null
                        && castOther.getRecType() != null && this.getRecType()
                        .equals(castOther.getRecType())))
                && ((this.getBackhrs() == castOther.getBackhrs()) || (this
                        .getBackhrs() != null
                        && castOther.getBackhrs() != null && this.getBackhrs()
                        .equals(castOther.getBackhrs())))
                && ((this.getForwardhrs() == castOther.getForwardhrs()) || (this
                        .getForwardhrs() != null
                        && castOther.getForwardhrs() != null && this
                        .getForwardhrs().equals(castOther.getForwardhrs())))
                && ((this.getAdjustendhrs() == castOther.getAdjustendhrs()) || (this
                        .getAdjustendhrs() != null
                        && castOther.getAdjustendhrs() != null && this
                        .getAdjustendhrs().equals(castOther.getAdjustendhrs())))
                && ((this.getMinorStage() == castOther.getMinorStage()) || (this
                        .getMinorStage() != null
                        && castOther.getMinorStage() != null && this
                        .getMinorStage().equals(castOther.getMinorStage())))
                && ((this.getModerateStage() == castOther.getModerateStage()) || (this
                        .getModerateStage() != null
                        && castOther.getModerateStage() != null && this
                        .getModerateStage()
                        .equals(castOther.getModerateStage())))
                && ((this.getMajorStage() == castOther.getMajorStage()) || (this
                        .getMajorStage() != null
                        && castOther.getMajorStage() != null && this
                        .getMajorStage().equals(castOther.getMajorStage())))
                && ((this.getMinorFlow() == castOther.getMinorFlow()) || (this
                        .getMinorFlow() != null
                        && castOther.getMinorFlow() != null && this
                        .getMinorFlow().equals(castOther.getMinorFlow())))
                && ((this.getModerateFlow() == castOther.getModerateFlow()) || (this
                        .getModerateFlow() != null
                        && castOther.getModerateFlow() != null && this
                        .getModerateFlow().equals(castOther.getModerateFlow())))
                && ((this.getMajorFlow() == castOther.getMajorFlow()) || (this
                        .getMajorFlow() != null
                        && castOther.getMajorFlow() != null && this
                        .getMajorFlow().equals(castOther.getMajorFlow())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result
                + (getLid() == null ? 0 : this.getLid().hashCode());
        result = 37 * result
                + (getName() == null ? 0 : this.getName().hashCode());
        result = 37 * result
                + (getCounty() == null ? 0 : this.getCounty().hashCode());
        result = 37 * result
                + (getState() == null ? 0 : this.getState().hashCode());
        result = 37 * result
                + (getHsa() == null ? 0 : this.getHsa().hashCode());
        result = 37
                * result
                + (getPrimaryBack() == null ? 0 : this.getPrimaryBack()
                        .hashCode());
        result = 37
                * result
                + (getSecondaryBack() == null ? 0 : this.getSecondaryBack()
                        .hashCode());
        result = 37 * result
                + (getStream() == null ? 0 : this.getStream().hashCode());
        result = 37 * result + (getBf() == null ? 0 : this.getBf().hashCode());
        result = 37 * result
                + (getWstg() == null ? 0 : this.getWstg().hashCode());
        result = 37 * result + (getFs() == null ? 0 : this.getFs().hashCode());
        result = 37 * result + (getFq() == null ? 0 : this.getFq().hashCode());
        result = 37
                * result
                + (getActionFlow() == null ? 0 : this.getActionFlow()
                        .hashCode());
        result = 37 * result + (getPe() == null ? 0 : this.getPe().hashCode());
        result = 37
                * result
                + (getUseLatestFcst() == null ? 0 : this.getUseLatestFcst()
                        .hashCode());
        result = 37 * result
                + (getProximity() == null ? 0 : this.getProximity().hashCode());
        result = 37 * result
                + (getReach() == null ? 0 : this.getReach().hashCode());
        result = 37 * result
                + (getGroupId() == null ? 0 : this.getGroupId().hashCode());
        result = 37 * result
                + (getOrdinal() == null ? 0 : this.getOrdinal().hashCode());
        result = 37
                * result
                + (getChgThreshold() == null ? 0 : this.getChgThreshold()
                        .hashCode());
        result = 37 * result
                + (getRecType() == null ? 0 : this.getRecType().hashCode());
        result = 37 * result
                + (getBackhrs() == null ? 0 : this.getBackhrs().hashCode());
        result = 37
                * result
                + (getForwardhrs() == null ? 0 : this.getForwardhrs()
                        .hashCode());
        result = 37
                * result
                + (getAdjustendhrs() == null ? 0 : this.getAdjustendhrs()
                        .hashCode());
        result = 37
                * result
                + (getMinorStage() == null ? 0 : this.getMinorStage()
                        .hashCode());
        result = 37
                * result
                + (getModerateStage() == null ? 0 : this.getModerateStage()
                        .hashCode());
        result = 37
                * result
                + (getMajorStage() == null ? 0 : this.getMajorStage()
                        .hashCode());
        result = 37 * result
                + (getMinorFlow() == null ? 0 : this.getMinorFlow().hashCode());
        result = 37
                * result
                + (getModerateFlow() == null ? 0 : this.getModerateFlow()
                        .hashCode());
        result = 37 * result
                + (getMajorFlow() == null ? 0 : this.getMajorFlow().hashCode());
        return result;
    }

}
