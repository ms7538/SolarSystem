package ms.solarsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.os.Bundle;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
public class MainActivity extends ActionBarActivity {
    private String cs;
    private int count1=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ActionBar myActionBar = getSupportActionBar();
        myActionBar.hide();
        Button buttonToggleActionBar = (Button)findViewById(R.id.innerorbs);
        buttonToggleActionBar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                View solarSystemView = new SolarSystemView(getApplicationContext());
                setContentView(solarSystemView);
                myActionBar.show();
                myActionBar.setDisplayHomeAsUpEnabled(true);
                solarSystemView.setBackgroundColor(Color.parseColor("#090404"));
            }
        });


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

        switch (item.getItemId()) {
            case R.id.home:
                    Toast.makeText(this,"back" ,
                        Toast.LENGTH_SHORT).show();
                    return true;

            case  R.id.menu_spinner:
                SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
                SharedPreferences.Editor editor = mSettings.edit();
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
                mSettings = this.getSharedPreferences("Settings", 0);
                SharedPreferences.Editor editor1 = mSettings.edit();
                editor1.putString("reset", "on");
                editor1.commit();
                return true;



            case R.id.orbitssel:
                mSettings = this.getSharedPreferences("Settings", 0);
                SharedPreferences.Editor editor2 = mSettings.edit();
                String sel= mSettings.getString("orbits","on");
                if (sel=="on"){
                    item.setTitle("OFF");editor2.putString("orbits", "off");
                }else {
                    item.setTitle("ON");editor2.putString("orbits", "on");
                }

                editor2.commit();
                return true;


        }


        return super.onOptionsItemSelected(item);
    }
}



