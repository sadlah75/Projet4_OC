package com.example.mareu.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mareu.R;
import com.example.mareu.databinding.FragmentAddMeetingBinding;
import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.MeetingApiService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMeetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMeetingFragment extends Fragment implements DialogTimePickerFragment.DialogTimePickerListener,DialogDatePickerFragment.DialogDatePickerListener,
        DialogParticipantsFragment.DialogParticipantsListener {

    private FragmentAddMeetingBinding binding;

    private MeetingApiService meetingApiService;

    private final Calendar mCalendar = Calendar.getInstance();
    private int mHour,mMinute;
    private int mDay,mMonth,mYear;

    private final List<String> mEmails = new ArrayList<>();

    private boolean bRoom = false;
    private boolean bSubject = false;
    private boolean bTime = false;
    private boolean bDate = false;
    private boolean bEmails = false;

    public AddMeetingFragment() {
    }

    public static AddMeetingFragment newInstance() {
        return  new AddMeetingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meetingApiService = DI.getMeetingApiService();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentAddMeetingBinding.inflate(inflater,container,false);
       setContainer();
       checkStatusButtonSave();
       return binding.getRoot();
    }

    private void setContainer() {
        setSpinnerRoom();
        setInputSubject();
        setTimePicker();
        setDatePicker();
        setButtonSave();
        setInputParticipant();
    }

    private void setSpinnerRoom() {
        List<String> mRoomsNames = Arrays.asList(getResources().getStringArray(R.array.rooms_array));
        ArrayAdapter<String> arrayAdapterRooms = new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, mRoomsNames);
        binding.addMeetingRoom.setAdapter(arrayAdapterRooms);
        binding.addMeetingRoom.setThreshold(1);
        binding.addMeetingRoom.setOnItemClickListener((adapterView, view, i, l) -> {
            bRoom = true;
            checkStatusButtonSave();
        });
    }

    private void setTimePicker() {
        binding.addMeetingButtonSaveTime.setOnClickListener(view -> displayTimePickerDialog());
    }

    private void setDatePicker() {
        binding.addMeetingButtonSaveDate.setOnClickListener(view -> displayDatePickerDialog());
    }

    private void setInputSubject() {
        binding.addMeetingSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                bSubject = editable.length() > 0;
                checkStatusButtonSave();
            }
        });
    }

    private void setInputParticipant() {
        binding.addMeetingButtonAddEmails.setOnClickListener(view -> displayAddEmail());
    }

    private void displayTimePickerDialog() {
        DialogTimePickerFragment dialogTimePickerFragment = new DialogTimePickerFragment();
        dialogTimePickerFragment.setTargetFragment(AddMeetingFragment.this,1);
        dialogTimePickerFragment.show(getFragmentManager(),"DialogTimePickerFragmant");
    }

    private void displayDatePickerDialog() {
        DialogDatePickerFragment dialogDatePickerFragment = new DialogDatePickerFragment();
        dialogDatePickerFragment.setTargetFragment(AddMeetingFragment.this,2);
        dialogDatePickerFragment.show(getFragmentManager(),"DialogDatePickerFragment");
    }

    private void displayAddEmail() {
        DialogParticipantsFragment dialogParticipantsFragment = new DialogParticipantsFragment();
        dialogParticipantsFragment.setTargetFragment(AddMeetingFragment.this, 2);
        dialogParticipantsFragment.show(getFragmentManager(),"DialogParticipantsFragment");
    }

    @Override
    public void onDialogTimePickerClick(DialogFragment dialogFragment) {
        TimePicker timePicker = (TimePicker) dialogFragment.getDialog().findViewById(R.id.dialog_time_picker);
        mHour = timePicker.getCurrentHour();
        mMinute = timePicker.getCurrentMinute();
        String timeFormatted = String.format("%02dh%02d", mHour, mMinute);
        binding.addMeetingTextViewTime.setText(timeFormatted);
        bTime = true;
        checkStatusButtonSave();
    }

    @Override
    public void onDialogDatePickerClick(DialogDatePickerFragment dialogDatePickerFragment) {
        DatePicker datePicker = (DatePicker) dialogDatePickerFragment.getDialog().findViewById(R.id.dialog_date_picker);
        mDay = datePicker.getDayOfMonth();
        mMonth = datePicker.getMonth() + 1;
        mYear = datePicker.getYear();
        String dateFormatted = String.format("%02d/%02d/%02d", mDay, mMonth, mYear);
        binding.addMeetingTextViewDate.setText(dateFormatted);
        bDate = true;
        checkStatusButtonSave();
    }

    @Override
    public void onDialogParticipantValidateClick(DialogFragment dialogFragment) {
        TextInputEditText participant = dialogFragment.getDialog().findViewById(R.id.addMeeting_dialog_participant);
        String mail = participant.getText().toString();
        if (isEmailValid(mail)){
           if (!mEmails.contains(mail)) {
                mEmails.add(mail);
               displayMails();
                if (mEmails.size() > 1) {
                    bEmails = true;
                    checkStatusButtonSave();
                }
           }else {
               Toast.makeText(getActivity(), "Email déjà saisi", Toast.LENGTH_LONG).show();
               displayAddEmail();
           }
        }else {
            Toast.makeText(getActivity(), "Email non valide", Toast.LENGTH_LONG).show();
            displayAddEmail();
        }
    }

    @Override
    public void onDialogCancelParticipant(DialogFragment dialogFragment) { }

    public boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void displayMails() {
        binding.addMeetingTextViewEmails.setVisibility(View.VISIBLE);
        StringBuilder allMails = new StringBuilder();
        for(String mail : mEmails) {
            allMails.append(mail).append("\n");
        }
        binding.addMeetingTextViewEmails.setText(allMails.toString());
    }
    private void setButtonSave() {
        binding.addMeetingSave.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(mYear,mMonth-1,mDay,mHour,mMinute);
            Date date = calendar.getTime();
            String nameRoom = binding.addMeetingRoom.getText().toString();
            if(!checkIfRoomAvailable(nameRoom,date)) {
                Toast.makeText(getActivity(), nameRoom + " non disponible à cette date"
                        , Toast.LENGTH_SHORT).show();
                binding.addMeetingRoom.setText("");
                bRoom = false;
                checkStatusButtonSave();
            }else {
                Room room = meetingApiService.getRoomByName(nameRoom);
                String sujet = binding.addMeetingSubject.getText().toString();
                Meeting meeting = new Meeting(room,date,sujet,mEmails);
                meetingApiService.createMeeting(meeting);
                getActivity().finish();
            }
        });
    }
    private void checkStatusButtonSave() {
        binding.addMeetingSave.setEnabled(bRoom && bSubject && bTime && bDate && bEmails);
    }

    private boolean checkIfRoomAvailable(String selectedRoom, Date selectedDate) {
        List<Meeting> meetings = meetingApiService.getMeetings();
        for (Meeting currentMeeting : meetings) {
            if(currentMeeting.getRoom().getName().equals(selectedRoom)) {
                mCalendar.setTime(currentMeeting.getDate());
                mCalendar.add(Calendar.MINUTE, -45);
                Date timeBegin = mCalendar.getTime();
                mCalendar.add(Calendar.MINUTE, 90);
                Date timeFinish = mCalendar.getTime();
                if (selectedDate.compareTo(timeBegin) == 1 && selectedDate.compareTo(timeFinish) == -1) {
                     return false;
                }
            }
        }
        return true;
    }
}