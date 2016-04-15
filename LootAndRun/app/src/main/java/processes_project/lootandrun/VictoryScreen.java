package processes_project.lootandrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class VictoryScreen extends AppCompatActivity {

    public ArrayList<Item> playerInventory = MainMap.getMainPlayer().getInventory();
    TextView loot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory_screen);
        loot = (TextView) (findViewById(R.id.lootText));
        displayLootz();
    }

    public void displayLootz() {
        loot.setText("You collected a " + playerInventory.get(playerInventory.size()-1) + " from the corpse.");

    }
    public void returnToMap(View view)
    {
        finish();
        Intent intent = new Intent(this, MainMap.class);
        startActivity(intent);


    }
}
