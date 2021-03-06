package com.youngsoft.sugartracker.weekviewp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.preferencesp.SettingsActivity;

public class AdapterWeekViewItem extends ListAdapter<WeekViewItem, AdapterWeekViewItem.WeekViewHolder> {

    private OnItemClickListener onItemClickListener;
    private Context context;

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

    public AdapterWeekViewItem (Context context, OnItemClickListener onItemClickListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AdapterWeekViewItem.WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_data_weekview, parent, false);
        return new AdapterWeekViewItem.WeekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterWeekViewItem.WeekViewHolder holder, final int position) {
        final WeekViewItem currentSugarMeasurement = getItem(position);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        holder.tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 9+5:
                    case 9+5*2:
                    case 9+5*3:
                    case 9+5*4:
                    case 9+5*5:
                    case 9+5*6:
                        break;
                    default:
                        // Only return on click for actual measurement spaces
                        onItemClickListener.onItemClick(currentSugarMeasurement);
                        break;
                }
            }
        });

        if (position < 9) {
            holder.tvData.setText("" + currentSugarMeasurement.getComment());
            holder.tvData.setBackgroundColor(Color.argb(0,0,0,0));
            holder.tvData.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tvData.setTypeface(null, Typeface.BOLD);
        } else {
            switch (position) {
                case 9:
                case 9+5:
                case 9+5*2:
                case 9+5*3:
                case 9+5*4:
                case 9+5*5:
                case 9+5*6:
                    // Day titles
                    holder.tvData.setText("" + currentSugarMeasurement.getComment());
                    holder.tvData.setBackgroundColor(Color.argb(0,0,0,0));
                    holder.tvData.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.tvData.setTypeface(null, Typeface.BOLD);
                    break;
                case 10:
                case 10+5:
                case 10+5*2:
                case 10+5*3:
                case 10+5*4:
                case 10+5*5:
                case 10+5*6:
                    // First measurement of the day
                    if (currentSugarMeasurement.getMeasurement() == -1) {
                        holder.tvData.setText("" + currentSugarMeasurement.getComment());
                        holder.tvData.setBackgroundColor(Color.argb(255,191, 191, 191));
                    } else {
                        if (currentSugarMeasurement.getMeasurement() > Double.longBitsToDouble(preferences.getLong(SettingsActivity.KEY_PREF_LIMIT1,-1))) {
                            holder.tvData.setBackgroundColor(Color.argb(255,237, 140, 133));
                        } else {
                            holder.tvData.setBackgroundColor(Color.argb(255,137, 240, 164));
                        }
                        holder.tvData.setText("" + currentSugarMeasurement.getMeasurement());
                    }
                    break;
                default:
                    // All other measurements
                    if (currentSugarMeasurement.getMeasurement() == -1) {
                        holder.tvData.setText("" + currentSugarMeasurement.getComment());
                        holder.tvData.setBackgroundColor(Color.argb(255,191, 191, 191));
                    } else {
                        if (currentSugarMeasurement.getMeasurement() > Double.longBitsToDouble(preferences.getLong(SettingsActivity.KEY_PREF_LIMIT2,-1))) {
                            holder.tvData.setBackgroundColor(Color.argb(255,237, 140, 133));
                        } else {
                            holder.tvData.setBackgroundColor(Color.argb(255,137, 240, 164));
                        }
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

    public interface OnItemClickListener {
        void onItemClick(WeekViewItem weekViewItem);
    }
}
