package com.youngsoft.sugartracker;

import android.icu.text.DecimalFormat;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;
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

import java.util.List;

public class AdapterMealList extends ListAdapter<MealRecord, AdapterMealList.MealListHolder> {

    DataRepository dataRepository;
    FragmentMeals fragmentMeals;
    ViewModelMainActivity viewModelMainActivity;

    private static final DiffUtil.ItemCallback<MealRecord> DIFF_CALLBACK = new DiffUtil.ItemCallback<MealRecord>() {
        @Override
        public boolean areItemsTheSame(@NonNull MealRecord oldItem, @NonNull MealRecord newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MealRecord oldItem, @NonNull MealRecord newItem) {
            return false;
        }
    };

    public AdapterMealList(DataRepository dataRepository, FragmentMeals fragmentMeals, ViewModelMainActivity viewModelMainActivity) {
        super(DIFF_CALLBACK);
        this.dataRepository = dataRepository;
        this.fragmentMeals = fragmentMeals;
        this.viewModelMainActivity = viewModelMainActivity;
    }

    @NonNull
    @Override
    public AdapterMealList.MealListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_meal, parent, false);
        return new AdapterMealList.MealListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMealList.MealListHolder holder, int position) {
        MealRecord currentMealRecord = getItem(position);

        holder.tvMealType.setText(UtilMethods.getMealType(currentMealRecord.getType()));

        holder.tvDate.setText(DateFormat.format("yyyy-MM-dd",currentMealRecord.getDate()).toString());
        holder.tvTime.setText(DateFormat.format("HH:mm",currentMealRecord.getDate()).toString());
        holder.tvMealDetails.setText(currentMealRecord.getDescription());

        ParamsSugarMeasurement paramsSugarMeasurement = new ParamsSugarMeasurement(holder, currentMealRecord.getId());
        GetValueBeforeMeal getValueBeforeMeal = new GetValueBeforeMeal();
        getValueBeforeMeal.execute(paramsSugarMeasurement);

        GetValueAfterMeal getValueAfterMeal = new GetValueAfterMeal();
        getValueAfterMeal.execute(paramsSugarMeasurement);


    }

    public class MealListHolder extends RecyclerView.ViewHolder{
        TextView tvMealType;
        TextView tvDate;
        TextView tvTime;
        TextView tvMealDetails;
        TextView tvBeforeTitle;
        TextView tvBeforeValue;
        TextView tvAfterTitle;
        TextView tvAfterValue;

        public MealListHolder(View itemView) {
            super(itemView);
            tvMealType = itemView.findViewById(R.id.tv_meal_title);
            tvDate = itemView.findViewById(R.id.tv_meal_date);
            tvTime = itemView.findViewById(R.id.tv_meal_time);
            tvMealDetails = itemView.findViewById(R.id.tv_meal_details);
            tvBeforeTitle = itemView.findViewById(R.id.tv_before_title);
            tvBeforeValue = itemView.findViewById(R.id.tv_before_value);
            tvAfterTitle = itemView.findViewById(R.id.tv_after_title);
            tvAfterValue = itemView.findViewById(R.id.tv_after_value);
        }
    }

    private class GetValueBeforeMeal extends AsyncTask<ParamsSugarMeasurement, Void, Void> {

        ParamsSugarMeasurement inputParams;
        int index;
        double outputMeasurement;
        List<SugarMeasurement> outputList;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DecimalFormat decimalFormat = new DecimalFormat("#");
            if (outputList == null) {
                Log.i("AdapterMeal","Meal " + index + ", List size null");
                inputParams.holder.tvBeforeValue.setText("");
                inputParams.holder.tvBeforeTitle.setVisibility(View.INVISIBLE);
                inputParams.holder.tvBeforeValue.setVisibility(View.INVISIBLE);
            } else if (outputList.isEmpty()) {
                Log.i("AdapterMeal","Meal " + index + ", List is empty");
                inputParams.holder.tvBeforeValue.setText("");
                inputParams.holder.tvBeforeTitle.setVisibility(View.INVISIBLE);
                inputParams.holder.tvBeforeValue.setVisibility(View.INVISIBLE);
            } else if (outputList.size() == 1) {
                Log.i("AdapterMeal","Meal " + index + ", List size is 1");
                inputParams.holder.tvBeforeValue.setText(decimalFormat.format(outputList.get(0).getMeasurement()));
                inputParams.holder.tvBeforeTitle.setVisibility(View.VISIBLE);
                inputParams.holder.tvBeforeValue.setVisibility(View.VISIBLE);
            } else {
                Log.i("AdapterMeal","Meal " + index + ", List size neither null, 1 nor empty");
            }
        }

        @Override
        protected Void doInBackground(ParamsSugarMeasurement... paramsSugarMeasurements) {
            inputParams = paramsSugarMeasurements[0];
            index = inputParams.getIndex();
            outputList = dataRepository.getBeforeMealSugarMeasurement(index);
            return null;
        }
    }

    private class GetValueAfterMeal extends AsyncTask<ParamsSugarMeasurement, Void, Void> {

        ParamsSugarMeasurement inputParams;
        int index;
        double outputMeasurement;
        List<SugarMeasurement> outputList;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DecimalFormat decimalFormat = new DecimalFormat("#");
            if (outputList == null) {
                Log.i("AdapterMeal","Meal " + index + ", List size null");
                inputParams.holder.tvAfterValue.setText("");
                inputParams.holder.tvAfterTitle.setVisibility(View.INVISIBLE);
                inputParams.holder.tvAfterValue.setVisibility(View.INVISIBLE);
            } else if (outputList.isEmpty()) {
                Log.i("AdapterMeal","Meal " + index + ", List is empty");
                inputParams.holder.tvAfterValue.setText("");
                inputParams.holder.tvAfterTitle.setVisibility(View.INVISIBLE);
                inputParams.holder.tvAfterValue.setVisibility(View.INVISIBLE);
            } else if (outputList.size() == 1) {
                Log.i("AdapterMeal","Meal " + index + ", List size is 1");
                inputParams.holder.tvAfterValue.setText(decimalFormat.format(outputList.get(0).getMeasurement()));
                inputParams.holder.tvAfterTitle.setVisibility(View.VISIBLE);
                inputParams.holder.tvAfterValue.setVisibility(View.VISIBLE);
            } else {
                Log.i("AdapterMeal","Meal " + index + ", List size neither null, 1 nor empty");
            }
        }

        @Override
        protected Void doInBackground(ParamsSugarMeasurement... paramsSugarMeasurements) {
            inputParams = paramsSugarMeasurements[0];
            index = inputParams.getIndex();
            outputList = dataRepository.getAfterMealSugarMeasurement(index);
            return null;
        }
    }

    private class ParamsSugarMeasurement {

        MealListHolder holder;
        int index;

        ParamsSugarMeasurement (MealListHolder holder, int index) {
            this.holder = holder;
            this.index = index;
        }

        public MealListHolder getHolder() {
            return holder;
        }

        public int getIndex() {
            return index;
        }
    }
}
