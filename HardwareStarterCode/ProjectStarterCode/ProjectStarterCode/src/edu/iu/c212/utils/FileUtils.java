package edu.iu.c212.utils;

import edu.iu.c212.models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static File inputFile = new File("C:\\Users\\youju\\OneDrive\\Documents\\GitHub\\C212Project2024\\HardwareStarterCode\\ProjectStarterCode\\ProjectStarterCode\\src\\edu\\iu\\c212\\resources\\input.txt");
    private static File outputFile = new File("C:\\Users\\youju\\OneDrive\\Documents\\GitHub\\C212Project2024\\HardwareStarterCode\\ProjectStarterCode\\ProjectStarterCode\\src\\edu\\iu\\c212\\resources\\output.txt");
    private static File inventoryFile = new File("C:\\Users\\maryg\\OneDrive\\Documents\\GitHub\\C212Project\\HardwareStarterCode\\ProjectStarterCode\\ProjectStarterCode\\src\\edu\\iu\\c212\\resources\\inventory.txt");
    private static File staffFile = new File("../resources/staff.txt");
    private static File staffAvailabilityFile = new File("C:\\Users\\youju\\OneDrive\\Documents\\GitHub\\C212Project2024\\HardwareStarterCode\\ProjectStarterCode\\ProjectStarterCode\\src\\edu\\iu\\c212\\resources\\staff_availability_IN.txt");
    private static File shiftSchedulesFile = new File("C:\\Users\\youju\\OneDrive\\Documents\\GitHub\\C212Project2024\\HardwareStarterCode\\ProjectStarterCode\\ProjectStarterCode\\src\\edu\\iu\\c212\\resources\\shift_schedules_IN.txt");
    private static File storeScheduleFile = new File("C:\\Users\\youju\\OneDrive\\Documents\\GitHub\\C212Project2024\\HardwareStarterCode\\ProjectStarterCode\\ProjectStarterCode\\src\\edu\\iu\\c212\\resources\\store_schedule_OUT.txt");

    /**
     * reads in all the items from inventory.txt
     * @return a list of the items in the inventory of the store
     * @throws IOException
     */
    public static List<Item> readInventoryFromFile() throws IOException {
//        System.out.println("this is throwing at readInventoryFromFile");
//        System.out.println(inventoryFile.exists());
        // depending on your OS, toURI() may need to be used when working with paths
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
//        System.out.println("this is throwing at readStaffFromFile");
//        System.out.println(staffAvailabilityFile.getPath() + "\n" + staffAvailabilityFile.exists());
        try{
            BufferedReader br = new BufferedReader(new FileReader(staffAvailabilityFile.getPath()));
            String line = null;
            List<Staff> ourList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                //splits on the new lines
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
     *saves all the items from inventory.txt
     * @param items
     */
    public static void writeInventoryToFile(List<Item> items) {
        //takes the initial file and rewrites it
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("tempInv"));
            String line;
            for (int i = 0; i < items.size(); i++) {
                bw.write(String.valueOf(items.get(i)));
            }
        } catch (Exception e) {
            return;
        }finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                //
            }
        }
        File oldFile = new File(inventoryFile.getPath());
        oldFile.delete();

        File newFile = new File("temp");
        newFile.renameTo(new File(inventoryFile.getName()));

    }

    /**
     * saves al the staff to a file
     * @param employees
     */
    public static void writeStaffToFile(List<Staff> employees) {
        // TODO
    }

    /**
     * reads a file of commands and stores them in a list of strings to return
     * @return a list of strings, each string is a commands
     * @throws IOException
     */
    public static List<String> readCommandsFromFile() throws IOException {
        //System.out.println(inputFile/*.toURI()*/.getPath() + "\n" + inputFile.exists());
        // depending on your OS, toURI() may need to be used when working with paths
        // for this one, save each line of the input as an item in a list
        try{
            BufferedReader br = new BufferedReader(new FileReader(inputFile.getPath()));
            String line = null;
            List<String> ourList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                ourList.add(line);
            }
            return ourList;
        } catch (IOException e) {
            System.exit(0);
        }
        return null;
    }

    /**
     *
     * @param lines
     */
    public static void writeStoreScheduleToFile(List<String> lines) {
        // TODO
    }

    /**
     *
     * @param line
     */
    public static void writeLineToOutputFile(String line) {
        // TODO
    }

}
