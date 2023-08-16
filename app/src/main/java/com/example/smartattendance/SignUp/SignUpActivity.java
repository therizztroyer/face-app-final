package com.example.smartattendance.SignUp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.video.VideoRecordEvent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.smartattendance.MainActivity;
import com.example.smartattendance.Model.Users;
import com.example.smartattendance.SignIn.SignInActivity;
import com.example.smartattendance.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView (binding.getRoot());
        getSupportActionBar().hide();
        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setTitle("Creating Account");
        dialog.setMessage("Creating your Account");

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //sign up
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String name = binding.editTextName.getText().toString();
                String email = binding.editTextEmail.getText().toString();
                String phone = binding.editTextMobile.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                // TODO add validation
                if(name.isEmpty()||email.isEmpty()||phone.isEmpty()||password.isEmpty()){
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Fill up the fields", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    dialog.dismiss();
                                    if(task.isSuccessful()){
                                        Users users = new Users(name,email,phone,password);
                                        String  id = task . getResult (). getUser (). getUid ();
                                        database.getReference().child("Users").child(id).setValue(users);

                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);

                                        Toast.makeText(SignUpActivity.this, "Account Created Successfully",
                                                Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                Intent i=new Intent(getBaseContext(),SignInActivity.class);
                startActivity(i);



            }
        });


    }

}