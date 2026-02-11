package com.example.workmanagerjava;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Data data = new Data.Builder().putInt("intKey",1).build(); // workera yollayacağımız değer

        Constraints constraints = new Constraints.Builder()
                //.setRequiredNetworkType(NetworkType.CONNECTED); // internete bağlı olması
                .setRequiresCharging(false) // şarja bağlı olup olmama durumu
                .build();

        //WORK REQUEST
        //WorkRequest // genel amaçlı

        //OneTimeWorkRequest //bir defaya mahsus request
        /*WorkRequest workRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
                .setConstraints(constraints)
                .setInputData(data)
                //.setInitialDelay(5, TimeUnit.MINUTES) // uygulama çalıştıktan 5 dakika sonra işlemi yap
                //.addTag("Tag"); // birden fazla work requesti karıştırmadan almak için kullanışlı
                .build();

        WorkManager.getInstance(this).enqueue(workRequest); //requesti sıraya al ve çalıştır
        */

        //PeriodicWorkRequest // belirli aralıklarla yapılan
        WorkRequest workRequest = new PeriodicWorkRequest.Builder(RefreshDatabase.class,15,TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(data)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo.getState() == WorkInfo.State.RUNNING){
                    System.out.println("running");
                } else if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    System.out.println("succeded");
                } else if (workInfo.getState() == WorkInfo.State.FAILED){
                    System.out.println("failed");
                }
            }
        });

        // WorkManager.getInstance(this).cancelAllWork()  // işlemlerin hepsini iptal etme

        //Chaining = zincirleme
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();
        // ardı ardına çalışacak bir kerelik işler için
        WorkManager.getInstance(this).beginWith(oneTimeWorkRequest)
                .then(oneTimeWorkRequest)
                .then(oneTimeWorkRequest)
                .enqueue();
    }

}