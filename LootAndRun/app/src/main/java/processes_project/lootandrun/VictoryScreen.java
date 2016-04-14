package processes_project.lootandrun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class VictoryScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory_screen);
    }


    public void returnToMap(View view)
    {
        finish();
        Intent intent = new Intent(this, MainMap.class);
        startActivity(intent);


    }
}
