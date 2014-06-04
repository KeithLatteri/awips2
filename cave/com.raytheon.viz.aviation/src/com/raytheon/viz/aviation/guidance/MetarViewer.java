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
package com.raytheon.viz.aviation.guidance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.raytheon.uf.viz.core.jobs.IRequestCompleteListener;
import com.raytheon.viz.aviation.cachedata.CacheGuidanceRequest;
import com.raytheon.viz.aviation.cachedata.MetarCacheGuidanceRequest;
import com.raytheon.viz.aviation.cachedata.PythonCacheGuidanceJob;
import com.raytheon.viz.aviation.guidance.GuidanceRequest.GuidanceType;
import com.raytheon.viz.aviation.observer.TafMonitorDlg;
import com.raytheon.viz.aviation.resource.ResourceConfigMgr;
import com.raytheon.viz.aviation.resource.ResourceConfigMgr.ResourceTag;

/**
 * MetarViewer class contains a basic setup for the Guidance tab on the TAF
 * viewer editor dialog.
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- -------------------------------------
 * 28 FEB 2008  938        lvenable    Initial creation. 
 * 12/3/2008    1588       grichard    Added callback for headersChk button.
 * 5/6/2009     1982       grichard    Select 'all' checkbox by default.
 * 08/31/2010   4022       rferrel     Highlight Metar alert discrepancy.
 * 12/14/2010   5782       rferrel     Use resource to populate hours combo
 *                                     and set default value for check hours.
 * 04/28/2011   8065       rferrel     Add flag to indicate display is current
 *                                     and implement data caching
 * 31JUL2012    14570      zhao        Highlight Metar alert for case of 'cat'
 * 09Apr2014    #3005      lvenable    Added method call to mark the data and header text
 *                                     controls to updating when the number
 *                                     of hours has changed (via combo control).
 * 
 * </pre>
 * 
 * @author lvenable
 * @version 1.0
 * 
 */
