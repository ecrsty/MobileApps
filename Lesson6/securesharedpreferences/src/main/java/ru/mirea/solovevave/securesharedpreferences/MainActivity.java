package ru.mirea.solovevave.securesharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

import ru.mirea.solovevave.securesharedpreferences.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
    String mainKeyAlias;

    SharedPreferences sp;
    final String SP_NAME = "MySecretSharedPreferences";
    final String KEY_POET = "poet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
            sp = EncryptedSharedPreferences.create(
                    SP_NAME,
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            binding.poetName.setText(sp.getString(KEY_POET, ""));
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        binding.saveBtn.setOnClickListener(v -> onSaveBtnClick(v));
    }

    public void onSaveBtnClick(View view) {
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_POET, binding.poetName.getText().toString());
        editor.apply();
        Toast.makeText(this, "Данные сохранены!", Toast.LENGTH_SHORT).show();
    }
}