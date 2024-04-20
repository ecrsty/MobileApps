package ru.mirea.solovevave.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {
    public static final String ARG_MSG = "cipherText";
    public static final String ARG_KEY = "key";
    private final SecretKey skey;
    private final byte[] cipherText;

    public MyLoader(@NonNull Context context, @Nullable Bundle args){
        super(context);
        if (args != null) {
            final byte[] decoded_key = args.getByteArray(ARG_KEY);
            skey = new SecretKeySpec(decoded_key, 0, decoded_key.length, "AES");
            cipherText = args.getByteArray(ARG_MSG);
        } else {
            skey = null;
            cipherText = null;
        }
    }

    @Override
    protected void onStartLoading(){
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground(){
        SystemClock.sleep(5000);
        return decryptMsg(cipherText, skey);
    }

    public static String decryptMsg(byte[] cipherText, SecretKey skey) {
        try{
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            return new String(cipher.doFinal(cipherText));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
}