public class MetarViewer extends ViewerTab implements
        IRequestCompleteListener<String[]> {

    /**
     * All check box.
     */
    private Button allChk;

    /**
     * Flight Category check box.
     */
    private Button flightCatChk;

    /**
     * Headers check box.
     */
    public Button headersChk;

    /**
     * Decoded check box.
     */
    public Button decodedChk;

    /**
     * Number of hours check box.
     */
    private Combo numHrsCbo;

    /**
     * Number of hours associated with the 'all' check box.
     */
    private String allChkHrs;

    /**
     * Number of hours associated with other than 'all' check box.
     */
    private String prevChkHrs;

    /**
     * The station list.
     */
    private List<String> stationList;

    private String[] currentGuidance;

    /**
     * Integer sorted list of hour values as strings.
     */
    private String[] hours;

    /**
     * Tags used to determine alert highlights. Must match the alert tags
     * generated by GuidanceEntry.py.
     */
    private static final HashMap<String, String[]> alertMap = new HashMap<String, String[]>();
    static {
        // alertMap.put("cat", new String[] { "<vsby>", "</vsby>", "<sky>",
        // "</sky>" }); // 14570
        alertMap.put("tempo", new String[] { "<vsby>", "</vsby>", "<wind>",
                "</wind>", "<wx>", "</wx>", "<sky>", "</sky>" }); // 14570
        alertMap.put("vsby", new String[] { "<vsby>", "</vsby>" });
        alertMap.put("wind", new String[] { "<wind>", "</wind>" });
        alertMap.put("wx", new String[] { "<wx>", "</wx>" });
        alertMap.put("sky", new String[] { "<sky>", "</sky>" });
    }

    /**
     * Constructor.
     * 
     * @param parent
     *            Parent composite.
     */
    public MetarViewer(Composite parent, String model) {
        super(parent);
    }

    /**
     * Method called after initializing all of the components.
     */
    @Override
    protected void finalInitialization() {
    }

    /**
     * Create the controls at the top of the composite.
     */
    @Override
    protected void createTopControls(ResourceConfigMgr configMgr) {
        Composite controlsComp = new Composite(this, SWT.NONE);
        GridLayout gl = new GridLayout(6, false);
        GridData gd = new GridData(SWT.RIGHT, SWT.DEFAULT, true, false);
        controlsComp.setLayout(gl);
        controlsComp.setLayoutData(gd);
        configMgr.setDefaultColors(controlsComp);

        allChk = new Button(controlsComp, SWT.CHECK);
        allChk.setText("All");
        allChk.setToolTipText("Displays all data for all sites");
        configMgr.setDefaultFontAndColors(allChk);
        allChk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                // Update the metar in the viewer tab.
                allChkIsSelected = allChk.getSelection();
                generateGuidance(siteID);
            }
        });

        flightCatChk = new Button(controlsComp, SWT.CHECK);
        flightCatChk.setText("Flight Categories");
        flightCatChk
                .setToolTipText("Background color depicts flight categories");
        configMgr.setDefaultFontAndColors(flightCatChk);
        flightCatChk.setSelection(configMgr
                .getResourceAsBoolean(ResourceTag.HighlightFlightCat));
        flightCatChk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                requestComplete(currentGuidance);
            }
        });

        headersChk = new Button(controlsComp, SWT.CHECK);
        headersChk.setText("Headers");
        headersChk.setToolTipText("Display WMO header for raw reports");
        configMgr.setDefaultFontAndColors(headersChk);
        headersChk.setSelection(configMgr
                .getResourceAsBoolean(ResourceTag.ShowHeaders));
        headersChk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                if (!decodedChk.getSelection() || allChk.getSelection()) {
                    generateGuidance(siteID);
                }
            }
        });

        decodedChk = new Button(controlsComp, SWT.CHECK);
        decodedChk.setText("Decoded");
        decodedChk.setToolTipText("Display decoded obs ARONET style");
        configMgr.setDefaultFontAndColors(decodedChk);
        decodedChk.setSelection(configMgr
                .getResourceAsBoolean(ResourceTag.ShowDecoded));
        decodedChk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                if (!allChk.getSelection()) {
                    generateGuidance(siteID);
                }
            }
        });

        gd = new GridData(150, SWT.DEFAULT);
        Label numHrsLbl = new Label(controlsComp, SWT.RIGHT);
        configMgr.setDefaultFontAndColors(numHrsLbl, " Num Hours: ", gd);

        gd = new GridData(50, SWT.DEFAULT);
        numHrsCbo = new Combo(controlsComp, SWT.DROP_DOWN | SWT.READ_ONLY);

        // This assumes the combo values for NumHours and NumHoursAll are the
        // same.
        for (String value : configMgr.getComboValues(ResourceTag.NumHours)) {
            numHrsCbo.add(value);
        }

        // Make an integer sorted list of hours.
        hours = numHrsCbo.getItems();
        int[] hoursInt = new int[hours.length];
        for (int i = 0; i < hours.length; ++i) {
            hoursInt[i] = Integer.parseInt(hours[i]);
        }
        Arrays.sort(hoursInt);
        for (int i = 0; i < hours.length; ++i) {
            hours[i] = Integer.toString(hoursInt[i]);
        }

        allChkHrs = configMgr.getDataAsString(ResourceTag.NumHoursAll);
        prevChkHrs = configMgr.getDataAsString(ResourceTag.NumHours);

        numHrsCbo.select(numHrsCbo.indexOf(prevChkHrs));

        configMgr.setDefaultFont(numHrsCbo);

        numHrsCbo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                // Update the metar in the viewer tab.
                markTextAsUpdating();
                if (MetarViewer.this.allChk.getSelection()) {
                    allChkHrs = numHrsCbo.getItem(numHrsCbo.getSelectionIndex());
                } else {
                    prevChkHrs = numHrsCbo.getItem(numHrsCbo
                            .getSelectionIndex());
                }
                // Update the metar in the viewer tab.
                generateGuidance(siteID);
            }
        });
    }

    /**
     * Get the tag for the desired Metar Cache Request.
     * 
     * @param siteID
     * @param size
     * @return tag
     */
    private String getTag(String siteID, String size) {
        return MetarCacheGuidanceRequest.getTag(siteID, size);
    }

    /**
     * Request desired data for display on the tab. A check is performed and if
     * all data is cached a request is queued to immediately update the tab.
     * Otherwise request is made to cache missing data and update the display
     * once it arrives.
     */
    @Override
    public int generateGuidance(String siteID) {
        int cnt = super.generateGuidance(siteID);
        MetarGuidanceRequest req = new MetarGuidanceRequest();
        req.setGuidanceType(GuidanceType.METAR);
        req.setListener(this);
        req.setAll(allChkIsSelected);
        req.setHeader(headersChk.getSelection());

        ArrayList<String> siteObjs = new ArrayList<String>();
        ArrayList<CacheGuidanceRequest> cacheRequests = new ArrayList<CacheGuidanceRequest>();
        if (allChkIsSelected) {
            String tag = getTag(siteID, allChkHrs);
            req.setTag(tag);
            req.setSize(allChkHrs);
            for (String sID : stationList) {
                String siteObj = myGetCacheSiteObj(sID, allChkHrs);
                if (siteObj == null) {
                    CacheGuidanceRequest cReq = createCacheRequest(sID,
                            allChkHrs);
                    cacheRequests.add(cReq);
                } else {
                    siteObjs.add(siteObj);
                }
            }
            if (cacheRequests.size() > 0) {
                queueCacheRequests(cnt, cacheRequests);
                return cnt;
            }
            numHrsCbo.select(numHrsCbo.indexOf(allChkHrs));
            req.setDecoded(false);
        } else {
            String tag = getTag(siteID, prevChkHrs);
            req.setTag(tag);
            req.setSize(prevChkHrs);
            String siteObj = myGetCacheSiteObj(siteID, prevChkHrs);
            if (siteObj == null) {
                // System.out
                // .println("Generate cache for: " + tag + " cnt " + cnt);
                CacheGuidanceRequest cReq = createCacheRequest(siteID,
                        prevChkHrs);
                cacheRequests.add(cReq);
                queueCacheRequests(cnt, cacheRequests);
                return cnt;
            }
            siteObjs.add(siteObj);

            numHrsCbo.select(numHrsCbo.indexOf(prevChkHrs));
            req.setDecoded(decodedChk.getSelection());
        }
        req.setSiteObjs(siteObjs);
        // textComp.getHeaderStTxt().setText("");
        // textComp.getDataStTxt().setText("");
        PythonGuidanceJob.getInstance().enqueue(req);
        return cnt;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.raytheon.viz.aviation.guidance.IGuidanceListener#guidanceUpdated(
     * java.lang.String[])
     */
    @Override
    public void requestComplete(String[] newGuidance) {
        currentGuidance = newGuidance;

        if (newGuidance != null && !textComp.isDisposed()) {
            if (newGuidance.length > 1) {
                String header = newGuidance[0];
                textComp.getHeaderStTxt().setText(header);

                StringBuilder guidance = new StringBuilder();
                for (int i = 1; i < newGuidance.length; i++) {
                    guidance.append(newGuidance[i]).append("\n");
                }
                textComp.getDataStTxt().setText(guidance.toString());
            } else if (newGuidance.length == 1) {
                textComp.getDataStTxt().setText(newGuidance[0]);
            }

            if (!flightCatChk.isDisposed()) {
                updateTextMarkers(flightCatChk.getSelection(),
                        allChk.getSelection());
            }

            setDisplayCurrent(true);
            super.requestComplete();
        }
    }

    /**
     * Indicates if flight category alerts are highlighted.
     * 
     * @return state true when highlighted otherwise false
     */
    public boolean highlightAlerts() {
        boolean state = false;
        if (!this.isDisposed()) {
            state = !flightCatChk.getSelection() && !allChk.getSelection();
        }
        return state;
    }

    /**
     * Update highlight for flight category or alert text and remove marker
     * tags.
     * 
     * @see com.raytheon.viz.aviation.guidance.ViewerTab#updateTextMarkers(boolean)
     * @param fltCat
     *            When true do not highlight alert text
     * @param allCheck
     *            When true do not highlight alert text
     */
    protected void updateTextMarkers(boolean fltCat, boolean allCheck) {
        ResourceConfigMgr configMgr = ResourceConfigMgr.getInstance();

        super.updateTextMarkers(fltCat, false);

        if (!fltCat && !allCheck) {
            Map<String, String[]> alertMap = TafMonitorDlg
                    .getCurrentAlertMap(siteID);

            if (alertMap != null && alertMap.size() > 0) {
                for (String key : alertMap.keySet()) {
                    if (key.equals("cat")) { // "cat" involves "visibility" and
                                             // "sky condition"
                        colorViewerAlert("vsby", configMgr);
                        colorViewerAlert("sky", configMgr);
                    } else {
                        colorViewerAlert(key, configMgr);
                    }
                }
            }
        }

        for (String key : alertMap.keySet()) {
            String[] tags = alertMap.get(key);
            removeMarkers(tags[0], tags[1]);
        }
    }

    /**
     * Highlight text contained within the key's tags.
     * 
     * @param key
     * @param configMgr
     */
    private void colorViewerAlert(String key, ResourceConfigMgr configMgr) {
        String[] tags = alertMap.get(key);
        String startTag = tags[0];
        String endTag = tags[1];
        StyledText stText = textComp.getDataStTxt();
        StringBuilder sb = new StringBuilder(stText.getText());

        int startTagIdx = sb.indexOf(startTag);
        int endTagIdx = -1;

        while (startTagIdx != -1) {
            endTagIdx = sb.indexOf(endTag, startTagIdx);

            StyleRange sr = new StyleRange(startTagIdx + startTag.length(),
                    endTagIdx - (startTagIdx + startTag.length()), null,
                    configMgr.getViwerAlertColor());
            stText.setStyleRange(sr);

            startTagIdx = sb.indexOf(startTag, endTagIdx);
        }
    }

    /**
     * Getter/Accessor of stationList (based on configuration)
     * 
     * @return stationList
     */
    @Override
    public List<String> getStationList() {
        return stationList;
    }

    /**
     * Setter/Mutator of stationList (based on configuration)
     * 
     * @param stationList
     */
    @Override
    public void setStationList(List<String> stationList) {
        this.stationList = stationList;
    }

    /**
     * Add a listener to the check boxes that impact alert highlighting.
     * 
     * @param listener
     */
    public void addHighlightAlertSelectionListener(SelectionListener listener) {
        allChk.addSelectionListener(listener);
        flightCatChk.addSelectionListener(listener);
    }

    /**
     * Get cached site object of the requested size or larger.
     * 
     * @param siteID
     *            - site object is for
     * @param size
     *            - desired size
     * @return siteObj or null if none in cache
     */
    public String myGetCacheSiteObj(String siteID, String size) {
        String tag = getTag(siteID, size);
        String siteObj = getCacheSiteObj(siteID, tag);
        if (siteObj == null) {
            int index = 0;
            for (index = 0; index < hours.length; ++index) {
                if (size.equals(hours[index])) {
                    break;
                }
            }
            for (++index; index < hours.length; ++index) {
                siteObj = getCacheSiteObj(siteID, getTag(siteID, hours[index]));
                if (siteObj != null) {
                    break;
                }
            }
        }
        return siteObj;
    }

    /**
     * This creates a Metar cache data request.
     * 
     * @param siteID
     * @param size
     * @return req
     */
    private CacheGuidanceRequest createCacheRequest(String siteID, String size) {
        MetarCacheGuidanceRequest req = new MetarCacheGuidanceRequest();
        req.setTag(getTag(siteID, size));
        req.setGuidanceType(GuidanceType.METAR);
        req.setSiteID(siteID);
        req.setSize(size);
        return req;
    }

    /**
     * Queue a cache request for the list of sites.
     */
    @Override
    public void generateCache(List<String> siteIDs) {
        String size = prevChkHrs;
        if (allChkIsSelected) {
            size = allChkHrs;
        }
        for (String siteID : siteIDs) {
            CacheGuidanceRequest req = createCacheRequest(siteID, size);
            cacheEnqueue(req);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.raytheon.viz.aviation.guidance.ViewerTab#alertSites(java.util.ArrayList
     * )
     */
    @Override
    public void alertSites(final ArrayList<String> siteIDs) {
        if (isDisposed()) {
            return;
        }
        Map<String, ArrayList<String>> tags = new HashMap<String, ArrayList<String>>();
        // Clear all cached objects for the list of sites.
        for (String siteID : siteIDs) {
            ArrayList<String> siteTags = new ArrayList<String>();
            tags.put(siteID, siteTags);
            for (String size : hours) {
                siteTags.add(getTag(siteID, size));
            }
        }
        PythonCacheGuidanceJob.getInstance().clearSiteObjs(tags);
        super.alertSites(siteIDs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.raytheon.viz.aviation.guidance.ViewerTab#getSite(java.lang.String)
     */
    @Override
    public String getSite(String site) {
        return getSiteData(site).getMetar()[0];
    }
}
