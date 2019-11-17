package com.youngsoft.sugartracker.weekviewp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.youngsoft.sugartracker.R;

public class AdapterWeekViewItem extends ListAdapter<WeekViewItem, AdapterWeekViewItem.WeekViewHolder> {

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

    public AdapterWeekViewItem() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public AdapterWeekViewItem.WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_data_weekview, parent, false);
        return new AdapterWeekViewItem.WeekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterWeekViewItem.WeekViewHolder holder, int position) {
        final WeekViewItem currentSugarMeasurement = getItem(position);

        if (position < 9) {
            holder.tvData.setText("" + currentSugarMeasurement.getComment());
            holder.tvData.setBackgroundColor(Color.argb(0,0,0,0));
            holder.tvData.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tvData.setTypeface(null, Typeface.BOLD);
            Log.i("AWVI", "Position: " + position + " Comment: " + currentSugarMeasurement.getComment());
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
                    holder.tvData.setBackgroundColor(Color.argb(0,0,0,0));
                    holder.tvData.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.tvData.setTypeface(null, Typeface.BOLD);
                    Log.i("AWVI", "Position: " + position + " Comment: " + currentSugarMeasurement.getComment());
                    break;
                default:
                    if (currentSugarMeasurement.getMeasurement() == -1) {
                        holder.tvData.setText("" + currentSugarMeasurement.getComment());
                        holder.tvData.setBackgroundColor(Color.argb(255,191, 191, 191));
                        Log.i("AWVI", "Position: " + position + " Comment: " + currentSugarMeasurement.getComment());
                    } else {
                        if (currentSugarMeasurement.getMeasurement() > 100) {
                            holder.tvData.setBackgroundColor(Color.argb(255,237, 140, 133));
                        } else {
                            holder.tvData.setBackgroundColor(Color.argb(255,137, 240, 164));
                        }
                        holder.tvData.setText("" + currentSugarMeasurement.getMeasurement());
                        Log.i("AWVI", "Position: " + position + " Measurement: " + currentSugarMeasurement.getMeasurement());
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
