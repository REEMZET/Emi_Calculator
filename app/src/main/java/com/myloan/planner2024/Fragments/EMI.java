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

public class EMI extends Fragment {

    private EditText editTextPrincipal, editTextInterestRate, editTextLoanTenureYears;
    private Button btnCalculateEMI,btnreset;
    Toolbar toolbar;
    NavController navController;
    private PieChart pieChart;
    TextView tvemi, tvinterest, tvtotal;
    private Switch switchView;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e_m_i, container, false);

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
        toolbar.setTitle("EMI Calculate");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> navController.popBackStack());
        editTextPrincipal = view.findViewById(R.id.editTextPrincipal);
        editTextInterestRate = view.findViewById(R.id.editTextInterestRate);
        editTextLoanTenureYears = view.findViewById(R.id.editTextLoanTenureYears);
        btnCalculateEMI = view.findViewById(R.id.btnCalculateEMI);
        pieChart = view.findViewById(R.id.pieChart);
        tvemi = view.findViewById(R.id.tvemi);
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
        btnCalculateEMI.setOnClickListener(v -> {

            calculateAndDisplayEMI();

        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextPrincipal.getText().clear();
                editTextInterestRate.getText().clear();
                editTextLoanTenureYears.getText().clear();
            }
        });
    }

    private void calculateAndDisplayEMI() {
        try {
            double principal = Double.parseDouble(editTextPrincipal.getText().toString());
            double interestRate = Double.parseDouble(editTextInterestRate.getText().toString());
            int loanTenureYears = Integer.parseInt(editTextLoanTenureYears.getText().toString());
            double[] result;
            if (switchView.isChecked()){
                result=calculateEMImonth(principal,interestRate,loanTenureYears);
            }else {
                 result= calculateEMIAndTotalPayment(principal, interestRate, loanTenureYears);
            }

            double emi = result[0];
            double totalInterest = result[1];
            double totalPayment = result[2];

            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            String emiFormatted = decimalFormat.format(emi);
            String totalInterestFormatted = decimalFormat.format(totalInterest);
            String totalPaymentFormatted = decimalFormat.format(totalPayment);

            tvemi.setText(emiFormatted);
            tvinterest.setText(totalInterestFormatted);
            tvtotal.setText(totalPaymentFormatted);

            updatePieChart(emi, principal, totalInterest);
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

    private void updatePieChart(double emi, double principal, double totalInterest) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) emi, "EMI"));
        entries.add(new PieEntry((float) principal, "Principal amount"));
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
                }, 4000);
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
