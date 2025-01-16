package com.example.lab02;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView cityList;
    ArrayAdapter<String> cityAdapter;
    ArrayList<String> dataList;
    int selectedPosition;  // -1 means there are no selections
    boolean addingCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedPosition = -1;
        addingCity = false;

        // Create the default city list view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityList = findViewById(R.id.city_list);
        String[] cities = { "Edmonton", "Vancouver", "Berlin", "Seoul", "Moscow", "Sydney", "Tokyo", "Beijing", "Osaka", "New Delhi" };
        dataList = new ArrayList<String>();
        dataList.addAll(Arrays.asList(cities));

        // Create a custom array adapter that highlights the selected city
        cityAdapter = new ArrayAdapter<String>(this, R.layout.content, dataList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // Highlight the selected item in the list view
                TextView textView = (TextView)view;
                if (position == selectedPosition) {
                    // Set highlighting styles
                    textView.setBackgroundColor(
                            ContextCompat.getColor(
                                    MainActivity.this, R.color.city_item_background_highlight));
                    textView.setTextColor(
                            ContextCompat.getColor(
                                    MainActivity.this, R.color.city_item_text_highlight));
                } else {
                    // Reset the highlighting styles
                    textView.setBackgroundColor(
                            ContextCompat.getColor(
                                    MainActivity.this, R.color.city_item_background));
                    textView.setTextColor(
                            ContextCompat.getColor(
                                    MainActivity.this, R.color.city_item_text));
                }

                return view;
            }
        };
        cityList.setAdapter(cityAdapter);

        // Click listeners for each city
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Store the selected position and then update the list view
                selectedPosition = position;
                cityAdapter.notifyDataSetChanged();
            }
        });

        // Delete city button
        Button deleteCityButton = findViewById(R.id.delete_city_button);
        deleteCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the selected city is not -1, delete it, then update the list view
                if (selectedPosition == -1) return;
                dataList.remove(selectedPosition);
                cityAdapter.notifyDataSetChanged();
                selectedPosition = -1;  // Reset the selected position
            }
        });

        // Add city button
        Button addCityButton = findViewById(R.id.add_city_button);
        ConstraintLayout addCityLayout = findViewById(R.id.city_input_layout);
        EditText addCityInputPrompt = findViewById(R.id.city_edit_text_input);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingCity = true;
                addCityInputPrompt.requestFocus();
                addCityLayout.setVisibility(View.VISIBLE);  // Show the prompt layout
            }
        });

        // Confirmation button
        Button confirmAddCityButton = findViewById(R.id.confirm_add_button);
        confirmAddCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addingCity) return;

                // Get the text from the edit text view, then insert it into the data list
                String inputText = addCityInputPrompt.getText().toString();
                dataList.add(inputText);

                // Notify the adapter and reset the edit text view:
                //   Clear focus, hide, reset text, boolean, etc
                cityAdapter.notifyDataSetChanged();
                addCityInputPrompt.setText("");
            }
        });

        // Cancel button
        Button cancelAddCityButton = findViewById(R.id.cancel_add_button);
        cancelAddCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addingCity) return;

                // Hide and reset everything
                addCityInputPrompt.setText("");
                addCityInputPrompt.clearFocus();
                addCityLayout.setVisibility(View.GONE);
                addingCity = false;
            }
        });
    }
}