package eu.gregstr.peoplecounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.util.JsonUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class AboutActivity extends NavigationActivity {

    OkHttpClient client = new OkHttpClient();
    public String url= "https://gregstr.eu/is-it-my-birthday.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        SetBottomMenu(R.id.about);
        SetActionBar(getString(R.string.about), true);

        Request get = new Request.Builder()
                .url(url)
                .build();

        client.newCall(get).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    ResponseBody responseBody = response.body();
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    JSONObject jsonObject = new JSONObject(responseBody.string());



                    System.out.println(jsonObject.get("birthday"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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