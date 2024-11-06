package com.example.resolute_ai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpActivity extends AppCompatActivity {
    private EditText otpInput;
    private Button verifyButton;
    private FirebaseAuth mAuth;
    private String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otpInput = findViewById(R.id.otp_input);
        verifyButton = findViewById(R.id.verify_otp);
        mAuth = FirebaseAuth.getInstance();

        String phoneNumber = getIntent(). getStringExtra("phoneNumber");
        verificationId = getIntent().getStringExtra("verificationId");
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = otpInput.getText().toString();
                if (!otp.isEmpty()) {
                    verifyOtp(phoneNumber, otp);
                } else {
                    Toast.makeText(OtpActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyOtp(String phoneNumber, String otp) {
        // Implement OTP verification logic using Firebase
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Navigate to Profile Activity
                        Intent intent = new Intent(OtpActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(OtpActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}