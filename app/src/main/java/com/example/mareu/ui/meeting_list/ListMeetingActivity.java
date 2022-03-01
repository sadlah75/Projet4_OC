package com.example.mareu.ui.meeting_list;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mareu.R;
import com.example.mareu.databinding.ActivityListMeetingBinding;
import com.example.mareu.fragments.MeetingFragment;

public class ListMeetingActivity extends AppCompatActivity {

    private ActivityListMeetingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListMeetingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initAddFragment();
        setAddFab();
    }

    private void initAddFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerView, MeetingFragment.newInstance())
                .commit();

    }

    private void setAddFab() {
        binding.startAddActivity.setOnClickListener(view -> AddMeetingActivity.navigate(ListMeetingActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}