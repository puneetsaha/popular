package com.example.puneetkumar.myapplication.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 ** Create a date picker dialog
 **/
public class DatePicker  extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = DatePicker.class.getCanonicalName() ;
    public static final String DATE_FORMAT =  "MMMM dd, yyyy";
    public static final String DATE_INPUT =  "date_input";

    public interface OnDateSelected {
        public void onDateSelected(final String formattedDate);
    }

    private OnDateSelected mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of TimePickerDialog and return it
        final Calendar c = Calendar.getInstance();
        if (getArguments() != null) {
            String previousDate = getArguments().getString(DATE_INPUT);
            Log.d(TAG, " Date input " + previousDate);
            try {
                Date d = new SimpleDateFormat(DATE_FORMAT).parse(previousDate);
                c.setTime(d);
            } catch (Exception e) {
                Log.e(TAG, "Exception ");
            }
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.YEAR, -30);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String formattedDate ="";
        Format formatter;

        final Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);


        formatter = new SimpleDateFormat(DATE_FORMAT);
        formattedDate = formatter.format(c.getTime());
        mCallback.onDateSelected(formattedDate);
    }

    public OnDateSelected getCallback() {
        return mCallback;
    }

    public void setCallback(OnDateSelected mCallback) {
        this.mCallback = mCallback;
    }
}