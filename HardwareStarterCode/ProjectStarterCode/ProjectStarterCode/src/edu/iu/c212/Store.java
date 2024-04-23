package edu.iu.c212;

import edu.iu.c212.models.Item;
import edu.iu.c212.models.Staff;
import edu.iu.c212.programs.StaffScheduler;
import edu.iu.c212.utils.FileUtils;

import java.io.IOException;
import java.util.*;

public class Store implements IStore{

    //created instance variables for easier use in methods below
    private ArrayList<Item> inven;
    private ArrayList<Staff> employ;

    public Store(){
        takeAction();
    }

    @Override
    public List<Item> getItemsFromFile() {
        try {
            return FileUtils.readInventoryFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Staff> getStaffFromFile() {
        try {
            return FileUtils.readStaffFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveItemsFromFile(List<Item> items) {
        FileUtils.writeInventoryToFile(items);
    }

    @Override
    public void saveStaffFromFile(List<Staff> staff) {
        FileUtils.writeStaffToFile(staff);
    }

    @Override
    public void takeAction() {
        //split up the commands using methods

        //get the working inventory and staff along with an empty list to hold commands
        List<Item> inventory = getItemsFromFile();
        List<Staff> storeStaff = getStaffFromFile();
        List<String> commands;
        //get the actions from the input file
        try {
            commands = FileUtils.readCommandsFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //for each command, read, then write to the output file
        
        for(String command: commands){
            String[] commandSplit = command.split(" ");
                applyCommand(command, commandSplit, inventory, storeStaff);
        }
    }

    public void applyCommand(String command, String[] commandSplit, List<Item> inventory, List<Staff> storeStaff){
        if(commandSplit[0].equals("ADD")){
            Add(command, inventory);
        }else if(commandSplit[0].equals("COST")){
            Cost(command, inventory);
        }else if(commandSplit[0].equals("EXIT")){
            FileUtils.writeLineToOutputFile("Thank you for visiting High's Hardware and Gardening!");
            System.out.println("Press Enter To Continue...");
            Scanner keyboard = new Scanner(System.in);
            if(keyboard.hasNextLine()){
                getUserInput();
            }
        }else if(commandSplit[0].equals("FIND")){
            Find(command,inventory);
        }else if(commandSplit[0].equals("FIRE")){
            Fire(command, storeStaff);
            //go back and edit the staff file
        }else if(commandSplit[0].equals("HIRE")){
            Hire(command, storeStaff, commandSplit);
        }else if(commandSplit[0].equals("PROMOTE")){
            Promote(command, storeStaff, commandSplit);
        }else if(commandSplit[0].equals("SAW")){

        }else if(commandSplit[0].equals("SCHEDULE")){
            StaffScheduler h = new StaffScheduler();
            h.scheduleStaff();
            FileUtils.writeLineToOutputFile("Schedule Created");
        }else if(commandSplit[0].equals("SELL")){
            Sell(command,inventory,commandSplit);

        }else if(commandSplit[0].equals("QUANTITY")){
            Quantity(command,inventory);

        }
    }

    /*

        I do not know if we are allowed to write our own methods
        I am however going to do so because without them, the takeAction() method will be FAR too long

     */

    //add needs to take in
    public List<Item> Add(String command, List<Item> inv){
        //takes in the String com, List<Item> inv
        String name = command.substring(command.indexOf("'")+1, command.lastIndexOf("'"));
        String[] intel = (command.substring(command.lastIndexOf("'")+1)).split("  ");
        inv.add(new Item(name, Double.parseDouble(intel[0]),Integer.parseInt(intel[1]),Integer.parseInt(intel[2])));
        FileUtils.writeInventoryToFile(new ArrayList<Item>(List.of(new Item(name, Double.parseDouble(intel[0]),Integer.parseInt(intel[1]),Integer.parseInt(intel[2])))));
        FileUtils.writeLineToOutputFile(name + " was added to inventory");
        return inv;
    }

    public void Cost(String command, List<Item> inv){
        String name = command.substring(command.indexOf("'")+1, command.lastIndexOf("'"));
        double price1 = 0.0;
        for (int i = 0; i < inv.size(); i++) {
            if(name.equals(inv.get(i).getName())){
                price1 += inv.get(i).getPrice();
            }
        }
        FileUtils.writeLineToOutputFile(name + ": $" + price1);
    }

    public void Find(String command, List<Item> inv){
        boolean found = false;
        int itemN =0;
        String result = "";
        String name = command.substring(command.indexOf("'")+1, command.lastIndexOf("'"));
        for (int i = 0; i < inv.size(); i++) {
            if(name.equals(inv.get(i).getName())){
                found = true;
                itemN = i;
            }
            if(found == true){
                if(inv.get(itemN).getQuantity() > 1){
                    result = inv.get(itemN).getQuantity() + " " + inv.get(itemN).getName() + " are available in aisle " + inv.get(itemN).getAisleNum();
                }else{
                    result = "A " + inv.get(itemN).getName() + " is available in aisle " + inv.get(itemN).getAisleNum();
                }

            }else{
                result = "ERROR: " + name + " cannot be found";
            }
        }
        FileUtils.writeLineToOutputFile(result);
    }

    public void Fire(String command, List<Staff> employees){
        boolean found = false;
        int staffNum = 0;
        String name = command.substring(command.indexOf("'")+1, command.lastIndexOf("'"));
        for(int i = 0; i < employees.size(); i++){
            if (name.equals(employees.get(i).getFullName())) {
                found = true;
                staffNum = i;
            }
        }
        if(found == true){
            FileUtils.writeLineToOutputFile(employees.get(staffNum).getFullName() + " was fired");
            employees.remove(staffNum);
            saveStaffFromFile(employees);
        }else{
            FileUtils.writeLineToOutputFile("ERROR: " + name + " cannot be found");
        }
    }

    public void Hire(String command, List<Staff> employees, String[] commandSplit){
        String name = command.substring(command.indexOf("'")+1, command.lastIndexOf("'")+1).replace("'","");
        String[] hold = name.split(" ");
        String finalName = hold[0] + " " + hold[1].substring(0,1).toUpperCase() + hold[1].substring(1);
        employees.add(new Staff(finalName, Integer.parseInt(commandSplit[3]),commandSplit[4],commandSplit[5]));
        saveStaffFromFile(employees);
        FileUtils.writeLineToOutputFile(finalName + " has been hired as a " + employees.getLast().getRole());
    }

    public void Promote(String command, List<Staff> employees, String[] commandSplit){
        String name = command.substring(command.indexOf("'")+1, command.lastIndexOf("'")+1).replace("'","");
        String[] hold1 = name.split(" ");
        String finalName = hold1[0] + " " + hold1[1].substring(0,1).toUpperCase() + hold1[1].substring(1);
        int staffNum = 0;
        for (int i = 0; i < employees.size(); i++) {
            if(finalName.equals(employees.get(i).getFullName())){
                staffNum = i;
            }
        }
        String result = "";
        if(commandSplit[3].equals("C")){
            result = "You cannot be promoted to cashier...";
        }else if(commandSplit[3].equals("G")){
            if(employees.get(staffNum).getRole().equals("Gardener")){
                result = employees.get(staffNum).getFullName() + " is already a " + employees.get(staffNum).getRole();
            }else {
                Staff hold = new Staff(employees.get(staffNum).getFullName(), employees.get(staffNum).getAge(), "G", employees.get(staffNum).getAvailability());
                employees.remove(employees.get(staffNum));
                employees.add(hold);
                result = hold.getFullName() + " was promoted to " + hold.getRole();
            }
        }else if(commandSplit[3].equals("M")){
            if(employees.get(staffNum).getRole().equals("Manager")){
                result = employees.get(staffNum).getFullName() + " is already a " + employees.get(staffNum).getRole();
            }else{
                Staff hold = new Staff(employees.get(staffNum).getFullName(),employees.get(staffNum).getAge(),"M",employees.get(staffNum).getAvailability());
                employees.remove(employees.get(staffNum));
                employees.add(hold);
                result = hold.getFullName() + " was promoted to " + hold.getRole();
            }
        }
        saveStaffFromFile(employees);
        FileUtils.writeLineToOutputFile(result);
    }

    public void Sell(String command, List<Item> inventory, String[] commandSplit){
        String name = command.substring(command.indexOf("'")+1, command.lastIndexOf("'"));
        int invNum = 0;
        boolean found = false;
        for (int i = 0; i < inventory.size(); i++) {
            if(name.equals(inventory.get(i).getName())){
                found = true;
                invNum = i;
            }
        }
        String result = "";
        if((found == true) || inventory.get(invNum).getQuantity() <= Integer.parseInt(commandSplit[2]) || Integer.parseInt(commandSplit[2]) == 0){
            Item hold = new Item(name,inventory.get(invNum).getPrice(),inventory.get(invNum).getQuantity()-Integer.parseInt(commandSplit[2]),inventory.get(invNum).getAisleNum());
            inventory.remove(invNum);
            inventory.add(hold);
            if(Integer.parseInt(commandSplit[2]) <= 1) {
                result = commandSplit[2] + " " + name + " was sold";
            }else{
                result =  commandSplit[2] + " " + name + " were sold";
            }
        }else{
            result = "ERROR: " + name + " could not be sold";
        }
        FileUtils.writeLineToOutputFile(result);
    }

    public void Quantity(String command, List<Item> inventory){
        String name = command.substring(command.indexOf("'")+1, command.lastIndexOf("'")).toLowerCase(Locale.ROOT);
        boolean found = false;
        int invNum = 0;
        String result = "";
        for (int i = 0; i < inventory.size(); i++) {
            if(name.equals(inventory.get(i).getName().toLowerCase())){
                found = true;
                invNum = i;
            }
        }
        if(found == true){
            result = String.valueOf(inventory.get(invNum).getQuantity());
        }else{
            result = "ERROR: " + name + " could not be found";
        }
        FileUtils.writeLineToOutputFile(result);
    }

    public void getUserInput(){
        System.out.println("Welcome to High's Hardware and Gardening \n Type EXIT to exit the program at any time \n or type your commands");
        //press enter again to end program
        Scanner userLine = new Scanner(System.in);


    }

}
