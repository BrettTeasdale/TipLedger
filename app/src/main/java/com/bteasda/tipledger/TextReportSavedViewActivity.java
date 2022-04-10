package com.bteasda.tipledger;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.bteasda.tipledger.BaseActivity;
import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.LedgerEntryEntity;
import com.bteasda.tipledger.Entities.TextReportDataEntity;
import com.bteasda.tipledger.Entities.TextReportEntity;
import com.bteasda.tipledger.LedgerFilter.Filter;
import com.bteasda.tipledger.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TextReportSavedViewActivity extends BaseActivity {
    private TextReportEntity mTextReportEntity;
    private List<TextReportDataEntity> mTextReportDataEntities;

    /**
     * Initialize the TextReportSavedViewActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_text_report_monthly);

        loadTextReportViewModel();
        loadTextReportDataViewModel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();


        mTextReportDataViewModel.getAllTextReportDataByTextReportId(extras.getInt("textReportId")).observe(this, new Observer<List<TextReportDataEntity>>() {
            @Override
            public void onChanged(List<TextReportDataEntity> textReportDataEntities) {
                mTextReportDataEntities = textReportDataEntities;

                buildLayout();
            }
        });

    }

    /**
     * Build the layout
     */
    private void buildLayout() {

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        LinearLayout linearLayout= new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearLayoutParams);

        Toolbar toolbar = new Toolbar(this);
        toolbar.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        toolbar.setTitle("Text Report");

        linearLayout.addView(toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ScrollView scrollView = new ScrollView(this);
        linearLayout.addView(scrollView);

        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearLayoutParams);
        scrollView.addView(linearLayout2);

        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(5);
        //gridLayout.setRowCount(4);
        //linearLayout2.addView(gridLayout);

        RelativeLayout.LayoutParams textViewLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textViewLayoutParam.setMargins(20, 10, 20, 10);

        int previousJobId = 0;
        for (TextReportDataEntity entry : mTextReportDataEntities) {
            if(previousJobId != entry.getJobId()) {
                TextView jobTitle = new TextView(this);
                jobTitle.setText(entry.getJobTitle());
                jobTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                linearLayout2.addView(jobTitle);

                gridLayout = new GridLayout(this);
                gridLayout.setColumnCount(5);

                TextView dateColTitle = new TextView(this);
                dateColTitle.setText("Month/Year");
                dateColTitle.setLayoutParams(textViewLayoutParam);
                dateColTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                gridLayout.addView(dateColTitle);

                TextView totalCreditColTitle = new TextView(this);
                totalCreditColTitle.setText("Total");
                totalCreditColTitle.setLayoutParams(textViewLayoutParam);
                totalCreditColTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                gridLayout.addView(totalCreditColTitle);

                TextView meanCreditColTitle = new TextView(this);
                meanCreditColTitle.setText("Mean");
                meanCreditColTitle.setLayoutParams(textViewLayoutParam);
                meanCreditColTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                gridLayout.addView(meanCreditColTitle);

                TextView lowestCreditColTitle = new TextView(this);
                lowestCreditColTitle.setText("Lowest");
                lowestCreditColTitle.setLayoutParams(textViewLayoutParam);
                lowestCreditColTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                gridLayout.addView(lowestCreditColTitle);

                TextView highestCreditColTitle = new TextView(this);
                highestCreditColTitle.setText("Highest");
                highestCreditColTitle.setLayoutParams(textViewLayoutParam);
                highestCreditColTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                gridLayout.addView(highestCreditColTitle);

                linearLayout2.addView(gridLayout);
            }

            TextView dateColumnTextView = new TextView(this);
            dateColumnTextView.setText(entry.getDateString());
            dateColumnTextView.setLayoutParams(textViewLayoutParam);
            gridLayout.addView(dateColumnTextView);

            TextView totalColumnTextView = new TextView(this);
            totalColumnTextView.setText(String.valueOf(entry.getTotal()));
            totalColumnTextView.setLayoutParams(textViewLayoutParam);
            gridLayout.addView(totalColumnTextView);

            TextView meanColumnTextView = new TextView(this);
            meanColumnTextView.setText(String.valueOf(entry.getMean()));
            meanColumnTextView.setLayoutParams(textViewLayoutParam);
            gridLayout.addView(meanColumnTextView);

            TextView lowestColumnTextView = new TextView(this);
            lowestColumnTextView.setText(String.valueOf(entry.getLowest()));
            lowestColumnTextView.setLayoutParams(textViewLayoutParam);
            gridLayout.addView(lowestColumnTextView);

            TextView highestColumnTextView = new TextView(this);
            highestColumnTextView.setText(String.valueOf(entry.getHighest()));
            highestColumnTextView.setLayoutParams(textViewLayoutParam);
            gridLayout.addView(highestColumnTextView);

            previousJobId = entry.getJobId();
        }

        this.addContentView(linearLayout, linearLayoutParams);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.text_report_saved_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_text_report_delete:
                mTextReportDataViewModel.deleteTextReportDataByTextReportId(getIntent().getExtras().getInt("textReportId"));
                mTextReportViewModel.deleteTextReportByTextReportId(getIntent().getExtras().getInt("textReportId"));

                Toast.makeText(getApplicationContext(), "Report was deleted.", Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
