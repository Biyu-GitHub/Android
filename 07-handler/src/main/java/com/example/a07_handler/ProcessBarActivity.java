package com.example.a07_handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class ProcessBarActivity extends AppCompatActivity {

    public static final int PROGRESS_CODE = 10003;
    public static final int DELAY_MILLIS = 100;
    private ProgressBar progressBar;
    private TextView textView;
    ProgressHandler handler = new ProgressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_bar);

        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = PROGRESS_CODE;
                message.arg1 = 0;
                handler.sendMessageDelayed(message, DELAY_MILLIS);
            }
        }).start();
    }


    public static class ProgressHandler extends Handler {
        WeakReference<ProcessBarActivity> weakReference;

        public ProgressHandler(ProcessBarActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            ProcessBarActivity activity = weakReference.get();

            switch (msg.what) {
                case PROGRESS_CODE:
                    int val = msg.arg1 + 1;

                    if (val <= 100) {
                        activity.progressBar.setProgress(val);
                        activity.textView.setText(val + "%");

                        Message message = Message.obtain();
                        message.what = PROGRESS_CODE;
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
