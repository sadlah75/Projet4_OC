package com.example.mareu.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mareu.R;

public class DialogTimePickerFragment extends DialogFragment {

    public interface DialogTimePickerListener {
        void onDialogTimePickerClick(DialogFragment dialogFragment);
    }

    private DialogTimePickerListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View picker = inflater.inflate(R.layout.dialog_time_picker,null);

        builder.setView(picker)
                .setMessage("Sélectionner une heure de réunion")
                .setPositiveButton("ok", (dialogInterface, i) -> mListener.onDialogTimePickerClick(DialogTimePickerFragment.this))
                .setNegativeButton("cancel", (dialogInterface, i) -> DialogTimePickerFragment.this.getDialog().cancel());
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogTimePickerListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement DialogTimePickerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
