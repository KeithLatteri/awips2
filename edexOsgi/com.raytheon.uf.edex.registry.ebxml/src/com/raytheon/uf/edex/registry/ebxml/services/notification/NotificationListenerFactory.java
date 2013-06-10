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
package com.raytheon.uf.edex.registry.ebxml.services.notification;

import oasis.names.tc.ebxml.regrep.wsdl.registry.services.v4.NotificationListener;
import oasis.names.tc.ebxml.regrep.xsd.rim.v4.NotificationType;

import com.google.common.annotations.VisibleForTesting;
import com.raytheon.uf.common.registry.constants.DeliveryMethodTypes;
import com.raytheon.uf.common.status.IUFStatusHandler;
import com.raytheon.uf.common.status.UFStatus;
import com.raytheon.uf.edex.registry.ebxml.services.notification.listeners.EmailNotificationListener;
import com.raytheon.uf.edex.registry.ebxml.services.notification.listeners.SpringBeanNotificationListener;
import com.raytheon.uf.edex.registry.ebxml.services.notification.listeners.WebServiceNotificationListener;

/**
 * The default notification listener factory.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Apr 17, 2013 1672       djohnson     Initial creation
 * 
 * </pre>
 * 
 * @author djohnson
 * @version 1.0
 */
public class NotificationListenerFactory implements
        INotificationListenerFactory {

    private static class UnsupportedDeliveryTypeNotificationListener implements
            NotificationListener {

        private static final IUFStatusHandler statusHandler = UFStatus
                .getHandler(UnsupportedDeliveryTypeNotificationListener.class);

        private final String endpointType;

        /**
         * @param stringDestination
         */
        public UnsupportedDeliveryTypeNotificationListener(
                NotificationDestination destination) {
            this.endpointType = destination.getEndpointType();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onNotification(NotificationType notification) {
            statusHandler.warn("Unsupported delivery type: " + endpointType
                    + ". Notification will not be delivered!");
        }
    }

    private final EmailSender emailSender;

    /**
     * Constructor.
     * 
     * @param emailSender
     *            the email sender
     */
    public NotificationListenerFactory(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationListener getNotificationListenerForDestination(
            NotificationDestination destination) {
        final String endpointType = destination.getEndpointType();
        if (DeliveryMethodTypes.SOAP.equals(endpointType)) {
            return getWebServiceNotificationListener(destination);
        } else if (DeliveryMethodTypes.EMAIL.equals(endpointType)) {
            return getEmailNotificationListener(destination);
        } else if (DeliveryMethodTypes.PLUGIN.equals(endpointType)) {
            return getPluginNotificationListener(destination);
        } else {
            return getUnsupportedDeliveryTypeNotificationListener(destination);
        }
    }

    /**
     * Create a web service notification listener.
     * 
     * @param destination
     *            the destination
     * @return the notification listener
     */
    @VisibleForTesting
    NotificationListener getWebServiceNotificationListener(
            final NotificationDestination destination) {
        return new WebServiceNotificationListener(destination);
    }

    /**
     * Create a plugin notification listener.
     * 
     * @param destination
     *            the destination
     * @return the notification listener
     */
    @VisibleForTesting
    NotificationListener getPluginNotificationListener(
            final NotificationDestination destination) {
        return new SpringBeanNotificationListener(destination);
    }

    /**
     * Create an emailnotification listener.
     * 
     * @param destination
     *            the destination
     * @return the notification listener
     */
    @VisibleForTesting
    NotificationListener getEmailNotificationListener(
            final NotificationDestination destination) {
        return new EmailNotificationListener(destination, emailSender);
    }

    /**
     * Create an unsupported delivery type notification listener.
     * 
     * @param destination
     *            the destination
     * @return the notification listener
     */
    @VisibleForTesting
    NotificationListener getUnsupportedDeliveryTypeNotificationListener(
            final NotificationDestination destination) {
        return new UnsupportedDeliveryTypeNotificationListener(destination);
    }
}
