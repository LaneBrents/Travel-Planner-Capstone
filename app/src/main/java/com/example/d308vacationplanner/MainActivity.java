package com.example.d308vacationplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button viewVacationsButton = findViewById(R.id.buttonViewVacations);

        viewVacationsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, VacationListActivity.class);
            startActivity(intent);
        });
    }
}