package ru.mirea.solovevave.mireaproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.mirea.solovevave.mireaproject.databinding.FragmentNavProfileBinding;
import ru.mirea.solovevave.mireaproject.databinding.FragmentRecorderBinding;

public class nav_profile extends Fragment {
    FragmentNavProfileBinding binding;

    static final int REQUEST_CODE_PERMISSION = 100;
    boolean isWork = false;
    Uri imageUri;

    String fileName = "user_data.txt";
    String imageUriKey = "image_uri";
    EditText name;
    EditText age;
    EditText bday;
    EditText email;
    EditText phone;

    public nav_profile() {
        // Required empty public constructor
    }

    public static nav_profile newInstance() {
        return new nav_profile();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // проверка текущего статуса разрешений
        int cameraPermissionStatus = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            // запрос разрешения
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentNavProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ImageView profilePhoto = binding.imProfile;
        name = binding.editTextName;
        age = binding.editTextAge;
        bday = binding.editTextDate;
        email = binding.editTextTextEmailAddress;
        phone = binding.editTextPhone;

        loadData();

        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    profilePhoto.setImageURI(imageUri);
                    // Сохраняем URI изображения в SharedPreferences
                    saveImageUri(imageUri);
                }
            }
        };
        ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), callback);

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String authorities = requireContext().getPackageName() + ".fileprovider";
                imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                cameraActivityResultLauncher.launch(cameraIntent);
            }
        });

        binding.saveBtn.setOnClickListener(v -> saveData());

        return view;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isWork = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void saveData() {
        try (FileOutputStream fos = requireActivity().openFileOutput(fileName, Context.MODE_PRIVATE)) {
            String data = name.getText().toString() + "\n" +
                    age.getText().toString() + "\n" +
                    bday.getText().toString() + "\n" +
                    email.getText().toString() + "\n" +
                    phone.getText().toString();
            fos.write(data.getBytes());
            Toast.makeText(getContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка сохранения данных", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        try (FileInputStream fis = requireActivity().openFileInput(fileName);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {
            String nameText = reader.readLine();
            String ageText = reader.readLine();
            String bdayText = reader.readLine();
            String emailText = reader.readLine();
            String phoneText = reader.readLine();

            if (nameText != null) name.setText(nameText);
            if (ageText != null) age.setText(ageText);
            if (bdayText != null) bday.setText(bdayText);
            if (emailText != null) email.setText(emailText);
            if (phoneText != null) phone.setText(phoneText);

            // Загрузка URI изображения из SharedPreferences
            SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
            String savedUriString = sharedPref.getString(imageUriKey, null);
            if (savedUriString != null) {
                imageUri = Uri.parse(savedUriString);
                binding.imProfile.setImageURI(imageUri);
            }
            Toast.makeText(getContext(), "Данные загружены", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageUri(Uri uri) {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(imageUriKey, uri.toString());
        editor.apply();
    }
}