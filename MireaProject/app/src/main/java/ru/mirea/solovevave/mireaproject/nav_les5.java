package ru.mirea.solovevave.mireaproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link nav_les5#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nav_les5 extends Fragment implements SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private Sensor pressureSensor;

    TextView temperatureText;
    TextView pressureText;
    TextView temperatureAdvice;
    TextView pressureAdvice;

    public nav_les5() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment nav_les5.
     */
    // TODO: Rename and change types and number of parameters
    public static nav_les5 newInstance(String param1, String param2) {
        nav_les5 fragment = new nav_les5();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_les5, container, false);

        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        temperatureText = view.findViewById(R.id.temperText);
        pressureText = view.findViewById(R.id.pressureText);
        temperatureAdvice = view.findViewById(R.id.temperAdvice);
        pressureAdvice = view.findViewById(R.id.pressureAdvice);

        if (temperatureSensor == null)
            temperatureAdvice.setText("Датчик температуры недоступен");
        if (pressureSensor == null)
            pressureAdvice.setText("Датчик давления недоступен");

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String adviceText = "";
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temperature = event.values[0];
            temperatureText.setText("Температура: "+temperature+" °C");
            if (temperature < 5) {
                adviceText += "На улице очень холодно, одевайтесь тепло!";
            } else if (temperature > 5 && temperature < 12) {
                adviceText += "На улице холодно, стоит надеть куртку.";
            } else if (temperature > 12 && temperature < 19) {
                adviceText += "На улице прохладно, стоит взять легкую куртку или теплую кофту.";
            } else if (temperature > 19 && temperature < 25) {
                adviceText += "На улице комфортная температура, идеально для прогулки!";
            } else adviceText += "На улице жарко, надевайте легкую одежду и пейте больше воды.";
            temperatureAdvice.setText(adviceText);
        }
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            float pressure = Math.round(event.values[0] * 0.750064);
            pressureText.setText("Давление: "+pressure+" мм.рт.ст.");
            if (pressure < 755 || pressure > 765) {
                adviceText += "Атмосферное давление изменилось и может сказаться на самочувствии.\n" +
                        "Будьте осторожны, пейте больше воды и не перетруждайтесь.";
            } else adviceText += "Атмосферное давление в норме!";
            pressureAdvice.setText(adviceText);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (temperatureSensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            temperatureAdvice.setText("Получение данных...");
        }
        if (pressureSensor != null) {
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            pressureAdvice.setText("Получение данных...");
        }
    }
}