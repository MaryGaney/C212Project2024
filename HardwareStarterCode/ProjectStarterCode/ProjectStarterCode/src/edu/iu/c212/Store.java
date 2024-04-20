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
        System.out.println("running here");
        List<Staff> storeStaff = getStaffFromFile();
        System.out.println(inventory.size());
        for(Item i: inventory){
            System.out.println(i.getName());
        }
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
                //add an item to the store's inventory
                System.out.println(commandSplit[1]);
            }else if(commandSplit[0].equals("COST")){

            }else if(commandSplit[0].equals("EXIT")){

            }else if(commandSplit[0].equals("FIND")){

            }else if(commandSplit[0].equals("FIRE")){

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
