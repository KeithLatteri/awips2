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
package com.raytheon.uf.edex.datadelivery.bandwidth.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.collect.Sets;
import com.raytheon.uf.common.datadelivery.registry.AdhocSubscription;
import com.raytheon.uf.common.datadelivery.registry.Coverage;
import com.raytheon.uf.common.datadelivery.registry.DataSetMetaData;
import com.raytheon.uf.common.datadelivery.registry.DataType;
import com.raytheon.uf.common.datadelivery.registry.GriddedTime;
import com.raytheon.uf.common.datadelivery.registry.Subscription;
import com.raytheon.uf.common.datadelivery.registry.Time;
import com.raytheon.uf.common.datadelivery.registry.handlers.DataDeliveryHandlers;
import com.raytheon.uf.common.registry.handler.RegistryHandlerException;
import com.raytheon.uf.common.status.IUFStatusHandler;
import com.raytheon.uf.common.status.UFStatus;
import com.raytheon.uf.common.status.UFStatus.Priority;
import com.raytheon.uf.common.time.util.TimeUtil;
import com.raytheon.uf.edex.datadelivery.bandwidth.dao.BandwidthAllocation;
import com.raytheon.uf.edex.datadelivery.bandwidth.dao.BandwidthDataSetUpdate;
import com.raytheon.uf.edex.datadelivery.bandwidth.dao.BandwidthSubscription;
import com.raytheon.uf.edex.datadelivery.bandwidth.dao.IBandwidthDao;
import com.raytheon.uf.edex.datadelivery.bandwidth.retrieval.RetrievalManager;
import com.raytheon.uf.edex.datadelivery.bandwidth.retrieval.RetrievalPlan;
import com.raytheon.uf.edex.datadelivery.bandwidth.retrieval.RetrievalStatus;

/**
 * Utility class that maintains state between in-memory objects and database
 * versions.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Oct 24, 2012 1286       djohnson     Extract methods from {@link BandwidthUtil}.
 * Dec 11, 2012 1286       djohnson     FULFILLED allocations are not in the retrieval plan either.
 * Feb 14, 2013 1595       djohnson     Fix not using calendar copies, and backwards max/min operations.
 * Jun 03, 2013 2038       djohnson     Add ability to schedule down to minute granularity.
 * Jun 04, 2013  223       mpduff       Refactor changes.
 * Sept 10, 2013 2351      dhladky      Made adhoc queries for pointdata work
 * Sept 17, 2013 2383      bgonzale     setAdhocMostRecentUrlAndTime returns null if grid and
 *                                      no metadata found.
 * Sept 24, 2013 1797      dhladky      separated time from GriddedTime
 * Oct 10, 2013 1797       bgonzale     Refactored registry Time objects.
 * 
 * </pre>
 * 
 * @author djohnson
 * @version 1.0
 */

public class BandwidthDaoUtil<T extends Time, C extends Coverage> {

    private static final IUFStatusHandler statusHandler = UFStatus
            .getHandler(BandwidthDaoUtil.class);

    private final IBandwidthDao<T,C> bandwidthDao;

    private final RetrievalManager retrievalManager;

    /**
     * Constructor.
     * 
     * @param bandwidthDao
     *            the bandwidth dao
     * @param retrievalManager
     *            the retrieval manager
     */
    public BandwidthDaoUtil(IBandwidthDao<T,C> bandwidthDao,
            RetrievalManager retrievalManager) {
        this.bandwidthDao = bandwidthDao;
        this.retrievalManager = retrievalManager;
    }

    /**
     * Calculate all the retrieval times for a subscription that should be in
     * the current retrieval plan for the specified subscription.
     * 
     * @param subscription
     * @param cycles
     * @return
     */
    public SortedSet<Calendar> getRetrievalTimes(Subscription<T,C> subscription,
            SortedSet<Integer> cycles) {
        return getRetrievalTimes(subscription, cycles,
                Sets.newTreeSet(Arrays.asList(0)));
    }

    /**
     * Calculate all the retrieval times for a subscription that should be in
     * the current retrieval plan for the specified subscription.
     * 
     * @param subscription
     * @param retrievalInterval
     *            the retrieval interval
     * @return the retrieval times
     */
    public SortedSet<Calendar> getRetrievalTimes(Subscription<T,C> subscription,
            int retrievalInterval) {
        // Add all hours of the days
        final SortedSet<Integer> hours = Sets.newTreeSet();
        for (int i = 0; i < TimeUtil.HOURS_PER_DAY; i++) {
            hours.add(i);
        }

        // Add every minute of the hour that is a multiple of the retrieval
        // interval
        final SortedSet<Integer> minutes = Sets.newTreeSet();
        for (int i = 0; i < TimeUtil.MINUTES_PER_HOUR; i += retrievalInterval) {
            minutes.add(i);
        }

        return getRetrievalTimes(subscription, hours, minutes);
    }

