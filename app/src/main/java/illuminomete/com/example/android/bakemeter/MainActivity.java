package illuminomete.com.example.android.bakemeter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button bluetoothsearch;
    private Button send;
    private TextView result;
    BluetoothManager  bluetoothManager;
    BluetoothAdapter  bluetoothAdapter;
    BluetoothDevice   device;
    public static BluetoothGatt     bluetoothGatt;
    public static  String   bluetoothadress;
    boolean con;
    private static BluetoothGattCharacteristic characteristic;
    public static List<ArrayList<BluetoothGattCharacteristic>> gattCharacteristicList = new ArrayList<>();
    public static boolean isableLink;
    LineChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        con=false;
        isableLink=false;
        chart=(LineChart) findViewById(R.id.chart);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Log.e("TAG", "run: " );
                try {
                    String i=runn("https://www.2cto.com/kf/201704/629164.html");
                    Log.e("TAG", "run:88888887777" );
                    Log.e("TAG", "run8888888888888: "+i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        createLineChart();
        addEntry(100000);
        bluetoothsearch=(Button) findViewById(R.id.bluetoothsearch);
        send=(Button) findViewById(R.id.send);
        bluetoothsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),BluetoothActivity.class));
            }
        });
        result=(TextView) findViewById(R.id.result);
        if(getSDKVersionNumber()<21){
            Toast.makeText(this, "手机版本过低", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this,"手机版本过低", Toast.LENGTH_SHORT)
                    .show();
            finish();
        }



    }
    public void createLineChart(){
        chart.setNoDataText("NO Data");
        chart.setNoDataTextColor(getResources().getColor(R.color.colorAccent));
        chart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);
        chart.setBorderColor(Color.RED);
        chart.setDrawBorders(true);
        chart.setGridBackgroundColor(Color.RED);
       // chart.setDrawGridBackground(true);
        chart.setBackgroundColor(Color.GRAY);
        Description description=new Description();
        description.setText("照度图表");
        description.setTextColor(Color.BLUE);
        description.setTextSize(14f);
        chart.setDescription(description);
        chart.animateX(1500);
        chart.animateY(1500);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDragDecelerationEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.5f);
        chart.setPinchZoom(false);
        chart.zoomIn();
// 缩小到0.7倍
        chart.zoomOut();
// 缩放(自定义)
       // chart.zoom(1,1,1,1);
        //X轴设置
        XAxis axis=chart.getXAxis();
        axis.setAxisMinimum(1);
        //axis.setAxisMaximum(50);
        axis.setAvoidFirstLastClipping(true);
        axis.setDrawAxisLine(true);
        axis.setAxisLineColor(Color.BLUE);
        axis.setTypeface(Typeface.DEFAULT_BOLD);
        axis.setTextColor(Color.BLUE);
        axis.setTextSize(14f);
        axis.setDrawGridLines(true);
        axis.setGridColor(Color.GREEN);
        axis.setGridDashedLine(new DashPathEffect(new float[]{30,20,30,20},1));
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
       // axis.setGranularity(0.01f); // one hour
        axis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("mm:ss");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });
        //-----------------------------------Y轴-----------------------------------
        YAxis yAxis = chart.getAxisLeft();
// Y轴最小值为5
        yAxis.setAxisMinimum(5);
// Y轴最大值为30
       // yAxis.setAxisMaximum(100);
// 是否绘制Y坐标轴
        yAxis.setDrawAxisLine(true);
// Y坐标轴颜色
        yAxis.setAxisLineColor(Color.RED);
// 设置y坐标轴字体颜色
        yAxis.setTextColor(Color.GREEN);
// 设置y坐标轴字体大小
        yAxis.setTextSize(14);
// 否是绘制网格线(Y轴代表的为横线)，默认true
        yAxis.setDrawGridLines(true);
// 设置网格线的颜色(Y轴代表的为横线)
        yAxis.setGridColor(Color.GREEN);

/**
 * 绘制虚线网格线，
 * DashPathEffect:
 * @params:new float[]{30,20,30,20}   先绘制30px实线，再绘制20px透明，再绘制30px实线，再绘制20px透明
 * @params:0  偏移量,若为20，则效果为：先绘制10px实线，再绘制20px透明，再绘制30px实线，再绘制20px透明
 */
        yAxis.setGridDashedLine(new DashPathEffect(new float[]{30,20,30,20},0));
// 不绘制右边Y值
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);

        Legend legend = chart.getLegend();
// 设置绘制图例(默认true)
        legend.setEnabled(true);
// 设置图例文字颜色
        legend.setTextColor(Color.RED);
// 设置图例文字大小
        legend.setTextSize(14);
