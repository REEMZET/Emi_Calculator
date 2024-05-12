package com.myloan.planner2024.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.myloan.planner2024.ChromeUtils;
import com.myloan.planner2024.R;

public class SICICalculate extends Fragment {

    private EditText etPrincipalAmount, etRateOfInterest, etTimePeriod;
    private RadioGroup radioInterestType, radioGroup;
    private RadioButton rbSimpleInterest, rbCompoundInterest, rbMonthly, rbQuarterly, rbHalfYearly, rbYearly;
    private Button btnCalculate;
    private TextView tvResult;
    Toolbar toolbar;
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_s_i_c_i_calculate, container, false);
        navController = NavHostFragment.findNavController(this);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Simple and Compound Calculate");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());

        // Initialize views
        etPrincipalAmount = view.findViewById(R.id.etPrincipalAmount);
        etRateOfInterest = view.findViewById(R.id.etRateOfInterest);
        etTimePeriod = view.findViewById(R.id.etTimePeriod);
        radioInterestType = view.findViewById(R.id.radioInterestType);
        radioGroup = view.findViewById(R.id.radioGroup);
        rbSimpleInterest = view.findViewById(R.id.rbSimpleInterest);
        rbCompoundInterest = view.findViewById(R.id.rbCompoundInterest);
        rbMonthly = view.findViewById(R.id.rbMonthly);
        rbQuarterly = view.findViewById(R.id.rbQuarterly);
        rbHalfYearly = view.findViewById(R.id.rbHalfYearly);
        rbYearly = view.findViewById(R.id.rbYearly);
        btnCalculate = view.findViewById(R.id.btnCalculate);
        tvResult = view.findViewById(R.id.tvResult);

        // Set click listener for the calculate button
        btnCalculate.setOnClickListener(v -> calculateInterest());

        TextView tvexplore=view.findViewById(R.id.tvexplore);
        tvexplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });
        return view;
    }

    private void calculateInterest() {
        try {
            double principalAmount = Double.parseDouble(etPrincipalAmount.getText().toString());
            double rateOfInterest = Double.parseDouble(etRateOfInterest.getText().toString());
            double timePeriod = Double.parseDouble(etTimePeriod.getText().toString());

            double result;

            if (radioInterestType.getCheckedRadioButtonId() == rbSimpleInterest.getId()) {
                // Simple Interest Calculation
                result = calculateSimpleInterest(principalAmount, rateOfInterest, timePeriod);
            } else {
                // Compound Interest Calculation
                int compoundingFrequency = getCompoundingFrequency();
                result = calculateCompoundInterest(principalAmount, rateOfInterest, timePeriod, compoundingFrequency);
            }

            // Display the result
            tvResult.setText("Calculated Result: ₹" + String.format("%.2f", result) + "\nTotal Amount: ₹" + String.format("%.2f", principalAmount + result));


        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid input. Please enter valid values.", Toast.LENGTH_LONG).show();
        }
    }

    private double calculateSimpleInterest(double principal, double rate, double time) {
        return (principal * rate * time) / 100;
    }

    private double calculateCompoundInterest(double principal, double rate, double time, int compoundingFrequency) {
        double compoundInterest = principal * Math.pow(1 + (rate / (100 * compoundingFrequency)), compoundingFrequency * time) - principal;
        return compoundInterest;
    }

    private int getCompoundingFrequency() {
        int compoundingFrequency = 1; // Default to yearly

        if (rbMonthly.isChecked()) {
            compoundingFrequency = 12;
        } else if (rbQuarterly.isChecked()) {
            compoundingFrequency = 4;
        } else if (rbHalfYearly.isChecked()) {
            compoundingFrequency = 2;
        }

        return compoundingFrequency;
    }
}
