package com.developer_rahul.agrideal.ProfileFragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.developer_rahul.agrideal.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FarmerProfileFragment extends Fragment {

    private EditText etName, etArea;
    private Spinner spinnerWaterSource, spinnerCrop1, spinnerCrop2, spinnerCrop3;
    private Button btnEditData;
    private DatabaseReference mDatabase;

    private boolean isEditable = false;

    public FarmerProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farmer_profile, container, false);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("FarmerProfiles");

        etName = view.findViewById(R.id.etName);
        etArea = view.findViewById(R.id.etarea);
        spinnerWaterSource = view.findViewById(R.id.watersource);
        spinnerCrop1 = view.findViewById(R.id.spinner1);
        spinnerCrop2 = view.findViewById(R.id.spinner2);
        spinnerCrop3 = view.findViewById(R.id.spinner3);
        btnEditData = view.findViewById(R.id.btnEditData);

        // String arrays for water sources and crops
        String[] waterSources = {
                "River", "Canal", "Tube Well", "Open Well", "Pond", "Borewell", "Tank", "Stream", "Drip Irrigation System"
        };

        String[] crops = {"Apple", "Bajra (Pearl Millet)", "Banana", "Barley", "Black Gram (Urad)", "Chickpeas", "Chillies",
                "Chiku (Sapodilla)", "Coconut", "Coffee", "Cotton", "Custard Apple (Sitaphal)", "Garlic", "Ginger",
                "Grapes", "Green Gram (Moong)", "Groundnuts (Peanuts)", "Guava", "Jackfruit", "Jowar (Sorghum)",
                "Lemon", "Lentils", "Litchi", "Maize (Corn)", "Mango", "Muskmelon", "Mustard", "Onions", "Orange",
                "Papaya", "Peach", "Pineapple", "Plum", "Pomegranate", "Potatoes", "Rice", "Rubber", "Sesame", "Soybeans",
                "Strawberry", "Sugarcane", "Sunflower", "Tamarind", "Tea", "Tomatoes", "Tur (Arhar/Pigeon Pea)",
                "Watermelon", "Wheat"};

        // Set data to spinners
        setSpinnerData(spinnerWaterSource, waterSources);
        setSpinnerData(spinnerCrop1, crops);
        setSpinnerData(spinnerCrop2, crops);
        setSpinnerData(spinnerCrop3, crops);

        // Initially set fields as non-editable
        toggleEditable(false);

        // Button click listener for Edit and Update functionality
        btnEditData.setOnClickListener(v -> {
            if (!isEditable) {
                toggleEditable(true);
                btnEditData.setText("Update");
            } else {
                saveData();
                toggleEditable(false);
                btnEditData.setText("Edit");
            }
        });

        return view;
    }

    // Set spinner data
    private void setSpinnerData(@NonNull Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // Toggle editable state for input fields
    private void toggleEditable(boolean isEditable) {
        this.isEditable = isEditable;
        etName.setEnabled(isEditable);
        etArea.setEnabled(isEditable);
        spinnerWaterSource.setEnabled(isEditable);
        spinnerCrop1.setEnabled(isEditable);
        spinnerCrop2.setEnabled(isEditable);
        spinnerCrop3.setEnabled(isEditable);
    }

    private void saveData() {
        String name = etName.getText().toString().trim();
        String area = etArea.getText().toString().trim();
        String selectedWaterSource = spinnerWaterSource.getSelectedItem().toString();
        String crop1 = spinnerCrop1.getSelectedItem().toString();
        String crop2 = spinnerCrop2.getSelectedItem().toString();
        String crop3 = spinnerCrop3.getSelectedItem().toString();

        // Validate input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(area)) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a HashMap to store data
        FarmerProfile farmerProfile = new FarmerProfile(name, area, selectedWaterSource, crop1, crop2, crop3);

        // Store data in Firebase Realtime Database
        mDatabase.child(name).setValue(farmerProfile)
                .addOnSuccessListener(aVoid -> Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to update", Toast.LENGTH_SHORT).show());
    }

    // FarmerProfile Model Class
    public static class FarmerProfile {
        public String name, area, waterSource, crop1, crop2, crop3;

        public FarmerProfile() {
        }

        public FarmerProfile(String name, String area, String waterSource, String crop1, String crop2, String crop3) {
            this.name = name;
            this.area = area;
            this.waterSource = waterSource;
            this.crop1 = crop1;
            this.crop2 = crop2;
            this.crop3 = crop3;
        }
    }
}
