package com.youngsoft.sugartracker;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.youngsoft.sugartracker.data.DataRepository;
import com.youngsoft.sugartracker.data.SugarMeasurement;

public class AdapterSugarList extends ListAdapter<SugarMeasurement, AdapterSugarList.SugarListHolder> {

    DataRepository dataRepository;
    FragmentSugar fragmentSugar;
    ViewModelMainActivity viewModelMainActivity;

    private static final DiffUtil.ItemCallback<SugarMeasurement> DIFF_CALLBACK = new DiffUtil.ItemCallback<SugarMeasurement>() {
        @Override
        public boolean areItemsTheSame(@NonNull SugarMeasurement oldItem, @NonNull SugarMeasurement newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull SugarMeasurement oldItem, @NonNull SugarMeasurement newItem) {
            return false;
        }
    };

    public AdapterSugarList(DataRepository dataRepository, FragmentSugar fragmentSugar, ViewModelMainActivity viewModelMainActivity) {
        super(DIFF_CALLBACK);
        this.dataRepository = dataRepository;
        this.fragmentSugar = fragmentSugar;
        this.viewModelMainActivity = viewModelMainActivity;
    }

    @NonNull
    @Override
    public AdapterSugarList.SugarListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_sugar, parent, false);
        return new AdapterSugarList.SugarListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SugarListHolder holder, int position) {
        SugarMeasurement currentSugarMeasurement = getItem(position);
        holder.tvSugarMeasurement.setText(Double.toString(currentSugarMeasurement.getMeasurement()));
        holder.tvDate.setText(DateFormat.format("yyyy-MM-dd",currentSugarMeasurement.getDate()).toString());
        holder.tvTime.setText(DateFormat.format("HH:mm",currentSugarMeasurement.getDate()).toString());
        if (currentSugarMeasurement.getIsFirstMeasurementOfDay()) {
            holder.tvFirstMeasurement.setText("Yes");
        } else {
            holder.tvFirstMeasurement.setText("No");
        }
        if (currentSugarMeasurement.getMealSequence() == 1) {
            holder.tvMealTiming.setText("Before");
        } else if (currentSugarMeasurement.getMealSequence() == 2 ) {
            holder.tvMealTiming.setText("After");
        } else {
            holder.tvMealTiming.setText("Unknown");
        }
        holder.tvAssociatedMeal.setText(Integer.toString(currentSugarMeasurement.getAssociatedMeal()));
    }


    public class SugarListHolder extends RecyclerView.ViewHolder {
        TextView tvSugarMeasurement;
        TextView tvDate;
        TextView tvTime;
        TextView tvFirstMeasurement;
        TextView tvMealTiming;
        TextView tvAssociatedMeal;

        public SugarListHolder(View itemView) {
            super(itemView);
            tvSugarMeasurement = itemView.findViewById(R.id.tv_sugar_value);
            tvDate = itemView.findViewById(R.id.tv_sugar_date);
            tvTime = itemView.findViewById(R.id.tv_sugar_time);
            tvFirstMeasurement = itemView.findViewById(R.id.tv_sugar_first_measurement);
            tvMealTiming = itemView.findViewById(R.id.tv_sugar_meal_timing);
            tvAssociatedMeal = itemView.findViewById(R.id.tv_sugar_associated_meal);
        }



    }
}
