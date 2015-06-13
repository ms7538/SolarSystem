package ms.solarsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("orbits", "on"); editor.commit();
        Button IObutton = (Button)findViewById(R.id.innerorbs);
        IObutton.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, SolarSystem.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        Button OObutton = (Button)findViewById(R.id.oorbs);
        OObutton.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, OuterOrbits.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

}