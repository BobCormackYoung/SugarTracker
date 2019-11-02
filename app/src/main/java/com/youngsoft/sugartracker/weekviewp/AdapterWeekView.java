package com.youngsoft.sugartracker.weekviewp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.ViewModelMainActivity;
import com.youngsoft.sugartracker.data.DataRepository;

public class AdapterWeekView extends ListAdapter<WeekViewItem, AdapterWeekView.WeekViewHolder> {

    private final DataRepository dataRepository;
    private final FragmentWeekView fragmentWeekView;
    private final ViewModelMainActivity viewModelMainActivity;

    private static final DiffUtil.ItemCallback<WeekViewItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<WeekViewItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull WeekViewItem oldItem, @NonNull WeekViewItem newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull WeekViewItem oldItem, @NonNull WeekViewItem newItem) {
            return false;
        }
    };

    public AdapterWeekView(DataRepository dataRepository,
                           FragmentWeekView fragmentWeekView,
                           ViewModelMainActivity viewModelMainActivity) {
        super(DIFF_CALLBACK);
        this.dataRepository = dataRepository;
        this.fragmentWeekView = fragmentWeekView;
        this.viewModelMainActivity = viewModelMainActivity;
    }

    @NonNull
    @Override
    public AdapterWeekView.WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_data_weekview, parent, false);
        return new AdapterWeekView.WeekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterWeekView.WeekViewHolder holder, int position) {
        final WeekViewItem currentSugarMeasurement = getItem(position);

        if (position < 9) {
            holder.tvData.setText("" + currentSugarMeasurement.getComment());
            holder.tvData.setBackgroundResource(R.color.white);
        } else {
            switch (position) {
                case 9:
                case 9+5:
                case 9+5*2:
                case 9+5*3:
                case 9+5*4:
                case 9+5*5:
                case 9+5*6:
                    holder.tvData.setText("" + currentSugarMeasurement.getComment());
                    holder.tvData.setBackgroundResource(R.color.white);
                    break;
                default:
                    if (currentSugarMeasurement.getMeasurement() == -1) {
                        holder.tvData.setText("" + currentSugarMeasurement.getComment());
                    } else {
                        holder.tvData.setText("" + currentSugarMeasurement.getMeasurement());
                    }
                    break;
            }
        }
    }

    public class WeekViewHolder extends RecyclerView.ViewHolder {
        TextView tvData;

        public WeekViewHolder(View itemView) {
            super(itemView);
            tvData = itemView.findViewById(R.id.tv_view_data_weekview);
        }
    }
}
