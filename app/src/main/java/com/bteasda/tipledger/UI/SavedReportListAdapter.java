package com.bteasda.tipledger.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bteasda.tipledger.Entities.TextReportEntity;
import com.bteasda.tipledger.R;
import com.bteasda.tipledger.TextReportSavedViewActivity;

import java.util.List;

public class SavedReportListAdapter extends RecyclerView.Adapter<SavedReportListAdapter.TextReportViewHolder> {
    private final int NEW_SAVED_REPORT_VIEW_ACTIVITY_REQUEST_CODE = 60;

    class TextReportViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTitleTextView;

        private TextReportViewHolder(View itemView, Activity activity) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.titleTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final TextReportEntity current = mTextReportEntities.get(position);


                    Intent intent = new Intent(context, TextReportSavedViewActivity.class);
                    intent.putExtra("textReportId", current.getTextReportId());
                    activity.startActivityForResult(intent, NEW_SAVED_REPORT_VIEW_ACTIVITY_REQUEST_CODE);
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private final Context context;
    private List<TextReportEntity> mTextReportEntities; // Cached copy of words

    public SavedReportListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public TextReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.saved_report_list_item, parent, false);
        return new TextReportViewHolder(itemView, (Activity) context);
    }

    @Override
    public void onBindViewHolder(TextReportViewHolder holder, int position) {
        if (mTextReportEntities != null) {
            final TextReportEntity current = mTextReportEntities.get(position);

            holder.mTitleTextView.setText(current.getTitle() + "(" + current.getGeneratedDateFormatted() + ")");
        }

    }

    public void setTextReportEntities(List<TextReportEntity> textReportEntities) {
        mTextReportEntities = textReportEntities;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mTextReportEntities != null)
            return mTextReportEntities.size();
        else return 0;
    }
}
