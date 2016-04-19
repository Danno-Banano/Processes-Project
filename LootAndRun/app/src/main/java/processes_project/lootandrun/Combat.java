package processes_project.lootandrun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

public class Combat extends AppCompatActivity {

    private Character player;
    private Character zombie;
    private TextView playerTV;
    private TextView zombieTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        player = MainMap.getMainPlayer();
        zombie = (Character) getIntent().getSerializableExtra("Zombie"); //new Character(100, "Rabid Zombie",null,30,null);

        setCombatInfo();
    }

    public void setCombatInfo()
    {
        playerTV = (TextView) findViewById(R.id.playerName);
        playerTV.setText(player.getCharName());
        playerTV = (TextView) findViewById(R.id.playerHealth);
        playerTV.setText("Health: "+Integer.toString(player.getHealth()));
        playerTV = (TextView) findViewById(R.id.playerAD);
        playerTV.setText("Attack: "+Integer.toString(player.getAttackDamage()));

        //set Zombie name, health and AD
        zombieTV = (TextView) findViewById(R.id.zombieName);
        zombieTV.setText(zombie.getCharName());
        zombieTV = (TextView) findViewById(R.id.zombieHealth);
        zombieTV.setText("Health: "+Integer.toString(zombie.getHealth()));
        zombieTV = (TextView) findViewById(R.id.zombieAD);
        zombieTV.setText("Attack: " + Integer.toString(zombie.getAttackDamage()));

    }

    public void onAttackClick(View view)
    {
        int newHP;


       //update zombie health
        newHP = zombie.getHealth();
        newHP = newHP - player.getAttackDamage();


        if(newHP<=0)
        {

            this.finish();
            Intent intent = new Intent(this, VictoryScreen.class);
            startActivity(intent);
            zombie.setDead(Boolean.TRUE);
            MainMap.getMainPlayer().doLoot();

        }

        zombie.setHealth(newHP);


        //update player health if zombie is still alive
        newHP = player.getHealth();

        if(!zombie.isDead())
        {
            newHP = newHP - zombie.getAttackDamage();
        }


        if(newHP<=0)
        {
            this.finish();
            Intent intent = new Intent(this, DefeatScreen.class);
            startActivity(intent);
        }

        player.setHealth(newHP);



        setCombatInfo();


    }
}

