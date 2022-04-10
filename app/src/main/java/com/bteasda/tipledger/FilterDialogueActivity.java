package com.bteasda.tipledger;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.Date;

public class FilterDialogueActivity extends BaseActivity {
        /**
         * The button used to confirm the filter and move to the next activity
         */
        private Button confirmFilterButton;

        /**
         * Switch used to determine whether or not the DateAboveFilter is active
         */
        private SwitchCompat dateAboveFilterSwitch;

        /**
         * Switch used to determine whether or not the DateBelowFilter is active
         */
        private SwitchCompat dateBelowFilterSwitch;

        /**
         * Switch used to determine whether or not the TipAboveFilter is active
         */
        private SwitchCompat tipAboveFilterSwitch;

        /**
         * Switch used to determine whether or not the TipBelowFilter is active
         */
        private SwitchCompat tipBelowFilterSwitch;

        /**
         * Switch used to determine whether or not the TipEqualsFilter is active
         */
        private SwitchCompat tipEqualsFilterSwitch;

        /**
         * The value for the DateAboveFilter to filter wither
         */
        private DatePicker dateAboveDatePicker;

        /**
         * The value for the DateBelowFilter to filter wither
         */
        private DatePicker dateBelowDatePicker;

        /**
         * The value for the TipAboveFilter to filter wither
         */
        private EditText tipAboveEditText;

        /**
         * The value for the DateAboveFilter to filter wither
         */
        private EditText tipBelowEditText;
        private EditText tipEqualsEditText;

