package com.bluetootharduino;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;

public class MainActivity extends Activity {
    private Context context;

    boolean hasCompleted = false;



    HashMap<String, HashMap<String, Object>> hm = new HashMap<String, HashMap<String, Object>>() {
        {
            put("Voltage", new HashMap<String, Object>() {
                        {
                            put("UUID", UUID.fromString("00002a30-0000-1000-8000-00805f9b34fb"));
                            put("Characteristic", null);
                            put("Text", null);
                        }
                    }
                );

            put("Current", new HashMap<String, Object>() {
                        {
                            put("UUID", UUID.fromString("00002a31-0000-1000-8000-00805f9b34fb"));
                            put("Characteristic", null);
                            put("Text", null);
                        }
                    }
            );

            put("Lift", new HashMap<String, Object>() {
                        {
                            put("UUID", UUID.fromString("00002a32-0000-1000-8000-00805f9b34fb"));
                            put("Characteristic", null);
                            put("Text", null);
                        }
                    }
            );
            put("AmbientTemp", new HashMap<String, Object>() {
                        {
                            put("UUID", UUID.fromString("00002a33-0000-1000-8000-00805f9b34fb"));
                            put("Characteristic", null);
                            put("Text", null);
                        }
                    }
            );
            put("MotorTemp", new HashMap<String, Object>() {
                        {
                            put("UUID", UUID.fromString("00002a34-0000-1000-8000-00805f9b34fb"));
                            put("Characteristic", null);
                            put("Text", null);
                        }
                    }
            );
            put("Pulse", new HashMap<String, Object>() {
                        {
                            put("UUID", UUID.fromString("00002a35-0000-1000-8000-00805f9b34fb"));
                            put("Characteristic", null);
                            put("Text", null);
                        }
                    }
            );
            put("PulseControl", new HashMap<String, Object>() {
                        {
                            put("UUID", UUID.fromString("00002a36-0000-1000-8000-00805f9b34fb"));
                            put("Characteristic", null);
                            put("Text", null);
                        }
                    }
            );
        }
    };

    private HashMap<Integer, HashMap<String, String>> data = new HashMap<>();
    private int dataIndex = 0;
    private boolean RECORDING_DATA = false;


    public static UUID MTB_BASE_UUID      = UUID.fromString("d38306df-369a-4c72-b393-2c99c03eb81a");

    public static UUID UART_UUID          = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID[] UART_UDIDs       = {UART_UUID};

    public static UUID CLIENT_UUID        = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    HandlerThread hThread;

    public int msPulseWidth = 0;


    BluetoothAdapter bluetoothAdapter;
    BluetoothGatt gatt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=getApplicationContext();

        hThread = new HandlerThread("BLE");
        hThread.start();

