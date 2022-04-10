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
import com.bteasda.tipledger.R;
import com.bteasda.tipledger.TipEditActivity;

import java.util.List;

public class LedgerAdapter extends RecyclerView.Adapter<LedgerAdapter.LedgerViewHolder> {

    class LedgerViewHolder extends RecyclerView.ViewHolder {
        private final TextView amountTextView;
        private final TextView headerTextView;
        private final TextView dateTextView;

         private static final int NEW_TIP_EDIT_ACTIVITY_REQUEST_CODE = 70;


        private LedgerViewHolder(View itemView) {
            super(itemView);

            headerTextView = itemView.findViewById(R.id.headerTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final LedgerEntryEntity current = mLedgerEntries.get(position);


                    Intent intent = new Intent(context, TipEditActivity.class);
                    intent.putExtra("ledgerEntryId", current.getLedgerEntryId());
                    ((Activity) context).startActivityForResult(intent, NEW_TIP_EDIT_ACTIVITY_REQUEST_CODE);
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private final Context context;
    private List<LedgerEntryEntity> mLedgerEntries; // Cached copy of words
    private List<JobEntity> mJobEntities;

    public LedgerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public LedgerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.ledger_list_item, parent, false);
        return new LedgerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LedgerViewHolder holder, int position) {
        if (mLedgerEntries != null) {
            final LedgerEntryEntity current = mLedgerEntries.get(position);

            if(current != null) {
                if(position > 0 && current.getJobId() == mLedgerEntries.get(position - 1).getJobId()) {
                    holder.headerTextView.setVisibility(View.GONE);
                } else {
                    holder.headerTextView.setText(getJobName(current.getJobId()));

                }

                holder.dateTextView.setText(current.getAddedDateTimeFormatted());
                holder.amountTextView.setText(String.valueOf(current.getCredit()));
            }
        }
    }

    public void setLedgerEntries(List<LedgerEntryEntity> ledgerEntries) {
        mLedgerEntries = ledgerEntries;
        notifyDataSetChanged();
    }

    public void setJobEntities(List<JobEntity> jobEntities) {
        this.mJobEntities = jobEntities;
        notifyDataSetChanged();
    }

    public String getJobName(int jobId) {
        if (mJobEntities != null) {
            for (JobEntity job :
                    mJobEntities) {
                if (job.getJobId() == jobId) {
                    return job.getName();
                }
            }
        }

        return "Not Found";
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mLedgerEntries != null)
            return mLedgerEntries.size();
        else return 0;
    }
}
