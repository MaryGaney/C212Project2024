package edu.iu.c212;

import edu.iu.c212.models.Item;
import edu.iu.c212.models.Staff;
import edu.iu.c212.utils.FileUtils;

import java.io.IOException;
import java.util.*;

public class Store implements IStore{

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
            if(commandSplit[0].equals("ADD")){
                String name = command.substring(command.indexOf("'")+1, command.lastIndexOf("'"));
                String[] intel = (command.substring(command.lastIndexOf("'")+1)).split("  ");
                inventory.add(new Item(name, Double.parseDouble(intel[0]),Integer.parseInt(intel[1]),Integer.parseInt(intel[2])));
                FileUtils.writeInventoryToFile(new ArrayList<Item>(List.of(new Item(name, Double.parseDouble(intel[0]),Integer.parseInt(intel[1]),Integer.parseInt(intel[2])))));
            }else if(commandSplit[0].equals("COST")){
                String name = command.substring(command.indexOf("'")+1, command.lastIndexOf("'"));
                double price1 = 0.0;
                for (int i = 0; i < inventory.size(); i++) {
                    if(name.equals(inventory.get(i).getName())){
                        price1 += inventory.get(i).getPrice();
                    }
                }
                FileUtils.writeLineToOutputFile(name + ": $" + price1);
            }else if(commandSplit[0].equals("EXIT")){

            }else if(commandSplit[0].equals("FIND")){
                boolean found = false;
                int itemN =0;
                String result = "";
                String name = command.substring(command.indexOf("'")+1, command.lastIndexOf("'"));
                for (int i = 0; i < inventory.size(); i++) {
                    if(name.equals(inventory.get(i).getName())){
                        found = true;
                        itemN = i;
                    }
                    if(found == true){
                        if(inventory.get(itemN).getQuantity() > 1){
                            result = inventory.get(itemN).getQuantity() + " " + inventory.get(itemN).getName() + " are available in aisle" + inventory.get(itemN).getAisleNum();
                        }else{
                            result = "A " + inventory.get(itemN).getName() + " is available in aisle" + inventory.get(itemN).getAisleNum();
                        }

                    }else{
                        result = "ERROR: " + name + " cannot be found";
                    }
                }
            }else if(commandSplit[0].equals("FIRE")){
                //for i in list name
                //if the commandSplit[1].equals(i.name())

            }else if(commandSplit[0].equals("HIRE")){

            }else if(commandSplit[0].equals("PROMOTE")){

            }else if(commandSplit[0].equals("SAW")){

            }else if(commandSplit[0].equals("SCHEDULE")){

            }else if(commandSplit[0].equals("SELL")){

            }else if(commandSplit[0].equals("QUANTITY")){

            }
        }


    }
}
