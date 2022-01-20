package com.example.trolley;


// new imports
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
// end

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CartActivity extends AppCompatActivity implements PaymentResultListener {

    // New Functions
    // end

    public static Boolean clickedToPay=false;
    public static String macAddress;
    static TextView status;
    BluetoothAdapter bluetoothAdapter;
    ImageView refreshButton;
    Button StartScan;

    CartActivity.SendReceive sendReceive;
    CartActivity.ServerClass serverClass;
    CartActivity.ClientClass clientClass;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=6;
    static final int STATE_THEY_NOT_ALLOWED=5;

    int REQUEST_ENABLE_BLUETOOTH=1;
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public void starter(){
        serverClass = new CartActivity.ServerClass();
        serverClass.start();
    }

    public void startAgain(){
        System.out.println(" Here in the Start Again");
        CartActivity.SaverClass saverClass = new CartActivity.SaverClass();
        saverClass.start();
    }
    public void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e("unpair TAG :-  ", e.getMessage());
        }
    }

        TextView textViewInvoiceId;
        static TextView textViewProdBarcode;
        private long pressedTime;
        Switch addDelSwitch;
        String prodName;
        static String invoiceId;
        Double prodPrice;
        LottieAnimationView notepadView;
        LottieAnimationView payBg;
        LottieAnimationView addup;
        LottieAnimationView removed;

        private RecyclerView recyclerView;
        private static RecyclerView.Adapter adapter;
        private List<ListItem> listItems;

        static TextView textViewAmount;
        static Double amount=0.0, mailAmount;
        String mailBody;

        User user = SharedPrefManager.getInstance(this).getUser();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cart);
            Invoice invoice = SharedPrefManager.getInstance(this).getInvoice();
            invoiceId = String.valueOf(invoice.getId());
            addDelSwitch=(Switch) findViewById(R.id.switch1);
            textViewInvoiceId = findViewById(R.id.textViewInvoiceId);
            textViewInvoiceId.setText(String.valueOf(invoice.getId()));
            status = (TextView) findViewById(R.id.status);
            textViewProdBarcode =  findViewById(R.id.textViewProdBarcode);
            refreshButton = (ImageView) findViewById(R.id.refreshButton);
            refreshButton.setEnabled(false);
            notepadView = (LottieAnimationView) findViewById(R.id.notepadAnimation);
            notepadView.setAnimation("notepadnew.json");
            notepadView.setMinAndMaxFrame(71,222);
            notepadView.playAnimation();
            notepadView.loop(true);

            payBg = (LottieAnimationView) findViewById(R.id.payBg);
            payBg.setAnimation("paybg.json");
            payBg.playAnimation();
            payBg.loop(true);


            addup = (LottieAnimationView) findViewById(R.id.addup);
            addup.setAnimation("addup.json");
            addup.loop(false);

            removed = (LottieAnimationView) findViewById(R.id.removed);
            removed.setAnimation("removenow.json");
            removed.loop(false);

            notepadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {openPopUpNotepad();

                }
            });


            textViewProdBarcode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) { }
            });

            String BLE_PIN= "1234";
            BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if(BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action))
                    {
//                    BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress);
                        bluetoothDevice.setPin(BLE_PIN.getBytes());
                        abortBroadcast();
                        Log.e("TAG","Auto-entering pin: " + BLE_PIN);


                    }


                }
            };

            IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
            intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
            this.getApplicationContext().registerReceiver(broadCastReceiver,intentFilter);

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            Context context = this;


            listItems = new ArrayList<>();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(CartActivity.this,R.color.white));// set status background white

            findViewById(R.id.buttonPay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("User Click to Pay Button");
                    if(amount!=0.0){
                    makePayment();
                    }
                }
            });

