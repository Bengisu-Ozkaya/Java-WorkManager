package com.example.workmanagerjava;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class RefreshDatabase extends Worker {
    Context rdbContext;
    public RefreshDatabase(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        rdbContext = context;
    }

    @NonNull
    @Override
    public Result doWork() { //workmanagerın ne yapacağını söylediğimiz yer
        Data data = getInputData();
        int myNumber = data.getInt("intKey",0);
        refreshDatabase(myNumber);
        return Result.success(); //sharedPreferences'den aldığım veriyi döndür
    }

    private void refreshDatabase(int myNumber){
        SharedPreferences sharedPreferences = rdbContext.getSharedPreferences("package com.example.workmanagerjava",MODE_PRIVATE);
        int savedNumber = sharedPreferences.getInt("number",0);
        savedNumber += myNumber;
        System.out.println(savedNumber);
        sharedPreferences.edit().putInt("number",savedNumber).apply();
    }
}
