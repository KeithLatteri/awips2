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
package com.raytheon.viz.ui.personalities.awips;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

import com.raytheon.uf.viz.personalities.cave.workbench.VizWorkbenchWindowAdvisor;
import com.raytheon.uf.viz.ui.menus.widgets.tearoff.TearOffMenuListener;
import com.raytheon.viz.ui.statusline.VizActionBarAdvisor;

/**
 * AWIPS window advisor, doesn't show perspective bar when -perspective argument
 * is used to launch the application
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Apr 15, 2013            mschenke     Initial creation
 * 
 * </pre>
 * 
 * @author mschenke
 * @version 1.0
 */

public class AWIPSWorkbenchWindowAdvisor extends VizWorkbenchWindowAdvisor {

    private boolean singlePerspective;

    /**
     * @param configurer
     * @param singlePerspective
     */
    public AWIPSWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer,
            boolean singlePerspective) {
        super(configurer);
        this.singlePerspective = singlePerspective;
    }

    @Override
    public void preWindowOpen() {
        super.preWindowOpen();
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowPerspectiveBar(!singlePerspective);
    }

    @Override
    public void postWindowOpen() {
        super.postWindowOpen();
        new TearOffMenuListener(VizActionBarAdvisor.getInstance(
                getWindowConfigurer().getWindow()).getMenuManager());
    }

}
