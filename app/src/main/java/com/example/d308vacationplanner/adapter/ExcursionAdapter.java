package com.example.d308vacationplanner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner.R;
import com.example.d308vacationplanner.database.Excursion;

import java.util.List;

public class ExcursionAdapter
        extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private final List<Excursion> excursions;
    private final OnExcursionClickListener listener;

    public interface OnExcursionClickListener {
        void onExcursionClick(Excursion excursion);
    }

    public ExcursionAdapter(
            List<Excursion> excursions,
            OnExcursionClickListener listener) {

        this.excursions = excursions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_excursion, parent, false);

        return new ExcursionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ExcursionViewHolder holder,
            int position) {

        Excursion excursion = excursions.get(position);

        holder.titleTextView.setText(excursion.getTitle());
        holder.dateTextView.setText(
                "Date: " + excursion.getDate()
        );

        holder.itemView.setOnClickListener(
                view -> listener.onExcursionClick(excursion)
        );
    }

    @Override
    public int getItemCount() {
        return excursions.size();
    }

    public static class ExcursionViewHolder
            extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView dateTextView;

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView =
                    itemView.findViewById(R.id.textViewExcursionTitle);

            dateTextView =
                    itemView.findViewById(R.id.textViewExcursionDate);
        }
    }
}
