package eu.gregstr.peoplecounter;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    Toolbar actionBar;



    protected void SetBottomMenu(int selectedItemId) {
        Intent main = new Intent(this, MainActivity.class);
        Intent about = new Intent(this, AboutActivity.class);
        Intent library = new Intent(this, LibraryActivity.class);

        bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setSelectedItemId(selectedItemId);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.about) {
                        startActivity(about);
                    }
                else if (itemId == R.id.main) {
                    startActivity(main);
                }
                else if (itemId == R.id.library) {
                    startActivity(library);
                }
                else {
                    return false;
                }
                return true;
            }
        });
    }

    protected void SetActionBar(String title, boolean bIsCancelable) {
        actionBar = findViewById(R.id.action_bar);
        setSupportActionBar(actionBar);

        ActionBar activeActionBar = getSupportActionBar();
        activeActionBar.setTitle(title);
        activeActionBar.setDisplayHomeAsUpEnabled(bIsCancelable);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_default, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent about = new Intent(this, AboutActivity.class);

        int optionId = item.getItemId();
        if (optionId == R.id.about) {
            startActivity(about);
        }
        return super.onOptionsItemSelected(item);
    }
}
