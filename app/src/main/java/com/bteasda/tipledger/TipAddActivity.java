package com.bteasda.tipledger;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.bteasda.tipledger.Entities.LedgerEntryEntity;
import com.bteasda.tipledger.ViewModels.LedgerEntryViewModel;

import java.util.Calendar;
import java.util.Date;

public class TipAddActivity extends BaseActivity {
    private EditText mAmountEditText;

    /**
     * Initialize the TipAddActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_add);

        loadLedgerEntryViewModel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button addTipButton = findViewById(R.id.addTipButton);
        addTipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTip();
            }
        });

        mAmountEditText = findViewById(R.id.amountEditText);
        mAmountEditText.requestFocus();
        mAmountEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_ENTER) {
                    addTip();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent intent = new Intent();
        intent.putExtra("exit", true);
        setResult(RESULT_OK, intent);
        onBackPressed();
        return true;
    }

    /**
     * Save the tip to the ledger table
     */
    private void addTip() {
        // @todo add amount validation

        LedgerEntryEntity newLedgerEntry = new LedgerEntryEntity(new Date(), Float.parseFloat(mAmountEditText.getText().toString()), getIntent().getIntExtra("jobId", 0));
        mLedgerEntryViewModel.insert(newLedgerEntry);

        Intent intent = new Intent();
        intent.putExtra("exit", false);
        intent.putExtra("jobId", getIntent().getExtras().getInt("jobId"));
        setResult(RESULT_OK, intent);

        finish();
    }
}
