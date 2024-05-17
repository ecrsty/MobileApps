package ru.mirea.solovevave.mireaproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ru.mirea.solovevave.mireaproject.databinding.FragmentFileWorkerBinding;

public class FileWorker extends Fragment {
    FragmentFileWorkerBinding binding;

    static final int REQUEST_CODE_PERMISSION = 100;
    private TextView textViewFileName;
    private EditText editTextPassword;
    private Uri fileUri;

    ActivityResultLauncher<Intent> filePickerResultLauncher;

    public FileWorker() {
        // Required empty public constructor
    }

    public static FileWorker newInstance() {
        return new FileWorker();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // проверка разрешений
        int storagePermissionStatus = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (storagePermissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFileWorkerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        textViewFileName = binding.textViewFileName;
        editTextPassword = binding.editTextPassword;
        Button selectFileBtn = binding.buttonSelectFile;
        Button encryptBtn = binding.buttonEncrypt;
        Button decryptBtn = binding.buttonDecrypt;
        FloatingActionButton fab = binding.fab;

        filePickerResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback -> {
                    if (callback.getResultCode() == Activity.RESULT_OK && callback.getData() != null) {
                        fileUri = callback.getData().getData();
                        textViewFileName.setText(fileUri.getPath());
            }
        });

        selectFileBtn.setOnClickListener(v -> selectFile());
        encryptBtn.setOnClickListener(v -> encryptFile());
        decryptBtn.setOnClickListener(v -> decryptFile());

        fab.setOnClickListener(v -> showCreateDialog());

        return view;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerResultLauncher.launch(intent);
    }

    private void showCreateDialog() {
        // Создание диалога
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_file, null);
        builder.setView(dialogView)
                .setTitle("Создать файл")
                .setPositiveButton("Сохранить", (dialog, id) -> {
                    EditText editTextFileName = dialogView.findViewById(R.id.editTextFileName);
                    EditText editTextFileContent = dialogView.findViewById(R.id.editTextFileContent);
                    String fileName = editTextFileName.getText().toString();
                    String fileContent = editTextFileContent.getText().toString();
                    if (!fileName.isEmpty() && !fileContent.isEmpty()) {
                        createFile(fileName, fileContent);
                    } else {
                        Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createFile(String fileName, String fileContent) {
        if (!isExternalStorageWritable()) {
            Toast.makeText(getContext(), "Хранилище недоступно", Toast.LENGTH_SHORT).show();
            return;
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
            outWriter.write(fileContent);
            outWriter.close();
            Toast.makeText(getContext(), "Файл сохранен", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.d("OTLADKA", e.toString());
            Toast.makeText(getContext(), "Ошибка при сохранении файла", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getFileNameWithoutExtension(Uri uri) {
        String path = uri.getPath();
        if (path == null) return null;

        // Получаем имя файла из пути
        String fileName = new File(path).getName();

        // Удаляем расширение из имени файла
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            fileName = fileName.substring(0, dotIndex);
        }

        return fileName;
    }

    private void encryptFile() {
        if (fileUri == null) {
            Toast.makeText(getContext(), "Выберите файл", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = editTextPassword.getText().toString();
        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String content = readFile(fileUri);
            Log.d("OTLADKA", "content: "+content);
            String encryptedContent = encrypt(content, password);
            Log.d("OTLADKA", "encryptedContent: "+encryptedContent);
            Log.d("OTLADKA", "path: "+fileUri.getPath());
//            writeFile(fileUri, encryptedContent);
            createFile(getFileNameWithoutExtension(fileUri)+"_encrypted.txt", encryptedContent);
            Toast.makeText(getContext(), "Файл зашифрован", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка шифрования", Toast.LENGTH_SHORT).show();
        }
    }

    private void decryptFile() {
        if (fileUri == null) {
            Toast.makeText(getContext(), "Выберите файл", Toast.LENGTH_SHORT).show();
            return;
        }
        String password = editTextPassword.getText().toString();
        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String content = readFile(fileUri);
            String decryptedContent = decrypt(content, password);
            createFile(getFileNameWithoutExtension(fileUri)+"_decrypted.txt", decryptedContent);
            Toast.makeText(getContext(), "Файл дешифрован", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("OTLADKA", "Ошибка дешифрования: "+ e.toString());
            Toast.makeText(getContext(), "Ошибка дешифрования", Toast.LENGTH_SHORT).show();
        }
    }

    private String readFile(Uri uri) throws Exception {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getContext().getContentResolver().openInputStream(uri)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private String encrypt(String data, String password) {
        Cipher cipher;
        SecretKey skey = generateKey(password);
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    private String decrypt(String data, String password) {
        SecretKey skey = generateKey(password);
        Log.d("OTLADKA", "skey: "+skey);
        try{
            Cipher cipher = Cipher.getInstance("AES");
            Log.d("OTLADKA", "cipher: "+cipher);
            cipher.init(Cipher.DECRYPT_MODE, skey);
            byte[] decryptedBytes = cipher.doFinal(Base64.decode(data, Base64.DEFAULT));
            Log.d("OTLADKA", "decryptedBytes: "+decryptedBytes);
            return new String(decryptedBytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException | IllegalBlockSizeException e) {
            Log.d("OTLADKA", "Ошибка дешифрования decrypt(): "+e.toString());
            throw new RuntimeException(e);
        }
    }

    private SecretKey generateKey(String password){
        try{
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(password.getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            return new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        }catch (NoSuchAlgorithmException e) {
            Log.d("OTLADKA", "Ошибка создания ключа: "+e.toString());
            throw new RuntimeException(e);
        }
    }
}