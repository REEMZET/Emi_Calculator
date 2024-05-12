package com.myloan.planner2024.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;



import com.myloan.planner2024.ChromeUtils;
import com.myloan.planner2024.R;

import java.time.LocalDate;

public class FDcalculation extends Fragment {

    private EditText editTextDepositAmount, editTextLoanTenure;
    private Spinner optionsSpinnerFD;
    private EditText editTextInterestRateFD;
    private Button btnCalculateFD;
    private Button btnResetFD;

    WebView webView;
    Switch tenureswitch;
    Toolbar toolbar;
    NavController navController;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_f_dcalculation, container, false);

        editTextDepositAmount = view.findViewById(R.id.editTextMonthlyDeposit2);
        optionsSpinnerFD = view.findViewById(R.id.optionsSpinnerFD);
        editTextInterestRateFD = view.findViewById(R.id.editTextInterestRateFD);
        btnCalculateFD = view.findViewById(R.id.btnCalculateFD);
        btnResetFD = view.findViewById(R.id.btnresetFD);

        editTextLoanTenure = view.findViewById(R.id.editTextLoanTenure);
        webView=view.findViewById(R.id.webview);
        tenureswitch=view.findViewById(R.id.switchtenure);
        navController = NavHostFragment.findNavController(this);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("FD Calculate");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());


        TextView tvexplore=view.findViewById(R.id.tvexplore);
        tvexplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.fd_options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinnerFD.setAdapter(adapter);

        btnCalculateFD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calculateFD();
            }
        });

        btnResetFD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });
        tenureswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tenureswitch.setText("Year");
            } else {
                tenureswitch.setText("Months");
            }
        });

        return view;
    }

    private void calculateFD() {
        // Retrieve and parse input data
        double depositAmount, annualInterestRate;
        String interestType;
        int tenure;
        boolean isTenureInMonths = tenureswitch.isChecked();

        try {
            depositAmount = Double.parseDouble(editTextDepositAmount.getText().toString());
            annualInterestRate = Double.parseDouble(editTextInterestRateFD.getText().toString());
            interestType = optionsSpinnerFD.getSelectedItem().toString();
            tenure = Integer.parseInt(editTextLoanTenure.getText().toString());
        } catch (NumberFormatException e) {
            displayErrorMessageInWebView("Invalid input. Please enter valid values.");
            return;
        }

        // Perform FD calculation based on interest type and tenure
        double maturityValue = calculateFD(depositAmount, annualInterestRate, tenure, isTenureInMonths, interestType);

        // Display or use the calculated values
        // For example, you can show them in TextViews
        String formattedMaturityValue = String.format("₹%.2f", maturityValue);

        LocalDate investmentTodayDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            investmentTodayDate = LocalDate.now();
        }
        LocalDate maturityDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            maturityDate = investmentTodayDate.plusMonths(tenure);
        }
        String htmlTable = "<html><head><style>" +
                "table { width: 80%; margin: auto; }" +
                "td { padding: 8px; text-align: left; }" +
                "</style></head><body>" +
                "<table border=\"1\">" +
                "<tr><td><b>Maturity Value:</b></td><td>" + formattedMaturityValue + "</td></tr>" +
                "<tr><td><b>Monthly Deposit:</b></td><td>" + String.format("₹%.2f", depositAmount) + "</td></tr>" +
                "<tr><td><b>Total Investment:</b></td><td>" + String.format("₹%.2f", depositAmount * tenure) + "</td></tr>" +
                "<tr><td><b>Total Interest:</b></td><td>" + String.format("₹%.2f", maturityValue - depositAmount) + "</td></tr>" +
                "<tr><td><b>Investment Date:</b></td><td>" + investmentTodayDate + "</td></tr>" +
                "<tr><td><b>Maturity Date:</b></td><td>" + maturityDate + "</td></tr>" +
                "</table></body></html>";
        webView.loadDataWithBaseURL(null, htmlTable, "text/html", "utf-8", null);
    }


    private double calculateFD(double depositAmount, double annualInterestRate, int tenure, boolean isTenureInMonths, String interestType) {
        // Calculate the monthly interest rate
        double monthlyInterestRate = annualInterestRate / 100 / 12;

        // Calculate the tenure in months
        int tenureInMonths = tenure;
        if (!isTenureInMonths) {
            tenureInMonths *= 12;
        }

        // Calculate the maturity value based on the interest type
        double maturityValue = depositAmount;
        switch (interestType) {
            case "Cumulative":
                maturityValue = depositAmount * Math.pow(1 + monthlyInterestRate, tenureInMonths);
                break;
            case "Quarterly":
                maturityValue = depositAmount * (1 + monthlyInterestRate / 4) * Math.pow(1 + monthlyInterestRate / 4, (tenureInMonths / 4) - 1);
                break;
            case "Monthly":
                maturityValue = depositAmount * Math.pow(1 + monthlyInterestRate, tenureInMonths);
                break;
            default:
                // Invalid interest type
                break;
        }

        return maturityValue;
    }

    private void resetFields() {
        // Reset input fields and result TextView
        editTextDepositAmount.getText().clear();
        editTextInterestRateFD.getText().clear();
        optionsSpinnerFD.setSelection(0); // Set the spinner to the first item
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
    }


    private void displayErrorMessageInWebView(String errorMessage) {
        // Load the HTML string into the WebView to display the error message
        String errorHtml = "<html><body><p style=\"text-align:center;color:red;\">" + errorMessage + "</p></body></html>";
        webView.loadDataWithBaseURL(null, errorHtml, "text/html", "utf-8", null);
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
