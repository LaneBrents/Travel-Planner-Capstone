package com.example.d308vacationplanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.database.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private final List<Vacation> vacations;
    private final OnVacationClickListener listener;

    public interface OnVacationClickListener {
        void onVacationClick(Vacation vacation);
    }

    public VacationAdapter(List<Vacation> vacations, OnVacationClickListener listener) {
        this.vacations = vacations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vacation, parent, false);

        return new VacationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        Vacation vacation = vacations.get(position);

        holder.titleTextView.setText(vacation.getTitle());
        holder.hotelTextView.setText("Hotel: " + vacation.getHotel());
        holder.datesTextView.setText(
                "Dates: " + vacation.getStartDate() + " - " + vacation.getEndDate()
        );

        holder.itemView.setOnClickListener(view -> listener.onVacationClick(vacation));
    }

    @Override
    public int getItemCount() {
        return vacations.size();
    }

    public static class VacationViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView hotelTextView;
        TextView datesTextView;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.textViewVacationTitle);
            hotelTextView = itemView.findViewById(R.id.textViewVacationHotel);
            datesTextView = itemView.findViewById(R.id.textViewVacationDates);
        }
    }
}
