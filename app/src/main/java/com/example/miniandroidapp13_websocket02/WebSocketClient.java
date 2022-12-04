package com.example.miniandroidapp13_websocket02;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

class WebSocketClient extends WebSocketListener {
    private WebSocket webSocket;

    // コンストラクタ
    public WebSocketClient() {
        OkHttpClient client = new OkHttpClient();

        // 接続先のエンドポイント
        // エミュレータから実行するために、ここではlocalhostとか127.0.0.1ではないことに注意
        Request.Builder request = new Request.Builder();
        request.url("ws://10.0.2.2:8080/demo/WebSocketDemo");

        webSocket = client.newWebSocket(request.build(), this);
    }

    public void send(String message) {
        System.out.println("kota: send message/ "+message);
        webSocket.send(message);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        System.out.println("kota: WebSocket opened successfully");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println("kota: Received text message/ " + text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        System.out.println("kota: Received binary message/ " + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        System.out.println("kota: Connection closed start/ " + code + reason);
        webSocket.close(1000, null);
        System.out.println("kota: Connection closed end/ " + code + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        System.out.println("kota: Connection failed/ " + t.getLocalizedMessage());
    }

}
