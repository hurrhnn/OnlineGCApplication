package app.mobilecontests.onlinegcapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

    private ArrayList<DashboardProperty> mList;

    public class DashboardViewHolder extends RecyclerView.ViewHolder {
        protected TextView number;
        protected TextView startDate;
        protected TextView endDate;
        protected TextView title;
        protected TextView subject;
        protected ProgressBar progressBar;
        protected TextView progressPercent;

        public DashboardViewHolder(View view) {
            super(view);
            this.number = (TextView) view.findViewById(R.id.class_number);
            this.startDate = (TextView) view.findViewById(R.id.class_startdate);
            this.endDate = (TextView) view.findViewById(R.id.class_enddate);
            this.title = (TextView) view.findViewById(R.id.class_title);
            this.subject = (TextView) view.findViewById(R.id.class_subject);
            this.progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            this.progressPercent = (TextView) view.findViewById(R.id.progress_bar_percent);
        }

    }
    public DashboardAdapter(ArrayList<DashboardProperty> list) {
        this.mList = list;
    }

    @Override
    public DashboardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dashboard_item_container, viewGroup, false);

        DashboardViewHolder viewHolder = new DashboardViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder viewholder, int position) {
        viewholder.number.setText(mList.get(position).getNumber());
        viewholder.startDate.setText(mList.get(position).getStartDate());
        viewholder.endDate.setText(mList.get(position).getEndDate());
        viewholder.title.setText(mList.get(position).getTitle());
        viewholder.subject.setText(mList.get(position).getSubject());
        viewholder.progressBar.setProgress(mList.get(position).getProgressPercent());
        viewholder.progressPercent.setText(mList.get(position).getProgressPercentString());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