        /**
         * Initialize the FilterDialogueActivity activity
         *
         * @param savedInstanceState saved state Bundle for restoring activity state
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_filter_dialogue);

                loadJobViewModel();
                loadLedgerEntryViewModel();

                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                dateAboveFilterSwitch = findViewById(R.id.dateAboveFilterSwitch);
                dateBelowFilterSwitch = findViewById(R.id.dateBelowFilterSwitch);
                tipAboveFilterSwitch = findViewById(R.id.tipAboveFilterSwitch);
                tipBelowFilterSwitch = findViewById(R.id.tipBelowFilterSwitch);
                tipEqualsFilterSwitch = findViewById(R.id.tipEqualsFilterSwitch);

                dateAboveDatePicker = findViewById(R.id.dateAboveDatePicker);
                dateBelowDatePicker = findViewById(R.id.dateBelowDatePicker);
                tipAboveEditText = findViewById(R.id.tipAboveEditText);
                tipBelowEditText = findViewById(R.id.tipBelowEditText);
                tipEqualsEditText = findViewById(R.id.tipEqualsEditText);

                confirmFilterButton = findViewById(R.id.confirmFilterButton);

                Bundle extras = getIntent().getExtras();

                confirmFilterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                try {
                                        FilterDialogueValidator validator = new FilterDialogueValidator(
                                                tipAboveEditText,
                                                tipBelowEditText,
                                                tipEqualsEditText,
                                                new Date(dateAboveDatePicker.getYear(), dateAboveDatePicker.getMonth(), dateAboveDatePicker.getDayOfMonth()).toInstant().toEpochMilli(),
                                                new Date(dateBelowDatePicker.getYear(), dateBelowDatePicker.getMonth(), dateBelowDatePicker.getDayOfMonth()).toInstant().toEpochMilli(),
                                                dateAboveFilterSwitch.isChecked(),
                                                dateBelowFilterSwitch.isChecked()
                                        );
                                        validator.validate();

                                        Intent intent = new Intent();
                                        intent.putExtra("enableDateAboveFilter", dateAboveFilterSwitch.isChecked());
                                        intent.putExtra("enableDateBelowFilter", dateBelowFilterSwitch.isChecked());
                                        intent.putExtra("enableTipAboveFilter", tipAboveFilterSwitch.isChecked());
                                        intent.putExtra("enableTipBelowFilter", tipBelowFilterSwitch.isChecked());
                                        intent.putExtra("enableTipEqualsFilter", tipEqualsFilterSwitch.isChecked());
                                        intent.putExtra("dateAboveFilterValue", new Date(dateAboveDatePicker.getYear(), dateAboveDatePicker.getMonth(), dateAboveDatePicker.getDayOfMonth()).toInstant().toEpochMilli());
                                        intent.putExtra("dateBelowFilterValue", new Date(dateBelowDatePicker.getYear(), dateBelowDatePicker.getMonth(), dateBelowDatePicker.getDayOfMonth()).toInstant().toEpochMilli());
                                        intent.putExtra("tipAboveFilterValue", Float.parseFloat(tipAboveEditText.getText().toString()));
                                        intent.putExtra("tipBelowFilterValue", Float.parseFloat(tipBelowEditText.getText().toString()));
                                        intent.putExtra("tipEqualsFilterValue", Float.parseFloat(tipEqualsEditText.getText().toString()));

                                        setResult(RESULT_OK, intent);
                                        finish();
                                } catch (Exception e) {
                                        Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                }
                        }
                });

                if(savedInstanceState != null) {
                        dateAboveFilterSwitch.setChecked(savedInstanceState.getBoolean("enableDateAboveFilter"));
                        dateBelowFilterSwitch.setChecked(savedInstanceState.getBoolean("enableDateBelowFilter"));
                        tipAboveFilterSwitch.setChecked(savedInstanceState.getBoolean("enableTipAboveFilter"));
                        tipBelowFilterSwitch.setChecked(savedInstanceState.getBoolean("enableTipBelowFilter"));
                        tipEqualsFilterSwitch.setChecked(savedInstanceState.getBoolean("enableTipEqualsFilter"));

                        Date dateAbove = new Date(savedInstanceState.getLong("dateAboveFilterValue"));
                        dateAboveDatePicker.updateDate(dateAboveDatePicker.getYear(), dateAboveDatePicker.getMonth(), dateAbove.getDate());

                        Date dateBelow = new Date(savedInstanceState.getLong("dateBelowFilterValue"));
                        dateBelowDatePicker.updateDate(dateBelow.getYear(), dateBelowDatePicker.getMonth(), dateBelow.getDate());

                        tipAboveEditText.setText(savedInstanceState.getString("tipAboveFilterValue"));
                        tipBelowEditText.setText(savedInstanceState.getString("tipBelowFilterValue"));
                        tipEqualsEditText.setText(savedInstanceState.getString("tipEqualsFilterValue"));
                } else {
                        dateAboveFilterSwitch.setChecked(extras.getBoolean("enableDateAboveFilter"));
                        dateBelowFilterSwitch.setChecked(extras.getBoolean("enableDateBelowFilter"));
                        tipAboveFilterSwitch.setChecked(extras.getBoolean("enableTipAboveFilter"));
                        tipBelowFilterSwitch.setChecked(extras.getBoolean("enableTipBelowFilter"));
                        tipEqualsFilterSwitch.setChecked(extras.getBoolean("enableTipEqualsFilter"));

                        if(extras.getLong("dateAboveFilterValue") != 0) {
                                Date dateAbove = new Date(extras.getLong("dateAboveFilterValue"));
                                dateAboveDatePicker.updateDate(dateAboveDatePicker.getYear(), dateAboveDatePicker.getMonth(), dateAbove.getDate());
                        }

                        if(extras.getLong("dateBelowFilterValue") != 0) {
                                Date dateBelow = new Date(extras.getLong("dateBelowFilterValue"));
                                dateBelowDatePicker.updateDate(dateBelow.getYear(), dateBelowDatePicker.getMonth(), dateBelow.getDate());
                        }

                        tipAboveEditText.setText(String.valueOf(extras.getFloat("tipAboveFilterValue")));
                        tipBelowEditText.setText(String.valueOf(extras.getFloat("tipBelowFilterValue")));
                        tipEqualsEditText.setText(String.valueOf(extras.getFloat("tipEqualsFilterValue")));
                }
        }

        /**
         * Save the instance state
         * @param savedInstanceState the Bundle to save the activity state to
         */
        @Override
        public void onSaveInstanceState(Bundle savedInstanceState) {
                super.onSaveInstanceState(savedInstanceState);

                savedInstanceState.putBoolean("enableDateAboveFilter", dateAboveFilterSwitch.isChecked());
                savedInstanceState.putBoolean("enableDateBelowFilter", dateBelowFilterSwitch.isChecked());
                savedInstanceState.putBoolean("enableTipAboveFilter", tipAboveFilterSwitch.isChecked());
                savedInstanceState.putBoolean("enableTipBelowFilter", tipBelowFilterSwitch.isChecked());
                savedInstanceState.putBoolean("enableTipEqualsFilter", tipEqualsFilterSwitch.isChecked());

                Calendar dateAboveCalendar = Calendar.getInstance();
                dateAboveCalendar.set(dateAboveDatePicker.getYear(), dateAboveDatePicker.getMonth(), dateAboveDatePicker.getDayOfMonth());

                savedInstanceState.putLong("dateAboveFilterValue", dateAboveCalendar.toInstant().toEpochMilli());

                Calendar dateBelowCalendar = Calendar.getInstance();
                dateBelowCalendar.set(dateBelowDatePicker.getYear(), dateBelowDatePicker.getMonth(), dateBelowDatePicker.getDayOfMonth());

                savedInstanceState.putLong("dateBelowFilterValue", dateBelowCalendar.toInstant().toEpochMilli());

                savedInstanceState.putString("tipAboveFilterValue", tipAboveEditText.getText().toString());
                savedInstanceState.putString("tipBelowFilterValue", tipBelowEditText.getText().toString());
                savedInstanceState.putString("tipEqualsFilterValue", tipEqualsEditText.getText().toString());
        }


