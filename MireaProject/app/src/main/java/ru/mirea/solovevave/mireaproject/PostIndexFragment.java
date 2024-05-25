package ru.mirea.solovevave.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.solovevave.mireaproject.databinding.FragmentPostIndexBinding;


public class PostIndexFragment extends Fragment {

    FragmentPostIndexBinding binding;

    EditText countryEditText;
    EditText postIndexEditText;
    TextView postInfoTextView;
    Button getPostInfoButton;
    PostIndexsApiService apiService;

    public PostIndexFragment() {
        // Required empty public constructor
    }

    public static PostIndexFragment newInstance() {
        return new PostIndexFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostIndexBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        countryEditText = binding.countryEditText;
        postIndexEditText = binding.postIndexrEditText;
        postInfoTextView = binding.postInfoTextView;
        getPostInfoButton = binding.getPostInfoButton;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.zippopotam.us/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(PostIndexsApiService.class);

        getPostInfoButton.setOnClickListener(v -> {
            String postIndex = postIndexEditText.getText().toString();
            String country = countryEditText.getText().toString();

            if (postIndex.isEmpty() || country.isEmpty()) {
                Toast.makeText(getContext(), "Введите страну и почтовый код", Toast.LENGTH_SHORT).show();
            } else {
                getPostInfo(country, postIndex);
            }
        });


        return view;
    }

    private void getPostInfo(String country, String postIndex) {
        apiService.getPostIndexInfo(country, postIndex).enqueue(new Callback<PostIndexResponse>() {
            @Override
            public void onResponse(Call<PostIndexResponse> call, Response<PostIndexResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PostIndexResponse responseBody = response.body();
                    PostIndexResponse.Place place = responseBody.getPlaces().get(0);
                    StringBuilder info = new StringBuilder();
                    info.append("Country: ").append(responseBody.getCountry()).append("\n")
                            .append("Place Name: ").append(place.getPlaceName()).append("\n")
                            .append("State: ").append(place.getState()).append("\n")
                            .append("Latitude: ").append(place.getLatitude()).append("\n")
                            .append("Longitude: ").append(place.getLongitude()).append("\n");

                    postInfoTextView.setText(info.toString());
                } else {
                    Toast.makeText(getContext(), "Ошибка при получении данных", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostIndexResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }
}