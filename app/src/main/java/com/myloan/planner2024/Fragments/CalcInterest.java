package com.myloan.planner2024.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.myloan.planner2024.R;
import com.github.mikephil.charting.charts.PieChart;
import com.myloan.planner2024.ChromeUtils;
;


import java.text.DecimalFormat;


public class CalcInterest extends Fragment {
    private EditText etemi, etprincipalamount, ettenure;
    private Button btngetinterest,btnreset;
    Toolbar toolbar;
    NavController navController;
    private PieChart pieChart;
   static WebView webView;
    private Switch switchView;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_calc_interest, container, false);
        setupUIElements(view);

        setOnClickListeners();

        TextView tvexplore=view.findViewById(R.id.tvexplore);
        tvexplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });

        switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switchView.setText("Year");

            } else {
                switchView.setText("Month");
            }
        });




        return view;
    }
    private void  setupUIElements(View view) {
        navController = NavHostFragment.findNavController(this);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Rate of Interest calculate");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());
        etemi = view.findViewById(R.id.etemi);
        etprincipalamount = view.findViewById(R.id.etprincipalamount);
        ettenure = view.findViewById(R.id.ettenure);
        btngetinterest = view.findViewById(R.id.btncalcrate);
        webView=view.findViewById(R.id.webview);
        switchView = view.findViewById(R.id.switchmonth);
        btnreset=view.findViewById(R.id.btnreset);

    }


    private void setOnClickListeners() {
      btngetinterest.setOnClickListener(v -> {

            calculateAndDisplayEMI();

        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               etemi.getText().clear();
                etprincipalamount.getText().clear();
                ettenure.getText().clear();
            }
        });
    }

    private void calculateAndDisplayEMI() {
        try {
            double principalAmount = Double.parseDouble(etprincipalamount.getText().toString());
            double emi = Double.parseDouble(etemi.getText().toString());
            int loanTenure = Integer.parseInt(ettenure.getText().toString());

            double[] result;
            if (!switchView.isChecked()) {
                result = calculateInterestRate(principalAmount, emi, loanTenure);
            } else {
                result = calculateInterestRateInYears(principalAmount, emi, loanTenure);
            }

            // Calculate interest rate, interest amount, and total payment
            double rateofinterest = result[0];
            double totalInterest = result[1];
            double totalAmount = result[2];

            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            String totalInterestFormatted = decimalFormat.format(totalInterest);
            String totalAmountFormatted = decimalFormat.format(totalAmount);
            String rateofinterestFormatted = decimalFormat.format(rateofinterest);

            String htmlTable = "<html>" +
                    "<head>" +
                    "<style>" +
                    "table { width: 100%; border-collapse: collapse; margin-top: 10px; }" +
                    "th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }" +
                    "th { background-color: #f2f2f2; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<h2>EMI Calculation Results</h2>" +
                    "<table>" +
                    "<tr><th>Total Amount</th><td>" + totalAmountFormatted + "</td></tr>" +
                    "<tr><th>Total Interest Amount</th><td>" + totalInterestFormatted + "</td></tr>" +
                    "<tr><th>Interest Rate</th><td>" + rateofinterestFormatted + "</td></tr>" +
                    "</table>" +
                    "</body>" +
                    "</html>";

            // Load the HTML string into the WebView
            webView.loadDataWithBaseURL(null, htmlTable, "text/html", "UTF-8", null);
        } catch (NumberFormatException e) {
            Toast.makeText(requireActivity(), "Invalid input. Please enter valid values.", Toast.LENGTH_LONG).show();
        }
    }
    public static void displayErrorMessageInWebView(WebView webView, String errorMessage) {
        // Load the HTML string into the WebView to display the error message
        String errorHtml = "<html><body><p style=\"text-align:center;color:red;\">" + errorMessage + "</p></body></html>";
        webView.loadDataWithBaseURL(null, errorHtml, "text/html", "utf-8", null);
    }


    public static double[] calculateInterestRate(double principal, double emi, int tenureInMonths) {
        double monthlyInterestRate = 0.01; // Initialize with a starting value
        double monthlyInterestRateIncrement = 0.001; // Increment for monthly interest rate
        int maxIterations = 1000; // Maximum number of iterations to avoid infinite loop

        double totalAmount = 0; // Initialize with a default value
        double totalInterest = 0; // Initialize with a default value
        double calculatedEMI;

        for (int i = 0; i < maxIterations; i++) {
            calculatedEMI = (principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureInMonths))
                    / (Math.pow(1 + monthlyInterestRate, tenureInMonths) - 1);
            totalInterest = (calculatedEMI * tenureInMonths) - principal;
            totalAmount = principal + totalInterest;

            if (Math.abs(calculatedEMI - emi) < 0.01 || Math.abs(monthlyInterestRateIncrement) < 1e-8) {
                break; // EMI matches the desired value or increment is too small
            } else if (calculatedEMI < emi) {
                monthlyInterestRate += monthlyInterestRateIncrement;
            } else {
                monthlyInterestRate -= monthlyInterestRateIncrement;
                monthlyInterestRateIncrement /= 10; // Reduce increment for finer adjustments
            }
        }

        double annualInterestRate = monthlyInterestRate * 12 * 100;

        // Check if monthlyInterestRateIncrement is too small
        if (Math.abs(monthlyInterestRateIncrement) < 1e-8) {
            displayErrorMessageInWebView(webView,"Error: Monthly interest rate increment is too small.");
        }

        double[] result = {annualInterestRate, totalInterest, totalAmount};
        return result;
    }




    public static double[] calculateInterestRateInYears(double principal, double emi, int tenureInYears) {
        int maxIterations = 1000; // Maximum number of iterations to avoid infinite loop
        int tenureInMonths = tenureInYears * 12;

        double monthlyInterestRate = 0.01; // Initialize with a starting value
        double monthlyInterestRateIncrement = 0.001; // Increment for monthly interest rate

        double totalAmount = 0; // Initialize with a default value
        double totalInterest = 0; // Initialize with a default value
        double calculatedEMI;

        for (int i = 0; i < maxIterations; i++) {
            calculatedEMI = (principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureInMonths))
                    / (Math.pow(1 + monthlyInterestRate, tenureInMonths) - 1);
            totalInterest = (calculatedEMI * tenureInMonths) - principal;
            totalAmount = principal + totalInterest;

            if (Math.abs(calculatedEMI - emi) < 0.01 || Math.abs(monthlyInterestRateIncrement) < 1e-8) {
                break; // EMI matches the desired value or increment is too small
            } else if (calculatedEMI < emi) {
                monthlyInterestRate += monthlyInterestRateIncrement;
            } else {
                monthlyInterestRate -= monthlyInterestRateIncrement;
                monthlyInterestRateIncrement /= 10; // Reduce increment for finer adjustments
            }
        }

        double annualInterestRate = monthlyInterestRate * 12 * 100;

        // Check if monthlyInterestRateIncrement is too small
        if (Math.abs(monthlyInterestRateIncrement) < 1e-8) {
            displayErrorMessageInWebView(webView,"Error: Monthly interest rate increment is too small.");
        }

        double[] result = {annualInterestRate, totalInterest, totalAmount};
        return result;
    }



}