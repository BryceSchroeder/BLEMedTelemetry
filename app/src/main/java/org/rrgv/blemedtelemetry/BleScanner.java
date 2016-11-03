package org.rrgv.blemedtelemetry;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.view.View;
import android.util.Log;
import android.util.ArraySet;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ToggleButton;
import android.widget.Button;
import android.widget.ListView;
import android.Manifest;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

public class BleScanner extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private ToggleButton mScanButton;
    private ListView mDeviceList;
    private ArrayList<String> mDeviceArray;
    private ArrayAdapter<String> mDeviceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_scanner);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();


        mDeviceArray = new ArrayList<String>();
        mDeviceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mDeviceArray);
        mDeviceList = (ListView)findViewById(R.id.ble_list);
        mDeviceList.setAdapter(mDeviceAdapter);


        // This is just disgusting. Is there anything the Java way of doing things
        // doesn't ruin, even a simple callback button click handler? I'd have thought
        // this was impossible to get wrong in an OO language but I was wrong.
        mScanButton = (ToggleButton)findViewById(R.id.scan_button);
        mScanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("ui","Scan button clicked.");
                if (mScanButton.isChecked()) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Log.e("ble", "Bluetooth isn't enabled.");
                        return;
                    }
                    Log.d("ble", "enabling scan");
                    mBluetoothAdapter.startLeScan(mBleScanCallback);

                } else {
                    mBluetoothAdapter.stopLeScan(mBleScanCallback);
                    Log.d("ble", "disabling scan");

                }
            }
        });

        ((Button)findViewById(R.id.quit_button)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("ui","Quit button clicked.");
                BleScanner.this.finishAffinity();
            }
        });

        //requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 8);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ble_scanner, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void addDevice(final BluetoothDevice device) {
        Log.d("ble", "The device "+device.getAddress()+" is being added.");
        String name = device.getName()+"/"+device.getAddress();
        if (!mDeviceArray.contains(name))
            mDeviceAdapter.add(name);
    }


    private BluetoothAdapter.LeScanCallback mBleScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rrsi, byte[] scanRecord) {
            Log.d("ble", "found device "+device.getName()+" "+device.getAddress() );

            BleScanner.this.addDevice(device);
        }
    };
}
