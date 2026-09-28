package com.example.d308vacationplanner;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308vacationplanner.database.AppDatabase;
import com.example.d308vacationplanner.database.VacationReportRow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportActivity extends AppCompatActivity {

    private LinearLayout reportContainer;

    private AppDatabase database;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportContainer = findViewById(R.id.reportContainer);

        Button closeReportButton = findViewById(R.id.buttonCloseReport);

        database = AppDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();

        closeReportButton.setOnClickListener(view -> finish());

        displayReportTimestamp();
        loadReport();
    }

    private void displayReportTimestamp() {

        TextView timestampTextView = findViewById(R.id.textViewReportTimestamp);

        String timestamp = new SimpleDateFormat(
                "MM/dd/yyyy HH:mm",
                Locale.US
        ).format(new Date());

        timestampTextView.setText("Generated: " + timestamp);
    }

    private void loadReport() {

        executorService.execute(() -> {

            List<VacationReportRow> reportRows =
                    database.vacationDao().getVacationReport();

            runOnUiThread(() -> {

                reportContainer.removeAllViews();

                addHeaderRow();

                for (VacationReportRow row : reportRows) {
                    addReportRow(row);
                }

                if (reportRows.isEmpty()) {
                    addNoDataMessage();
                }
            });
        });
    }

    private void addHeaderRow() {

        LinearLayout row = createRow();

        row.addView(createCell("Vacation", 180));
        row.addView(createCell("Hotel", 180));
        row.addView(createCell("Start Date", 120));
        row.addView(createCell("End Date", 120));
        row.addView(createCell("Excursions", 100));

        reportContainer.addView(row);
    }

    private void addReportRow(VacationReportRow reportRow) {

        LinearLayout row = createRow();

        row.addView(createCell(reportRow.getTitle(), 180));
        row.addView(createCell(reportRow.getHotel(), 180));
        row.addView(createCell(reportRow.getStartDate(), 120));
        row.addView(createCell(reportRow.getEndDate(), 120));
        row.addView(createCell(
                String.valueOf(reportRow.getExcursionCount()),
                100
        ));

        reportContainer.addView(row);
    }

    private LinearLayout createRow() {

        LinearLayout row = new LinearLayout(this);

        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);

        return row;
    }

    private TextView createCell(String text, int width) {

        TextView cell = new TextView(this);

        cell.setText(text);
        cell.setTextSize(14);
        cell.setTextColor(getColor(android.R.color.black));
        cell.setPadding(8, 12, 8, 12);

        cell.setWidth(
                (int) (width * getResources().getDisplayMetrics().density)
        );

        return cell;
    }

    private void addNoDataMessage() {

        TextView noDataTextView = new TextView(this);

        noDataTextView.setText("No vacations are currently available.");
        noDataTextView.setTextSize(16);
        noDataTextView.setGravity(Gravity.CENTER);
        noDataTextView.setPadding(8, 24, 8, 24);

        reportContainer.addView(noDataTextView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
