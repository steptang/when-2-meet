package com.a4.cis350.when2meetmobile;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by stephaniestang on 2/18/18.
 */

@IgnoreExtraProperties
public class User {

    public String name;
    public String bio;
    private LinkedList<Schedule> schedules;
    public String email;
    public List<Invite> invites;      // list of event id's which this user has been invited to
    public List<String> joinedEvents;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String bio, String email, LinkedList<Schedule> schedules,
                List<Invite> invites, List<String> joinedEvents) {
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.schedules = schedules;
        this.invites = invites;
        this.joinedEvents = joinedEvents;
    }

    // determines whether or not this user has joined the event corresponding to eventKey
    public boolean hasJoined(String eventKey) {

        // need to add a listener for

        if (joinedEvents == null || joinedEvents.isEmpty()) {
            return false;
        }

        for (String key : joinedEvents) {

            if (eventKey.equals(key)) {

                // then this user has joined this event
                return true;
            }
        }

        // otherwise
        return false;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setBio(String bio){
        this.bio = bio;
    }

    public void setSchedules(LinkedList<Schedule> schedules) {
        this.schedules = schedules;
    }

    // cannot have a getter for invites, if it is to work
    public void setInvites(List<Invite> invites) {
        this.invites = invites;
    }

    public void setJoinedEvents(List<String> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    public String getEmail() {
        return email;
    }

    public LinkedList<Schedule> getSchedules() {
        return schedules;
    }

//    public List<String> getJoinedEvents() {
//        return joinedEvents;
//    }
}

//@IgnoreExtraProperties
//public class User {
//    private String name;
//    private String bio;
//    //private LinkedList<Schedule> schedules;
//
//    public User(){
//
//    }
//
////    public User(String name, String bio, LinkedList<Schedule> schedules){
////        this.name = name;
////        this.bio = bio;
////        this.schedules = schedules;
////    }
//
//    public void setName(String name){
//        this.name = name;
//    }
//
//    public void setBio(String bio){
//        this.bio = bio;
//    }
//
////    public void setSchedules(LinkedList schedules){
////        this.schedules = schedules;
////    }
//}
