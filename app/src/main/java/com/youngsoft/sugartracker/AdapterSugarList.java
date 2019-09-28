package com.youngsoft.sugartracker;

import android.icu.text.DecimalFormat;
import android.os.AsyncTask;
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
import com.youngsoft.sugartracker.data.MealRecord;
import com.youngsoft.sugartracker.data.SugarMeasurement;

public class AdapterSugarList extends ListAdapter<SugarMeasurement, AdapterSugarList.SugarListHolder> {

    //TODO: add delete & edit item functionality

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

        // Convert the sugar measurement to 1dp when displaying
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        holder.tvSugarMeasurement.setText(decimalFormat.format(currentSugarMeasurement.getMeasurement()));
        holder.tvDate.setText(DateFormat.format("yyyy-MM-dd",currentSugarMeasurement.getDate()).toString());
        holder.tvTime.setText(DateFormat.format("HH:mm",currentSugarMeasurement.getDate()).toString());
        if (currentSugarMeasurement.getIsFirstMeasurementOfDay()) {
            holder.tvFirstMeasurement.setText("Yes");
        } else {
            holder.tvFirstMeasurement.setText("No");
        }
        if (currentSugarMeasurement.getMealSequence() == 1) {
            holder.tvMealTiming.setText("Before meal");
        } else if (currentSugarMeasurement.getMealSequence() == 2 ) {
            holder.tvMealTiming.setText("After meal");
        } else {
            holder.tvMealTiming.setText("Unknown");
        }

        if (currentSugarMeasurement.getAssociatedMeal() == -1) {
            holder.tvAssociatedMeal.setText("None");
        } else {
            ParamsGetMealData paramsGetMealData = new ParamsGetMealData(holder, currentSugarMeasurement.getAssociatedMeal());
            GetMealDataAsync getMealDataAsync = new GetMealDataAsync();
            getMealDataAsync.execute(paramsGetMealData);
        }
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

    private class GetMealDataAsync extends AsyncTask<ParamsGetMealData, Void, Void> {

        int index;
        String outputDate;
        String outputMealType;
        MealRecord outputMealRecord;
        ParamsGetMealData inputParams;

        @Override
        protected Void doInBackground(ParamsGetMealData... paramsGetMealData) {

            inputParams = paramsGetMealData[0];
            index = inputParams.getIndex();
            outputMealRecord = dataRepository.getMealRecordById(index);
            outputDate = DateFormat.format("yyyy-MM-dd HH:mm", outputMealRecord.getDate()).toString();
            outputMealType = UtilMethods.getMealType(outputMealRecord.getType());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            inputParams.getHolder().tvAssociatedMeal.setText("" + outputMealType + " | " + outputDate);
        }
    }

    private class ParamsGetMealData {

        SugarListHolder holder;
        int index;

        ParamsGetMealData(SugarListHolder holder, int index) {
            this.holder = holder;
            this.index = index;
        }

        public SugarListHolder getHolder() {
            return holder;
        }

        public int getIndex() {
            return index;
        }
    }
}
