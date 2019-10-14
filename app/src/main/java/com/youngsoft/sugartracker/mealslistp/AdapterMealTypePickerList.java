package com.youngsoft.sugartracker.mealslistp;

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

public class AdapterMealTypePickerList extends ListAdapter<Integer, AdapterMealTypePickerList.ViewHolder> {


    private OnMealTypeClickListener onMealTypeClickListener;

    private static final DiffUtil.ItemCallback<Integer> DIFF_CALLBACK = new DiffUtil.ItemCallback<Integer>() {
        @Override
        public boolean areItemsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Integer oldItem, @NonNull Integer newItem) {
            return false;
        }
    };


    public AdapterMealTypePickerList(OnMealTypeClickListener onMealTypeClickListener) {
        super(DIFF_CALLBACK);
        this.onMealTypeClickListener = onMealTypeClickListener;
    }

    @NonNull
    @Override
    public AdapterMealTypePickerList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_meal_picker, parent, false);
        return new AdapterMealTypePickerList.ViewHolder(view, onMealTypeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMealTypePickerList.ViewHolder holder, int position) {
        Integer index = getItem(position);

        holder.textViewType.setText(UtilMethods.getMealType(index));
        holder.textViewDate.setVisibility(View.GONE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewType;
        TextView textViewDate;
        OnMealTypeClickListener onMealTypeClickListener;

        public ViewHolder(@NonNull View itemView, OnMealTypeClickListener onMealTypeClickListener) {
            super(itemView);
            textViewType = itemView.findViewById(R.id.tv_meal_picker_type);
            textViewDate = itemView.findViewById(R.id.tv_meal_picker_date);
            this.onMealTypeClickListener = onMealTypeClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMealTypeClickListener.onMealTypeClick(getItem(getAdapterPosition()));
        }
    }

    public interface OnMealTypeClickListener {
        void onMealTypeClick(int position);
    }

}
