package com.example.resolute_ai;

import androidx.appcompat.app.AppCompatActivity;

//import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthProvider;// Adjust this to your actual package name
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.FirebaseException;
import java.util.concurrent.TimeUnit;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private EditText phoneNumberInput;
    private Button sendOtpButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberInput = findViewById(R.id.phone_number);
        sendOtpButton = findViewById(R.id.send_otp);
        mAuth = FirebaseAuth.getInstance();

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phoneNumberInput.getText().toString();
                if (!phoneNumber.isEmpty()) {
                    sendOtp(phoneNumber);
                } else {
                    Toast.makeText(MainActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendOtp(String phoneNumber) {
        // Implement OTP sending logic using Firebase
        // Navigate to OTP verification activity
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        // Automatically verified
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(MainActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        // Code sent
                        Intent intent = new Intent(MainActivity.this, OtpActivity.class);
                        intent.putExtra("phoneNumber", phoneNumber);
                        intent.putExtra("verificationId", verificationId); // Pass verification ID to OtpActivity
                        startActivity(intent);
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        Toast.makeText(MainActivity.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                        // Update UI with the signed-in user's information
                        FirebaseUser user = task.getResult().getUser();
                        // You can start a new activity or update the UI here
                    } else {
                        // Sign in failed
                        Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(MainActivity.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    }