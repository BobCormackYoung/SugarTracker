package com.youngsoft.sugartracker.sugarlistp;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.UtilMethods;
import com.youngsoft.sugartracker.data.DataRepository;
import com.youngsoft.sugartracker.data.MealRecord;

public class AdapterMealPickerList extends ListAdapter<MealRecord, AdapterMealPickerList.ViewHolder> {

    DataRepository dataRepository;
    ViewModelAddSugarMeasurement viewModelAddSugarMeasurement;
    FragmentMealPicker fragmentSugar;
    private OnMealClickListener onMealClickListener;

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

    //constructor
    public AdapterMealPickerList(DataRepository dataRepository,
                                 FragmentMealPicker fragmentSugar,
                                 ViewModelAddSugarMeasurement viewModelAddSugarMeasurement,
                                 OnMealClickListener onMealClickListener) {
        super(DIFF_CALLBACK);
        this.dataRepository = dataRepository;
        this.fragmentSugar = fragmentSugar;
        this.viewModelAddSugarMeasurement = viewModelAddSugarMeasurement;
        this.onMealClickListener = onMealClickListener;
    }

    @NonNull
    @Override
    public AdapterMealPickerList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_meal_picker, parent, false);
        return new AdapterMealPickerList.ViewHolder(view, onMealClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealRecord currentMealRecord = getItem(position);

        if (currentMealRecord.getId() == -1) {
            holder.textViewType.setText("" + currentMealRecord.getDescription());
            holder.textViewDate.setText("");
        } else {
            holder.textViewType.setText("" + UtilMethods.getMealType(currentMealRecord.getType()));
            holder.textViewDate.setText("" + DateFormat.format("yyyy-MM-dd HH:mm", currentMealRecord.getDate()));
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewType;
        TextView textViewDate;
        OnMealClickListener onMealClickListener;

        public ViewHolder(@NonNull View itemView, OnMealClickListener onMealClickListener) {
            super(itemView);
            textViewType = itemView.findViewById(R.id.tv_meal_picker_type);
            textViewDate = itemView.findViewById(R.id.tv_meal_picker_date);
            this.onMealClickListener = onMealClickListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //onMealClickListener.onMealClick(getAdapterPosition());
            onMealClickListener.onMealClick(getItem(getAdapterPosition()).getId());
        }
    }

    public interface OnMealClickListener {
        void onMealClick(int position);
    }
}
