package com.developer_rahul.agrideal;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BuyerProfileActivity extends AppCompatActivity {

    private EditText etName, etBusinessName, etGstNumber;
    private Spinner spinner1, spinner2, spinner3;
    private Button btnEditData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_profile);

        etName = findViewById(R.id.etName);
        etBusinessName = findViewById(R.id.etBusinessName);
        etGstNumber = findViewById(R.id.etGstNumber);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        btnEditData = findViewById(R.id.btnEditData);

        String[] spinnerItems = {"Apple","Bajra (Pearl Millet)", "Banana", "Barley", "Black Gram (Urad)", "Chickpeas", "Chillies", "Chiku (Sapodilla)", "Coconut", "Coffee", "Cotton", "Custard Apple (Sitaphal)", "Garlic", "Ginger", "Grapes", "Green Gram (Moong)", "Groundnuts (Peanuts)", "Guava", "Jackfruit", "Jowar (Sorghum)", "Lemon", "Lentils", "Litchi", "Maize (Corn)", "Mango", "Muskmelon", "Mustard", "Onions", "Orange", "Papaya", "Peach", "Pineapple", "Plum", "Pomegranate", "Potatoes", "Rice", "Rubber", "Sesame", "Soybeans", "Strawberry", "Sugarcane", "Sunflower", "Tamarind", "Tea", "Tomatoes", "Tur (Arhar/Pigeon Pea)", "Watermelon", "Wheat"};

        setSpinnerData(spinner1, spinnerItems);
        setSpinnerData(spinner2, spinnerItems);
        setSpinnerData(spinner3, spinnerItems);

        toggleEditable(false);

        btnEditData.setOnClickListener(view -> {
            String buttonText = btnEditData.getText().toString();
            if (buttonText.equals("Edit")) {
                toggleEditable(true);
                btnEditData.setText("Update");
            } else if (buttonText.equals("Update")) {
                saveData();
                toggleEditable(false);
                btnEditData.setText("Edit");
            }
        });
    }

    private void setSpinnerData(@NonNull Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void toggleEditable(boolean isEditable) {
        etName.setEnabled(isEditable);
        etBusinessName.setEnabled(isEditable);
        etGstNumber.setEnabled(isEditable);
        spinner1.setEnabled(isEditable);
        spinner2.setEnabled(isEditable);
        spinner3.setEnabled(isEditable);
    }

    private void saveData() {
        // Logic to save updated data (e.g., send to server or store locally)
    }
}