package com.example.a07_handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class CountDownTimeActivity extends AppCompatActivity {

    public static final int COUNT_DOWN_TIME_CODE = 10001;
    public static final int MAX_TIME = 10;
    public static final int DELAY_MILLIS = 1000;
    private TextView textView;
    private CountDownTimeHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down_time);

        textView = findViewById(R.id.textView);

        handler = new CountDownTimeHandler(this);

        Message message = Message.obtain();
        message.what = COUNT_DOWN_TIME_CODE;
        message.arg1 = MAX_TIME;
        handler.sendMessageDelayed(message, DELAY_MILLIS);
    }


    public static class CountDownTimeHandler extends Handler {
        WeakReference<CountDownTimeActivity> weakReference;

        public CountDownTimeHandler(CountDownTimeActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            CountDownTimeActivity activity = weakReference.get();

            switch (msg.what) {
                case COUNT_DOWN_TIME_CODE:
                    int val = msg.arg1 - 1;

                    if (val >= 0) {
                        activity.textView.setText(String.valueOf(val));

                        Message message = Message.obtain();
                        message.what = COUNT_DOWN_TIME_CODE;
                        message.arg1 = val;
                        sendMessageDelayed(message, DELAY_MILLIS);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
