package com.developer_rahul.agrideal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class BudgetTrackingActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_tracking);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Button btnAddActivity = findViewById(R.id.btn_add_activity);

        btnAddActivity.setOnClickListener(view -> showAddActivityDialog());
    }

    private void showAddActivityDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_activity_content, null);

        Spinner spinnerActivity = dialogView.findViewById(R.id.spinner_activity);
        EditText editTextAmount = dialogView.findViewById(R.id.et_amount);
        EditText editTextNote = dialogView.findViewById(R.id.et_note);
        TextView tvSelectedDate = dialogView.findViewById(R.id.tv_selected_date);
        Button btnAdd = dialogView.findViewById(R.id.btn_add);

        String[] activities = {"Land Prep", "Sowing", "Irrigation", "Fertilizing", "Weeding", "Pest Control", "Pruning", "Harvesting", "Post-Harvest", "Crop Rotation", "Soil Testing", "Seed Saving", "Fencing", "Marketing", "Animal Feeding", "Health Check", "Breeding", "Housing Cleanup", "Milk Collection"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                activities
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivity.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();
        dialog.show();

        tvSelectedDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year1, month1, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        tvSelectedDate.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        btnAdd.setOnClickListener(v -> {
            String selectedActivity = spinnerActivity.getSelectedItem().toString();
            String enteredAmount = editTextAmount.getText().toString();
            String enteredNote = editTextNote.getText().toString();
            String selectedDate = tvSelectedDate.getText().toString();

            if (enteredAmount.isEmpty() || selectedDate.equals("Select a date")) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "unknown_user");

                DatabaseReference userBudgetRef = databaseReference.child("Users").child(username).child("Budget").child(selectedActivity);

                // Add data under the activity node
                userBudgetRef.child("Amount").setValue(enteredAmount);
                userBudgetRef.child("Note").setValue(enteredNote);
                userBudgetRef.child("Date").setValue(selectedDate);

                // Display success message and dismiss dialog
                Toast.makeText(this, "Activity added successfully!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}