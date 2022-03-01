package com.example.mareu.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.events.DeleteMeetingEvent;
import com.example.mareu.model.Meeting;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {

    private final List<Meeting> meetings;

    public MeetingAdapter(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.meeting_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);
        holder.displayMeeting(meeting);
    }

    @Override
    public int getItemCount() {
        return this.meetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mCircle,mDeleteButton;
        public TextView mRoom,mSubject,mTime,mMails,mLetter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCircle = itemView.findViewById(R.id.item_meeting_circle);
            mLetter = itemView.findViewById(R.id.item_meeting_letter);
            mRoom = itemView.findViewById(R.id.item_meeting_room);
            mSubject = itemView.findViewById(R.id.item_meeting_subject);
            mTime = itemView.findViewById(R.id.item_meeting_date);
            mMails = itemView.findViewById(R.id.item_meeting_mails);
            mDeleteButton = itemView.findViewById(R.id.item_meeting_delete_button);
        }

        @SuppressLint("NewApi")
        public void displayMeeting(Meeting meeting) {
            mCircle.getDrawable().setTint(Color.parseColor(meeting.getRoom().getColor()));
            mLetter.setText("" + meeting.getRoom().getName().charAt(5));
            mRoom.setText(meeting.getRoom().getName());
            mSubject.setText(meeting.getSubject() + "  -");
            mMails.setText(meeting.getMailsFormatted());
            mTime.setText(meeting.getTimeFormatted() + "  -");
            itemView.setTooltipText(meeting.getDateFormatted());
            mDeleteButton.setOnClickListener(view -> EventBus.getDefault().post(new DeleteMeetingEvent(meeting)));
        }
    }
}
