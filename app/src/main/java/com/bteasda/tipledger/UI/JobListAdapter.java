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
import com.bteasda.tipledger.Entities.LedgerEntryEntity;
import com.bteasda.tipledger.JobAddActivity;
import com.bteasda.tipledger.R;
import com.bteasda.tipledger.TipEditActivity;

import java.util.List;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobViewHolder> {
    private static final int NEW_JOB_ADD_ACTIVITY_REQUEST_CODE = 82;

    class JobViewHolder extends RecyclerView.ViewHolder {
        private final TextView mNameTextView;

        private JobViewHolder(View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.nameTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final JobEntity current = mJobEntities.get(position);

                    Intent intent = new Intent(context, JobAddActivity.class);
                    intent.putExtra("jobId", current.getJobId());
                    ((Activity) context).startActivityForResult(intent, NEW_JOB_ADD_ACTIVITY_REQUEST_CODE);
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private final Context context;
    private List<JobEntity> mJobEntities; // Cached copy of words

    public JobListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.job_list_item, parent, false);
        return new JobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        if (mJobEntities != null) {
            final JobEntity current = mJobEntities.get(position);

            if(current != null) {
                holder.mNameTextView.setText(current.getName());
            }
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
