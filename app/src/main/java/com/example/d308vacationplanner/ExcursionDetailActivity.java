package com.example.d308vacationplanner;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308vacationplanner.database.AppDatabase;
import com.example.d308vacationplanner.database.Excursion;
import com.example.d308vacationplanner.database.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExcursionDetailActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText dateEditText;

    private Button saveButton;
    private Button deleteButton;

    private AppDatabase database;
    private ExecutorService executorService;

    private int excursionId = -1;
    private int vacationId = -1;

    private Excursion currentExcursion;
    private Vacation vacation;

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_detail);

        requestNotificationPermission();

        titleEditText = findViewById(R.id.editTextExcursionTitle);
        dateEditText = findViewById(R.id.editTextExcursionDate);

        TextView detailTitle =
                findViewById(R.id.textViewExcursionDetailTitle);

        saveButton = findViewById(R.id.buttonSaveExcursion);
        deleteButton = findViewById(R.id.buttonDeleteExcursion);

        database = AppDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();

        excursionId =
                getIntent().getIntExtra("excursionId", -1);

        vacationId =
                getIntent().getIntExtra("vacationId", -1);

        if (vacationId == -1) {
            Toast.makeText(
                    this,
                    "Vacation information is missing.",
                    Toast.LENGTH_LONG
            ).show();
            finish();
            return;
        }

        if (excursionId == -1) {
            detailTitle.setText("Add Excursion");
            deleteButton.setVisibility(Button.GONE);
        } else {
            detailTitle.setText("Edit Excursion");
            deleteButton.setVisibility(Button.VISIBLE);
            loadExcursion();
        }

        loadVacation();

        saveButton.setOnClickListener(view -> saveExcursion());

        deleteButton.setOnClickListener(view -> deleteExcursion());
    }

    private void loadVacation() {
        executorService.execute(() -> {
            vacation =
                    database.vacationDao()
                            .getVacationById(vacationId);
        });
    }

    private void loadExcursion() {
        executorService.execute(() -> {
            currentExcursion =
                    database.excursionDao()
                            .getExcursionById(excursionId);

            runOnUiThread(() -> {
                if (currentExcursion != null) {
                    titleEditText.setText(
                            currentExcursion.getTitle()
                    );

                    dateEditText.setText(
                            currentExcursion.getDate()
                    );
                }
            });
        });
    }

    private void saveExcursion() {

        String title =
                titleEditText.getText().toString().trim();

        String date =
                dateEditText.getText().toString().trim();

        if (title.isEmpty()) {
            titleEditText.setError(
                    "Enter an excursion title"
            );
            titleEditText.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            dateEditText.setError(
                    "Enter an excursion date"
            );
            dateEditText.requestFocus();
            return;
        }

        // B5: Validate excursion date format.
        if (!isValidDate(date)) {
            dateEditText.setError(
                    "Use the format MM/dd/yyyy"
            );
            dateEditText.requestFocus();
            return;
        }

        Date excursionDate = parseDate(date);

        if (excursionDate == null) {
            return;
        }

        executorService.execute(() -> {

            Vacation associatedVacation =
                    database.vacationDao()
                            .getVacationById(vacationId);

            if (associatedVacation == null) {
                runOnUiThread(() ->
                        Toast.makeText(
                                ExcursionDetailActivity.this,
                                "Associated vacation was not found.",
                                Toast.LENGTH_LONG
                        ).show()
                );
                return;
            }

            Date vacationStart =
                    parseDate(associatedVacation.getStartDate());

            Date vacationEnd =
                    parseDate(associatedVacation.getEndDate());

            if (vacationStart == null || vacationEnd == null) {
                return;
            }

            // B5: Excursion must occur during the vacation.
            if (excursionDate.before(vacationStart)
                    || excursionDate.after(vacationEnd)) {

                runOnUiThread(() -> {
                    dateEditText.setError(
                            "Excursion date must be during the vacation"
                    );
                    dateEditText.requestFocus();
                });

                return;
            }

            if (excursionId == -1) {

                Excursion excursion =
                        new Excursion(
                                title,
                                date,
                                vacationId
                        );

                long newExcursionId =
                        database.excursionDao().insert(excursion);

                excursion.setId((int) newExcursionId);

                runOnUiThread(() -> {
                    // B5: Schedule excursion notification.
                    scheduleExcursionAlert(excursion);

                    Toast.makeText(
                            ExcursionDetailActivity.this,
                            "Excursion saved",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                });

            } else {

                currentExcursion.setTitle(title);
                currentExcursion.setDate(date);

                database.excursionDao()
                        .update(currentExcursion);

                runOnUiThread(() -> {
                    // B5: Schedule excursion notification.
                    scheduleExcursionAlert(currentExcursion);

                    Toast.makeText(
                            ExcursionDetailActivity.this,
                            "Excursion updated",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                });
            }
        });
    }

    private void deleteExcursion() {

        if (currentExcursion == null) {
            return;
        }

        executorService.execute(() -> {

            database.excursionDao()
                    .delete(currentExcursion);

            runOnUiThread(() -> {
                Toast.makeText(
                        ExcursionDetailActivity.this,
                        "Excursion deleted",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            });
        });
    }

    /**
     * Schedules the excursion alert for 9:00 AM
     * on the excursion date.
     */
    private void scheduleExcursionAlert(Excursion excursion) {

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Date excursionDate =
                parseDate(excursion.getDate());

        if (excursionDate == null) {
            return;
        }

        Calendar excursionCalendar =
                Calendar.getInstance();

        excursionCalendar.setTime(excursionDate);
        excursionCalendar.set(Calendar.HOUR_OF_DAY, 9);
        excursionCalendar.set(Calendar.MINUTE, 0);
        excursionCalendar.set(Calendar.SECOND, 0);
        excursionCalendar.set(Calendar.MILLISECOND, 0);

        scheduleExcursionAlert(
                alarmManager,
                excursion.getId(),
                excursionCalendar.getTimeInMillis(),
                excursion.getTitle()
        );
    }

    /**
     * Creates the alarm that will trigger
     * ExcursionAlertReceiver.
     */
    private void scheduleExcursionAlert(
            AlarmManager alarmManager,
            int requestCode,
            long triggerTime,
            String excursionTitle) {

        Intent intent =
                new Intent(
                        this,
                        ExcursionAlertReceiver.class
                );

        intent.putExtra(
                "excursionTitle",
                excursionTitle
        );

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        this,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );

        if (alarmManager != null) {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        }
    }

    /**
     * Validates that the date follows MM/dd/yyyy
     * and represents a real calendar date.
     */
    private boolean isValidDate(String dateString) {

        dateFormat.setLenient(false);

        try {
            Date date =
                    dateFormat.parse(dateString);

            return date != null
                    && dateFormat.format(date)
                    .equals(dateString);

        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Converts an MM/dd/yyyy string into a Date.
     */
    private Date parseDate(String dateString) {

        dateFormat.setLenient(false);

        try {
            return dateFormat.parse(dateString);

        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Requests notification permission on Android 13
     * and newer devices.
     */
    private void requestNotificationPermission() {

        if (android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.POST_NOTIFICATIONS
                        },
                        101
                );
            }
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