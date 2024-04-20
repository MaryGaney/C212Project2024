package edu.iu.c212;

import edu.iu.c212.models.Item;
import edu.iu.c212.models.Staff;

import java.util.*;

public interface IStore {
    //interface that the store class implements
    abstract List<Item> getItemsFromFile();
    abstract List<Staff> getStaffFromFile();
    abstract void saveItemsFromFile(List<Item> items);
    abstract void saveStaffFromFile(List<Staff> staff);
    abstract void takeAction();

}
