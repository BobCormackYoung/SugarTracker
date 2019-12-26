package com.youngsoft.sugartracker.weekviewp;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.youngsoft.sugartracker.R;
import com.youngsoft.sugartracker.data.DataRepository;
import com.youngsoft.sugartracker.data.SugarMeasurement;

import java.util.ArrayList;
import java.util.List;

public class AdapterWeekView extends ListAdapter<WeekDatesItem, AdapterWeekView.WeekViewHolder> {

    private FragmentWeekView parentFragment;
    private DataRepository dataRepository;
    private AdapterWeekViewItem.OnItemClickListener onItemClickListener;
    private Context context;

    private static final DiffUtil.ItemCallback<WeekDatesItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<WeekDatesItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull WeekDatesItem oldItem, @NonNull WeekDatesItem newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull WeekDatesItem oldItem, @NonNull WeekDatesItem newItem) {
            return false;
        }
    };

    public AdapterWeekView(Context context,
                           FragmentWeekView parentFragment,
                           DataRepository dataRepository,
                           AdapterWeekViewItem.OnItemClickListener onItemClickListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.parentFragment = parentFragment;
        this.dataRepository = dataRepository;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AdapterWeekView.WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_week, parent, false);
        return new AdapterWeekView.WeekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterWeekView.WeekViewHolder holder, int position) {
        final WeekDatesItem currentDateSet = getItem(position);

        holder.textView.setText(DateFormat.format("yyyy-MM-dd",currentDateSet.getStartDate()).toString()
        + " to " + DateFormat.format("yyyy-MM-dd",currentDateSet.getEndDate()).toString());

        ArrayList<WeekViewItem> weekViewItemArrayList = new ArrayList<>();
        holder.recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(parentFragment.getActivity(), 5);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int size;
                if (position == 1) {
                    size = 2;
                } else {
                    size = 1;
                }
                return size;
            }
        });

        holder.recyclerView.setLayoutManager(manager);

        AdapterWeekViewItem adapterWeekViewItem = new AdapterWeekViewItem(context, onItemClickListener);
        holder.recyclerView.setAdapter(adapterWeekViewItem);

        weekViewItemArrayList.clear();
        for (int item = 0; item < 44; item++) {
            weekViewItemArrayList.add(new WeekViewItem(" "));
        }

        adapterWeekViewItem.submitList(weekViewItemArrayList);

        ParamsGetSugarWeekData paramsGetSugarWeekData = new ParamsGetSugarWeekData(currentDateSet.getStartDate(),
                currentDateSet.getEndDate(), holder, dataRepository, adapterWeekViewItem,
                weekViewItemArrayList);

        GetSugarWeekDataAsync getSugarWeekDataAsync = new GetSugarWeekDataAsync();
        getSugarWeekDataAsync.execute(paramsGetSugarWeekData);

    }

    public class WeekViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        RecyclerView recyclerView;

        public WeekViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_dummy_weekitem);
            recyclerView = itemView.findViewById(R.id.rv_week_view_item);
        }
    }

    private class GetSugarWeekDataAsync extends AsyncTask<ParamsGetSugarWeekData, Void, Void> {
        //TODO: move to viewModel with a listener
        long startDate;
        long endDate;
        DataRepository dataRepository;
        AdapterWeekViewItem adapterWeekViewItem;
        WeekViewHolder weekViewHolder;
        List<SugarMeasurement> outputSugarMeasurements;
        ArrayList<WeekViewItem> weekViewItemArrayList;

        ParamsGetSugarWeekData inputParams;

        @Override
        protected Void doInBackground(ParamsGetSugarWeekData... paramsGetSugarWeekData) {

            inputParams = paramsGetSugarWeekData[0];
            startDate = inputParams.getStartDate();
            endDate = inputParams.getEndDate();
            dataRepository = inputParams.getDataRepository();
            adapterWeekViewItem = inputParams.getAdapter();
            weekViewHolder = inputParams.getHolder();
            weekViewItemArrayList = inputParams.getArrayList();

            outputSugarMeasurements = dataRepository.getSugarMeasurementsBetweenDatesNonLive(startDate, endDate);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            weekViewItemArrayList.clear();
            weekViewItemArrayList.add(new WeekViewItem(" "));
            weekViewItemArrayList.add(new WeekViewItem("Breakfast"));
            weekViewItemArrayList.add(new WeekViewItem("Dinner"));
            weekViewItemArrayList.add(new WeekViewItem("Supper"));
            weekViewItemArrayList.add(new WeekViewItem(" "));
            weekViewItemArrayList.add(new WeekViewItem("B"));
            weekViewItemArrayList.add(new WeekViewItem("A"));
            weekViewItemArrayList.add(new WeekViewItem("A"));
            weekViewItemArrayList.add(new WeekViewItem("A"));

            //Cycle through all days and fill the data for each item
            for (int item = 0; item < 35; item++) {
                //Define the start and end of the day
                long dayStart = startDate+(item/5)*(1000*60*60*24);
                long dayEnd = dayStart+(1000*60*60*24);
                //Input the first column with day data
                switch (item) {
                    case 0:
                        weekViewItemArrayList.add(new WeekViewItem("Mon"));
                        break;
                    case 5:
                        weekViewItemArrayList.add(new WeekViewItem("Tue"));
                        break;
                    case 10:
                        weekViewItemArrayList.add(new WeekViewItem("Wed"));
                        break;
                    case 15:
                        weekViewItemArrayList.add(new WeekViewItem("Thu"));
                        break;
                    case 20:
                        weekViewItemArrayList.add(new WeekViewItem("Fri"));
                        break;
                    case 25:
                        weekViewItemArrayList.add(new WeekViewItem("Sat"));
                        break;
                    case 30:
                        weekViewItemArrayList.add(new WeekViewItem("Sun"));
                        break;
                    case 1:
                    case 6:
                    case 11:
                    case 16:
                    case 21:
                    case 26:
                    case 31:
                        weekViewItemArrayList.add(getWeekViewItem(outputSugarMeasurements, dayStart, dayEnd, 1));
                        break;
                    case 2:
                    case 7:
                    case 12:
                    case 17:
                    case 22:
                    case 27:
                    case 32:
                        weekViewItemArrayList.add(getWeekViewItem(outputSugarMeasurements, dayStart, dayEnd, 2));
                        break;
                    case 3:
                    case 8:
                    case 13:
                    case 18:
                    case 23:
                    case 28:
                    case 33:
                        weekViewItemArrayList.add(getWeekViewItem(outputSugarMeasurements, dayStart, dayEnd, 3));
                        break;
                    case 4:
                    case 9:
                    case 14:
                    case 19:
                    case 24:
                    case 29:
                    case 34:
                        weekViewItemArrayList.add(getWeekViewItem(outputSugarMeasurements, dayStart, dayEnd, 4));
                        break;
                    default:
                        weekViewItemArrayList.add(new WeekViewItem("err"));//not one of the header or day cells, find relevant data for the specific cell
                        break;
                }
            }

            adapterWeekViewItem.submitList(weekViewItemArrayList);
            adapterWeekViewItem.notifyDataSetChanged();
        }
    }

    private class ParamsGetSugarWeekData {

        long startDate;
        long endDate;
        WeekViewHolder holder;
        DataRepository dataRepository;
        AdapterWeekViewItem adapter;
        ArrayList<WeekViewItem> arrayList;

        ParamsGetSugarWeekData(long startDate, long endDate, WeekViewHolder holder,
                               DataRepository dataRepository, AdapterWeekViewItem adapter,
                               ArrayList<WeekViewItem> arrayList) {
            this.adapter=adapter;
            this.dataRepository=dataRepository;
            this.endDate=endDate;
            this.startDate=startDate;
            this.adapter=adapter;
            this.arrayList=arrayList;
        }

        public ArrayList<WeekViewItem> getArrayList() {
            return arrayList;
        }

        public void setArrayList(ArrayList<WeekViewItem> arrayList) {
            this.arrayList = arrayList;
        }

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(long endDate) {
            this.endDate = endDate;
        }

        public WeekViewHolder getHolder() {
            return holder;
        }

        public void setHolder(WeekViewHolder holder) {
            this.holder = holder;
        }

        public DataRepository getDataRepository() {
            return dataRepository;
        }

        public void setDataRepository(DataRepository dataRepository) {
            this.dataRepository = dataRepository;
        }

        public AdapterWeekViewItem getAdapter() {
            return adapter;
        }

        public void setAdapter(AdapterWeekViewItem adapter) {
            this.adapter = adapter;
        }
    }

    private WeekViewItem getWeekViewItem (List<SugarMeasurement> inputArray, long dayStart, long dayEnd, int criteria) {
        for (int i = 0; i < inputArray.size(); i++) {
            SugarMeasurement currentItem = inputArray.get(i);
            //Check if the items is for the current day
            if (currentItem.getDate() < dayEnd && currentItem.getDate() > dayStart)
            {
                //Check whether meets data criteria
                // 1 = first meal of the day
                // 2 = after breakfast
                // 3 = after dinner
                // 4 = after supper
                if (criteria == 1 && currentItem.getIsFirstMeasurementOfDay()) {
                    return new WeekViewItem(currentItem.getId(),
                            currentItem.getDate(),
                            currentItem.getMeasurement(),
                            currentItem.getMealSequence(),
                            currentItem.getAssociatedMealType(),
                            currentItem.getAssociatedMeal(),
                            currentItem.getIsFirstMeasurementOfDay());
                } else if ((criteria == 2 && currentItem.getMealSequence() == 2 && currentItem.getAssociatedMealType() == 1)) {
                    return new WeekViewItem(currentItem.getId(),
                            currentItem.getDate(),
                            currentItem.getMeasurement(),
                            currentItem.getMealSequence(),
                            currentItem.getAssociatedMealType(),
                            currentItem.getAssociatedMeal(),
                            currentItem.getIsFirstMeasurementOfDay());
                } else if ((criteria == 3 && currentItem.getMealSequence() == 2 && currentItem.getAssociatedMealType() == 4)) {
                    return new WeekViewItem(currentItem.getId(),
                            currentItem.getDate(),
                            currentItem.getMeasurement(),
                            currentItem.getMealSequence(),
                            currentItem.getAssociatedMealType(),
                            currentItem.getAssociatedMeal(),
                            currentItem.getIsFirstMeasurementOfDay());
                } else if ((criteria == 4 && currentItem.getMealSequence() == 2 && currentItem.getAssociatedMealType() == 5)) {
                    return new WeekViewItem(currentItem.getId(),
                            currentItem.getDate(),
                            currentItem.getMeasurement(),
                            currentItem.getMealSequence(),
                            currentItem.getAssociatedMealType(),
                            currentItem.getAssociatedMeal(),
                            currentItem.getIsFirstMeasurementOfDay());
                }
            } else {
                if (i == inputArray.size()-1) {
                    //End of the loop, nothing found, insert empty data and break loop
                    switch (criteria) {
                        case 1:
                            return new WeekViewItem(" - ", true, 1, 1, dayStart+1000*60*60*6);
                        case 2:
                            return new WeekViewItem(" - ", false, 1, 2, dayStart+1000*60*60*7);
                        case 3:
                            return new WeekViewItem(" - ", false, 4, 2, dayStart+1000*60*60*14);
                        case 4:
                            return new WeekViewItem(" - ", false, 5, 2, dayStart+1000*60*60*18);
                    }
                }
            }
        }
        switch (criteria) {
            case 1:
                return new WeekViewItem(" - ", true, 1, 1, dayStart+1000*60*60*6);
            case 2:
                return new WeekViewItem(" - ", false, 1, 2, dayStart+1000*60*60*7);
            case 3:
                return new WeekViewItem(" - ", false, 4, 2, dayStart+1000*60*60*14);
            case 4:
                return new WeekViewItem(" - ", false, 5, 2, dayStart+1000*60*60*18);
            default:
                return new WeekViewItem(" - ");
        }
        //return new WeekViewItem(" - ");
    }
}
