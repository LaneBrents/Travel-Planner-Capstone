package com.example.d308vacationplanner;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.d308vacationplanner.adapter.ExcursionAdapter;
import com.example.d308vacationplanner.database.Excursion;
import java.util.ArrayList;
import java.util.List;
import android.widget.CheckBox;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308vacationplanner.database.AppDatabase;
import com.example.d308vacationplanner.database.Vacation;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VacationDetailActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText hotelEditText;
    private EditText startDateEditText;
    private EditText endDateEditText;

    private CheckBox startAlertCheckBox;
    private CheckBox endAlertCheckBox;

    private RecyclerView excursionRecyclerView;

    private TextView noExcursionsTextView;

    private ExcursionAdapter excursionAdapter;

    private final List<Excursion> excursionList = new ArrayList<>();

    private Button saveButton;
    private Button deleteButton;

    private Button shareButton;

    private AppDatabase database;
    private ExecutorService executorService;

    private int vacationId = -1;
    private Vacation currentVacation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_detail);

        requestNotificationPermission();

        titleEditText = findViewById(R.id.editTextVacationTitle);
        hotelEditText = findViewById(R.id.editTextVacationHotel);
        startDateEditText = findViewById(R.id.editTextVacationStartDate);
        endDateEditText = findViewById(R.id.editTextVacationEndDate);
        startAlertCheckBox = findViewById(R.id.checkBoxStartAlert);
        endAlertCheckBox = findViewById(R.id.checkBoxEndAlert);

        TextView detailTitle = findViewById(R.id.textViewVacationDetailTitle);

        saveButton = findViewById(R.id.buttonSaveVacation);
        deleteButton = findViewById(R.id.buttonDeleteVacation);
        shareButton = findViewById(R.id.buttonShareVacation);

        excursionRecyclerView =
                findViewById(R.id.recyclerViewExcursions);

        noExcursionsTextView =
                findViewById(R.id.textViewNoExcursions);

        Button addExcursionButton =
                findViewById(R.id.buttonAddExcursion);

        excursionRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        excursionAdapter = new ExcursionAdapter(
                excursionList,
                excursion -> {

                    Intent intent = new Intent(
                            VacationDetailActivity.this,
                            ExcursionDetailActivity.class
                    );

                    intent.putExtra("excursionId", excursion.getId());
                    intent.putExtra("vacationId", vacationId);

                    startActivity(intent);
                }
        );

        excursionRecyclerView.setAdapter(excursionAdapter);

        addExcursionButton.setOnClickListener(view -> {

            Intent intent = new Intent(
                    VacationDetailActivity.this,
                    ExcursionDetailActivity.class
            );

            intent.putExtra("excursionId", -1);
            intent.putExtra("vacationId", vacationId);

            startActivity(intent);
        });

        database = AppDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();

        vacationId = getIntent().getIntExtra("vacationId", -1);

        if (vacationId == -1) {
            detailTitle.setText("Add Vacation");
            deleteButton.setVisibility(Button.GONE);
            shareButton.setVisibility(Button.GONE);
        } else {
            detailTitle.setText("Edit Vacation");
            deleteButton.setVisibility(Button.VISIBLE);
            shareButton.setVisibility(Button.VISIBLE);
            loadVacation();
        }

        saveButton.setOnClickListener(view -> saveVacation());
        deleteButton.setOnClickListener(view -> deleteVacation());
        shareButton.setOnClickListener(view -> shareVacation());
    }

    private void loadVacation() {
        executorService.execute(() -> {
            currentVacation = database.vacationDao().getVacationById(vacationId);

            runOnUiThread(() -> {
                if (currentVacation != null) {
                    titleEditText.setText(currentVacation.getTitle());
                    hotelEditText.setText(currentVacation.getHotel());
                    startDateEditText.setText(currentVacation.getStartDate());
                    endDateEditText.setText(currentVacation.getEndDate());
                    startAlertCheckBox.setChecked(currentVacation.isStartAlert());
                    endAlertCheckBox.setChecked(currentVacation.isEndAlert());
                }
            });
        });
    }

    private void loadExcursions() {

        if (vacationId == -1) {
            return;
        }

        executorService.execute(() -> {

            List<Excursion> excursions =
                    database.excursionDao()
                            .getExcursionsForVacation(vacationId);

            runOnUiThread(() -> {

                excursionList.clear();
                excursionList.addAll(excursions);

                excursionAdapter.notifyDataSetChanged();

                if (excursionList.isEmpty()) {

                    noExcursionsTextView.setVisibility(
                            TextView.VISIBLE
                    );

                    excursionRecyclerView.setVisibility(
                            RecyclerView.GONE
                    );

                } else {

                    noExcursionsTextView.setVisibility(
                            TextView.GONE
                    );

                    excursionRecyclerView.setVisibility(
                            RecyclerView.VISIBLE
                    );
                }
            });
        });
    }

    private void saveVacation() {
        String title = titleEditText.getText().toString().trim();
        String hotel = hotelEditText.getText().toString().trim();
        String startDate = startDateEditText.getText().toString().trim();
        String endDate = endDateEditText.getText().toString().trim();

        if (!ValidationUtils.isValidTitle(title)) {
            titleEditText.setError("Vacation title must be between 1 and 100 characters");
            titleEditText.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidLocation(hotel)) {
            hotelEditText.setError("Hotel or place must be between 1 and 100 characters");
            hotelEditText.requestFocus();
            return;
        }

        if (title.isEmpty()) {
            titleEditText.setError("Enter a vacation title");
            titleEditText.requestFocus();
            return;
        }

        if (hotel.isEmpty()) {
            hotelEditText.setError("Enter a hotel or place");
            hotelEditText.requestFocus();
            return;
        }

        if (startDate.isEmpty()) {
            startDateEditText.setError("Enter a start date");
            startDateEditText.requestFocus();
            return;
        }

        if (endDate.isEmpty()) {
            endDateEditText.setError("Enter an end date");
            endDateEditText.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidDate(startDate)) {
            startDateEditText.setError("Enter a valid date (MM/dd/yyyy)");
            startDateEditText.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidDate(endDate)) {
            endDateEditText.setError("Enter a valid date (MM/dd/yyyy)");
            endDateEditText.requestFocus();
            return;
        }

        Date startDateValue = ValidationUtils.parseDate(startDate);
        Date endDateValue = ValidationUtils.parseDate(endDate);

        if (startDateValue == null || endDateValue == null) {
            return;
        }

        if (!endDateValue.after(startDateValue)) {
            endDateEditText.setError("End date must be after the start date");
            endDateEditText.requestFocus();
            return;
        }

        if (vacationId == -1) {
            Vacation vacation = new Vacation(
                    title,
                    hotel,
                    startDate,
                    endDate
            );

            vacation.setStartAlert(startAlertCheckBox.isChecked());
            vacation.setEndAlert(endAlertCheckBox.isChecked());

            executorService.execute(() -> {
                database.vacationDao().insert(vacation);

                runOnUiThread(() -> {
                    scheduleVacationAlerts(vacation);

                    Toast.makeText(
                            VacationDetailActivity.this,
                            "Vacation saved",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                });
            });

        } else {
            currentVacation.setTitle(title);
            currentVacation.setHotel(hotel);
            currentVacation.setStartDate(startDate);
            currentVacation.setEndDate(endDate);

            currentVacation.setStartAlert(startAlertCheckBox.isChecked());
            currentVacation.setEndAlert(endAlertCheckBox.isChecked());

            executorService.execute(() -> {
                database.vacationDao().update(currentVacation);

                runOnUiThread(() -> {
                    scheduleVacationAlerts(currentVacation);

                    Toast.makeText(
                            VacationDetailActivity.this,
                            "Vacation updated",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                });
            });
        }
    }

    private void scheduleVacationAlerts(Vacation vacation) {

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Date startDate = ValidationUtils.parseDate(vacation.getStartDate());
        Date endDate = ValidationUtils.parseDate(vacation.getEndDate());

        if (startDate == null || endDate == null) {
            return;
        }

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        startCalendar.set(Calendar.HOUR_OF_DAY, 9);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        endCalendar.set(Calendar.HOUR_OF_DAY, 9);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);

        if (vacation.isStartAlert()) {
            scheduleAlert(
                    alarmManager,
                    vacation.getId() * 2,
                    startCalendar.getTimeInMillis(),
                    vacation.getTitle(),
                    "Vacation starts today"
            );
        } else {
            cancelAlert(alarmManager, vacation.getId() * 2);
        }

        if (vacation.isEndAlert()) {
            scheduleAlert(
                    alarmManager,
                    vacation.getId() * 2 + 1,
                    endCalendar.getTimeInMillis(),
                    vacation.getTitle(),
                    "Vacation ends today"
            );
        } else {
            cancelAlert(alarmManager, vacation.getId() * 2 + 1);
        }
    }

    private void scheduleAlert(
            AlarmManager alarmManager,
            int requestCode,
            long triggerTime,
            String vacationTitle,
            String alertType) {

        Intent intent = new Intent(this, VacationAlertReceiver.class);

        intent.putExtra("vacationTitle", vacationTitle);
        intent.putExtra("alertType", alertType);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        }
    }

    private void cancelAlert(AlarmManager alarmManager, int requestCode) {

        Intent intent = new Intent(this, VacationAlertReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        pendingIntent.cancel();
    }

    private void requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        100
                );
            }
        }
    }

    private void deleteVacation() {
        if (currentVacation == null) {
            return;
        }

        executorService.execute(() -> {
            int excursionCount =
                    database.vacationDao().getExcursionCount(vacationId);

            runOnUiThread(() -> {
                if (excursionCount > 0) {
                    Toast.makeText(
                            VacationDetailActivity.this,
                            "Cannot delete vacation with associated excursions.",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    executorService.execute(() -> {
                        database.vacationDao().delete(currentVacation);

                        runOnUiThread(() -> {
                            Toast.makeText(
                                    VacationDetailActivity.this,
                                    "Vacation deleted",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();
                        });
                    });
                }
            });
        });
    }

    private void shareVacation() {

        if (currentVacation == null) {
            Toast.makeText(
                    this,
                    "Vacation details are not loaded yet.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String vacationDetails =
                "Vacation Details\n\n" +
                        "Title: " + currentVacation.getTitle() + "\n" +
                        "Hotel or Place: " + currentVacation.getHotel() + "\n" +
                        "Start Date: " + currentVacation.getStartDate() + "\n" +
                        "End Date: " + currentVacation.getEndDate();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, currentVacation.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, vacationDetails);

        startActivity(Intent.createChooser(
                shareIntent,
                "Share Vacation Details"
        ));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (database != null) {
            loadExcursions();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
