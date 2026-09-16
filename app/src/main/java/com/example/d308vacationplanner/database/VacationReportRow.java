package com.example.d308vacationplanner.database;

public class VacationReportRow {

    private String title;
    private String hotel;
    private String startDate;
    private String endDate;
    private int excursionCount;

    public VacationReportRow(
            String title,
            String hotel,
            String startDate,
            String endDate,
            int excursionCount) {

        this.title = title;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.excursionCount = excursionCount;
    }

    public String getTitle() {
        return title;
    }

    public String getHotel() {
        return hotel;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getExcursionCount() {
        return excursionCount;
    }
}
