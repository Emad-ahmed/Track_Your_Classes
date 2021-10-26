package com.example.firebaselearning;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterClassForPost extends RecyclerView.Adapter<AdapterClassForPost.MyViewHolder>{

    Context context;
    ArrayList<PostingModel>list;
    private DatabaseReference postingDatabase;
    AlertDialog.Builder builder;

    private TextView tvOnceTime, tvOnceDate, tvRepeatingTime;
    private ImageButton ibOnceTime, ibOnceDate, ibRepeatingTime;
    private EditText etOnceMessage, etRepeatingMessage;
    private Button btnSetOnceAlarm, btnSetRepeatingAlarm, btnCancelRepeatingAlarm;

    private AlarmReceiver alarmReceiver;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int mHourRepeat, mMinuteRepeat;

    private String OwnerID, Title;

    public AdapterClassForPost(Context context, ArrayList<PostingModel> list, String OwnerID, String Title) {
        this.context = context;
        this.list = list;
        this.OwnerID = OwnerID;
        this.Title = Title;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cardview_for_post, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PostingModel postingModel = list.get(position);
        holder.post.setText(postingModel.getPost());
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.more_menu_item, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        builder = new AlertDialog.Builder(context);

                        switch (menuItem.getItemId()) {
                            case R.id.editPostID:
                                editPost(postingModel);
                                break;
                            case R.id.deletePostID:
                                deletePost(postingModel);
                                break;
                            case R.id.setAlarmID:
                                setAnAlarm();
                                break;
                        }

                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView post;
        ImageView moreBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            post = itemView.findViewById(R.id.PostCardID);
            moreBtn = itemView.findViewById(R.id.MoreID);
        }
    }

    // edit post
    public void editPost(PostingModel postingModel) {
        final DialogPlus dialogPlus = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.dialog_for_edit_post))
                .setExpanded(true, 1100)
                .create();

        View myView = dialogPlus.getHolderView();
        final EditText postEt = myView.findViewById(R.id.postETiD);
        final Button update = myView.findViewById(R.id.updateBtnID);

        postEt.setText(postingModel.getPost());
        dialogPlus.show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String post = postEt.getText().toString().trim();
                if(post.isEmpty()) {
                    postEt.setError("Post Field is required");
                    postEt.requestFocus();
                    return;
                }

                switch(Title)
                {
                    case "Class Info":
                        postingDatabase = FirebaseDatabase.getInstance().getReference("PostInClassInfo");
                        postingDatabase.child(OwnerID).child(postingModel.getID()).child("Post").setValue(post);
                        dialogPlus.dismiss();
                        break;

                    case "Exam Info":
                        postingDatabase = FirebaseDatabase.getInstance().getReference("PostInExamInfo");
                        postingDatabase.child(OwnerID).child(postingModel.getID()).child("Post").setValue(post);
                        dialogPlus.dismiss();
                        break;

                    case "Assignment Info":
                        postingDatabase = FirebaseDatabase.getInstance().getReference("PostInAssignmentInfo");
                        postingDatabase.child(OwnerID).child(postingModel.getID()).child("Post").setValue(post);
                        dialogPlus.dismiss();
                        break;

                    case "Presentation Info":
                        postingDatabase = FirebaseDatabase.getInstance().getReference("PostInPresentationInfo");
                        postingDatabase.child(OwnerID).child(postingModel.getID()).child("Post").setValue(post);
                        dialogPlus.dismiss();
                        break;
                }

                postEt.setText("");
            }
        });
    }

    // delete Post
    public void deletePost(PostingModel postingModel) {
        switch(Title)
        {
            case "Class Info":
                postingDatabase = FirebaseDatabase.getInstance().getReference("PostInClassInfo");
                builder.setTitle("Alert!!")
                        .setMessage("Do you want to delate this post")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                postingDatabase.child(OwnerID).child(postingModel.getID()).removeValue();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
                break;
            case "Exam Info":
                postingDatabase = FirebaseDatabase.getInstance().getReference("PostInExamInfo");
                builder.setTitle("Alert!!")
                        .setMessage("Do you want to delate this post")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                postingDatabase.child(OwnerID).child(postingModel.getID()).removeValue();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
                break;
            case "Assignment Info":
                postingDatabase = FirebaseDatabase.getInstance().getReference("PostInAssignmentInfo");
                builder.setTitle("Alert!!")
                        .setMessage("Do you want to delate this post")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                postingDatabase.child(OwnerID).child(postingModel.getID()).removeValue();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
                break;
            case "Presentation Info":
                postingDatabase = FirebaseDatabase.getInstance().getReference("PostInPresentationInfo");
                builder.setTitle("Alert!!")
                        .setMessage("Do you want to delate this post")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                postingDatabase.child(OwnerID).child(postingModel.getID()).removeValue();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
                break;
        }
    }

    // Set Alarm
    public void  setAnAlarm() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_for_alarm);

        tvOnceTime = dialog.findViewById(R.id.tv_once_time);
        tvOnceDate = dialog.findViewById(R.id.tv_once_date);
        tvRepeatingTime = dialog.findViewById(R.id.tv_repeating_time);
        ibOnceTime = dialog.findViewById(R.id.ib_once_time);
        ibOnceDate = dialog.findViewById(R.id.ib_once_date);
        ibRepeatingTime = dialog.findViewById(R.id.ib_repeating_time);
        etOnceMessage = dialog.findViewById(R.id.et_once_message);
        etRepeatingMessage = dialog.findViewById(R.id.et_repeating_message);
        btnSetOnceAlarm = dialog.findViewById(R.id.btn_set_once_alarm);
        btnSetRepeatingAlarm = dialog.findViewById(R.id.btn_set_repeating_alarm);
        btnCancelRepeatingAlarm = dialog.findViewById(R.id.btn_cancel_repeating_alarm);

        alarmReceiver = new AlarmReceiver();

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        mHourRepeat = mHour;
        mMinuteRepeat = mMinute;

        ibOnceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        tvOnceDate.setText(String.format("%04d-%02d-%02d", year, month+1, dayOfMonth));
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        ibOnceTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        if(hourOfDay>12)
                            tvOnceTime.setText(String.format("%02d:%02d", hourOfDay-12, minute) + "PM");
                        else
                            tvOnceTime.setText(String.format("%02d:%02d", hourOfDay, minute) + " AM");
                        mHour = hourOfDay;
                        mMinute = minute;
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        ibRepeatingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        if(hourOfDay>12)
                            tvRepeatingTime.setText(String.format("%02d:%02d", hourOfDay-12, minute) + " PM");
                        else
                            tvRepeatingTime.setText(String.format("%02d:%02d", hourOfDay, minute) + " AM");
                        mHourRepeat = hourOfDay;
                        mMinuteRepeat = minute;
                    }
                }, mHourRepeat, mMinuteRepeat, false);
                timePickerDialog.show();
            }
        });

        btnSetOnceAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvOnceDate.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(context, "Data is empty", Toast.LENGTH_SHORT).show();
                }
                else if(tvOnceTime.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(context, "Time is empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(etOnceMessage.getText().toString())){
                    etOnceMessage.setError("Message can't be empty!");
                }
                else{
                    String temp = tvOnceTime.getText().toString();
                    alarmReceiver.setOneTimeAlarm(context, AlarmReceiver.TYPE_ONE_TIME,
                            tvOnceDate.getText().toString(), temp.substring(0,4),
                            etOnceMessage.getText().toString());
                }
            }
        });

        btnSetRepeatingAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvRepeatingTime.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(context, "Time is empty", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(etRepeatingMessage.getText().toString())){
                    etRepeatingMessage.setError("Message can't be empty!");
                }
                else{
                    String temp = tvRepeatingTime.getText().toString();
                    alarmReceiver.setRepeatingAlarm(context, AlarmReceiver.TYPE_Repeating,
                            temp.substring(0,4),
                            etRepeatingMessage.getText().toString());
                }
            }
        });

        btnCancelRepeatingAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alarmReceiver.isAlarmSet(context, AlarmReceiver.TYPE_Repeating)){
                    tvRepeatingTime.setText("");
                    etRepeatingMessage.setText("");
                    alarmReceiver.cencelAlarm(context, AlarmReceiver.TYPE_Repeating);
                }
            }
        });

        dialog.show();
    }
}
