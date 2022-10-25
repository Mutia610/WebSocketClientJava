package com.mutia.websocketscarletjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import tech.gusavila92.websocketclient.WebSocketClient;

public class MainDuaActivity extends AppCompatActivity {

    private static final String ECHO_URL = "ws://172.16.0.4:7110/wb?email=akindiv01@gmail.com?pasarSekunderId=140401";
    private WebSocketClient webSocketClient;
    private MessageAdapter adapter;
    RecyclerView rvMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dua);

        setupRecyclerViewMessage();
        createWebSocketClient();
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI(ECHO_URL);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send("Hello World!");
            }

            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            TextView textView = findViewById(R.id.tv_message);
//                            textView.setText(message);

                            try {
                                List<MessegeResponse> list = new ArrayList<>();

                                MessegeResponse messegeResponse = new MessegeResponse();
                                JSONArray jsonArray = new JSONArray(message);

                                for (int i = 0; i < jsonArray.length(); i++)
                                {

                                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                                    messegeResponse.setPrice((Double) jsonObj.get("price"));
                                    messegeResponse.setQuantity((Double) jsonObj.get("quantity"));

                                    list.add(i, messegeResponse);
//                                    adapter.addItem(messegeResponse);

//                                    System.out.println(jsonObj.get("quantity"));
                                }
                                System.out.println(list);

                                adapter.setData(list);

//                                textView.setText(jsonArray.toString());
                            }catch (JSONException err){
                                Log.d("Error", err.toString());
                            }

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }

            @Override
            public void onPingReceived(byte[] data) {
            }

            @Override
            public void onPongReceived(byte[] data) {
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };


        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    private final void setupRecyclerViewMessage() {
        this.adapter = new MessageAdapter();
        rvMessage = findViewById(R.id.recycler_message);

        rvMessage.setAdapter(this.adapter);
        rvMessage.setItemAnimator((RecyclerView.ItemAnimator)(new DefaultItemAnimator()));
        rvMessage.setLayoutManager((RecyclerView.LayoutManager)(new LinearLayoutManager((Context)this, RecyclerView.VERTICAL, false)));
    }
}