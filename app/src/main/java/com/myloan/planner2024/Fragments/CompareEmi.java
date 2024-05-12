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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.myloan.planner2024.ChromeUtils;
import com.myloan.planner2024.R;

import java.text.DecimalFormat;




public class CompareEmi extends Fragment {
    private EditText editTextPrincipal1, editTextInterestRate1, editTextLoanTenure1,editTextPrincipal2, editTextInterestRate2, editTextLoanTenure2;
    private Button btncompare,btnreset;
    Toolbar toolbar;
    NavController navController;

    TextView tvemi1, tvinterest1, tvtotal1,tvemi2, tvinterest2, tvtotal2;
    private Switch switchView;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view= inflater.inflate(R.layout.fragment_compare_emi, container, false);


        initializeEditTextViews(view);
        setOnClickListeners();

        TextView tvexplore=view.findViewById(R.id.tvexplore);
        tvexplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });
    return view;
    }
    private void initializeEditTextViews(View view) {
        navController = NavHostFragment.findNavController(this);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Loan Compare");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());
        editTextPrincipal1 = view.findViewById(R.id.editTextPrincipal1);
        editTextInterestRate1 = view.findViewById(R.id.editTextInterestRate1);
        editTextLoanTenure1 = view.findViewById(R.id.editTextLoanTenure1);

        editTextPrincipal2 = view.findViewById(R.id.editTextPrincipal2);
        editTextInterestRate2 = view.findViewById(R.id.editTextInterestRate2);
        editTextLoanTenure2 = view.findViewById(R.id.editTextLoanTenure2);
        btncompare=view.findViewById(R.id.btncompare);
        btnreset=view.findViewById(R.id.btnreset);

        tvemi1 = view.findViewById(R.id.tvemi1);
        tvinterest1 = view.findViewById(R.id.tvintrest1);
        tvtotal1 = view.findViewById(R.id.tvtotal1);

        tvemi2 = view.findViewById(R.id.tvemi2);
        tvinterest2 = view.findViewById(R.id.tvintrest2);
        tvtotal2 = view.findViewById(R.id.tvtotal2);

        switchView = view.findViewById(R.id.switchmonth2);
    }

    private void setOnClickListeners() {
        btncompare.setOnClickListener(v -> {
            calculateAndDisplayEMI();

        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextPrincipal1.getText().clear();
                editTextInterestRate1.getText().clear();
                editTextLoanTenure1.getText().clear();
                editTextPrincipal2.getText().clear();
                editTextInterestRate2.getText().clear();
                editTextLoanTenure2.getText().clear();
            }
        });
    }


    private void calculateAndDisplayEMI() {
        try {
            double principal1 = Double.parseDouble(editTextPrincipal1.getText().toString());
            double interestRate1 = Double.parseDouble(editTextInterestRate1.getText().toString());
            int loanTenureYears1 = Integer.parseInt(editTextLoanTenure1.getText().toString());
            double principal2 = Double.parseDouble(editTextPrincipal2.getText().toString());
            double interestRate2 = Double.parseDouble(editTextInterestRate2.getText().toString());
            int loanTenureYears2 = Integer.parseInt(editTextLoanTenure2.getText().toString());
            double[] result1;
            double[] result2;
            if (switchView.isChecked()){
                result1=calculateEMImonth(principal1,interestRate1,loanTenureYears1);
                result2=calculateEMImonth(principal2,interestRate2,loanTenureYears2);
            }else {
                result1= calculateEMIAndTotalPayment(principal1, interestRate1, loanTenureYears1);
                result2= calculateEMIAndTotalPayment(principal2, interestRate2, loanTenureYears2);
            }

            double emi1 = result1[0];
            double totalInterest1 = result1[1];
            double totalPayment1 = result1[2];

            double emi2 = result2[0];
            double totalInterest2 = result2[1];
            double totalPayment2 = result2[2];

            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            String emiFormatted1 = decimalFormat.format(emi1);
            String totalInterestFormatted1 = decimalFormat.format(totalInterest1);
            String totalPaymentFormatted1 = decimalFormat.format(totalPayment1);
            String emiFormatted2 = decimalFormat.format(emi2);
            String totalInterestFormatted2 = decimalFormat.format(totalInterest2);
            String totalPaymentFormatted2 = decimalFormat.format(totalPayment2);


            tvemi1.setText(emiFormatted1);
            tvinterest1.setText(totalInterestFormatted1);
            tvtotal1.setText(totalPaymentFormatted1);

            tvemi2.setText(emiFormatted2);
            tvinterest2.setText(totalInterestFormatted2);
            tvtotal2.setText(totalPaymentFormatted2);

        } catch (NumberFormatException e) {
            Toast.makeText(requireActivity(), "Invalid input. Please enter valid values.", Toast.LENGTH_LONG).show();
        }
    }



    public static double[] calculateEMIAndTotalPayment(double principalAmount, double annualInterestRate, int tenureInYears) {
        double emi, totalInterest, totalPayment;
        double monthlyInterestRate = annualInterestRate / (12 * 100);
        int tenureInMonths = tenureInYears * 12;

        emi = (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureInMonths))
                / (Math.pow(1 + monthlyInterestRate, tenureInMonths) - 1);

        totalInterest = (emi * tenureInMonths) - principalAmount;
        totalPayment = emi * tenureInMonths;

        double[] result = {emi, totalInterest, totalPayment};
        return result;
    }

    public static double[] calculateEMImonth(double principalAmount, double annualInterestRate, int tenureInMonths) {
        double emi, totalInterest, totalPayment;
        double monthlyInterestRate = annualInterestRate / (12 * 100);

        emi = (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureInMonths))
                / (Math.pow(1 + monthlyInterestRate, tenureInMonths) - 1);

        totalInterest = (emi * tenureInMonths) - principalAmount;
        totalPayment = emi * tenureInMonths;

        double[] result = {emi, totalInterest, totalPayment};
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