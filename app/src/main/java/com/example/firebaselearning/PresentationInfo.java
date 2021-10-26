package com.example.firebaselearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PresentationInfo extends AppCompatActivity {

    private ImageView addBtn;
    private Button postBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference postingDatabase;
    private EditText postEditText;
    private String OwnerID = "OwnerID not set";
    private String Title = "Title not set";
    private RecyclerView recyclerView;
    private ArrayList<PostingModel> list;
    private AdapterClassForPost adapterClassForPost;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_info);

        title = findViewById(R.id.TitleID);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Title = extras.getString("Title");
            OwnerID = extras.getString("OwnerID");
        }
        title.setText(Title);


        postingDatabase = FirebaseDatabase.getInstance().getReference("PostInPresentationInfo");


        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutID);
        recyclerView = findViewById(R.id.recyclerViewID);
        addBtn = findViewById(R.id.AddbtnId);
        postBtn = findViewById(R.id.postBtnID);
        postEditText = findViewById(R.id.postEditTextID);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), addBtn);
                dropDownMenu.getMenuInflater().inflate(R.menu.add_menu_item, dropDownMenu.getMenu());

                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.UpImgID)
                            Toast.makeText(getApplicationContext(), "Please Upload a Image", Toast.LENGTH_SHORT).show();
                        else if(menuItem.getItemId() == R.id.UpFileID)
                            Toast.makeText(getApplicationContext(), "Please Upload a File", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String post = postEditText.getText().toString().trim();
                if(post.isEmpty()) {
                    postEditText.setError("Post Field is required");
                    postEditText.requestFocus();
                    return;
                }
                postingDatabase.child(OwnerID).child(postingDatabase.push().getKey()).child("Post").setValue(post);
                postEditText.setText("");
            }
        });

        // Recycler View for Post
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        list = new ArrayList<>();
        adapterClassForPost = new AdapterClassForPost(this, list);
        recyclerView.setAdapter(adapterClassForPost);

        postingDatabase.child(OwnerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot DS: snapshot.getChildren()) {
                    PostingModel postingModel = DS.getValue(PostingModel.class);
                    list.add(postingModel);
                }
                adapterClassForPost.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database not found", Toast.LENGTH_SHORT).show();
            }
        });

        // Swipe Refresh Layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapterClassForPost.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}