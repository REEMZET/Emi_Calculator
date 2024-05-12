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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PPFCalc extends Fragment {

    private EditText etDepositAmount, etRateOfInterest;
    private RadioGroup rgDurations;
    private RadioButton rb15Years, rb20Years, rb25Years, rb30Years;
    private Button btnCalculate;
    private TextView tvResult;
    private int duration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_p_p_f_calc, container, false);

        etDepositAmount = view.findViewById(R.id.etDepositAmount);
        etRateOfInterest = view.findViewById(R.id.etRateOfInterest);
        rgDurations = view.findViewById(R.id.rgDurations);
        rb15Years = view.findViewById(R.id.rb15Years);
        rb20Years = view.findViewById(R.id.rb20Years);
        rb25Years = view.findViewById(R.id.rb25Years);
        rb30Years = view.findViewById(R.id.rb30Years);
        btnCalculate = view.findViewById(R.id.btnCalculate);
        tvResult = view.findViewById(R.id.tvResult);
        Toolbar toolbar;
        NavController navController;
        navController = NavHostFragment.findNavController(this);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("PPF Calculate");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());

        TextView tvexplore=view.findViewById(R.id.tvexplore);
        tvexplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double depositAmount = Double.parseDouble(etDepositAmount.getText().toString());
                double rateOfInterest = Double.parseDouble(etRateOfInterest.getText().toString());
                int duration = getSelectedDuration();

// Calculate the PPF values using the function
                List<Double> result = calculatePPF(depositAmount, rateOfInterest, duration);

// Display the result to the user
                tvResult.setText(String.format(Locale.getDefault(),
                        "Investment Amount: ₹%.2f\nTotal Interest: ₹%.2f\nMaturity Value: ₹%.2f",
                        result.get(0), result.get(1), result.get(2)));
            }
        });

        return view;
    }



    private int getSelectedDuration() {
        int selectedId = rgDurations.getCheckedRadioButtonId();

        if (selectedId == rb15Years.getId()) {
            duration = 15;
        } else if (selectedId == rb20Years.getId()) {
            duration = 20;
        } else if (selectedId == rb25Years.getId()) {
            duration = 25;
        } else if (selectedId == rb30Years.getId()) {
            duration = 30;
        } else {
            // Show error message
            Toast.makeText(requireContext(), "Please select a duration.", Toast.LENGTH_LONG).show();
        }

        return duration;
    }

    public static List<Double> calculatePPF(double depositAmount, double rateOfInterest, int duration) {
        // Convert interest rate to decimal
        double r = rateOfInterest / 100.0;

        // Calculate number of times interest is compounded per year
        double n = 12.0; // Monthly compounding

        // Calculate maturity value
        double maturityValue = depositAmount * Math.pow((1 + r / n), (n * duration));

        // Calculate interest earned
        double interestEarned = maturityValue - depositAmount;

        // Return a List containing depositAmount, interestEarned, and maturityValue
        List<Double> result = new ArrayList<>();
        result.add(depositAmount);
        result.add(interestEarned);
        result.add(maturityValue);

        return result;
    }




}
