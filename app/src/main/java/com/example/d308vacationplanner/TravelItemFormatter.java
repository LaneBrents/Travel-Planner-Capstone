package com.example.d308vacationplanner;

import com.example.d308vacationplanner.database.TravelItem;

public class TravelItemFormatter {

    public static String formatItem(TravelItem item) {
        return item.getItemType() + " | " + item.getDisplayDate();
    }
}
