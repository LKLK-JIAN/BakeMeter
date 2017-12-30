package illuminomete.com.example.android.bakemeter;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;



public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final int REQUEST_LOCATION = 1;
    ListView bluetoothlist;
    Button   search;
    Handler handler;
    BluetoothAdapter bluetoothAdapter;
    BluetoothManager bluetoothManager;
    boolean isScan;
    MyAdapter adapter;
    BluetoothDevice device;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothlist=(ListView) findViewById(R.id.bluetoothlist);
        search=(Button) findViewById(R.id.search);
        adapter=new MyAdapter(this);
        bluetoothlist.setAdapter(adapter);
        handler=new Handler();
        bluetoothlist.setOnItemClickListener(this);
        search.setOnClickListener(this);

    }
    
    private BluetoothAdapter.LeScanCallback mLeScanCallback =new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.addDevice(bluetoothDevice);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    //扫描蓝牙
    private void scanDevice(final boolean enable) {
        if (enable) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScan = false;
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, 5000);
            isScan = true;
            bluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            isScan = false;
            bluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }


    //abdriod6.0添加权限
    private void requestPer() {
        if (Build.VERSION.SDK_INT >= 23) {
            int check = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            } else {
                scanDevice(true);
            }
        } else {
            //版本低于6。0
            scanDevice(true);
        }
    }

    @Override
    public void onClick(View view) {
        if(!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();
        }
        requestPer();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        if (adapter.getCount()>0) {
//                MainActivity.bluetoothGatt.close();
//            }
            device = (BluetoothDevice) adapter.getItem(i);
            Log.e("TAG", "onItemClick: "+device.getAddress() );
            MainActivity.bluetoothadress = device.getAddress();
            MainActivity.isableLink=true;
            if (isScan) {
                bluetoothAdapter.stopLeScan(mLeScanCallback);
                isScan = false;
            }
            finish();
        }

    }