// 设置图例位置：水平居中
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
// 设置图例样式(默认是小方格)
        legend.setForm(Legend.LegendForm.CIRCLE);
// 设置图例大小
        legend.setFormSize(30);
    }
    public void addEntry(int num){
        List<Entry> data1 = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            int m = new Random().nextInt(10000) + 10;
            // i:x值，m：y值

            data1.add(new Entry(i,m));
        }

        LineDataSet set2 = new LineDataSet(data1,"line2");
// 设置线条颜色
        set2.setColor(Color.BLUE);
// 设置线条宽度
        set2.setLineWidth(2f);
// 设置圆的颜色
        set2.setCircleColor(Color.RED);
// 设置节点圆的半径
        set2.setCircleRadius(2f);
// 是否绘制空心圆(默认true)
      //set2.setDrawCircleHole(true);
// 高亮颜色(点击时出现的颜色)
        set2.setHighLightColor(Color.BLUE);
/**
 * 虚线绘制：
 * @params 30 实线长
 * @params 20 虚线长
 * @params 0  偏移
 */
        set2.enableDashedLine(30,20,0);
// 空心圆半径,需比实心圆半径小，否则变成了实心圆
        set2.setCircleHoleRadius(5);
// 空心圆颜色,默认白色
        set2.setCircleColorHole(Color.YELLOW);
        LineData lineData = new LineData(set2);
// 设置是否显示高亮，默认true

        lineData.setHighlightEnabled(false);
// 线条显示的数值颜色
        lineData.setValueTextColor(Color.BLUE);
// 线条显示的数值文字大小
        lineData.setValueTextSize(9f);
//

        chart.setData(lineData);
        chart.setVisibleXRangeMinimum(1);
        chart.setVisibleXRangeMaximum(5000);
        //chart.setVisibleXRange(1,5000);
    }
    public void connectBlue(){
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter =bluetoothManager.getAdapter();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(bluetoothadress);
        bluetoothGatt = device.connectGatt(this, false, gattCallback);
        bluetoothGatt.connect();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isableLink) {
            connectBlue();
        }
        isableLink = false;

    }
    public BluetoothGattCallback gattCallback=new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (BluetoothGatt.GATT_SUCCESS == status) {
                gatt.getServices();
                gattCharacteristicList.clear();
                String uuid = null;
                ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
                ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
                for (BluetoothGattService gattService : gatt.getServices()) {
                    Log.e("TAG", "onServicesDiscovered:" + gattService.getUuid());
                    HashMap<String, String> currentServiceData = new HashMap<String, String>();
                    uuid = gattService.getUuid().toString();
//                    currentServiceData.put("name",
//                            SampleGattAttributes.lookup(uuid, getResources().getString(R.string.unknownService)));
                    currentServiceData.put("uuid", uuid);
                    gattServiceData.add(currentServiceData);
                    ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
                    List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                            .getCharacteristics();
                    ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        Log.e("TAG", "onCharacteristicDiscovered:" + gattCharacteristic.getUuid());
                        charas.add(gattCharacteristic);
                        HashMap<String, String> currentCharaData = new HashMap<String, String>();
                        uuid = gattCharacteristic.getUuid().toString();
//                        currentCharaData.put("name",
//                                SampleGattAttributes.lookup(uuid, getResources().getString(R.string.unknownCharacteristic)));
                        currentCharaData.put("uuid", uuid);
                        if (uuid.equals("0000ff02-0000-1000-8000-00805f9b34fb")) {
                            setCharacteristicNotification(gattCharacteristic, true);
                        }
                        gattCharacteristicGroupData.add(currentCharaData);
                    }
                    gattCharacteristicList.add(charas);
                    gattCharacteristicData.add(gattCharacteristicGroupData);
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            final byte[] data= characteristic.getValue();
            Log.e("TAG", "onCharacteristicChanged:the datareturn" + data.length);
        }

    };
    private void setCharacteristicNotification(BluetoothGattCharacteristic gattCharacteristic, boolean b) {
        bluetoothGatt.setCharacteristicNotification(gattCharacteristic, b);
        BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID
                .fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
        if (descriptor != null) {
            System.out.println("write descriptor");
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            bluetoothGatt.writeDescriptor(descriptor);
        }
    }
    public static void SendInfo(byte[] value) {
        if(gattCharacteristicList.size()>0){
            Log.e("TAG", "SendInfo:发送数据-----");
            characteristic = gattCharacteristicList.get(3).get(0);
            characteristic.setValue(value);
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            bluetoothGatt.writeCharacteristic(characteristic);}
    }
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    public String runn(String url) throws IOException {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        Response response=client.newCall(request).execute();
        return  response.body().toString();
    }
}
