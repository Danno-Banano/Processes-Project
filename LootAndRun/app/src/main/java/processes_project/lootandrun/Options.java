package processes_project.lootandrun;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

    private void progressReset() {
        ArrayList<Item> emptyInventory = new ArrayList<>();
        MainMap.getMainPlayer().setHealth(100);
        MainMap.getMainPlayer().setInventory(emptyInventory);
    }
}
