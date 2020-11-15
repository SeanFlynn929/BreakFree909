package www.youtube.The_Break_Free_Program;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static int MESCORE, ADDICTIONSCORE;
    public static SharedPreferences SHAREDPREFERENCES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SHAREDPREFERENCES = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        MESCORE = SHAREDPREFERENCES.getInt("MESCORE", 0);
        ADDICTIONSCORE = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getInt("ADDICTIONSCORE", 0);
    }

    public static void upMeScore() {
        SHAREDPREFERENCES.edit().putInt("MESCORE", ++MESCORE).apply();
    }

    public static void upAddictionScore() {
        SHAREDPREFERENCES.edit().putInt("ADDICTIONSCORE", ++ADDICTIONSCORE).apply();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_improvment:
                            selectedFragment = new ClockFragment();
                            break;
                        case R.id.nav_motovation:
                            selectedFragment = new MotivationFragment();
                            break;
                        case R.id.nav_recovery:
                            selectedFragment = new RecoveryFragment();
                            break;
                        case R.id.nav_donate:
                            selectedFragment = new DonateFragment();
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}