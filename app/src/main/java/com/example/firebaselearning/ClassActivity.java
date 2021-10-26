package com.example.firebaselearning;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class ClassActivity extends AppCompatActivity {

    private TextView className, section, ownerName;
    private String OwnerID = "OwnerID not set";
    private String OwnerName = "OwnerName not set";
    private String Subject = "Subject not set";
    private String Section = "Section not set";

    private GridLayout girdLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        //getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerID, new ClassInfoFragment()).commit();

        className = findViewById(R.id.tvClassName);
        section = findViewById(R.id.tvSection);
        ownerName = findViewById(R.id.tvOwnerName);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Subject = extras.getString("ClassName");
            Section = extras.getString("Section");
            OwnerName = extras.getString("OwnerName");
            OwnerID = extras.getString("OwnerID");
        }
        OwnerID += Subject;
        className.setText(Subject);
        section.setText(Section);
        ownerName.setText(OwnerName);

        girdLayout = findViewById(R.id.gridLayoutID);
        setSingleEvent(girdLayout);
    }

    private void setSingleEvent(GridLayout girdLayout) {

        for(int i=0; i<girdLayout.getChildCount(); i++)
        {
            CardView cardView = (CardView) girdLayout.getChildAt(i);
            final int index = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(index == 0)
                    {
                        Intent intent = new Intent(ClassActivity.this, ClassInfo.class);
                        intent.putExtra("Title", "Class Info");
                        intent.putExtra("OwnerID", OwnerID);
                        startActivity(intent);
                    }
                    else if(index == 1)
                    {
                        Intent intent = new Intent(ClassActivity.this, ExamInfo.class);
                        intent.putExtra("Title", "Exam Info");
                        intent.putExtra("OwnerID", OwnerID);
                        startActivity(intent);
                    }
                    else if(index == 2)
                    {
                        Intent intent = new Intent(ClassActivity.this, AssignmentInfo.class);
                        intent.putExtra("Title", "Assignment Info");
                        intent.putExtra("OwnerID", OwnerID);
                        startActivity(intent);
                    }
                    else if(index == 3)
                    {
                        Intent intent = new Intent(ClassActivity.this, PresentationInfo.class);
                        intent.putExtra("Title", "Presentation Info");
                        intent.putExtra("OwnerID", OwnerID);
                        startActivity(intent);
                    }
                    else if(index == 4)
                    {
                        Toast.makeText(getApplicationContext(), "Settings Button is Clicked", Toast.LENGTH_SHORT).show();
                    }
                    else if(index == 5)
                    {
                        Toast.makeText(getApplicationContext(), "More Button is Clicked", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}