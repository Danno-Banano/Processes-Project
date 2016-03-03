package processes_project.lootandrun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openMap(View view) {
        Intent intent = new Intent(this, MainMap.class);
        startActivity(intent);
    }

    public void openOptions(View view) {
        Intent intent = new Intent(this, Options.class);
        startActivity(intent);
    }


}
