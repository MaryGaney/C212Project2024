package edu.iu.c212;

import edu.iu.c212.models.Item;
import edu.iu.c212.models.Staff;
import edu.iu.c212.programs.StaffScheduler;
import edu.iu.c212.utils.FileUtils;

import java.io.IOException;
import java.util.*;

import static edu.iu.c212.programs.SawPrimePlanks.getPlankLengths;

public class Store implements IStore{


    /**
     * this method instantiates the store
     */
    public Store(){
        takeAction();
    }

    /**
     * this method reads the inventory from the inventory file
     * @return: a list of items in the inventory
     */
    @Override
    public List<Item> getItemsFromFile() {
        try {
            return FileUtils.readInventoryFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * this method reads the staff from the availability file
     * @return: the list of staff members
     */
    @Override
    public List<Staff> getStaffFromFile() {
        try {
            return FileUtils.readStaffFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * this method updates the inventory file with the current staff list
     * @param items: the inventory list to update the file with
     */
    @Override
    public void saveItemsFromFile(List<Item> items) {
        FileUtils.writeInventoryToFile(items);
    }

    /**
     * this method updates the staff file with the current staff list
     * @param staff: the staff list to update the file with
     */
    @Override
    public void saveStaffFromFile(List<Staff> staff) {
        FileUtils.writeStaffToFile(staff);
    }

    /**
     * this method
     *   -creates the inventory list
     *   -creates the staff list
     *   -takes in the commands from the input file and feeds them to the applyCommand method
     */
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

    /**
     * this function calls the respective function for each command
     * @param command: the string version of the command, contains all of the arguments needed for each command to run
     * @param commandSplit: the split version of the string command
     * @param inventory: the list of items in the inventory
     * @param storeStaff: the list of the employees in the staff
     */
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
                getUserInput(inventory, storeStaff);
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
            Map<Item,Integer> holdThePlanksPlease = new HashMap<>();
            for (int i = 0; i < inventory.size(); i++) {
                if(inventory.get(i).getName().replace("'","").startsWith("Plank")){
                    holdThePlanksPlease.put(inventory.get(i),i);
                }
            }
            ArrayList<Item> fin = new ArrayList<>();
            for(Item i: holdThePlanksPlease.keySet()){
                int mainNum = Integer.parseInt(i.getName().substring(6,9));
                List<Integer> result = getPlankLengths(mainNum);
                Item hold = new Item("Plank-" + result.getFirst(), result.getFirst()*result.getFirst(),result.size(),inventory.get(holdThePlanksPlease.get(i)).getAisleNum());
                inventory.remove(inventory.get(holdThePlanksPlease.get(i)));
                inventory.add(hold);
            }
            saveItemsFromFile(inventory);
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

    /**
     * this method adds an item to the inventory list
     *
     * @param command: the string of the command, all of the arguments of the add function are in this string
     * @param inv:     the list of inventory items to add to
     */
    public void Add(String command, List<Item> inv){
        try {
            //takes in the String com, List<Item> inv
            String name = command.substring(command.indexOf("'") + 1, command.lastIndexOf("'"));
            String[] intel = (command.substring(command.lastIndexOf("'") + 1)).split("  ");
            inv.add(new Item(name, Double.parseDouble(intel[0]), Integer.parseInt(intel[1]), Integer.parseInt(intel[2])));
            FileUtils.writeInventoryToFile(inv);
            FileUtils.writeLineToOutputFile(name + " was added to inventory");
        }catch (NumberFormatException | StringIndexOutOfBoundsException e){
            System.out.println("Remember to format your command as follows: ADD(1 space)'Name'(1 Space)Price(2 spaces)Quantity(2 spaces)AisleNumber(2 spaces)");
        }
    }

    /**
     * this method takes in an item, finds it (if it exists) in the inventory, and outputs the cost to the output file
     * @param command:  the string of the command, all of the arguments of the cost function are in this string
     * @param inv: the list of inventory items to find the cost in
     */
    public void Cost(String command, List<Item> inv){
        try{
            String name = command.substring(command.indexOf("'")+1, command.lastIndexOf("'"));
            double price1 = 0.0;
            for (int i = 0; i < inv.size(); i++) {
                if(name.equals(inv.get(i).getName())){
                    price1 += inv.get(i).getPrice();
                }
            }
            FileUtils.writeLineToOutputFile(name + ": $" + price1);
        }catch (NumberFormatException | StringIndexOutOfBoundsException e){
            System.out.println("Remember to format your command as follows: COST(1 space)'Name'");
        }
    }

    /**
     * this method takes in a string of an item and finds it in the inventory, it returns the quantity of the item
     * @param command: the string of the command, all of the arguments of the find function are in this string
     * @param inv: the list of inventory items to find the cost in
     */
    public void Find(String command, List<Item> inv){
        try{
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
        }catch (NumberFormatException | StringIndexOutOfBoundsException e){
            System.out.println("Remember to format your command as follows: FIND(1 space)'Name'");
        }
    }

    /**
     * this method takes in the string of the command and the list of employees on the staff and fires the specified employee
     * in the command call
     * @param command: the string of the command, all of the arguments of the fire function are in this string
     * @param employees: the list of staff items to fire the employee from
     */
    public void Fire(String command, List<Staff> employees){
        try {
            boolean found = false;
            int staffNum = 0;
            String name = command.substring(command.indexOf("'") + 1, command.lastIndexOf("'"));
            for (int i = 0; i < employees.size(); i++) {
                if (name.equals(employees.get(i).getFullName())) {
                    found = true;
                    staffNum = i;
                }
            }
            if (found == true) {
                FileUtils.writeLineToOutputFile(employees.get(staffNum).getFullName() + " was fired");
                employees.remove(staffNum);
                saveStaffFromFile(employees);
            } else {
                FileUtils.writeLineToOutputFile("ERROR: " + name + " cannot be found");
            }
        }catch (NumberFormatException | StringIndexOutOfBoundsException e){
            System.out.println("Remember to format your command as follows: FIRE(1 space)'FirstName LastName'");
        }
    }

    /**
     * this method adds an employee to the staff
     * @param command: the string of the command, all of the arguments of the hire function are in this string
     * @param employees: the list of staff items to fire the employee from
     * @param commandSplit: the split list of the command string
     */
    public void Hire(String command, List<Staff> employees, String[] commandSplit){
        try {
            String name = command.substring(command.indexOf("'") + 1, command.lastIndexOf("'") + 1).replace("'", "");
            String[] hold = name.split(" ");
            String finalName = hold[0] + " " + hold[1].substring(0, 1).toUpperCase() + hold[1].substring(1);
            employees.add(new Staff(finalName, Integer.parseInt(commandSplit[3]), commandSplit[4], commandSplit[5]));
            saveStaffFromFile(employees);
            FileUtils.writeLineToOutputFile(finalName + " has been hired as a " + employees.getLast().getRole());
        }catch (NumberFormatException | StringIndexOutOfBoundsException e){
            System.out.println("Remember to format your command as follows: HIRE(1 space)'FirstName LastName'");
        }
    }

    /**
     * this method promotes an employee on the staff
     * @param command: the string of the command, all of the arguments of the promote function are in this string
     * @param employees: the list of staff items to fire the employee from
     * @param commandSplit: the split list of the command string
     */
    public void Promote(String command, List<Staff> employees, String[] commandSplit){
        try {
            String name = command.substring(command.indexOf("'") + 1, command.lastIndexOf("'") + 1).replace("'", "");
            String[] hold1 = name.split(" ");
            String finalName = hold1[0] + " " + hold1[1].substring(0, 1).toUpperCase() + hold1[1].substring(1);
            int staffNum = 0;
            for (int i = 0; i < employees.size(); i++) {
                if (finalName.equals(employees.get(i).getFullName())) {
                    staffNum = i;
                }
            }
            String result = "";
            if (commandSplit[3].equals("C")) {
                result = "You cannot be promoted to cashier...";
            } else if (commandSplit[3].equals("G")) {
                if (employees.get(staffNum).getRole().equals("Gardener")) {
                    result = employees.get(staffNum).getFullName() + " is already a " + employees.get(staffNum).getRole();
                } else {
                    Staff hold = new Staff(employees.get(staffNum).getFullName(), employees.get(staffNum).getAge(), "G", employees.get(staffNum).getAvailability());
                    employees.remove(employees.get(staffNum));
                    employees.add(hold);
                    result = hold.getFullName() + " was promoted to " + hold.getRole();
                }
            } else if (commandSplit[3].equals("M")) {
                if (employees.get(staffNum).getRole().equals("Manager")) {
                    result = employees.get(staffNum).getFullName() + " is already a " + employees.get(staffNum).getRole();
                } else {
                    Staff hold = new Staff(employees.get(staffNum).getFullName(), employees.get(staffNum).getAge(), "M", employees.get(staffNum).getAvailability());
                    employees.remove(employees.get(staffNum));
                    employees.add(hold);
                    result = hold.getFullName() + " was promoted to " + hold.getRole();
                }
            }
            saveStaffFromFile(employees);
            FileUtils.writeLineToOutputFile(result);
        }catch (NumberFormatException | StringIndexOutOfBoundsException e){
            System.out.println("Remember to format your command as follows: PROMOTE(1 space)'FirstName LastName'(1 space)Role");
        }
    }

    /**
     * this method sells an item in the inventory
     * @param command: the string of the command, all of the arguments of the sell function are in this string
     * @param inventory: the list of inventory items to find the cost in
     * @param commandSplit: the split list of the command string
     */
    public void Sell(String command, List<Item> inventory, String[] commandSplit){
        try {
            String name = command.substring(command.indexOf("'") + 1, command.lastIndexOf("'"));
            int invNum = 0;
            boolean found = false;
            for (int i = 0; i < inventory.size(); i++) {
                if (name.equals(inventory.get(i).getName())) {
                    found = true;
                    invNum = i;
                }
            }
            String result = "";
            if ((found == true) || inventory.get(invNum).getQuantity() <= Integer.parseInt(commandSplit[2]) || Integer.parseInt(commandSplit[2]) == 0) {
                Item hold = new Item(name, inventory.get(invNum).getPrice(), inventory.get(invNum).getQuantity() - Integer.parseInt(commandSplit[2]), inventory.get(invNum).getAisleNum());
                inventory.remove(invNum);
                inventory.add(hold);
                if (Integer.parseInt(commandSplit[2]) <= 1) {
                    result = commandSplit[2] + " " + name + " was sold";
                } else {
                    result = commandSplit[2] + " " + name + " were sold";
                }
            } else {
                result = "ERROR: " + name + " could not be sold";
            }
            FileUtils.writeLineToOutputFile(result);
        }catch (NumberFormatException | StringIndexOutOfBoundsException e){
            System.out.println("Remember to format your command as follows: SELL(1 space)'Name'(1 space)Quantity");
        }
    }

    /**
     * this function returns the quantity of the item in the inventory
     * @param command:  the string of the command, all of the arguments of the quantity function are in this string
     * @param inventory:  the list of inventory items to find the cost in
     */
    public void Quantity(String command, List<Item> inventory){
        try {
            String name = command.substring(command.indexOf("'") + 1, command.lastIndexOf("'")).toLowerCase(Locale.ROOT);
            boolean found = false;
            int invNum = 0;
            String result = "";
            for (int i = 0; i < inventory.size(); i++) {
                if (name.equals(inventory.get(i).getName().toLowerCase())) {
                    found = true;
                    invNum = i;
                }
            }
            if (found == true) {
                result = String.valueOf(inventory.get(invNum).getQuantity());
            } else {
                result = "ERROR: " + name + " could not be found";
            }
            FileUtils.writeLineToOutputFile(result);
        }catch (NumberFormatException | StringIndexOutOfBoundsException e){
            System.out.println("Remember to format your command as follows: Quantity(1 space)'Name'");
        }
    }

    /**
     * this item gets the user input from the terminal and runs the commands that the user inputs
     * @param inventory: this is the list of items in the inventory of the store
     * @param storeStaff: this is the list of employees on the staff
     */
    public void getUserInput(List<Item> inventory, List<Staff> storeStaff){
        System.out.println("Welcome to High's Hardware and Gardening \nType EXIT to exit the program at any time \nOr type your commands");
        //press enter again to end program
        Scanner userLine = new Scanner(System.in);
        while (true) {
            String line = userLine.nextLine();
            String[] lineSplit = line.split(" ");
            if ("EXIT".equalsIgnoreCase(line)) {
                break;
            }else{
                applyCommand(line, lineSplit, inventory, storeStaff);
            }
        }
    }

}
