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
package com.raytheon.viz.awipstools.ui.action;

import com.raytheon.uf.viz.core.rsc.tools.AwipsToolsResourceData;
import com.raytheon.viz.awipstools.ui.layer.ShearLayer;
import com.raytheon.viz.awipstools.ui.layer.VRShearLayer;

/**
 * Handles the VR Shear Action.
 * 
 * <pre>
 * 
 *  SOFTWARE HISTORY
 * 
 *  Date         Ticket#     Engineer    Description
 *  ------------ ----------  ----------- --------------------------
 *  Oct172007    #492        ebabin      Initial Creation.
 * 
 * </pre>
 * 
 * @author ebabin
 * @version 1
 */
public class VRShearAction extends ShearAction {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.raytheon.viz.awipstools.ui.action.MapToolAction#getResourceData()
     */
    @Override
    protected AwipsToolsResourceData<ShearLayer> getResourceData() {
        return new AwipsToolsResourceData<ShearLayer>(
                VRShearLayer.VRSHEAR_LOCATION, VRShearLayer.class);
    }

}
