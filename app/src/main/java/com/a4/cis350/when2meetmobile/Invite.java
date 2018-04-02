package com.a4.cis350.when2meetmobile;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Daniel on 3/29/2018.
 */

@IgnoreExtraProperties
public class Invite {

    public String eventKey;         // unique key / id assigned to this event in firebase
    public String inviteName;             // name of the invitation
    public String adminEmail;

    public Invite() {
        // Default constructor required for calls to DataSnapshot.getValue(Invite.class)
    }

    public Invite(String eventKey, String inviteName, String adminEmail) {
        this.eventKey = eventKey;
        this.inviteName = inviteName;
        this.adminEmail = adminEmail;
    }

    public String getEventKey() {
        return eventKey;
    }

    public String getInviteName() {
        return inviteName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public void setInviteName(String inviteName) {
        this.inviteName = inviteName;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
}
