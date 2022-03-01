package com.example.mareu.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mareu.R;
import com.example.mareu.di.DI;
import com.example.mareu.model.Room;
import com.example.mareu.service.MeetingApiService;

import java.util.List;

public class DialogRoomSpinnerFragment extends DialogFragment {

    public interface DialogRoomSpinnerListener {
        void onDialogRoomSpinnerClick(DialogFragment dialog);
    }

    DialogRoomSpinnerListener mListener;
    MeetingApiService meetingApiService = DI.getMeetingApiService();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_room_spinner, null);
        builder.setView(view)
                .setMessage("Sélectionner une salle de réunion")
                .setPositiveButton("ok", (dialog, which) -> mListener.onDialogRoomSpinnerClick(DialogRoomSpinnerFragment.this))
                .setNegativeButton("cancel", (dialog, which) -> DialogRoomSpinnerFragment.this.getDialog().cancel());

        Spinner spinner = (Spinner) view.findViewById(R.id.dialog_room_spinner);
        List<Room> rooms = meetingApiService.getAllRooms();
        spinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, rooms));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogRoomSpinnerListener) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement DialogRoomSpinnerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
