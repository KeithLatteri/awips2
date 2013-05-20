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
package com.raytheon.uf.common.datadelivery.registry;

import com.raytheon.uf.common.registry.ebxml.RegistryUtil;

/**
 * Constants file for data delivery registry object types.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Oct 5, 2012  0726       djohnson     Initial creation
 * Dec 11, 2012 1403       djohnson     Adhoc subscriptions no longer go to the registry.
 * May 21, 2013 2020       mpduff       Rename UserSubscription to SiteSubscription.
 * 
 * </pre>
 * 
 * @author djohnson
 * @version 1.0
 */

public final class DataDeliveryRegistryObjectTypes {
    /**
     * Private constructor.
     */
    private DataDeliveryRegistryObjectTypes() {
    }

    public static final String DATASETMETADATA = RegistryUtil
            .getObjectType(DataSetMetaData.class);

    public static final String SITE_SUBSCRIPTION = RegistryUtil
            .getObjectType(SiteSubscription.class);

    public static final String SHARED_SUBSCRIPTION = RegistryUtil
            .getObjectType(SharedSubscription.class);

    public static final String DATASET = RegistryUtil
            .getObjectType(DataSet.class);
}
