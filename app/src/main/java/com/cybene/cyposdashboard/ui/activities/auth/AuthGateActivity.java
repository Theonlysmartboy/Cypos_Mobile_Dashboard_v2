package com.cybene.cyposdashboard.ui.activities.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.cybene.cyposdashboard.ui.activities.MenuActivity;

import java.util.concurrent.Executor;

public class AuthGateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This activity doesn't need a layout as it's a logic gate, 
        // but we can set a themed background if desired.
        
        checkAuthentication();
    }

    private void checkAuthentication() {
        BiometricManager biometricManager = BiometricManager.from(this);
        int canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.BIOMETRIC_WEAK);

        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            showBiometricPrompt();
        } else {
            // Fallback to PIN if biometric not available/enrolled
            launchLockActivity();
        }
    }

    private void showBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(AuthGateActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // User cancelled or error occurred -> Fallback to PIN
                launchLockActivity();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivity(new Intent(AuthGateActivity.this, MenuActivity.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Failed attempt - BiometricPrompt handles retries automatically
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("App Authentication")
                .setSubtitle("Use biometric to unlock CyPOS Dashboard")
                .setNegativeButtonText("Use PIN")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void launchLockActivity() {
        Intent intent = new Intent(this, LockActivity.class);
        // No special flags needed as per requirements
        startActivity(intent);
        finish();
    }
}
