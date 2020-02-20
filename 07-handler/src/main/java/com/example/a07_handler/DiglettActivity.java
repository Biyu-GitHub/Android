package com.example.a07_handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class DiglettActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    public static final int CODE = 10002;
    private TextView score;
    private ImageView imageView;
    private Button button;

    public int[][] position = new int[][]{
            {342, 180}, {432, 880},
            {521, 256}, {429, 780},
            {456, 976}, {145, 665},
            {123, 678}, {564, 567}
    };

    private int mTotalCount;
    private int mSuccessCount;
    public static final int MAX_COUNT = 10;

    private DiglettHandler handler = new DiglettHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diglett);

        imageView = findViewById(R.id.image_view);
        button = findViewById(R.id.start);
        score = findViewById(R.id.score);

        button.setOnClickListener(this);
        imageView.setOnTouchListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                start();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.setVisibility(View.GONE);
        mSuccessCount++;
        score.setText("打到了" + mSuccessCount + "只，共" + MAX_COUNT + "只");
        return false;
    }

    private void start() {
        button.setText("游戏中");
        button.setEnabled(false);
        next(0);
    }

    private void next(int delayTime) {
        int pos = new Random().nextInt(position.length);

        Message message = Message.obtain();
        message.what = CODE;
        message.arg1 = pos;

        handler.sendMessageDelayed(message, delayTime);
        mTotalCount++;
    }


    public static class DiglettHandler extends Handler {
        WeakReference<DiglettActivity> weakReference;

        public DiglettHandler(DiglettActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            DiglettActivity diglettActivity = weakReference.get();

            switch (msg.what) {
                case CODE:
                    if (diglettActivity.mTotalCount > MAX_COUNT) {
                        diglettActivity.clear();
                        return;
                    }

                    int pos = msg.arg1;
                    diglettActivity.imageView.setX(diglettActivity.position[pos][0]);
                    diglettActivity.imageView.setY(diglettActivity.position[pos][1]);
                    diglettActivity.imageView.setVisibility(View.VISIBLE);

                    int randtime = new Random().nextInt(1000) + 1000;

                    diglettActivity.next(randtime);
                    break;
                default:
                    break;
            }
        }
    }

    private void clear() {
        mTotalCount = 0;
        mSuccessCount = 0;
        imageView.setVisibility(View.GONE);
        button.setText("开始游戏");
        button.setEnabled(true);
        score.setText("游戏结束");
    }


}