        setContentView(R.layout.activity_main);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        hm.get("Voltage").put("Text", findViewById(R.id.textVoltage));
        hm.get("Current").put("Text", findViewById(R.id.textCurrent));
        hm.get("Lift").put("Text", findViewById(R.id.textLift));
        hm.get("AmbientTemp").put("Text", findViewById(R.id.textAmbient));
        hm.get("MotorTemp").put("Text", findViewById(R.id.textMotor));
        hm.get("Pulse").put("Text", findViewById(R.id.textPulse));
        hm.get("PulseControl").put("Text", null);

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()){
            Log.d("Bluetooth","Bluetooth not enables");
        }
    }

    public void startScan(View view){
        bluetoothAdapter.startLeScan(UART_UDIDs,scanCallback);

    }

    @Override
    public void onDestroy() {
        gatt.disconnect();
        hThread.quit();
        super.onDestroy();
    }

    public void incrementDutyCycle(View view){
        msPulseWidth++;

        final TextView pulseWidthTextViiew = (TextView) findViewById(R.id.pulseWidth_id);

        switch (msPulseWidth){
            case 0: pulseWidthTextViiew.setText("1.0");
                    ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.0");
                    break;
            case 1: pulseWidthTextViiew.setText("1.1");
                    ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.1");
                    break;
            case 2: pulseWidthTextViiew.setText("1.2");
                    ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.2");
                    break;
            case 3: pulseWidthTextViiew.setText("1.3");
                    ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.3");
                    break;
            case 4: pulseWidthTextViiew.setText("1.4");
                    ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.4");
                    break;
            case 5: pulseWidthTextViiew.setText("1.5");
                    ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.5");
                    break;
            case 6: pulseWidthTextViiew.setText("1.6");
                    ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.6");
                    break;
            case 7: pulseWidthTextViiew.setText("1.7");
                    ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.7");
                    break;
            case 8: pulseWidthTextViiew.setText("1.8");
                    ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.8");
                    break;
            case 9: pulseWidthTextViiew.setText("1.9");
                    ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.9");
                    break;
            case 10: pulseWidthTextViiew.setText("2.0");
                    ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("2.0");
                    break;
        }
        gatt.writeCharacteristic(((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")));
    }

    public void exportData(View view){
        telemetry dataOut = new telemetry(data,context);
        dataOut.toClipboard();

    }

    public void decrementDutyCycle(View view){
        msPulseWidth--;
        final TextView pulseWidthTextViiew = (TextView) findViewById(R.id.pulseWidth_id);

        switch (msPulseWidth){
            case 0: pulseWidthTextViiew.setText("1.0");
                ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.0");
                break;
            case 1: pulseWidthTextViiew.setText("1.1");
                ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.1");
                break;
            case 2: pulseWidthTextViiew.setText("1.2");
                ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.2");
                break;
            case 3: pulseWidthTextViiew.setText("1.3");
                ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.3");
                break;
            case 4: pulseWidthTextViiew.setText("1.4");
                ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.4");
                break;
            case 5: pulseWidthTextViiew.setText("1.5");
                ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.5");
                break;
            case 6: pulseWidthTextViiew.setText("1.6");
                ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.6");
                break;
            case 7: pulseWidthTextViiew.setText("1.7");
                ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.7");
                break;
            case 8: pulseWidthTextViiew.setText("1.8");
                ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.8");
                break;
            case 9: pulseWidthTextViiew.setText("1.9");
                ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("1.9");
                break;
            case 10: pulseWidthTextViiew.setText("2.0");
                ((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")).setValue("2.0");
                break;
        }
        gatt.writeCharacteristic(((BluetoothGattCharacteristic) hm.get("PulseControl").get("Characteristic")));

    }

    public void toggleCapture(View view)  {
        RECORDING_DATA = !RECORDING_DATA;
        Button b = findViewById(R.id.buttonCapture);
        if (RECORDING_DATA) {
            b.setText("Stop Capture");
        } else {
            b.setText("Start Capture");
        }
    }

    BluetoothAdapter.LeScanCallback scanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    Log.d("BluetoothDebug", "scanning for devices");
                    gatt = device.connectGatt(context, true, gattCallback);
                }
            };

    BluetoothGattCallback gattCallback =
            new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    if (newState == STATE_CONNECTED){
                        Log.d("BluetoothDebug","discoveringServices");
                        bluetoothAdapter.stopLeScan(scanCallback);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Button b = findViewById(R.id.connect_id);
                                b.setEnabled(false);
                            }
                        });
                        gatt.discoverServices();
                    }
                }

                @Override
                public void onServicesDiscovered(final BluetoothGatt gatt, int status){
                    Log.d("BluetoothDebug","discoveringServices");

                    hm.get("Voltage").put("Characteristic", gatt.getService(MTB_BASE_UUID).getCharacteristic((UUID) hm.get("Voltage").get("UUID")));
                    hm.get("Current").put("Characteristic", gatt.getService(MTB_BASE_UUID).getCharacteristic((UUID) hm.get("Current").get("UUID")));
                    hm.get("Lift").put("Characteristic", gatt.getService(MTB_BASE_UUID).getCharacteristic((UUID) hm.get("Lift").get("UUID")));
                    hm.get("AmbientTemp").put("Characteristic", gatt.getService(MTB_BASE_UUID).getCharacteristic((UUID) hm.get("AmbientTemp").get("UUID")));
                    hm.get("MotorTemp").put("Characteristic", gatt.getService(MTB_BASE_UUID).getCharacteristic((UUID) hm.get("MotorTemp").get("UUID")));
                    hm.get("Pulse").put("Characteristic", gatt.getService(MTB_BASE_UUID).getCharacteristic((UUID) hm.get("Pulse").get("UUID")));
                    hm.get("PulseControl").put("Characteristic", gatt.getService(MTB_BASE_UUID).getCharacteristic((UUID) hm.get("PulseControl").get("UUID")));

                    Looper l = hThread.getLooper();
                    final Handler h = new Handler(l);

                    class DataTake implements Runnable {
                        int time;

                        public DataTake() {
                            this.time = 0;
                        }

                        @Override
                        public void run() {
                            time += 1000;
                            for (Map.Entry<String, HashMap<String, Object>> e : hm.entrySet()) {
                                final BluetoothGattCharacteristic c = (BluetoothGattCharacteristic) e.getValue().get("Characteristic");
                                if (c != null) {
                                    gatt.readCharacteristic(c);
                                }

                                try {
                                    Thread.sleep(150);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            updateLabels();
                            h.postDelayed(this, 1000);
                        }
                    }
                    h.postDelayed(new DataTake(), 1000);
                }

                public void updateLabels() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            for(Map.Entry<String, HashMap<String, Object>> e : hm.entrySet()) {
                                BluetoothGattCharacteristic c = (BluetoothGattCharacteristic) e.getValue().get("Characteristic");
                                TextView t = (TextView) e.getValue().get("Text");
                                if (c != null && t != null){
                                    t.setText(c.getStringValue(0));
                                }
                            }
                        }
                    });
                }

                @Override
                public void onCharacteristicRead(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, int status) {
                    //super.onCharacteristicRead(gatt, characteristic, status);
                    for (Map.Entry<String, HashMap<String, Object>> e : hm.entrySet()) {
                        HashMap<String, Object> entry = e.getValue();
                        if (characteristic.getUuid().equals(entry.get("UUID"))) {
                            hm.get(e.getKey()).put("Characteristic", characteristic);
                            if (RECORDING_DATA) {
                                HashMap<String, String> x = new HashMap<>();
                                if (data.containsKey(dataIndex)) {
                                    x = data.get(dataIndex);
                                }
                                if (!x.containsKey(e.getKey())) {
                                    x.put(e.getKey(), characteristic.getStringValue(0));
                                }

                                data.put(dataIndex, x);
                                Log.d("BLEDataDebug", data.toString());
                                Log.d("BLEDataDebug", String.valueOf(dataIndex));
                                if (x.size() == 6){
                                    dataIndex++;
                                }
                            }
                            break;
                        }
                    }
                }
            };


}
