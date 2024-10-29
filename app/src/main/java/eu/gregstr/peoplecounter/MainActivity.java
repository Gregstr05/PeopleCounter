package eu.gregstr.peoplecounter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private BluetoothManager bluetoothManager;

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = findViewById(R.id.Search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Searching", Toast.LENGTH_SHORT).show();
                System.out.println(scanForDevices());
            }
        });

        bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, "Device doesn't support bluetooth!", Toast.LENGTH_SHORT).show();
        }
        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_SCAN);
        }

        scanForDevices();

    }

    private boolean scanForDevices() {
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGpsEnabled) {
        }
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT);

        BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Permission for bluetooth scanning denied!", Toast.LENGTH_SHORT).show();
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_SCAN);
            return false;
        }
        System.out.println(bluetoothAdapter.getScanMode());
        System.out.println(BluetoothAdapter.SCAN_MODE_NONE);

        bluetoothLeScanner.stopScan(bluetoothLEScanCallback);
        bluetoothLeScanner.startScan(Collections.singletonList(new ScanFilter.Builder().build()), new ScanSettings.Builder().setScanMode(ScanSettings.MATCH_MODE_AGGRESSIVE).build(), bluetoothLEScanCallback);
        return true;
    }

    private final ScanCallback bluetoothLEScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            System.out.println(result.getDevice().getAddress());
            AddToList(result.getDevice().getName(), result.getDevice().getAddress());
            //AddToList(result.getDevice().getName());
        }
    };

    protected void AddToList(String name, String address) {
        LinearLayout device_list = findViewById(R.id.device_list);

        if (!IsOnList(address))
        {
            LinearLayout device = new LinearLayout(this);
            device.setOrientation(LinearLayout.VERTICAL);

            TextView deviceName = new TextView(this);
            deviceName.setText((name == null)?address:name);
            deviceName.setTextSize(20);
            device.addView(deviceName);

            TextView deviceAddress = new TextView(this);
            deviceAddress.setText(address);
            device.addView(deviceAddress);

            device_list.addView(device);
        }
    }

    private boolean IsOnList(String address) {
        LinearLayout device_list = findViewById(R.id.device_list);

        for (int i = 0; i < device_list.getChildCount(); i++) {
            LinearLayout device = (LinearLayout) device_list.getChildAt(i);
            TextView deviceAddress = (TextView) device.getChildAt(1);
            if (deviceAddress.getText().toString().equals(address)) {
                return true;
            }
        }
        return false;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                System.out.println(deviceHardwareAddress);
                AddToList(deviceName, deviceHardwareAddress);
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }
}