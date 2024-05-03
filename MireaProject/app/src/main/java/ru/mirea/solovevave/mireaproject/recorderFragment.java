package ru.mirea.solovevave.mireaproject;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import ru.mirea.solovevave.mireaproject.databinding.FragmentRecorderBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link recorderFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class recorderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentRecorderBinding binding;

    static final int REQUEST_CODE_PERMISSION = 200;
    boolean isWork;
    final String TAG = "recorderFragment";

    String fileName = null;
    String recordFilePath;
    Button startBtn;
    Button playBtn;
    TextView processText;
    MediaPlayer mediaPlayer;
    MediaRecorder recorder;
    boolean isStartRecording = true;
    boolean isStartPlaying = true;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment recorderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static recorderFragment newInstance(String param1, String param2) {
        recorderFragment fragment = new recorderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public recorderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_recorder, container, false);
        binding = FragmentRecorderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        processText = binding.processText;

        // инициализация кнопок записи и воспроизведения
        startBtn = binding.startBtn;
        playBtn = binding.playBtn;
        playBtn.setEnabled(false);
        recordFilePath = (new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordnote.3gp")).getAbsolutePath();

        // проверка разрешений
        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            // запросить у пользователя разрешения
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartRecording) {
                    processText.setText("Идет запись...");
                    startBtn.setText("Остановить запись");
                    playBtn.setEnabled(false);
                    startRecording();
                } else {
                    processText.setText("Записано!");
                    startBtn.setText("Записать");
                    playBtn.setEnabled(true);
                    stopRecording();
                }
                isStartRecording = !isStartRecording;
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartPlaying) {
                    processText.setText("Воспроизведение...");
                    playBtn.setText("Остановить воспроизведение");
                    startBtn.setEnabled(false);
                    startPlaying();
                } else {
                    processText.setText("Можете послушать еще раз или записать новую заметку");
                    playBtn.setText("Воспроизвести");
                    startBtn.setEnabled(true);
                    stopPlaying();
                }
                isStartPlaying = !isStartPlaying;
            }
        });

        return root;
    }

    // проверка полученного результата на запрос разрешения диктофона и внутреннего хранилища
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                isWork = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() in startRecording() failed");
            throw new RuntimeException(e);
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void startPlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(recordFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() in startPlaying() failed");
            throw new RuntimeException(e);
        }
    }

    private void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}