//            findViewById(R.id.buttonNotepad).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    openPopUpNotepad();
//                }
//            });

            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("You Clicked Retry");
                    Context context = CartActivity.this;
                    if(!bluetoothAdapter.isEnabled())
                        connectAgain();
                    ServerClass serverClass = new ServerClass();
                    serverClass.start();
                    IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
                    intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
                    context.registerReceiver(broadCastReceiver,intentFilter);
                }
            });

            bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
            if(!bluetoothAdapter.isEnabled())
            {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,0);
            }
            BluetoothDevice hc05 = bluetoothAdapter.getRemoteDevice(macAddress);
            starter();

        }

    private void openPopUpNotepad() {
            startActivity(new Intent(CartActivity.this, NotepadActivity.class));
    }


    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();

        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }


    private void searchProduct(String barcode,Boolean del) {
//            final String barcode = textViewProdBarcode.getText().toString()
//        String barcode="8901030716263";

            System.out.println(barcode + "This Data Received");

            class searchProduct extends AsyncTask<Void, Void, String> {
                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("barcode", barcode);

                    //returning the response
                    return requestHandler.sendPostRequest(URLs.URL_SEARCH, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //hiding the progressbar after completion
                    System.out.println("In post execute");
                    System.out.println("Printing s" + s);

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);

                        //if no error in response
                        if (!obj.getBoolean("error")) {
//                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            System.out.println("In post execute without error");
                            //getting the user from the response
                            JSONObject userJson = obj.getJSONObject("product");

                            prodName = userJson.getString("name");
                            prodPrice = userJson.getDouble("price");
                            System.out.println("We Got HEre (addProduct)");
                            if (!del) {

                                addProduct(prodName, String.valueOf(prodPrice), barcode);


                                listItems.clear();
                                new GetCartList().execute();

                                addup.playAnimation();

                            }
                            else {
                                minusProduct(prodName, String.valueOf(prodPrice));
                                if (listItems.isEmpty() ){
                                    textViewAmount.setText("Total: 0.0");
                                }
                                listItems.clear();
                                new GetCartList().execute();
                                removed.playAnimation();
                            }




                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            searchProduct sp = new searchProduct();
            sp.execute();

        }

        private void addProduct(String prodName, String prodPrice,String prodBarcode) {
//            final String prodBarcode = textViewProdBarcode.getText().toString();

            class addProduct extends AsyncTask<Void, Void, String> {
                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("invoiceId", invoiceId);
                    params.put("prodBarcode", prodBarcode);
                    params.put("prodName", prodName);
                    params.put("prodPrice", prodPrice);

                    //returning the response
                    return requestHandler.sendPostRequest(URLs.URL_ADD, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);

                        //if no error in response
                        if (!obj.getBoolean("error")) {
                            //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            textViewProdBarcode.setText("");
//                            listItems.clear();
//                            new GetCartList().execute();


                        } else {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            addProduct ap = new addProduct();
            ap.execute();

        }


        class GetCartList extends AsyncTask<Void, Void, String> {


            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("invoiceId", invoiceId);

                //returning the response
                return requestHandler.sendPostRequest(URLs.URL_CART, params);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                textViewAmount = findViewById(R.id.textViewAmount);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        textViewAmount.setText("Total: " + String.valueOf(obj.getDouble("Total")));
                        amount = obj.getDouble("Total");


                        //getting the user from the response
                        JSONArray products = obj.getJSONArray("cart");

                        for (int i = 0; i < products.length(); i++) {
                            JSONObject o = products.getJSONObject(i);
                            ListItem item = new ListItem(
                                    o.getString("prodName"),
                                    o.getString("prodQuantity"),
                                    o.getString("prodPrice")
                            );
                            listItems.add(item);

                        }


                        if (!findViewById(R.id.buttonPay).isEnabled()){
                            findViewById(R.id.buttonPay).setEnabled(true);
                        }
                        adapter = new MyAdapter(listItems, getApplicationContext());
                        recyclerView.setAdapter(adapter);


                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        if (listItems.isEmpty() ){
                            textViewAmount.setText("Total: 0.0");
                            amount=0d;
                            findViewById(R.id.buttonPay).setEnabled(false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }


        public static void minusProduct(String prodName, String prodPrice) {


            //final String invoiceId = textViewProdBarcode.getText().toString();


            class minusProduct extends AsyncTask<Void, Void, String> {
                @Override
                protected String doInBackground(Void... voids) {


                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("invoiceId", invoiceId);
                    params.put("prodName", prodName);
                    params.put("prodPrice", prodPrice);

                    //returning the response
                    return requestHandler.sendPostRequest(URLs.URL_MINUS, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);

                        //if no error in response
                        if (!obj.getBoolean("error")) {

                            adapter.notifyDataSetChanged();


                        } else {
                            //Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();




                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            minusProduct mp = new minusProduct();
            mp.execute();
        }

        public static void getTotal() {

            //final String invoiceId = "169";

            class getTotal extends AsyncTask<Void, Void, String> {
                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("invoiceId", invoiceId);

                    //returing the response
                    return requestHandler.sendPostRequest(URLs.URL_CART, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //hiding the progressbar after completion

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);

                        //if no error in response
                        if (!obj.getBoolean("error")) {
                            textViewAmount.setText("Total: " + String.valueOf(obj.getDouble("Total")));
                            amount = obj.getDouble("Total");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            getTotal gt = new getTotal();
            gt.execute();

        }


        private void makePayment() {
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_CBA9BNgW9rdt9V");

            //checkout.setImage(R.drawable.logo);

            /**
             * Reference to current activity
             */
            final Activity activity = CartActivity.this;

            /**
             * Pass your payment options to the Razorpay Checkout as a JSONObject
             */
            try {
                JSONObject options = new JSONObject();

                options.put("name", "YCCE PROJECT");
                options.put("description", "Invoice no: " + invoiceId);
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
                options.put("theme.color", "#3399cc");
                options.put("currency", "INR");
                options.put("amount", amount * 100);//500 * 100 == INR 500
                options.put("prefill.email", user.getEmail());
                options.put("prefill.contact", user.getPhone()); //You will get a OTP on this During payment
                JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);

                checkout.open(activity, options);

            } catch (Exception e) {
                Log.e("TAG", "Error in starting Razorpay Checkout", e);
            }
        }

        @Override
        public void onPaymentSuccess(String s) {

            updateInvoice(s);

        }

        @Override
        public void onPaymentError(int i, String s) {
            Toast.makeText(getApplicationContext(), "Payment Failed " + s, Toast.LENGTH_SHORT).show();
        }

        private void updateInvoice(String PaymentId) {
            final String id = invoiceId;
            final String total = amount.toString();
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);;

            class updateInvoice extends AsyncTask<Void, Void, String> {
                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("PaymentId", PaymentId);
                    params.put("total", total);

                    //returning the response
                    return requestHandler.sendPostRequest(URLs.URL_PAY, params);
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    //displaying the progress bar while user pays the bill
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    //hiding the progressbar after completion
                    progressBar.setVisibility(View.GONE);
                    System.out.println("On Post Execute in RazorPay");

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(result);
                        System.out.println("result :- " + result);
                        //if no error in response
                        if (!obj.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), "Payment Successful!\n" + "Invoice- " + id + "  Total: " + amount, Toast.LENGTH_SHORT).show();
                            clickedToPay=true;
                            sendReceive.interrupt();
                            clientClass.interrupt();
                            serverClass.interrupt();
                            unpairDevice(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress));
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            getMailBody(PaymentId);

                        } else {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            updateInvoice ui = new updateInvoice();
            ui.execute();

        }


        private void getMailBody(String PaymentId) {

            class getMailBody extends AsyncTask<Void, Void, String> {
                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("invoiceId", invoiceId);

                    //returning the response
                    return requestHandler.sendPostRequest(URLs.URL_CART, params);

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    try {
                        mailBody = "";
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);

                        //if no error in response
                        if (!obj.getBoolean("error")) {
                            mailAmount = obj.getDouble("Total");


                            //getting the user from the response
                            JSONArray products = obj.getJSONArray("cart");
                            for (int i = 0; i < products.length(); i++) {
                                JSONObject o = products.getJSONObject(i);
                                mailBody = mailBody + ("Name: " + o.getString("prodName") + "<br>Quantity: " + o.getString("prodQuantity") + " Price: " + o.getString("prodPrice")) + "<br><br>";

                            }

                            sendMAil(PaymentId);



                        } else {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            getMailBody mb = new getMailBody();
            mb.execute();
        }


    private void sendMAil(String PaymentId){

        SendGrid sendGrid = new SendGrid(BuildConfig.API_KEY);
        sendGrid.setSendgridResponseCallbacks(new SendGridResponseCallback() {
            @Override
            public void onMailSendSuccess(SendGrid.Response response) {
                Log.e("LOG", "" + response.getCode());
                System.out.println("Sent Successful");
            }

            @Override
            public void onMailSendFailed(SendGrid.Response response) {
                Log.e("LOG", "" + response.getCode());
                System.out.println("failed");

            }
        });
        SendGrid.Email email = new SendGrid.Email()
                .addTo(user.getEmail(), user.getUsername())
                .setSubject("Thanks for Shopping with us "+user.getUsername())
                .setReplyTo("e.trolley99@gmail.com","E-Trolley")
                .setFrom("e.trolley99@gmail.com","E-Trolley")
                .setHtml("<strong>Invoice no.- </strong>" + invoiceId +
                        "<br><br>"+ mailBody +
                        "<br><strong>Total: </strong>" +amount+
                        "<br><strong>Transaction ID :</strong>"+PaymentId)
                .setText("Text");

        try {
            sendGrid.send(email);
        } catch (SendGridException e) {
            Log.d("LOG", e.getLocalizedMessage());
        }


    }

// New Codes
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

//        while (!bluetoothAdapter.isEnabled()){};
////        status.setText("Connecting Again Please Wait...");
//        startAgain();
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


    Handler handler=new Handler(Looper.getMainLooper(), new Handler.Callback() {

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
//                    String tempMsg = (String) msg.obj;
//                    System.out.println("received : - "+ tempMsg);
////                    msg_box.setText("Barcode Adding : "+tempMsg);
                    break;
            }
            return true;
        }
    });


    public class ServerClass extends Thread{

        BluetoothDevice hc05 = bluetoothAdapter.getRemoteDevice(macAddress);
        public ServerClass()
        {

//            textViewProdBarcode.setText("Barcode");
//            status.setText("Click on Start Button");

        }

        public void run(){
            Message message = Message.obtain();
            message.what=STATE_CONNECTING;
            handler.sendMessage(message);
            System.out.println("ServerClass Called");
             clientClass = new CartActivity.ClientClass(hc05);
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
            CartActivity.ClientClass clientClass = new CartActivity.ClientClass(hc05);
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
                    if(!socket.isConnected())
                    {
                        socket.connect();

                    }
                    System.out.println("Connected :-  "+socket.isConnected());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                counter++;
            }while(!socket.isConnected() && counter<5);
            counter=0;
        }

        public void run()
        {
            System.out.println("ClientClass called");
            if(clickedToPay){
                clickedToPay=false;
            }
            Message message=Message.obtain();
            message.what=STATE_CONNECTED;
            handler.sendMessage(message);
            sendReceive=new CartActivity.SendReceive(socket);
            sendReceive.start();
        }
    }

    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;
        BluetoothDevice hc05 = bluetoothAdapter.getRemoteDevice(macAddress);

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket=socket;
            InputStream tempIn=null;
            OutputStream tempOut=null;
            refreshButton.setEnabled(false);

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

                while (!clickedToPay) {
                    try {
                        if (!bluetoothSocket.isConnected()) {
                            Message message = Message.obtain();
                            message.what = STATE_CONNECTING;
                            handler.sendMessage(message);
//                            if(!clickedToPay){
//                                connectAgain();
//                                startAgain();}
                            interrupt();
                            inputStream.close();
                            outputStream.close();
                            bluetoothSocket.close();
                            sendReceive.interrupt();
                            clientClass.interrupt();
                            serverClass.interrupt();

//                        status.setText("Connecting");
                        } else {
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

                            searchProduct(b.replaceAll("\\s", ""), addDelSwitch.isChecked());


//                        addProduct("Vaseline","50");

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        unpairDevice(hc05);
                        Message message = Message.obtain();
                        message.what = STATE_CONNECTION_FAILED;
                        handler.sendMessage(message);
//                        if(!clickedToPay){
//                        connectAgain();
//                        startAgain();}
                        if(!bluetoothAdapter.isEnabled())
                            connectAgain();
                        interrupt();
                        try {
                            inputStream.close();
                            outputStream.close();
                            bluetoothSocket.close();
                            sendReceive.interrupt();
                            clientClass.interrupt();
                            serverClass.interrupt();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshButton.setEnabled(true);
                            }
                        });

                        break;
                    }
                }
            }
        }
    }
// end


