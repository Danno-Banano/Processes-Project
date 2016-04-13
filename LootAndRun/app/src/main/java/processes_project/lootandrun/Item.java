package processes_project.lootandrun;

/**
 * Created by danny on 3/31/2016.
 */
public class Item {
    private String name;
    private int power;
    private String itemType;

    public Item( String name, int power, String itemType )              // Added this constructor because I would think if an Item is created it must have these components to
    {                                                                   // begin with. - Daniel Healy
        this.name = name;
        this.power = power;
        this.itemType = itemType;
    }

    @Override
    public String toString()                    // I added this method to override the orginal toString method, this is so that what I can get the name of the item
    {                                           // to display in the Inventory. - Daniel Healy
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
