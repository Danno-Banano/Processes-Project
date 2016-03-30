package processes_project.lootandrun;

import android.location.Location;

import java.util.HashMap;

/**
 * Created by Danny on 3/30/2016.
 */
public class Character {
    private int health;
    private String charName;
    private int attackDamage;
    private HashMap Inventory;

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

    public HashMap getInventory() {
        return Inventory;
    }

    public void setInventory(HashMap inventory) {
        Inventory = inventory;
    }

    public Location getCharLocation() {
        return charLocation;
    }

    public void setCharLocation(Location charLocation) {
        this.charLocation = charLocation;
    }

    private Location charLocation;

    public Character(int health, String charName, Location charLocation, int attackDamage, HashMap inventory) {
        this.health = health;
        this.charName = charName;
        this.charLocation = charLocation;
        this.attackDamage = attackDamage;
        Inventory = new HashMap();
    }
}
