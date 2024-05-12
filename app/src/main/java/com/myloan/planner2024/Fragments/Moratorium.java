package com.myloan.planner2024.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;


import com.myloan.planner2024.ChromeUtils;
import com.myloan.planner2024.R;

public class Moratorium extends Fragment {
    private Spinner optionsSpinner;
    private EditText editTextPrincipal;
    private EditText editTextLoanTenureMonths;
    private EditText editTextInterestRate;
    private EditText etinstallmentpaid;
    private EditText etmoratoriumperiod;
    private WebView webview;
    private Button btnCalculateMoratorium,btnreset;
    private Switch switchview;

    private String selectedOption;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moratorium, container, false);

        optionsSpinner = view.findViewById(R.id.optionsSpinner);
        editTextPrincipal = view.findViewById(R.id.editTextPrincipal);
        editTextLoanTenureMonths = view.findViewById(R.id.editTextLoanTenure);
        editTextInterestRate = view.findViewById(R.id.editTextInterestRate);
        etinstallmentpaid = view.findViewById(R.id.etinstallmentpaid);
        etmoratoriumperiod = view.findViewById(R.id.etmoratoriumperiod);
        webview=view.findViewById(R.id.webview);
        btnCalculateMoratorium = view.findViewById(R.id.btnCAlcmoratorium);
        switchview = view.findViewById(R.id.switchmonth);
        btnreset=view.findViewById(R.id.btnreset);
        Toolbar toolbar;
        NavController navController;
        navController = NavHostFragment.findNavController(this);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Moratorium Calculate");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());

       

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);
        TextView tvexplore=view.findViewById(R.id.tvexplore);
        tvexplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });
        optionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedOption = parentView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });

        switchview.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                switchview.setText("Month");
            } else {
                switchview.setText("Year");
            }
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear input fields
                editTextPrincipal.getText().clear();
                editTextLoanTenureMonths.getText().clear();
                editTextInterestRate.getText().clear();
                etinstallmentpaid.getText().clear();
                etmoratoriumperiod.getText().clear();

                // Clear WebView
                webview.loadDataWithBaseURL(null, "", "text/html", "UTF-8", null);
            }
        });


        btnCalculateMoratorium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedOption != null) {
                    if (selectedOption.equals("No Change in Monthly EMI")) {
                        if (!switchview.isChecked()) {
                            calculateMoratoriumNoChangeInEMIMonths();
                        } else {
                            calculateMoratoriumNoChangeInEMIYears();
                        }

                    } else if (selectedOption.equals("No Change in Loan Tenure")) {
                        // Handle the "No Change in Loan Tenure" option
                        if (switchview.isChecked()) {
                            calculateMoratoriumNoChangeInLoanTenureMonths();
                        } else {
                            calculateMoratoriumNoChangeInLoanTenureYears();
                        }
                    }
                }
            }
        });

        return view;
    }



    private void calculateMoratoriumNoChangeInEMIMonths() {
        // Retrieve and parse input data
        double principalAmount, annualInterestRate, emi;
        int loanTenureMonths, installmentsPaid, moratoriumPeriod;

        try {
            principalAmount = Double.parseDouble(editTextPrincipal.getText().toString());
            annualInterestRate = Double.parseDouble(editTextInterestRate.getText().toString());
            loanTenureMonths = Integer.parseInt(editTextLoanTenureMonths.getText().toString());
            installmentsPaid = Integer.parseInt(etinstallmentpaid.getText().toString());
            moratoriumPeriod = Integer.parseInt(etmoratoriumperiod.getText().toString());
        } catch (NumberFormatException e) {

            displayErrorMessageInWebView("Invalid input. Please enter valid values.");
            return;
        }

        // Calculate EMI
        emi = calculateEMI(principalAmount, annualInterestRate, loanTenureMonths);

        // Calculate total interest amount
        double totalInterest = (emi * (loanTenureMonths + moratoriumPeriod)) - principalAmount;

        // Calculate total principal
        double totalPrincipal = principalAmount;

        // Calculate overall tenure in months (including moratorium)
        int overallTenure = loanTenureMonths + moratoriumPeriod;

        // Calculate total payment (principal + interest)
        double totalPayment = totalPrincipal + totalInterest;

        // Display or use the calculated values
        // For example, you can show them in TextViews


        String htmlTable = "<html>" +
                "<head>" +
                "<style>" +
                "table { width: 100%; border-collapse: collapse; margin-top: 10px; }" +
                "th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }" +
                "th { background-color: #f2f2f2; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h2>Moratorium Results</h2>" +
                "<table>" +
                "<tr><th>EMI</th><td>₹" + String.format("%.2f", emi) + "</td></tr>" +
                "<tr><th>Total Interest Amount</th><td>₹" + String.format("%.2f", totalInterest) + "</td></tr>" +
                "<tr><th>Total Principal</th><td>₹" + String.format("%.2f", totalPrincipal) + "</td></tr>" +
                "<tr><th>Overall Tenure (in months)</th><td>" + overallTenure + "</td></tr>" +
                "<tr><th>Total Payment (Principal + Interest)</th><td>₹" + String.format("%.2f", totalPayment) + "</td></tr>" +
                "</table>" +
                "</body>" +
                "</html>";
        webview.loadDataWithBaseURL(null, htmlTable, "text/html", "UTF-8", null);

    }
    private void calculateMoratoriumNoChangeInEMIYears() {
        // Retrieve and parse input data
        double principalAmount, annualInterestRate, emi;
        int loanTenureYears, loanTenureMonths, installmentsPaid, moratoriumPeriod;

        try {
            principalAmount = Double.parseDouble(editTextPrincipal.getText().toString());
            annualInterestRate = Double.parseDouble(editTextInterestRate.getText().toString());
            loanTenureYears = Integer.parseInt(editTextLoanTenureMonths.getText().toString());
            installmentsPaid = Integer.parseInt(etinstallmentpaid.getText().toString());
            moratoriumPeriod = Integer.parseInt(etmoratoriumperiod.getText().toString());
        } catch (NumberFormatException e) {
            displayErrorMessageInWebView("Invalid input. Please enter valid values.");
            return;
        }

        // Calculate loan tenure in months
        loanTenureMonths = loanTenureYears * 12;

        // Calculate EMI
        emi = calculateEMI(principalAmount, annualInterestRate, loanTenureMonths);

        // Calculate total interest amount
        double totalInterest = (emi * (loanTenureMonths + moratoriumPeriod)) - principalAmount;

        // Calculate total principal
        double totalPrincipal = principalAmount;

        // Calculate overall tenure in months (including moratorium)
        int overallTenure = loanTenureMonths + moratoriumPeriod;

        // Calculate total payment (principal + interest)
        double totalPayment = totalPrincipal + totalInterest;

        // Display or use the calculated values
        // For example, you can show them in TextViews


        String htmlTable = "<html>" +
                "<head>" +
                "<style>" +
                "table { width: 100%; border-collapse: collapse; margin-top: 10px; }" +
                "th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }" +
                "th { background-color: #f2f2f2; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h2>Moratorium Results</h2>" +
                "<table>" +
                "<tr><th>EMI</th><td>₹" + String.format("%.2f", emi) + "</td></tr>" +
                "<tr><th>Total Interest Amount</th><td>₹" + String.format("%.2f", totalInterest) + "</td></tr>" +
                "<tr><th>Total Principal</th><td>₹" + String.format("%.2f", totalPrincipal) + "</td></tr>" +
                "<tr><th>Overall Tenure (in months)</th><td>" + overallTenure + "</td></tr>" +
                "<tr><th>Total Payment (Principal + Interest)</th><td>₹" + String.format("%.2f", totalPayment) + "</td></tr>" +
                "</table>" +
                "</body>" +
                "</html>";

        // Load the HTML string into the WebView
        webview.loadDataWithBaseURL(null, htmlTable, "text/html", "UTF-8", null);

    }

    // Function to calculate moratorium with no change in loan tenure for months
    private void calculateMoratoriumNoChangeInLoanTenureMonths() {

        double principalAmount, annualInterestRate, emi;
        int loanTenureMonths, installmentsPaid, moratoriumPeriod;

        try {
            principalAmount = Double.parseDouble(editTextPrincipal.getText().toString());
            annualInterestRate = Double.parseDouble(editTextInterestRate.getText().toString());
            loanTenureMonths = Integer.parseInt(editTextLoanTenureMonths.getText().toString());
            installmentsPaid = Integer.parseInt(etinstallmentpaid.getText().toString());
            moratoriumPeriod = Integer.parseInt(etmoratoriumperiod.getText().toString());
        } catch (NumberFormatException e) {
            displayErrorMessageInWebView("Invalid input. Please enter valid values.");
            return;
        }


        emi = calculateEMI(principalAmount, annualInterestRate, loanTenureMonths);


        double totalInterest = (emi * (loanTenureMonths + moratoriumPeriod)) - principalAmount;

        double totalPrincipal = principalAmount;
        int overallTenure = loanTenureMonths + moratoriumPeriod;

        double totalPayment = totalPrincipal + totalInterest;

        String htmlTable = "<html>" +
                "<head>" +
                "<style>" +
                "table { width: 100%; border-collapse: collapse; margin-top: 10px; }" +
                "th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }" +
                "th { background-color: #f2f2f2; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h2>Moratorium Results</h2>" +
                "<table>" +
                "<tr><th>EMI</th><td>₹" + String.format("%.2f", emi) + "</td></tr>" +
                "<tr><th>Total Interest Amount</th><td>₹" + String.format("%.2f", totalInterest) + "</td></tr>" +
                "<tr><th>Total Principal</th><td>₹" + String.format("%.2f", totalPrincipal) + "</td></tr>" +
                "<tr><th>Overall Tenure (in months)</th><td>" + overallTenure + "</td></tr>" +
                "<tr><th>Total Payment (Principal + Interest)</th><td>₹" + String.format("%.2f", totalPayment) + "</td></tr>" +
                "</table>" +
                "</body>" +
                "</html>";

// Load the HTML string into the WebView
        webview.loadDataWithBaseURL(null, htmlTable, "text/html", "UTF-8", null);


    }

    private void calculateMoratoriumNoChangeInLoanTenureYears() {
        double principalAmount, annualInterestRate, emi;
        int loanTenureYears, loanTenureMonths, installmentsPaid, moratoriumPeriod;

        try {
            principalAmount = Double.parseDouble(editTextPrincipal.getText().toString());
            annualInterestRate = Double.parseDouble(editTextInterestRate.getText().toString());
            loanTenureYears = Integer.parseInt(editTextLoanTenureMonths.getText().toString());
            installmentsPaid = Integer.parseInt(etinstallmentpaid.getText().toString());
            moratoriumPeriod = Integer.parseInt(etmoratoriumperiod.getText().toString());
        } catch (NumberFormatException e) {
            displayErrorMessageInWebView("Invalid input. Please enter valid values.");
            return;
        }
        loanTenureMonths = loanTenureYears * 12;
        emi = calculateEMI(principalAmount, annualInterestRate, loanTenureMonths);
        double totalInterest = (emi * (loanTenureMonths + moratoriumPeriod)) - principalAmount;
        double totalPrincipal = principalAmount;
        int overallTenure = loanTenureMonths + moratoriumPeriod;
        double totalPayment = totalPrincipal + totalInterest;
        String htmlTable = "<html>" +
                "<head>" +
                "<style>" +
                "table { width: 100%; border-collapse: collapse; margin-top: 10px; }" +
                "th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }" +
                "th { background-color: #f2f2f2; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h2>Moratorium Results</h2>" +
                "<table>" +
                "<tr><th>EMI</th><td>₹" + String.format("%.2f", emi) + "</td></tr>" +
                "<tr><th>Total Interest Amount</th><td>₹" + String.format("%.2f", totalInterest) + "</td></tr>" +
                "<tr><th>Total Principal</th><td>₹" + String.format("%.2f", totalPrincipal) + "</td></tr>" +
                "<tr><th>Overall Tenure (in months)</th><td>" + overallTenure + "</td></tr>" +
                "<tr><th>Total Payment (Principal + Interest)</th><td>₹" + String.format("%.2f", totalPayment) + "</td></tr>" +
                "</table>" +
                "</body>" +
                "</html>";

        webview.loadDataWithBaseURL(null, htmlTable, "text/html", "utf-8", null);



    }

    private double calculateEMI(double principal, double annualInterestRate, int tenureInMonths) {
        // Implement your EMI calculation logic here
        double monthlyInterestRate = annualInterestRate / (12 * 100);

        double emi = (principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureInMonths)) /
                (Math.pow(1 + monthlyInterestRate, tenureInMonths) - 1);

        return emi;
    }

    private void displayErrorMessageInWebView(String errorMessage) {
        // Load the HTML string into the WebView to display the error message
        String errorHtml = "<html><body><p style=\"text-align:center;color:red;\">" + errorMessage + "</p></body></html>";
        webview.loadDataWithBaseURL(null, errorHtml, "text/html", "utf-8", null);
    }
}
