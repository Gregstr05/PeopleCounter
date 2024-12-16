package eu.gregstr.peoplecounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

public class AboutActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        SetBottomMenu(R.id.about);
        SetActionBar(getString(R.string.about), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}