    /**
     * Calculate all the retrieval times for a subscription that should be in
     * the current retrieval plan for the specified subscription.
     * 
     * @param subscription
     * @param hours
     * @param minutes
     * @return
     */
    private SortedSet<Calendar> getRetrievalTimes(Subscription<T,C> subscription,
            SortedSet<Integer> hours, SortedSet<Integer> minutes) {

        SortedSet<Calendar> subscriptionTimes = new TreeSet<Calendar>();

        RetrievalPlan plan = retrievalManager.getPlan(subscription.getRoute());
        if (plan == null) {
            return subscriptionTimes;
        }

        Calendar planEnd = plan.getPlanEnd();
        Calendar planStart = plan.getPlanStart();

        // Make sure the RetrievalPlan's start and end times intersect
        // the Subscription's active period.
        Date activePeriodEnd = subscription.getActivePeriodEnd();

        if (activePeriodEnd != null) {
            Date activePeriodStart = subscription.getActivePeriodStart();
            Calendar active = BandwidthUtil.copy(activePeriodStart);

            // Substitute the active periods month and day for the
            // plan start month and day.
            Calendar s = BandwidthUtil.copy(planStart);
            s.set(Calendar.MONTH, active.get(Calendar.MONTH));
            s.set(Calendar.DAY_OF_MONTH, active.get(Calendar.DAY_OF_MONTH));

            // If the active period start in outside the plan bounds,
            // there is no intersection - just return an empty set.
            if (s.before(planStart) && s.after(planEnd)) {
                return subscriptionTimes;
            }

            // Do the same for active plan end..
            activePeriodStart = subscription.getActivePeriodEnd();
            active = BandwidthUtil.copy(activePeriodStart);

            // Substitute the active periods month and day for the
            // plan ends month and day.
            s = BandwidthUtil.copy(planStart);
            s.set(Calendar.MONTH, active.get(Calendar.MONTH));
            s.set(Calendar.DAY_OF_MONTH, active.get(Calendar.DAY_OF_MONTH));

            // If the active period end is before the start of the plan,
            // there is no intersection - just return an empty set.
            if (s.before(planStart)) {
                return subscriptionTimes;
            }
        }

        // Now check the Subscription start and end times for intersection
        // with the RetrievalPlan...

        // Figure out the 'active' period for a subscription..

        Calendar subscriptionEndDate = BandwidthUtil.copy(subscription
                .getSubscriptionEnd());
        Calendar subscriptionStartDate = null;
        // Check to see if this is a non-expiring subscription
        if (subscriptionEndDate == null) {
            // If there is no subscription start end dates then the largest
            // window that can be scheduled is the RetrievalPlan size..
            subscriptionEndDate = BandwidthUtil.copy(planEnd);
            subscriptionStartDate = BandwidthUtil.copy(planStart);
        } else {
            // If there is a start and end time, then modify the start and
            // end times to 'fit' within the RetrievalPlan times
            subscriptionStartDate = BandwidthUtil.copy(BandwidthUtil.max(
                    subscription.getSubscriptionStart(), planStart));
            subscriptionEndDate = BandwidthUtil.copy(BandwidthUtil.min(
                    subscription.getSubscriptionEnd(), planEnd));
        }

        // Create a Set of Calendars for all the baseReferenceTimes that a
        // Subscription can contain...
        TimeUtil.minCalendarFields(subscriptionStartDate, Calendar.MILLISECOND,
                Calendar.SECOND, Calendar.MINUTE, Calendar.HOUR_OF_DAY);

        outerloop: while (!subscriptionStartDate.after(subscriptionEndDate)) {

            for (Integer cycle : hours) {
                subscriptionStartDate.set(Calendar.HOUR_OF_DAY, cycle);
                for (Integer minute : minutes) {
                    subscriptionStartDate.set(Calendar.MINUTE, minute);
                    if (subscriptionStartDate.after(subscriptionEndDate)) {
                        break outerloop;
                    } else {
                        Calendar time = TimeUtil.newCalendar();
                        time.setTimeInMillis(subscriptionStartDate
                                .getTimeInMillis());
                        subscriptionTimes.add(time);
                    }
                }
            }

            // Start the next day..
            subscriptionStartDate.add(Calendar.DAY_OF_YEAR, 1);
            subscriptionStartDate.set(Calendar.HOUR_OF_DAY, hours.first());
        }

        // Now walk the subscription times and throw away anything outside the
        // plan hours, taking into account the availability delay...
        int availabilityDelay = BandwidthUtil
                .getDataSetAvailablityDelay(subscription);
        Iterator<Calendar> itr = subscriptionTimes.iterator();
        while (itr.hasNext()) {

            Calendar time = itr.next();
            Calendar withAvailabilityDelay = BandwidthUtil.copy(time);
            withAvailabilityDelay.add(Calendar.MINUTE, availabilityDelay);

            // We allow base reference times that are still possible to retrieve
            // within the availability window to be included
            if (withAvailabilityDelay.before(planStart) || time.after(planEnd)) {
                itr.remove();
            }
        }

        return subscriptionTimes;
    }

