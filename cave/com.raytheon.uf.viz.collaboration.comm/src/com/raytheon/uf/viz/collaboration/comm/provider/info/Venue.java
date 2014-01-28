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
package com.raytheon.uf.viz.collaboration.comm.provider.info;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.raytheon.uf.viz.collaboration.comm.identity.info.IVenue;
import com.raytheon.uf.viz.collaboration.comm.provider.user.IDConverter;
import com.raytheon.uf.viz.collaboration.comm.provider.user.UserId;

/**
 * Provides information about a venue.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Mar 1, 2012            jkorman     Initial creation
 * Dec  6, 2013 2561       bclement    removed ECF
 * Jan 28, 2014 2698       bclement    removed getInfo, added methods to replace
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */
public class Venue implements IVenue {

    private final MultiUserChat muc;

    private Map<String, Presence> presenceMap = new HashMap<String, Presence>();

    public Venue(XMPPConnection conn, MultiUserChat muc) {
        this.muc = muc;
    }

    @Override
    public Collection<UserId> getParticipants() {
        Set<UserId> participants = new HashSet<UserId>();
        Iterator<String> iter = muc.getOccupants();
        while (iter.hasNext()) {
            String id = iter.next();
            participants.add(IDConverter.convertFromRoom(muc, id));
        }
        return participants;
    }

    @Override
    public Presence getPresence(UserId user) {
        Presence presence = presenceMap.get(user.getNormalizedId());
        if (presence == null) {
            presence = new Presence(Type.unavailable);
            presence.setMode(Mode.away);
        }
        return presence;
    }

    public void handlePresenceUpdated(UserId fromID, Presence presence) {
        presenceMap.put(fromID.getNormalizedId(), presence);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.raytheon.uf.viz.collaboration.comm.identity.info.IVenue#getId()
     */
    @Override
    public String getName() {
        return StringUtils.parseName(getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.raytheon.uf.viz.collaboration.comm.identity.info.IVenue#getId()
     */
    @Override
    public String getId() {
        return muc.getRoom();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.raytheon.uf.viz.collaboration.comm.identity.info.IVenue#
     * getParticipantCount()
     */
    @Override
    public int getParticipantCount() {
        return muc.getOccupantsCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.raytheon.uf.viz.collaboration.comm.identity.info.IVenue#getSubject()
     */
    @Override
    public String getSubject() {
        return muc.getSubject();
    }

}
