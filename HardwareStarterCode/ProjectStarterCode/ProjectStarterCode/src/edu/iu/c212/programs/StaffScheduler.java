package edu.iu.c212.programs;
import edu.iu.c212.models.Staff;
import edu.iu.c212.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class StaffScheduler {
    private static final Path basePath = Path.of("./HardwareStarterCode/ProjectStarterCode/ProjectStarterCode/src/edu/iu/c212/resources");
    private static File shiftSchedulesFile = new File(basePath + "/shift_schedules_IN.txt");

    /**
     * this method schedules the store staff
     * it reads in a schedule of the store hours
     * then it calcuates the total amount of hours the store is open
     * it counts the amount of staffers and divides the hours evenly amongst the workers
     * finally it schedules the staffers to the available days and time slots
     * it outputs this schedule to the file staff_schedule_OUT.txt
     */
    public void scheduleStaff(){
        Map<String,Double> hours = new HashMap<>();
        List<Staff> storeStaff;
        try {
            storeStaff = FileUtils.readStaffFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //reads in staff availibility and shifts
        //create a store staff
        List<String> scheduleFile = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(shiftSchedulesFile.getPath()));
            String line = null;
            while ((line = br.readLine()) != null) {
                scheduleFile.add(line);
            }
        } catch (IOException e) {
            System.exit(0);
        }

        //calculate the amount of hours
        int totalHours = 0;
        for (int i = 0; i < scheduleFile.size(); i++) {
            String[] hold = scheduleFile.get(i).split(" ");
            String day = hold[0];
            double openHour =(Double.parseDouble(hold[2]) - Double.parseDouble(hold[1]));
            if(openHour % 100 == 30){
                openHour += 20;
            }
            hours.put(day,openHour/100.0);
            totalHours += openHour/100.0;
        }

       //the staff member, and a map of their day, and if they can work that day
        Map<String,Map<String,Boolean>> h = new HashMap<>();
        //put an empty hash map in for each worker, it could be just an array list of strings (days of the week)
        // i just like hashmaps more <3
        for (int i = 0; i < storeStaff.size(); i++) {
            h.put(storeStaff.get(i).getFullName(), new HashMap<String, Boolean>());
        }
        //go through the staff and enter which days they can work into the hashmaps
        for (int i = 0; i < storeStaff.size(); i++) {
            String[] sched = storeStaff.get(i).getAvailability().split("\\.");
            for (int j = 0; j < sched.length; j++) {
                if(sched[j].equals("M")){
                    h.get(storeStaff.get(i).getFullName()).put("M",true);
                }else if(sched[j].equals("T")){
                    h.get(storeStaff.get(i).getFullName()).put("T",true);
                }else if(sched[j].equals("W")){
                    h.get(storeStaff.get(i).getFullName()).put("W",true);
                }else if(sched[j].equals("TR")){
                    h.get(storeStaff.get(i).getFullName()).put("TR",true);
                }else if(sched[j].equals("F")){
                    h.get(storeStaff.get(i).getFullName()).put("F",true);
                }else if(sched[j].equals("SAT")){
                    h.get(storeStaff.get(i).getFullName()).put("SAT",true);
                }else if(sched[j].equals("SUN")){
                    h.get(storeStaff.get(i).getFullName()).put("SUN",true);
                }
            }

        }
        //get day of the week
        //get how many hours open that day is
        //divide by 3 to see how many people to put in shifts
        //go through staff list
        //if the staff can work that corresponding day, then add them to the holder array list
        //adds the array list if the following conditions haven't been met
            //the amount of people working each day
            //the amount of shifts the person has worked

        Map<String,ArrayList<String>> schedu = new HashMap<>();
        //put the days in random order to improve performance of assignments
        String[] daysOfWeek = {"TR","SUN","M","W","SAT","T","F"};
        //populate schedu with empty array list
        for (int i = 0; i < daysOfWeek.length; i++) {
            schedu.put(daysOfWeek[i],new ArrayList<>());
        }
        //enter all the workers as having 0 shifts at the beginning
        Map<String,Integer> shiftCount = new HashMap<>();
        for(String worker: h.keySet()){
            shiftCount.put(worker,0);
        }
        //this is a nasty couple lines of code I apologize
        //splitting up total hours into 3-hour shifts and get shift amount for workers, these are the max amount of shifts each worker can work
        double shiftamt = Math.ceil((totalHours / Double.parseDouble(String.valueOf(storeStaff.size()))) / 3);
        //max 3 people working per day, these are the shifts that are available for workers per day
        int shifts = 3;
        //for each worker in the set
        for(String worker : h.keySet()) {
            //for each day of th week
            for (int i = 0; i < daysOfWeek.length; i++) {
                double openHours = hours.get(daysOfWeek[i]);
                //for each day that the worker is available to work
                for (String day : h.get(worker).keySet()) {
                    //check if the day is equal to the day of the week
                    if (day.equals(daysOfWeek[i])) {
                        //check the amount of shifts the worker has worked so far
                        if (shiftCount.get(worker) < shiftamt) {
                            //if the day isn't full
                            if(schedu.get(day).size() < shifts){
                                //add the worker to the day to work
                                schedu.get(day).add(worker);
                                //for the worker, update the amount of shifts that they have
                                shiftCount.put(worker, shiftCount.get(worker) + 1);

                            }
                        }
                    }
                }
            }
        }

        //sort the map by last name
        for(String day : schedu.keySet()){
            sortLastName(schedu.get(day));
        }

        //output to a file
        //add date
        Date d = new Date();
        //didn't use getTime() because it looked weird lol
        String fLine = "Created on " + d.getMonth() + "/" + d.getDay() + "/" + d.getYear() + " at " + d.getHours() + d.getMinutes();
        ArrayList<String> finSched = new ArrayList<>();
        finSched.add(fLine);
        for (String day: schedu.keySet()){
            finSched.add("" );
        }
        for(String day: schedu.keySet()){
            String hold = day;
            for (int i = 0; i < schedu.get(day).size(); i++) {
                hold += " (" + schedu.get(day).get(i) + ")";
            }
            if(hold.substring(0,2).equals("M ")){
                finSched.set(1,hold);
            }else if(hold.substring(0,2).equals("T ")){
                finSched.set(2,hold);
            }else if(hold.substring(0,2).equals("W ")){
                finSched.set(3,hold);
            }else if(hold.substring(0,2).equals("TR")){
                finSched.set(4,hold);
            }else if(hold.substring(0,2).equals("F ")){
                finSched.set(5,hold);
            }else if(hold.substring(0,4).equals("SAT ")){
                finSched.set(6,hold);
            }else if(hold.substring(0,4).equals("SUN ")){
                finSched.set(7,hold);
            }

        }

        //use file utils to write to the schedule file
        FileUtils.writeStoreScheduleToFile(finSched);

    }

    /**
     * this method sorts the last names of the passed in list, the list is full of strings "First Last"
     * @param named: a list of names, first and last, to be sorted
     */
    public static void sortLastName(List<String> named){
        for (int i = 0; i < named.size(); i++) {
            for (int j = 0; j < named.size(); j++) {
                Collections.sort(named, new Comparator(){
                    public int compare(Object o1, Object o2){
                        String p1 = (String) o1;
                        String p2 = (String) o2;
                        return p1.split(" ")[1].compareToIgnoreCase(p2.split(" ")[1]);
                    }
                });
            }
        }

    }

}
