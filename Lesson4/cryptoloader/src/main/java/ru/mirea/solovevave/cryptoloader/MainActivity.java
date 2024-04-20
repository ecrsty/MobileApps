package ru.mirea.solovevave.cryptoloader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ru.mirea.solovevave.cryptoloader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    ActivityMainBinding binding;
    public final String TAG = this.getClass().getSimpleName();
    private final int LoaderID = 5848;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(v -> onClickButton(v));
    }

    public void onClickButton(View view) {
        String userText = binding.userText.getText().toString();
        SecretKey skey = generateKey();
        byte[] encMessage = encryptMsg(userText, skey);
        Bundle bundle = new Bundle();
        bundle.putByteArray(MyLoader.ARG_MSG, encMessage);
        bundle.putByteArray(MyLoader.ARG_KEY, skey.getEncoded());
        LoaderManager.getInstance(this).initLoader(LoaderID, bundle, this);
    }

    public static SecretKey generateKey(){
        try{
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("gdkqHJh43u5FRcyr9JHec4FTiY87gGYYg".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            return new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptMsg(String message, SecretKey secret) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return cipher.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader<String> loader){
        Log.d(TAG, "onLoaderReset");
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @NonNull Bundle bundle){
        if (id == LoaderID){
            Log.d(TAG, "Loader " + id + " was created");
            Toast.makeText(this, "Loader " + id + " was created", Toast.LENGTH_LONG).show();
            return new MyLoader(this, bundle);
        }
        throw new InvalidParameterException("Invalid loader id");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        if (loader.getId() == LoaderID){
            Log.d(TAG, "Result: " + s);
            Toast.makeText(this, "Result: " + s, Toast.LENGTH_SHORT).show();
            LoaderManager.getInstance(this).destroyLoader(LoaderID);
        }
    }
}