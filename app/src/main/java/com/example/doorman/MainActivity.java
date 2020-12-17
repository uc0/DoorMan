package com.example.doorman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;


public class MainActivity extends AppCompatActivity {
    private static FirebaseCustomLocalModel localModel;
    private static FirebaseModelInterpreter interpreter;
    private static FirebaseModelInputOutputOptions inputOutputOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Camera Fragment 실행
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new Camera2BasicFragment())
                    .commit();
        }

        // 마스크 착용 유무 판별
//        TFLiteModel model = new TFLiteModel();
//
//        localModel = model.loadModel(localModel);
//        Context context = this.getApplicationContext();
//        try {
//            interpreter = model.setInterpreter(interpreter, localModel);
//            model.setInputArray(interpreter, context);
//        } catch (FirebaseMLException e) {
//            Log.e("Firebase", "error(onCreate)" + e.getMessage());
//            e.printStackTrace();
//        }
    }
}