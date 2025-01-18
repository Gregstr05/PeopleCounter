package eu.gregstr.peoplecounter;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends NavigationActivity {

    DeviceLibraryContract.DeviceLibraryDbHelper dbHelper = new DeviceLibraryContract.DeviceLibraryDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        SetBottomMenu(R.id.library);
        SetActionBar(getString(R.string.library), false);

        LinearLayout device_list = findViewById(R.id.device_list);

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


}