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
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.PieChart;
import com.myloan.planner2024.ChromeUtils;
import com.myloan.planner2024.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;


public class LoanTenure extends Fragment {
    private EditText etPrincipal, etInterest, etemi;
    private Button btngetduration,btnreset;
    Toolbar toolbar;
    NavController navController;
    private PieChart pieChart;
   WebView webView;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_loan_tenure, container, false);
        setupUIElements(view);
        setOnClickListeners();


    return view;
    }
    private void setupUIElements(View view) {
        navController = NavHostFragment.findNavController(this);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Loan Tenure");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());
        etPrincipal = view.findViewById(R.id.etprincipalamount);
        etInterest = view.findViewById(R.id.etintrest);
        etemi = view.findViewById(R.id.etemi);
        btngetduration = view.findViewById(R.id.btngetduration);
        webView=view.findViewById(R.id.webview);
        TextView tvexplore=view.findViewById(R.id.tvexplore);
        tvexplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });
        btnreset=view.findViewById(R.id.btnreset);


    }

    private void setOnClickListeners() {
        btngetduration.setOnClickListener(v -> {

            calculateAndDisplayEMI();
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etemi.getText().clear();
                etPrincipal.getText().clear();
                etInterest.getText().clear();
            }
        });
    }

    private void calculateAndDisplayEMI() {
        try {
            double principal = Double.parseDouble(etPrincipal.getText().toString());
            double interestRate = Double.parseDouble(etInterest.getText().toString());
            int emi = Integer.parseInt(etemi.getText().toString());

            double[] result = calculateLoanTenureDetails(principal, emi, interestRate);

            double tenureInYears = result[0];
            double tenureInMonths = result[1];
            double totalInterest = result[2];
            double totalAmount = result[3];

            int years = (int)tenureInYears;
            int months = (int)(tenureInMonths % 12);

            DecimalFormat rupeeFormat = new DecimalFormat("₹#,##0.00");
            String totalInterestFormatted = rupeeFormat.format(totalInterest);
            String totalPaymentFormatted = rupeeFormat.format(totalAmount);

            String formattedTenure;

            if (years > 0 && months > 0) {
                formattedTenure = years + " Years " + months + " Months";
            } else if (years > 0) {
                formattedTenure = years + " Years";
            } else if (months > 0) {
                formattedTenure = months + " Months";
            } else {
                formattedTenure = "0 Months"; // Handle case when both years and months are 0
            }

            // Build the HTML string
            String htmlTable = "<html>" +
                    "<head>" +
                    "<style>" +
                    "table { width: 100%; border-collapse: collapse; margin-top: 10px; }" +
                    "th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }" +
                    "th { background-color: #f2f2f2; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<h2>Loan Tenure Details</h2>" +
                    "<table>" +
                    "<tr><th>Tenure</th><td>" + formattedTenure + "</td></tr>" +
                    "<tr><th>Total Interest</th><td>" + totalInterestFormatted + "</td></tr>" +
                    "<tr><th>Total Payment</th><td>" + totalPaymentFormatted + "</td></tr>" +
                    "</table>" +
                    "</body>" +
                    "</html>";

            // Load the HTML string into the WebView
            webView.loadDataWithBaseURL(null, htmlTable, "text/html", "UTF-8", null);

        } catch (NumberFormatException e) {
            Toast.makeText(requireActivity(), "Invalid input. Please enter valid values.", Toast.LENGTH_LONG).show();
        }
    }


    public static double[] calculateLoanTenureDetails(double principalAmount, double emi, double annualInterestRate) {
        double monthlyInterestRate = annualInterestRate / (12 * 100);

        BigDecimal numerator = BigDecimal.valueOf(Math.log(emi)).subtract(BigDecimal.valueOf(Math.log(emi - (principalAmount * monthlyInterestRate))));
        BigDecimal denominator = BigDecimal.valueOf(Math.log(1 + monthlyInterestRate));

        BigDecimal tenureInMonths = numerator.divide(denominator, 10, RoundingMode.HALF_EVEN);
        double tenureInYears = tenureInMonths.doubleValue() / 12;

        double totalInterest = (emi * tenureInMonths.doubleValue()) - principalAmount;
        double totalAmount = principalAmount + totalInterest;

        double[] result = {tenureInYears, tenureInMonths.doubleValue(), totalInterest, totalAmount};
        return result;
    }
    /*void Showad(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progressbar);
        progressDialog.show();
        MaxInterstitialAd interstitialAd = new MaxInterstitialAd("e4f4a34b455c88d2", getActivity());
        interstitialAd.setListener(new MaxAdListener() {

            @Override
            public void onAdLoaded(MaxAd ad) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        interstitialAd.showAd();
                    }
                }, 3000);
            }


            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                // Handle error, you might want to hide the loading indicator and show a message
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Ad failed to load: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                // Ad displayed, you can hide the loading indicator if needed
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                // Ad dismissed, you can handle this event if needed
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                // Ad clicked, you can handle this event if needed
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                // Not used for interstitial ads
            }
        });
        interstitialAd.loadAd();
    }*/

}