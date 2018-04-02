package com.a4.cis350.when2meetmobile;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by stephaniestang on 2/18/18.
 */

@IgnoreExtraProperties
public class Schedule {

    private HashMap<String, LinkedList<String>> schedule;

    public Schedule() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Schedule(HashMap<String, LinkedList<String>> schedule) {
        this.schedule = schedule;
    }

    public void setSchedule(HashMap<String, LinkedList<String>> schedule) {this.schedule = schedule; }

    public HashMap<String, LinkedList<String>> getSchedule() {
        return schedule;
    }
}

//    public void addAvailability(String day, String timeBlock){
//        LinkedList<String> times = weeklySchedule.get(day);
//        if(times == null) {
//            times = new LinkedList<>();
//        }
//        times.add(timeBlock);
//        weeklySchedule.put(day, times);
//    }

