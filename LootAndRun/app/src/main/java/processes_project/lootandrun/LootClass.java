package processes_project.lootandrun;
import java.util.*;

import static processes_project.lootandrun.MainMap.*;

/**
 * Created by danny on 4/7/2016.
 */
public class LootClass {
    Item[] lootTable = new Item[]{new Item("Glock", 10, "Weapon"), new Item("M16", 15, "Weapon"), new Item("Karambit", 5, "Weapon"), new Item("Aluminum Bat", 5, "Weapon"), new Item("Spiked Bat", 5, "Weapon"), new Item("AK-47", 15, "Weapon"),
            new Item("Baretta", 10, "Weapon"), new Item("Frying Pan", 5, "Weapon"), new Item("Dragunov SVD", 20, "Weapon"), new Item("RPG", 50, "Weapon"), new Item("Rotten Sandwich", 5, "First Aid"), new Item("Beef Jerky", 5, "First Aid"), new Item("Flesh", 5, "First Aid"),
            new Item("Bandage", 10, "First Aid"), new Item("Antiseptic", 10, "First Aid"), new Item("Hamburger", 10, "First Aid"), new Item("Morphine", 10, "First Aid"), new Item("Sirloin Steak", 15, "First Aid"), new Item("Spinach", 25, "First Aid"), new Item("Adrenaline", 50, "First Aid"),
            new Item("Casual Friday", 5, "Armor"), new Item("Classy Suit", 5, "Armor"), new Item("Wooden Armor", 5, "Armor"), new Item("Umpire's Armor", 10, "Armor"), new Item("Football Gear", 10, "Armor"), new Item("Swat Uniform", 15, "Armor"), new Item("Classy Suit w/ Kevlar", 15, "Armor"),
            new Item("Half Life 3 Confirmed", 20, "Armor"), new Item("Power Armor", 25, "Armor"), new Item("Experimental X Armor", 50, "Armor"),};

    // Used in either combat class or cache manager in order to take items from loot table

    public Item randomLooter() {
        Item lootItem;
        int index = new Random().nextInt(lootTable.length);
        lootItem = lootTable[index];
        return lootItem;
    }

    // Loots from a cache depending on items already contained within the player inventory

    protected void cacheLoot() {
        Item newItem = randomLooter();
        boolean check = false;
        for (Item temp : getMainPlayer().getInventory()) {
            if (temp == newItem) {
                check = true;
            }
        }
        if (check == false) {
            getMainPlayer().addItemToInventory(newItem);
        }
    }

    public String toString() {
        return "Cache";
    }
}


