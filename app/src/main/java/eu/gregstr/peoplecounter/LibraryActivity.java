package eu.gregstr.peoplecounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends NavigationActivity {

    DeviceLibraryContract.DeviceLibraryDbHelper dbHelper = new DeviceLibraryContract.DeviceLibraryDbHelper(this);

    LinearLayout device_list;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent about = new Intent(this, AboutActivity.class);

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, "devices.csv");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.EMPTY);

        int optionId = item.getItemId();
        if (optionId == R.id.save) {
            startActivityForResult(intent , 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        SetBottomMenu(R.id.library);
        SetActionBar(getString(R.string.library), false);

        SearchView searchBar = findViewById(R.id.searchView);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                UpdateLibrary("%"+query+"%");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty())
                    UpdateLibrary("");
                return false;
            }
        });

        device_list = findViewById(R.id.device_list);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                DeviceLibraryContract.DeviceEntry.COLUMN_NAME_DEVICE_NAME,
                DeviceLibraryContract.DeviceEntry.COLUMN_NAME_DEVICE_ADDRESS
        };

        String sortOrder =
                DeviceLibraryContract.DeviceEntry.COLUMN_NAME_DEVICE_NAME + " DESC";

        Cursor cursor = db.query(
                DeviceLibraryContract.DeviceEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );


        ArrayList<Device> devices = new ArrayList<>();
        while(cursor.moveToNext()) {
            devices.add(new Device(cursor.getString(cursor.getColumnIndexOrThrow(DeviceLibraryContract.DeviceEntry.COLUMN_NAME_DEVICE_NAME)), cursor.getString(cursor.getColumnIndexOrThrow(DeviceLibraryContract.DeviceEntry.COLUMN_NAME_DEVICE_ADDRESS))));
        }
        cursor.close();

        for (Device device : devices)
        {
            LinearLayout deviceInfo = new LinearLayout(this);
            deviceInfo.setOrientation(LinearLayout.VERTICAL);

            TextView deviceName = new TextView(this);
            deviceName.setText((device.name == null)?device.address:device.name);
            deviceName.setTextSize(20);
            deviceInfo.addView(deviceName);

            TextView deviceAddress = new TextView(this);
            deviceAddress.setText(device.address);
            deviceInfo.addView(deviceAddress);

            device_list.addView(deviceInfo);
        }
    }

    void UpdateLibrary(String Match) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                DeviceLibraryContract.DeviceEntry.COLUMN_NAME_DEVICE_NAME,
                DeviceLibraryContract.DeviceEntry.COLUMN_NAME_DEVICE_ADDRESS
        };

        String sortOrder =
                DeviceLibraryContract.DeviceEntry.COLUMN_NAME_DEVICE_NAME + " DESC";

        // Filter results WHERE "title" = 'My Title'
        String selection = DeviceLibraryContract.DeviceEntry.COLUMN_NAME_DEVICE_NAME + " like ? OR " + DeviceLibraryContract.DeviceEntry.COLUMN_NAME_DEVICE_ADDRESS + " like ?";
        String[] selectionArgs = { Match, Match };

        Cursor cursor = db.query(
                DeviceLibraryContract.DeviceEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                (Match.isEmpty())?null:selection,              // The columns for the WHERE clause
                (Match.isEmpty())?null:selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );


        ArrayList<Device> devices = new ArrayList<>();
        while(cursor.moveToNext()) {
            devices.add(new Device(cursor.getString(cursor.getColumnIndexOrThrow(DeviceLibraryContract.DeviceEntry.COLUMN_NAME_DEVICE_NAME)), cursor.getString(cursor.getColumnIndexOrThrow(DeviceLibraryContract.DeviceEntry.COLUMN_NAME_DEVICE_ADDRESS))));
        }
        cursor.close();

        device_list.removeAllViewsInLayout();

        for (Device device : devices)
        {
            LinearLayout deviceInfo = new LinearLayout(this);
            deviceInfo.setOrientation(LinearLayout.VERTICAL);

            TextView deviceName = new TextView(this);
            deviceName.setText((device.name == null)?device.address:device.name);
            deviceName.setTextSize(20);
            deviceInfo.addView(deviceName);

            TextView deviceAddress = new TextView(this);
            deviceAddress.setText(device.address);
            deviceInfo.addView(deviceAddress);

            device_list.addView(deviceInfo);
        }
    }
}