package com.a4.cis350.when2meetmobile;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by stephaniestang on 2/18/18.
 */

@IgnoreExtraProperties
public class Event {

    public User admin;
    public String name;
    private boolean weekly;
    private boolean locked;
    private ArrayList<String> days;
    private List<Integer> weekdays;
    private String startTime;
    private String endTime;
    private HashMap<String, Schedule> availabilities;
    private boolean isLocked;    // indicates if this event is locked
    private boolean isConfirmed;   // indicates whether the event has been confirmed by the organiser
    private String confirmedDateTime;   // String containing the confirmed date and time - can be converted to another format later

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Event(User admin, String name, boolean weekly, ArrayList<String> days, List<Integer> weekdays,
                 String startTime, String endTime, HashMap<String, Schedule> availabilities, boolean isLocked,
                 boolean isConfirmed, String confirmedDateTime) {
        this.admin = admin;
        this.name = name;
        this.weekly = weekly;
        this.locked = false;
        this.days = days;
        this.weekdays = weekdays;
        this.startTime = startTime;
        this.endTime = endTime;
        this.availabilities = availabilities;
        this.isLocked = isLocked;
        this.isConfirmed = isConfirmed;
        this.confirmedDateTime = confirmedDateTime;
    }

    public User getAdmin(){
        return admin;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDays(ArrayList<String> days) {this.days = days; }

    public ArrayList<String> getDays() {
        return days;
    }

    public String getStartTime(){
        return startTime;
    }

    public String getEndTime(){
        return endTime;
    }

    public HashMap<String, Schedule> getAvailabilities(){
        return availabilities;
    }

    public void setAvailabilities(HashMap<String, Schedule> availabilities){
        this.availabilities = availabilities;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isWeekly() {
        return weekly;
    }

    public void setWeekly(boolean weekly) {
        this.weekly = weekly;
    }

    public List<Integer> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(List<Integer> weekdays) {
        this.weekdays = weekdays;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public String getConfirmedDateTime() {
        return confirmedDateTime;
    }

    public void setConfirmedDateTime() {
        this.confirmedDateTime = confirmedDateTime;
    }
}


//@IgnoreExtraProperties
//public class Event {
//    private User admin;
//    private String name;
//    private LinkedList<String> days;
//    private String startTime;
//    private String endTime;
//    private HashMap<User, Schedule> availabilities;
//
//    public Event() {
//        // Default constructor required for calls to DataSnapshot.getValue(User.class)
//    }
//
//    public Event(User admin, String name, LinkedList<String> days, String startTime, String endTime, HashMap<User, Schedule> availabilities){
//        this.admin = admin;
//        this.name = name;
//        this.days = days;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.availabilities = availabilities;
//    }
//
//    public User getAdmin(){
//        return admin;
//    }
//
//    public String getName(){
//        return name;
//    }
//
//    public LinkedList<String> getDays(){
//        return days;
//    }
//
//    public String getStartTime(){
//        return startTime;
//    }
//
//    public String getEndTime(){
//        return endTime;
//    }
//
//    public HashMap<User, Schedule> getAvailabilities(){
//        return availabilities;
//    }
//}
