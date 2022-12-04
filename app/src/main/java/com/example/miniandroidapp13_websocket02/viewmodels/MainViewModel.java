package com.example.miniandroidapp13_websocket02.viewmodels;


import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

// インナークラスの参考記事
// http://ichitcltk.hustle.ne.jp/gudon2/index.php?pageType=file&id=java008_inner_class3

public class MainViewModel extends ViewModel {

    private WebSocketClient webSocketClient;

    //ライブデータ
    private static MutableLiveData<String> textLiveData;

    // コンストラクタ
    public MainViewModel() {
        this.textLiveData = new MutableLiveData<>();
        this.textLiveData.setValue("テキスト");
        webSocketClient = new WebSocketClient();
    }

    //-----------------------------------------------------------------------------
    // getter
    public LiveData<String> getTextLiveData() {
        return this.textLiveData;
    }

    //setter
    public void sendMessageWebSocket(String message) {
        webSocketClient.sendMessage(message);
    }

    public void closeWebSocket() {
        //第1引数(code)には、1000、または3000〜4000の整数を指定
        //第2引数(reason)には、切断理由を表す文字列を指定
        webSocketClient.webSocket.close(1000,"切断のボタンを押した");
    }

    public void openWebSocket() {
        webSocketClient = new WebSocketClient();
    }
    //-----------------------------------------------------------------------------


    /*
    WebSocketに関わるインナークラス
     */
    public class WebSocketClient extends WebSocketListener {

        private WebSocket webSocket;

        private Thread mThread;
        private Handler mHandler = new Handler();

        // 外部クラスのLiveDateにアクセスする
        private void setTextLiveData(String message) {
            // UIスレッドにしないと動かない...
            mHandler.post(new Runnable() {
                public void run() {
                    // UIの操作
                    textLiveData.setValue(message);
                }
            });
        }

        public WebSocketClient() {
            OkHttpClient client = new OkHttpClient();

            // 接続先のエンドポイント
            // エミュレータから実行するために、ここではlocalhostとか127.0.0.1ではないことに注意
            Request.Builder request = new Request.Builder();
            request.url("ws://10.0.2.2:8080/demo/WebSocketDemo");

            webSocket = client.newWebSocket(request.build(), this);
        }

        //-----------------------------------------------------------------------------
        //setter
        public void sendMessage(String message) {
            System.out.println("kota: メッセージを送信："+message);
            webSocket.send(message);
        }
        //-----------------------------------------------------------------------------

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            this.setTextLiveData("接続完了");
            System.out.println("kota: WebSocketの接続成功");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            this.setTextLiveData("接続中"+text);
            System.out.println("kota: メッセージを受け取りました：" + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            this.setTextLiveData("接続中"+bytes.hex());
            System.out.println("kota: メッセージを受け取りました：" + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(1000, null);
            this.setTextLiveData("切断しました");
            System.out.println("kota: WebSocketが切断されました：" + code + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            this.setTextLiveData("接続失敗");
            System.out.println("kota: WebSocketの接続が失敗しました："+ t.getLocalizedMessage());
        }
    }
}