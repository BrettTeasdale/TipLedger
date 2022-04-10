package com.bteasda.tipledger.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.R;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class JobSelectorAdapter extends RecyclerView.Adapter<JobSelectorAdapter.JobViewHolder> {

    class JobViewHolder extends RecyclerView.ViewHolder {
        private final TextView mNameTextView;

        private JobViewHolder(View itemView, Activity activity) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.nameTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final JobEntity current = mJobEntities.get(position);


                    Intent intent = new Intent();
                    intent.putExtra("exit", false);
                    intent.putExtra("jobId", current.getJobId());
                    activity.setResult(RESULT_OK, intent);
                    activity.finish();
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private final Context context;
    private List<JobEntity> mJobEntities; // Cached copy of words

    public JobSelectorAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.job_list_item, parent, false);
        return new JobViewHolder(itemView, (Activity) context);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        if (mJobEntities != null) {
            final JobEntity current = mJobEntities.get(position);

            holder.mNameTextView.setText(current.getName());
        }

    }

    public void setJobEntities(List<JobEntity> jobEntities) {
        mJobEntities = jobEntities;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mJobEntities != null)
            return mJobEntities.size();
        else return 0;
    }
}
