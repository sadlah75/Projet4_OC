package com.example.mareu.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.adapter.MeetingAdapter;
import com.example.mareu.di.DI;
import com.example.mareu.events.DeleteMeetingEvent;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.MeetingApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MeetingFragment extends Fragment implements DialogRoomSpinnerFragment.DialogRoomSpinnerListener,
DialogDatePickerFragment.DialogDatePickerListener{


    private MeetingApiService mApiService;
    private RecyclerView mRecyclerView;

    /**
     * Create and return a new instance
     * @return @{@link MeetingFragment}
     */
    public static MeetingFragment newInstance() {
        return new MeetingFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getMeetingApiService();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_list,container,false);
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        List<Meeting> mMeetings = mApiService.getMeetings();
        mRecyclerView.setAdapter(new MeetingAdapter(mMeetings));
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteMeetingEvent event) {
        mApiService.deleteMeeting(event.meeting);
        initList();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.filter_room:
                filterByRoom();
                return true;
            case R.id.filter_date:
                filterByDate();
                return true;
            case R.id.reset:
                reset();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reset() {
        initList();
    }

    private void filterByRoom() {
        DialogRoomSpinnerFragment dialogRoomSpinnerFragment = new DialogRoomSpinnerFragment();
        dialogRoomSpinnerFragment.setTargetFragment(MeetingFragment.this,2);
        dialogRoomSpinnerFragment.show(getFragmentManager(),"DialogRoomSpinnerFragment");

    }

    private void filterByDate() {
        DialogDatePickerFragment dialogDatePickerFragment = new DialogDatePickerFragment();
        dialogDatePickerFragment.setTargetFragment(MeetingFragment.this,2);
        dialogDatePickerFragment.show(getFragmentManager(),"DialogDatePickerFragment");
    }

    @Override
    public void onDialogRoomSpinnerClick(DialogFragment dialog) {
        Spinner spinner = (Spinner)dialog.getDialog().findViewById(R.id.dialog_room_spinner);
        Room room = (Room)spinner.getSelectedItem();
        List<Meeting> roomsByName = mApiService.getMeetingsFilteredByRoom(room.getName());
        mRecyclerView.setAdapter(new MeetingAdapter(new ArrayList<>(roomsByName)));
    }

    @Override
    public void onDialogDatePickerClick(DialogDatePickerFragment dialog) {
        DatePicker datePicker = (DatePicker) dialog.getDialog().findViewById(R.id.dialog_date_picker);
        int lDay = datePicker.getDayOfMonth();
        int lMonth = datePicker.getMonth();
        int lYear = datePicker.getYear();


        Calendar calendar = Calendar.getInstance();
        calendar.set(lYear,lMonth,lDay);
        Date date = calendar.getTime();
        String selectedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);

        List<Meeting> roomsByDate = mApiService.getMeetingsFilteredByDate(selectedDate);
        mRecyclerView.setAdapter(new MeetingAdapter(new ArrayList<>(roomsByDate)));
    }


}
