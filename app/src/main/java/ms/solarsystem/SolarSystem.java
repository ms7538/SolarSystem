package ms.solarsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class SolarSystem extends ActionBarActivity {
    private String cs;
    private int count1=0;
    private int count2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View solarSystemView = new SolarSystemView(getApplicationContext());
        setContentView(solarSystemView);
        solarSystemView.setBackgroundColor(Color.parseColor("#090404"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solar_system, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = mSettings.edit();
        switch (item.getItemId()) {

            case R.id.home:
                Toast.makeText(this,"back" ,
                        Toast.LENGTH_SHORT).show();
                return true;

            case  R.id.menu_spinner:

                int modch= count1 % 5;
                count1++;
                switch (modch){
                    case 0: item.setTitle("HOURS ");editor.putString("id", "hrs"); break;
                    case 1: item.setTitle("DAYS  ");editor.putString("id", "dys"); break;
                    case 2: item.setTitle("WEEKS"); editor.putString("id", "wks"); break;
                    case 3: item.setTitle("MONTHS");editor.putString("id", "mth"); break;
                    case 4: item.setTitle("ACTUAL");editor.putString("id", "act");break;
                }
                editor.commit();
                return true;
            case R.id.loc_reset:
                editor.putString("reset", "on");
                editor.commit();
                return true;

            case R.id.orbitssel:

                int modch2= count2 % 2;
                count2++;
                switch (modch2) {
                    case 0:
                        item.setTitle("OFF");
                        editor.putString("orbits", "off");
                        break;
                    case 1:
                        item.setTitle("ON");
                        editor.putString("orbits", "on");
                        break;
                }
                editor.commit();
                return true;


        }


        return super.onOptionsItemSelected(item);
    }
}
