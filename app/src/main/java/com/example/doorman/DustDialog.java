package com.example.doorman;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import retrofit2.Call;

public class DustDialog extends Dialog {
    private TextView apiTest;
    private Button mLeftButton;

    private View.OnClickListener mLeftClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        lpWindow.width = WindowManager.LayoutParams.MATCH_PARENT;
        lpWindow.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dust_dialog);

        mLeftButton = findViewById(R.id.btDustClose);
        apiTest = findViewById(R.id.tvDustApiTest);


        new Thread() {
            public void run() {

                Call<Object> getTest = RetrofitClient.getApiService().getTest();

                try {
                    Log.i("AABc", "test");
                    apiTest.setText(getTest.execute().body().toString());
                } catch (IOException e) {
                    Log.i("ABC", "API 실패");
                    e.printStackTrace();
                }
            }
        }.start();

        // 클릭 이벤트 셋팅

        if (mLeftClickListener != null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
        }
    }

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public DustDialog(Context context, String title, String content,
                        View.OnClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
//        this.mTitle = title;
//        this.mContent=content;
        this.mLeftClickListener = singleListener;
    }
}
