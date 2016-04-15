package processes_project.lootandrun;
//test
import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Danny on 3/30/2016.
 */
public class Character {
    private int health;
    private String charName;
    private int attackDamage;
    private ArrayList<Item> Inventory;
    private Location charLocation;
    private Boolean isDead;

    public void doTheShit(){
        LootClass looter = new LootClass();
        looter.cacheLoot();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public ArrayList<Item> getInventory() {
        return Inventory;
    }

    public void setInventory(ArrayList<Item> inventory) {
        Inventory = inventory;
    }

    public void addItemToInventory( Item item ){ Inventory.add(item);}                          // so I can add an individual item to the inventory - Daniel Healy

    public void removeItemFromInventory( Item item ){ Inventory.remove(item);}                  // so I can remove an individual item from the inventory (IS THAT THE RIGHT METHOD TO USE????) - Daniel Healy

    public Location getCharLocation() {
        return charLocation;
    }

    public void setCharLocation(Location charLocation) {
        this.charLocation = charLocation;
    }

    public void setDead (Boolean isdead)
    {
        this.isDead = isdead;
    }

    public Boolean isDead()
    {
        return isDead;
    }


    public Character(int health, String charName, Location charLocation, int attackDamage, ArrayList<Item> inventory) {
        this.health = health;
        this.charName = charName;
        this.charLocation = charLocation;
        this.attackDamage = attackDamage;
        Inventory = new ArrayList<Item>();
        this.setDead(false);
    }

    public Character() {
        this.health = 100;
        this.charName = "John Doe";
        this.attackDamage = 50;
        Inventory = new ArrayList<Item>();
        this.setDead(false);
    }
}
