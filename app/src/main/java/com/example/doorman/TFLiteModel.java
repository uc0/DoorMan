package com.example.doorman;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class TFLiteModel {
//    private static FirebaseCustomLocalModel localModel;
//    private static FirebaseModelInterpreter interpreter;
    private static FirebaseModelInputOutputOptions inputOutputOptions;
    float[][] rst;

    // "tflite" 모델 불러오기
    public FirebaseCustomLocalModel loadModel(FirebaseCustomLocalModel localModel) {
        localModel = new FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("mask.tflite")
                .build();

        return localModel;
    }

    // 인터프리터 만들기
    public FirebaseModelInterpreter setInterpreter(FirebaseModelInterpreter interpreter, FirebaseCustomLocalModel localModel) throws FirebaseMLException {
        try {
            FirebaseModelInterpreterOptions options =
                    new FirebaseModelInterpreterOptions.Builder(localModel).build();
            interpreter = FirebaseModelInterpreter.getInstance(options);
        } catch (FirebaseMLException e) {
            Log.e("Firebase", "error" + e.getMessage());
        }

        inputOutputOptions = new FirebaseModelInputOutputOptions.Builder()
                .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 224, 224, 3})
                .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 2})
                .build();

        return interpreter;
    }

    // 판별 시작
    public void setInputArray(FirebaseModelInterpreter firebaseInterpreter, Context context, Bitmap bitmap) throws FirebaseMLException {
        // Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.test2);

        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

        int batchNum = 0;
        float[][][][] input = new float[1][224][224][3];

        for (int x = 0; x < 224; x++) {
            for (int y = 0; y < 224; y++) {
                int pixel = bitmap.getPixel(x, y);
                // Normalize channel values to [-1.0, 1.0]. This requirement varies by
                // model. For example, so     .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 224, 224, 3})
                // me models might require values to be normalized
                // to the range [0.0, 1.0] instead.
                input[batchNum][x][y][0] = (Color.red(pixel) - 127) / 128.0f;
                input[batchNum][x][y][1] = (Color.green(pixel) - 127) / 128.0f;
                input[batchNum][x][y][2] = (Color.blue(pixel) - 127) / 128.0f;
            }
        }

        FirebaseModelInputs inputs = new FirebaseModelInputs.Builder()
                .add(input)  // add() as many input arrays as your model requires
                .build();
        firebaseInterpreter.run(inputs, inputOutputOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseModelOutputs>() {
                            @Override
                            public void onSuccess(FirebaseModelOutputs result) {
                                float[][] output = result.getOutput(0);
                                float[] probabilities = output[0];

                                BufferedReader reader = null;
                                try {
                                    reader = new BufferedReader(new InputStreamReader(context.getAssets().open("labels.txt")));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                for (float probability : probabilities) {
                                    String label = null;
                                    try {
                                        label = reader.readLine();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("MLKit", String.format("%s: %1.4f", label, probability));

                                    if (label.equals("unmask") && probability >= 0.6) {
                                        Log.d("event-mask", "전송");
                                        EventBus.getDefault().post(new MainActivity.DataEvent("unmask"));

                                    }
                                }
                                Log.i("Output", "Output: " + Arrays.deepToString(output));
                                rst = output;
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
    }
}
