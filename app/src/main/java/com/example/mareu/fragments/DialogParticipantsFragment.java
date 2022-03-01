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

public class DialogParticipantsFragment extends DialogFragment {

    public interface DialogParticipantsListener {
        void onDialogParticipantValidateClick(DialogFragment dialogFragment);
        void onDialogCancelParticipant(DialogFragment dialogFragment);
    }

    private DialogParticipantsListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(R.layout.dialog_particpants)
                .setTitle("Veuillez ajouter un participant")
                .setNegativeButton("cancel", (dialogInterface, i) -> mListener.onDialogCancelParticipant(DialogParticipantsFragment.this))
                .setPositiveButton("validate", (dialogInterface, i) -> {
                    mListener.onDialogParticipantValidateClick(DialogParticipantsFragment.this);
                    dialogInterface.dismiss();
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogParticipantsListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement DialogParticipantsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