    public void remove(BandwidthSubscription dao) {

        List<BandwidthAllocation> reservations = bandwidthDao
                .getBandwidthAllocations(dao.getIdentifier());

        // Unschedule all the BandwidthReservations for the subscription..
        for (BandwidthAllocation reservation : reservations) {
            final RetrievalStatus reservationStatus = reservation.getStatus();
            // Allocations with these statuses are not actively managed by
            // the retrieval manager
            if (RetrievalStatus.UNSCHEDULED == reservationStatus
                    || RetrievalStatus.FULFILLED == reservationStatus) {
                continue;
            }
            retrievalManager.remove(reservation);
        }

        bandwidthDao.remove(dao);
    }

    /**
     * Handle updating the database and propagate the update of any status
     * changes to the RetrievalManager.
     * 
     * @param allocation
     */
    public void update(BandwidthAllocation allocation) {

        bandwidthDao.createOrUpdate(allocation);
        retrievalManager.updateBandwidthAllocation(allocation);
    }

    /**
     * Add the url and start time for the most recent dataset metadata update
     * that has arrived, which matches the requested cycle of the
     * {@link AdhocSubscription}.
     * 
     * @param adhoc
     *            the adhoc subscription, with its {@link Time} object's cycles
     *            populated
     * @param mostRecent
     *            if true, then any base reference time will match
     * @return the adhoc subscription, or null if no matching metadata could be
     *         found
     */
    public AdhocSubscription<T,C> setAdhocMostRecentUrlAndTime(
            AdhocSubscription<T,C> adhoc, boolean mostRecent) {
        AdhocSubscription<T,C> retVal = null;

        if (adhoc.getDataSetType() == DataType.POINT) {

            List<DataSetMetaData> dataSetMetaDatas = null;
            try {
                dataSetMetaDatas = DataDeliveryHandlers
                        .getDataSetMetaDataHandler().getByDataSet(
                                adhoc.getDataSetName(), adhoc.getProvider());
            } catch (RegistryHandlerException e) {
                statusHandler.handle(Priority.PROBLEM,
                        "No DataSetMetaData matching query! DataSetName: "
                                + adhoc.getDataSetName() + " Provider: "
                                + adhoc.getProvider(), e);
            }

            if (dataSetMetaDatas != null && !dataSetMetaDatas.isEmpty()) {
                // just grab the most recent one, all we need is the URL
                adhoc.setUrl(dataSetMetaDatas.get(0).getUrl());
                retVal = adhoc;
            }

        } else if (adhoc.getDataSetType() == DataType.GRID) {

            // if the start time is there, it already has it, skip
            if (adhoc.getTime().getStart() == null) {

                List<BandwidthDataSetUpdate> dataSetMetaDataUpdates = bandwidthDao
                        .getBandwidthDataSetUpdate(adhoc.getProvider(),
                                adhoc.getDataSetName());

                if (dataSetMetaDataUpdates != null
                        && !dataSetMetaDataUpdates.isEmpty()) {
                    // getDataSetMetaData returns the dataset meta-data in
                    // descending
                    // order of time, so walk the iterator finding the first
                    // subscribed
                    // to cycle
                    BandwidthDataSetUpdate daoToUse = null;
                    Time adhocTime = adhoc.getTime();
                    for (BandwidthDataSetUpdate current : dataSetMetaDataUpdates) {
                        if (mostRecent
                                || ((GriddedTime)adhocTime).getCycleTimes().contains(
                                        current.getDataSetBaseTime().get(
                                                Calendar.HOUR_OF_DAY))) {
                            daoToUse = current;
                            break;
                        }
                    }

                    if (daoToUse == null) {
                        if (statusHandler.isPriorityEnabled(Priority.DEBUG)) {
                            statusHandler
                                    .debug(String
                                            .format("There wasn't applicable most recent dataset metadata to use for the adhoc subscription [%s].",
                                                    adhoc.getName()));
                        }
                    } else {
                        if (statusHandler.isPriorityEnabled(Priority.DEBUG)) {
                            statusHandler
                                    .debug(String
                                            .format("Found most recent metadata for adhoc subscription [%s], using url [%s]",
                                                    adhoc.getName(),
                                                    daoToUse.getUrl()));
                        }

                        adhoc.setUrl(daoToUse.getUrl());
                        adhocTime.setStart(daoToUse.getDataSetBaseTime()
                                .getTime());

                        retVal = adhoc;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("DataType: "
                    + adhoc.getDataSetType()
                    + " Not yet implemented for adhoc subscriptions");
        }

        return retVal;
    }
}
