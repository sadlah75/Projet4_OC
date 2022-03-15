package com.example.mareu.ui.meeting_list;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mareu.R;
import com.example.mareu.adapter.MeetingAdapter;
import com.example.mareu.databinding.ActivityListMeetingBinding;
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

public class ListMeetingActivity extends AppCompatActivity {

    private ActivityListMeetingBinding binding;
    private ArrayList<Meeting> mMeetingArrayList;
    private MeetingApiService mMeetingApiService = DI.getMeetingApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGUI();
    }

    private void initGUI() {
        binding = ActivityListMeetingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setAddFab();
        initData();
        initRecyclerView();
    }

    private void setAddFab() {
        binding.startAddActivity.setOnClickListener(view -> AddMeetingActivity.navigate(ListMeetingActivity.this));
    }

    private void initData() {
        mMeetingArrayList = new ArrayList<>(mMeetingApiService.getMeetings());
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(layoutManager);

        MeetingAdapter mAdapter = new MeetingAdapter(mMeetingArrayList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerview.getContext(),
                layoutManager.getOrientation());
        binding.recyclerview.addItemDecoration(dividerItemDecoration);
        binding.recyclerview.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetFilter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
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
        mMeetingApiService.deleteMeeting(event.meeting);
        resetFilter();
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
                resetFilter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void filterByRoom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_room_spinner, null);
        Spinner spinner = view.findViewById(R.id.dialog_room_spinner);

        builder.setView(view)
                .setTitle("Sélectionner une salle de réunion")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Room room = (Room) spinner.getSelectedItem();
                        mMeetingArrayList.clear();
                        mMeetingArrayList.addAll(mMeetingApiService.getMeetingsFilteredByRoom(room.getName()));
                        binding.recyclerview.getAdapter().notifyDataSetChanged();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        List<Room> rooms = mMeetingApiService.getAllRooms();
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rooms));
        builder.create().show();

    }

    private void filterByDate() {
        int selectedYear = 2022;
        int selectedMonth = 3;
        int selectedDayOfMonth = 15;

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                Date date = calendar.getTime();
                String selectedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
                mMeetingArrayList.clear();
                mMeetingArrayList.addAll(mMeetingApiService.getMeetingsFilteredByDate(selectedDate));
                binding.recyclerview.getAdapter().notifyDataSetChanged();
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

        datePickerDialog.show();
    }
    private void resetFilter() {
        mMeetingArrayList.clear();
        mMeetingArrayList.addAll(mMeetingApiService.getMeetings());
        binding.recyclerview.getAdapter().notifyDataSetChanged();
    }
}