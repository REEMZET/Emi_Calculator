package com.myloan.planner2024.Fragments;

import android.app.ProgressDialog;
import android.graphics.Typeface;
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


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.myloan.planner2024.ChromeUtils;
import com.myloan.planner2024.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ComputeLoanAmount extends Fragment {

    private EditText editTextemi, editTextInterestRate, editTexttenure;
    private Button btngetloanamount,btnreset;
    Toolbar toolbar;
    NavController navController;
    private PieChart pieChart;
    TextView tvloanamount, tvinterest, tvtotal;
    private Switch switchView;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_compute_loan_amount, container, false);

        setupUIElements(view);
        setupChart();
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
                switchView.setText("Month");
            } else {
                switchView.setText("Year");
            }
        });




        return view;
    }
    private void setupUIElements(View view) {
        navController = NavHostFragment.findNavController(this);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Loan Amount Calculate");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());
        editTextemi = view.findViewById(R.id.editTextemi);
        editTextInterestRate = view.findViewById(R.id.editTextloanamountInterest);
        editTexttenure = view.findViewById(R.id.editTexttenure);
        btngetloanamount = view.findViewById(R.id.btngetloanamount);
        pieChart = view.findViewById(R.id.pieChart);
        tvloanamount = view.findViewById(R.id.tvloanamount);
        tvinterest = view.findViewById(R.id.tvintrest);
        tvtotal = view.findViewById(R.id.tvtotal);
        switchView = view.findViewById(R.id.switchmonth);
        btnreset=view.findViewById(R.id.btnreset);

    }
    private void setupChart() {
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("EMI Data");
        pieChart.setCenterTextSize(18f);
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setCenterTextRadiusPercent(100f);
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleRadius(65f);
    }

    private void setOnClickListeners() {
        btngetloanamount.setOnClickListener(v -> {

            calculateAndDisplayEMI();

        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextemi.getText().clear();
                editTextInterestRate.getText().clear();
                editTexttenure.getText().clear();
            }
        });
    }

    private void calculateAndDisplayEMI() {
        try {
            double emi = Double.parseDouble(editTextemi.getText().toString());
            double interestRate = Double.parseDouble(editTextInterestRate.getText().toString());
            int loantenure = Integer.parseInt(editTexttenure.getText().toString());
            double[] result;
            if (switchView.isChecked()){
                result= calculateLoanAmount(emi,interestRate,loantenure);
            }else {
                result=  calculateLoanAmountyear(emi, interestRate, loantenure);
            }


            double principalAmount = result[0];
            double totalAmount = result[1];
            double totalInterest = result[2];

            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            String principalAmountFormatted = decimalFormat.format(principalAmount);
            String totalInterestFormatted = decimalFormat.format(totalInterest);
            String totalAmountFormatted = decimalFormat.format(totalAmount);

            tvloanamount.setText(principalAmountFormatted);
            tvinterest.setText(totalInterestFormatted);
            tvtotal.setText(totalAmountFormatted);

            updatePieChart(totalAmount, principalAmount, totalInterest);
        } catch (NumberFormatException e) {
            Toast.makeText(requireActivity(), "Invalid input. Please enter valid values.", Toast.LENGTH_LONG).show();
        }
    }

    public static double[] calculateLoanAmount(double emi, double annualInterestRate, int tenureInMonths) {
        double monthlyInterestRate = annualInterestRate / (12 * 100);

        double principal = (emi * (Math.pow(1 + monthlyInterestRate, tenureInMonths) - 1))
                / (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureInMonths));

        double totalInterest = (emi * tenureInMonths) - principal;
        double totalAmount = principal + totalInterest;

        double[] result = { principal, totalAmount, totalInterest };
        return result;
    }

    public static double[] calculateLoanAmountyear(double emi, double annualInterestRate, int tenureInYears) {
        double monthlyInterestRate = annualInterestRate / (12 * 100);
        int tenureInMonths = tenureInYears * 12;

        double principal = (emi * (Math.pow(1 + monthlyInterestRate, tenureInMonths) - 1))
                / (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureInMonths));

        double totalInterest = (emi * tenureInMonths) - principal;
        double totalAmount = principal + totalInterest;

        double[] result = { principal, totalAmount, totalInterest };
        return result;
    }





    private void updatePieChart(double totalAmount, double principalAmount, double totalInterest) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) totalAmount, "totalAmount"));
        entries.add(new PieEntry((float) principalAmount, "Principal amount"));
        entries.add(new PieEntry((float) totalInterest, "Interest"));

        PieDataSet dataSet = new PieDataSet(entries, "EMI Data");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setSliceSpace(3f);

        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.invalidate();
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