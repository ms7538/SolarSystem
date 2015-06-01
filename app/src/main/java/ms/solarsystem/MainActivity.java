package ms.solarsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    private String cs;
    private int count1=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View solarSystemView = new SolarSystemView(this);
        setContentView(solarSystemView);
        solarSystemView.setBackgroundColor(Color.parseColor("#090404"));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.menu_spinner) {
            SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
            SharedPreferences.Editor editor = mSettings.edit();
            int modch= count1 % 4;
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
        }
        if (id == R.id.loc_reset) {
            SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString("reset", "on");
            editor.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



