package com.example.d308vacationplanner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.adapter.VacationAdapter;
import com.example.d308vacationplanner.database.AppDatabase;
import com.example.d308vacationplanner.database.Vacation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VacationListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noVacationsTextView;
    private VacationAdapter adapter;

    private final List<Vacation> vacationList = new ArrayList<>();

    private AppDatabase database;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        recyclerView = findViewById(R.id.recyclerViewVacations);
        noVacationsTextView = findViewById(R.id.textViewNoVacations);
        Button addVacationButton = findViewById(R.id.buttonAddVacation);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new VacationAdapter(vacationList, vacation -> {
            Intent intent = new Intent(VacationListActivity.this,
                    VacationDetailActivity.class);

            intent.putExtra("vacationId", vacation.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        database = AppDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();

        addVacationButton.setOnClickListener(view -> {
            Intent intent = new Intent(VacationListActivity.this,
                    VacationDetailActivity.class);

            intent.putExtra("vacationId", -1);
            startActivity(intent);
        });

        loadVacations();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (database != null) {
            loadVacations();
        }
    }

    private void loadVacations() {
        executorService.execute(() -> {
            List<Vacation> vacations = database.vacationDao().getAllVacations();

            runOnUiThread(() -> {
                vacationList.clear();
                vacationList.addAll(vacations);
                adapter.notifyDataSetChanged();

                if (vacationList.isEmpty()) {
                    noVacationsTextView.setVisibility(TextView.VISIBLE);
                    recyclerView.setVisibility(RecyclerView.GONE);
                } else {
                    noVacationsTextView.setVisibility(TextView.GONE);
                    recyclerView.setVisibility(RecyclerView.VISIBLE);
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
