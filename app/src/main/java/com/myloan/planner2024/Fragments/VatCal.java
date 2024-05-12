package com.myloan.planner2024.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import com.myloan.planner2024.ChromeUtils;
import com.myloan.planner2024.R;

import java.text.DecimalFormat;

public class VatCal extends Fragment {

    private EditText etAmount, etOtherVATRate;
    private RadioGroup rgVAT;
    private RadioButton rbVAT1, rbVAT4, rbVAT5, rbVAT12_5, rbVATOther;
    private Spinner spinnerVATAction;
    private Button btnCalculateVAT;

    WebView webView;

    Toolbar toolbar;
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vat_cal, container, false);

        etAmount = view.findViewById(R.id.etAmount);
        etOtherVATRate = view.findViewById(R.id.etOtherVATRate);
        rgVAT = view.findViewById(R.id.rgVAT);
        rbVAT1 = view.findViewById(R.id.rbVAT1);
        rbVAT4 = view.findViewById(R.id.rbVAT4);
        rbVAT5 = view.findViewById(R.id.rbVAT5);
        rbVAT12_5 = view.findViewById(R.id.rbVAT12_5);
        rbVATOther = view.findViewById(R.id.rbVATOther);
        spinnerVATAction = view.findViewById(R.id.spinnerVATAction);
        btnCalculateVAT = view.findViewById(R.id.btnCalculateVAT);

        navController = NavHostFragment.findNavController(this);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("VAT Calculate");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());
        webView= view.findViewById(R.id.webview);

        TextView tvexplore=view.findViewById(R.id.tvexplore);
        tvexplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });
        // Set up the spinner with options for VAT action
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.vat_actions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVATAction.setAdapter(adapter);

        // Set up the listener for the VAT radio group
        rgVAT.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                handleVATRadioButton(checkedId);
            }
        });

        // Set up the listener for the spinner
        spinnerVATAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item in the spinner
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Set up the listener for the Calculate VAT button
        btnCalculateVAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateVAT();
            }
        });

        return view;
    }

    private void handleVATRadioButton(int checkedId) {
        // Show or hide the 'Other' VAT rate based on the selected radio button
        etOtherVATRate.setVisibility(checkedId == R.id.rbVATOther ? View.VISIBLE : View.GONE);
    }

    private void calculateVAT() {
        try {
            double originalCost = Double.parseDouble(etAmount.getText().toString());
            double vatRate = getSelectedVATRate();

            // Calculate VAT values
            double vatPrice = (originalCost * vatRate) / 100.0;
            double netPrice;

            // Determine the VAT action based on the selected spinner item
            String vatAction = spinnerVATAction.getSelectedItem().toString();

            if (vatAction.equals("Add VAT")) {
                netPrice = originalCost + vatPrice;
            } else {
                netPrice = originalCost - vatPrice;
            }

            // Display the result
           displayResultInWebView(originalCost, vatPrice, netPrice);

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid input. Please enter valid values.", Toast.LENGTH_LONG).show();
        }
    }

    private double getSelectedVATRate() {
        int selectedId = rgVAT.getCheckedRadioButtonId();
        double vatRate = 0.0;

        switch (selectedId) {
            case R.id.rbVAT1:
                vatRate = 1.0;
                break;
            case R.id.rbVAT4:
                vatRate = 4.0;
                break;
            case R.id.rbVAT5:
                vatRate = 5.0;
                break;
            case R.id.rbVAT12_5:
                vatRate = 12.5;
                break;
            case R.id.rbVATOther:
                vatRate = Double.parseDouble(etOtherVATRate.getText().toString());
                break;
        }

        return vatRate;
    }

    private void displayResultInWebView(double originalCost, double vatPrice, double netPrice) {
        DecimalFormat currencyFormat = new DecimalFormat("₹#.00");

        String htmlTable = "<table border='1' style='width:80%;margin:auto;'>" +
                "<tr><th style='text-align:center;'>Category</th><th style='text-align:center;'>Amount</th></tr>" +
                "<tr><td style='text-align:center;'>Original Cost</td><td style='text-align:center;'>" + currencyFormat.format(originalCost) + "</td></tr>" +
                "<tr><td style='text-align:center;'>VAT Price</td><td style='text-align:center;'>" + currencyFormat.format(vatPrice) + "</td></tr>" +
                "<tr><td style='text-align:center;'>Net Price</td><td style='text-align:center;'>" + currencyFormat.format(netPrice) + "</td></tr>" +
                "</table>";

        String resultHtml = "<html><body style='text-align:center;'>" + htmlTable + "</body></html>";

        // Assuming you have a WebView in your XML layout with the id 'webViewResult'
        webView.loadData(resultHtml, "text/html", "UTF-8");
    }


}
