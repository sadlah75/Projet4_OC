package com.example.mareu.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mareu.R;

public class DialogDatePickerFragment extends DialogFragment {

    public interface DialogDatePickerListener {
        void onDialogDatePickerClick(DialogDatePickerFragment dialogDatePickerFragment);
    }

    DialogDatePickerListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_date_picker, null))
                .setMessage("Date de réunion")
                .setPositiveButton("ok", (dialog, which) -> mListener.onDialogDatePickerClick(DialogDatePickerFragment.this))
                .setNegativeButton("cancel", (dialog, which) -> DialogDatePickerFragment.this.getDialog().cancel());
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogDatePickerListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement DialogDatePickerListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}