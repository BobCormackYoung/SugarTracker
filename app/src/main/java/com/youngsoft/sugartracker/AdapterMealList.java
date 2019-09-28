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
import com.youngsoft.sugartracker.data.MealRecord;

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
    }

    public class MealListHolder extends RecyclerView.ViewHolder{
        TextView tvMealType;
        TextView tvDate;
        TextView tvTime;
        TextView tvMealDetails;

        public MealListHolder(View itemView) {
            super(itemView);
            tvMealType = itemView.findViewById(R.id.tv_meal_title);
            tvDate = itemView.findViewById(R.id.tv_meal_date);
            tvTime = itemView.findViewById(R.id.tv_meal_time);
            tvMealDetails = itemView.findViewById(R.id.tv_meal_details);
        }
    }
}
