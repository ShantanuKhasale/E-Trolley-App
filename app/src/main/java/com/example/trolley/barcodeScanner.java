package com.example.trolley;



import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class barcodeScanner extends AppCompatActivity {
    public static String macAddress;
    TextView textViewProdBarcode,status;
    BluetoothAdapter bluetoothAdapter;
    Button StartScan;

    SendReceive sendReceive;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=6;
    static final int STATE_THEY_NOT_ALLOWED=5;

    int REQUEST_ENABLE_BLUETOOTH=1;
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public void starter(){
        ServerClass serverClass = new ServerClass();
        serverClass.start();
    }

    public void startAgain(){
        SaverClass saverClass = new SaverClass();
        saverClass.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        textViewProdBarcode = findViewById(R.id.textViewProdBarcode);
        status= findViewById(R.id.status);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,0);
        }
        BluetoothDevice hc05 = bluetoothAdapter.getRemoteDevice(macAddress);
        starter();
    }







    public boolean isBonded(BluetoothAdapter mBluetoothAdapter, String MAC){
        Set<BluetoothDevice> myBondedDevices = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice myDevice:myBondedDevices )
        {
            if(myDevice.getAddress().equals(MAC))
            {
                return true;
            }
        }
        return false;
    }

    public void connectFirst(){
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }
    }



    public void connectAgain(){
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }

        while (!bluetoothAdapter.isEnabled()){};
        status.setText("Connecting Again Please Wait...");
        startAgain();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){

        }else{

            Message message = Message.obtain();
            message.what=STATE_THEY_NOT_ALLOWED;
            handler.sendMessage(message);
        }
    }


    Handler handler=new Handler(Looper.getMainLooper(),new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what)
            {
                case STATE_LISTENING:
                    status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case STATE_THEY_NOT_ALLOWED:
                    status.setText("Please Turn On Bluetooth to Continue");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    String tempMsg = (String) msg.obj;
                    System.out.println("received : - "+ tempMsg);
//                    msg_box.setText("Barcode Adding : "+tempMsg);
                    break;
            }
            return true;
        }
    });


    public  class ServerClass extends Thread{
        BluetoothDevice hc05 = bluetoothAdapter.getRemoteDevice(macAddress);
        public ServerClass()
        {

            textViewProdBarcode.setText("Barcode");
            status.setText("Clicl on Start Button");

        }

        public void run(){
            Message message = Message.obtain();
            message.what=STATE_CONNECTING;
            handler.sendMessage(message);
            System.out.println("ServerClass Called");
            ClientClass clientClass = new ClientClass(hc05);
            clientClass.start();

        }
    }


    public  class SaverClass extends Thread{
        BluetoothDevice hc05 = bluetoothAdapter.getRemoteDevice(macAddress);
        public SaverClass()
        {

        }

        public void run(){
            Message message = Message.obtain();
            message.what=STATE_CONNECTING;
            handler.sendMessage(message);
            System.out.println("SaverClass Called");
            ClientClass clientClass = new ClientClass(hc05);
            clientClass.start();

        }
    }

    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket = null;

        public ClientClass (BluetoothDevice device1)
        {
            device=device1;
            int counter = 0;
            do {
                try {
                    socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    System.out.println("Socket Info Printed : "+socket);
                    socket.connect();
                    System.out.println("Connected :-  "+socket.isConnected());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                counter++;
            }while(!socket.isConnected() && counter<5);
        }

        public void run()
        {
            System.out.println("ClientClass called");
            Message message=Message.obtain();
            message.what=STATE_CONNECTED;
            handler.sendMessage(message);
            sendReceive=new SendReceive(socket);
            sendReceive.start();
        }
    }

    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;

            try {
                tempIn=bluetoothSocket.getInputStream();
                tempOut=bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream=tempIn;
            outputStream=tempOut;
        }

        public void run()
        {
            while (true)
            {
                try {
                    if(!bluetoothSocket.isConnected())
                    {
                        Message message = Message.obtain();
                        message.what=STATE_CONNECTING;
                        handler.sendMessage(message);

                        status.setText("Connecting");
                    }
                    else {
//                        InputStream inputStream = bluetoothSocket.getInputStream();
                        inputStream.skip(inputStream.available());
                        String b = "";
                        while (true) {
                            byte x = (byte) inputStream.read();
                            if (x == 32) {
                                break;
                            }
                            b = b + (char) x;
                        }
                        System.out.println("received : " + b);
                        textViewProdBarcode.setText(b);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                    connectAgain();
                    startAgain();
                    break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

