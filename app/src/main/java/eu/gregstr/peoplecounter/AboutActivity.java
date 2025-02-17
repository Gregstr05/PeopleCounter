package eu.gregstr.peoplecounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        SetBottomMenu(R.id.about);
        SetActionBar(getString(R.string.about), true);

        Button githubBtn = findViewById(R.id.GithubButton);

        githubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent github = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Gregstr05"));

                startActivity(github);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}