        /**
         * Validator used to validate the form
         */
        public class FilterDialogueValidator {
                EditText tipAboveEditText;
                EditText tipBelowEditText;
                EditText tipEqualsEditText;
                long dateAboveValue;
                long dateBelowValue;
                boolean dateAboveEnabled;
                boolean dateBelowEnabled;

                public FilterDialogueValidator(EditText tipAboveEditText, EditText tipBelowEditText,
                                               EditText tipEqualsEditText, long dateAboveValue, long dateBelowValue,
                                               boolean dateAboveEnabled, boolean dateBelowEnabled) {
                        this.tipAboveEditText = tipAboveEditText;
                        this.tipBelowEditText = tipBelowEditText;
                        this.tipEqualsEditText = tipEqualsEditText;
                        this.dateAboveValue = dateAboveValue;
                        this.dateBelowValue = dateBelowValue;
                        this.dateAboveEnabled = dateAboveEnabled;
                        this.dateBelowEnabled = dateBelowEnabled;
                }

                /**
                 * Throws exceptions when invalid data is detected
                 * @throws StringIsNotDecimalException thrown when invalid data is detected
                 * @throws DatesUncoordinatedException thrown when invalid data is detected
                 */
                public void validate() throws StringIsNotDecimalException, DatesUncoordinatedException {
                        if(dateBelowEnabled && dateAboveEnabled && dateAboveValue >= dateBelowValue) {
                                throw new DatesUncoordinatedException("Date Above Value must be less than the Date Below Value");
                        }

                        String tipAboveValue = tipAboveEditText.getText().toString();
                        String tipBelowValue = tipBelowEditText.getText().toString();
                        String tipEqualsValue = tipEqualsEditText.getText().toString();

                        if(!tipAboveValue.matches("^\\d+\\.\\d+") && !tipAboveValue.matches("^\\d+")) {
                                throw new StringIsNotDecimalException("Tip Above Value is not a valid integer or decimal");
                        }

                        if(!tipBelowValue.matches("^\\d+\\.\\d+") && !tipBelowValue.matches("^\\d+")) {
                                throw new StringIsNotDecimalException("Tip Below Value is not a valid integer or decimal");
                        }

                        if(!tipEqualsValue.matches("^\\d+\\.\\d+") && !tipEqualsValue.matches("^\\d+")) {
                                throw new StringIsNotDecimalException("Tip Equals Value is not a valid integer or decimal");
                        }
                }

                /**
                 * An exception thrown when a string value is not a decimal
                 */
                public class StringIsNotDecimalException extends Exception {
                        public StringIsNotDecimalException() {
                                super();
                        }

                        public StringIsNotDecimalException(String message) {
                                super(message);
                        }
                }

                /**
                 * An exception thrown when a date should be below another but is not
                 */
                public class DatesUncoordinatedException extends Exception {
                        public DatesUncoordinatedException() {
                                super();
                        }

                        public DatesUncoordinatedException(String message) {
                                super(message);
                        }
                }
        }
}
