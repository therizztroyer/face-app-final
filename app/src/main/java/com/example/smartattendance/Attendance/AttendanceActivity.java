package com.example.smartattendance.Attendance;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.smartattendance.Adapters.AttendanceAdapter;
import com.example.smartattendance.FaceRecognition.FaceRecogntionActivity;
import com.example.smartattendance.MainActivity;
import com.example.smartattendance.Model.UploadFile;
import com.example.smartattendance.R;
import com.example.smartattendance.databinding.ActivityAttendanceBinding;
import com.example.smartattendance.db.DBHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

   private ActivityAttendanceBinding binding;
    private DBHelper dbHelper;
    DatabaseReference  databaseReference ;
    List<UploadFile> uploadFiles;
    AttendanceAdapter adapter;
    private FirebaseDatabase database;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.shimmer.startShimmer();
        uploadFiles = new ArrayList<>();
        mauth=FirebaseAuth.getInstance();
        viewAllFiles();
        adapter = new AttendanceAdapter(this,uploadFiles);
        dbHelper = new DBHelper(this);
        database = FirebaseDatabase.getInstance();
        binding.listView.setAdapter(adapter);
        binding.listView.setLayoutManager(new LinearLayoutManager(this));
        binding.listView.setHasFixedSize(true);
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceActivity.this, FaceRecogntionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void viewAllFiles() {
        binding.emptyView.setVisibility(View.GONE);
        binding.shimmer.startShimmer();
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.listView.setVisibility(View.VISIBLE);
        String UID=mauth.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("uploads").child(UID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadFiles.clear();
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    UploadFile uploadFile = postSnapshot.getValue(UploadFile.class);
                    uploadFile.getData(postSnapshot.getKey());
                    uploadFiles.add(uploadFile);
                }
                if(uploadFiles.size()==0){
                    binding.shimmer.stopShimmer();;
                    binding.emptyView.setVisibility(View.VISIBLE);
                }else{
                    binding.emptyView.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                binding.shimmer.stopShimmer();
                binding.shimmer.setVisibility(View.GONE);
                Toast.makeText(AttendanceActivity.this, "Data Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.attendance_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete:

                database.getReference().child("uploads").child(mauth.getUid()).removeValue();
                dbHelper.deleteAll();
                binding.emptyView.setVisibility(View.VISIBLE);
                Toast.makeText(this, "All Attendance deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.export_xl:
                Log.d("todo","todo");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }


}