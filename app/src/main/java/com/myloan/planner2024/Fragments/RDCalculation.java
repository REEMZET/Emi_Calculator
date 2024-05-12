package com.myloan.planner2024.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.myloan.planner2024.ChromeUtils;
import com.myloan.planner2024.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RDCalculation extends Fragment {

    private EditText editTextMonthlyDeposit;
    private EditText editTextLoanTenureYears;
    private EditText editTextInterestRate;
    private Switch switchView;
    private Button btnCalculateRD;
    private Button btnResetRD;
    WebView webView;

    Toolbar toolbar;
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_r_d_calculation, container, false);

        editTextMonthlyDeposit = view.findViewById(R.id.editTextMonthlyDeposit);
        editTextLoanTenureYears = view.findViewById(R.id.editTextLoanTenure);
        editTextInterestRate = view.findViewById(R.id.editTextInterestRate);
        switchView = view.findViewById(R.id.switchtenure);
        btnCalculateRD = view.findViewById(R.id.btnCalculateRD);
        btnResetRD = view.findViewById(R.id.btnresetRD);
        webView=view.findViewById(R.id.webview);
        navController = NavHostFragment.findNavController(this);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("RD Calculate");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());

        TextView tvexplore=view.findViewById(R.id.tvexplore);
        tvexplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });
        btnCalculateRD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateRD();
            }
        });

        btnResetRD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });
        switchView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switchView.setText("Years");
            } else {
                switchView.setText("Months");
            }
        });

        return view;
    }

    private void calculateRD() {
        double monthlyDeposit, annualInterestRate;
        int loanTenure, totalMonths;
        boolean isMonthly = switchView.isChecked();

        try {
            monthlyDeposit = Double.parseDouble(editTextMonthlyDeposit.getText().toString());
            annualInterestRate = Double.parseDouble(editTextInterestRate.getText().toString());
            loanTenure = Integer.parseInt(editTextLoanTenureYears.getText().toString());

            // Calculate total months based on the switch
            totalMonths = isMonthly ? loanTenure * 12 : loanTenure;
        } catch (NumberFormatException e) {
            displayErrorMessageInWebView("Invalid input. Please enter valid values.");
            return;
        }

        // Perform RD calculation logic here
        double maturityValue = calculateMaturityValue(monthlyDeposit, annualInterestRate, totalMonths);
        double investmentAmount = monthlyDeposit * totalMonths;
        double totalInterest = maturityValue - investmentAmount;

        // Display or use the calculated values
        // Format numeric values to two decimal places and include rupee sign
        String formattedMaturityValue = String.format(Locale.getDefault(), "₹%.2f", maturityValue);
        String formattedInvestmentAmount = String.format(Locale.getDefault(), "₹%.2f", investmentAmount);
        String formattedTotalInterest = String.format(Locale.getDefault(), "₹%.2f", totalInterest);

        String htmlText = "<html>" +
                "<head>" +
                "<style>" +
                "body { text-align: center; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h2>Maturity Details</h2>" +
                "<table align='center' border='1'>" +
                "<tr><td><b>Maturity Value:</b></td><td>" + formattedMaturityValue + "</td></tr>" +
                "<tr><td><b>Investment Amount:</b></td><td>" + formattedInvestmentAmount + "</td></tr>" +
                "<tr><td><b>Total Interest:</b></td><td>" + formattedTotalInterest + "</td></tr>" +
                "<tr><td><b>Investment Date:</b></td><td>" + getCurrentDate() + "</td></tr>" +
                "<tr><td><b>Maturity Date:</b></td><td>" + getMaturityDate(totalMonths) + "</td></tr>" +
                "</table>" +
                "</body>" +
                "</html>";

        webView.loadDataWithBaseURL(null, htmlText, "text/html", "UTF-8", null);


    }


    private double calculateMaturityValue(double monthlyDeposit, double annualInterestRate, int totalMonths) {
        double monthlyInterestRate = annualInterestRate / (12 * 100);
        double maturityValue = monthlyDeposit * (((Math.pow(1 + monthlyInterestRate, totalMonths)) - 1) / monthlyInterestRate) * (1 + monthlyInterestRate);

        return maturityValue;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String getMaturityDate(int totalMonths) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, totalMonths);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private void resetForm() {
        editTextMonthlyDeposit.setText("");
        editTextLoanTenureYears.setText("");
        editTextInterestRate.setText("");
        switchView.setChecked(false);
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
    }

    private void displayErrorMessageInWebView(String errorMessage) {
        // Load the HTML string into the WebView to display the error message
        String errorHtml = "<html><body><p style=\"text-align:center;color:red;\">" + errorMessage + "</p></body></html>";
        webView.loadDataWithBaseURL(null, errorHtml, "text/html", "utf-8", null);
    }
}
