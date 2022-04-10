package com.bteasda.tipledger;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.bteasda.tipledger.Entities.LedgerEntryEntity;

import java.util.Calendar;
import java.util.Date;

public class TipEditActivity extends BaseActivity {
    EditText mAmountEditText;
    DatePicker mDatePicker;
    TimePicker mTimePicker;
    LedgerEntryEntity mLedgerEntryEntity;

    /**
     * Initialize the TipEditActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_edit);

        loadLedgerEntryViewModel();

        Bundle extras = getIntent().getExtras();

        mAmountEditText = findViewById(R.id.amountEditText);
        mDatePicker = findViewById(R.id.datePicker);
        mTimePicker = findViewById(R.id.timePicker);

        mLedgerEntryViewModel.getLedgerEntryById(extras.getInt("ledgerEntryId")).observe(this, new Observer<LedgerEntryEntity>() {
            @Override
            public void onChanged(LedgerEntryEntity ledgerEntryEntity) {
                if(ledgerEntryEntity != null) {
                    mLedgerEntryEntity = ledgerEntryEntity;

                    mAmountEditText.setText(String.valueOf(mLedgerEntryEntity.getCredit()));

                    Calendar addedCalendar = Calendar.getInstance();
                    addedCalendar.setTime(mLedgerEntryEntity.getAddedDateTime());

                    mDatePicker.updateDate(addedCalendar.get(Calendar.YEAR), addedCalendar.get(Calendar.MONTH), addedCalendar.get(Calendar.DATE));
                    mTimePicker.setHour(addedCalendar.get(Calendar.HOUR_OF_DAY));
                    mTimePicker.setMinute(addedCalendar.get(Calendar.MINUTE));
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Button saveTipButton = findViewById(R.id.saveTipButton);
        saveTipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTip();
            }
        });
    }

    /**
     * Save the tip data to the ledger table
     */
    public void saveTip() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), mTimePicker.getHour(), mTimePicker.getMinute());
        mLedgerEntryEntity.setAddedDateTime(calendar.getTime());
        mLedgerEntryEntity.setCredit(Float.parseFloat(mAmountEditText.getText().toString()));

        mLedgerEntryViewModel.insert(mLedgerEntryEntity);

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tip_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_tip_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Deleting this job will delete all it's data");
                dialog.setTitle("Are you sure you want to continue?");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mLedgerEntryViewModel.deleteLedgerEntryByLedgerEntryId(mLedgerEntryEntity.getLedgerEntryId());

                        Toast.makeText(getApplicationContext(), "The has been deleted.", Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);
                        finish();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
