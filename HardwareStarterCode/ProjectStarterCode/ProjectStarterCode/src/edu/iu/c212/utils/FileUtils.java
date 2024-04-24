package edu.iu.c212.utils;

import edu.iu.c212.models.*;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    //had to ask a tutor about this one :(
    private static final Path basePath = Path.of("./HardwareStarterCode/ProjectStarterCode/ProjectStarterCode/src/edu/iu/c212/resources");
    private static File inputFile = new File(basePath + "/input.txt");
    private static File outputFile = new File(basePath + "/output.txt");
    private static File inventoryFile = new File(basePath + "/inventory.txt");
    //did not use the staffFile because 1. did not come in resources and 2. staff availability could be used in the way
    private static File staffFile = new File(basePath + "/staff.txt");
    private static File staffAvailabilityFile = new File(basePath + "/staff_availability_IN.txt");
    //i use shiftSchedulesFile in the staff scheduler file
    private static File shiftSchedulesFile = new File(basePath + "/shift_schedules_IN.txt");
    private static File storeScheduleFile = new File(basePath + "/store_schedule_OUT.txt");

    /**
     * reads in all the items from inventory.txt
     * @return a list of the items in the inventory of the store
     * @throws IOException
     */
    public static List<Item> readInventoryFromFile() throws IOException {
        System.out.println(inputFile.getAbsolutePath());
        // for this one, save each line of the input as an item in a list
        try{
            BufferedReader br = new BufferedReader(new FileReader(inventoryFile.getPath()));
            String line = null;
            List<Item> ourList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                //splits on the new lines
                String[] splits = line.split(",");
                ourList.add(new Item(splits[0].replace("'",""),Double.parseDouble(splits[1]),Integer.parseInt(splits[2]),Integer.parseInt(splits[3])));
            }
            return ourList;
        } catch (IOException e) {
            System.exit(0);
        }
        return null;
    }

    /**
     * reads all the staff from the file
     * @return a list of the staff members
     * @throws IOException
     */
    public static List<Staff> readStaffFromFile() throws IOException {
        try{
            BufferedReader br = new BufferedReader(new FileReader(staffAvailabilityFile.getPath()));
            String line = null;
            List<Staff> ourList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] splits = line.split(" ");
                //maybe go back and add strip to this later
                ourList.add(new Staff(splits[0] + " " + splits[1], Integer.parseInt(splits[2]), splits[3],splits[4]));
            }
            return ourList;
        } catch (IOException e) {
            System.exit(0);
        }
        return null;
    }

    /**
     *updates the inventory
     * @param items: takes in a list of items
     */
    public static void writeInventoryToFile(List<Item> items) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(inventoryFile.getPath(), false))) {
            for(Item i : items){
                bw.write("'" + i.getName() + "'," + i.getPrice() + "," + i.getQuantity() + "," + i.getAisleNum());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    /**
     * updates the staff file
     * @param employees: list of employees
     */
    public static void writeStaffToFile(List<Staff> employees) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(staffAvailabilityFile.getPath(), false))) {
            for(Staff s : employees){
                bw.write(s.getFullName() +  " " + s.getAge() + " " + s.getRole().substring(0,1) + " " + s.getAvailability());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    /**
     * reads a file of commands and stores them in a list of strings to return
     * @return a list of strings, each string is a commands
     * @throws IOException
     */
    public static List<String> readCommandsFromFile() throws IOException {
        // for this one, save each line of the input as an item in a list
        List<String> ourList = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(inputFile.getPath()));
            String line = null;
            while ((line = br.readLine()) != null) {
                ourList.add(line);
            }
            return ourList;
        } catch (IOException e) {
            System.exit(0);
        }
        //unreachable but needs it
        return ourList;
    }

    /**
     * writes the finalized schedule to a file
     * @param lines: a list of the string that need to be put in the schedule file
     */
    public static void writeStoreScheduleToFile(List<String> lines) {
        // storeScheduleFile
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(storeScheduleFile.getPath(), true))) {
            for(String line: lines){
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    /**
     * appends a string the output file
     * @param line: string that is appended to the file
     */
    public static void writeLineToOutputFile(String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile.getPath(), true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }


}
