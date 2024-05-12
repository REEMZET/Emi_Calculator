package com.myloan.planner2024.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import com.myloan.planner2024.ChromeUtils;
import com.myloan.planner2024.R;

import java.text.DecimalFormat;

public class GSTCalc extends Fragment {

    private EditText etInitialAmount, etGSTRate;
    private Button btnAddGST, btnSubtractGST;
    WebView webView;

    Toolbar toolbar;
    NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_g_s_t_calc, container, false);

        etInitialAmount = view.findViewById(R.id.etInitialAmount);
        etGSTRate = view.findViewById(R.id.etGSTRate);
        btnAddGST = view.findViewById(R.id.btnAddGST);
        btnSubtractGST = view.findViewById(R.id.btnSubtractGST);
        navController = NavHostFragment.findNavController(this);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("GST Calculate");
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
        btnAddGST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAddGST();
            }
        });

        btnSubtractGST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateSubtractGST();
            }
        });

        return view;
    }

    private void calculateAddGST() {
        try {
            double initialAmount = Double.parseDouble(etInitialAmount.getText().toString());
            double gstRate = Double.parseDouble(etGSTRate.getText().toString());

            double gstAmount = (initialAmount * gstRate) / 100;
            double cgstAmount = gstAmount / 2; // Assuming equal division for CGST and SGST
            double sgstAmount = gstAmount / 2;
            double netAmount = initialAmount + gstAmount;

            displayResult(netAmount, gstAmount, cgstAmount, sgstAmount);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid input. Please enter valid values.", Toast.LENGTH_LONG).show();
        }
    }

    private void calculateSubtractGST() {
        try {
            double initialAmount = Double.parseDouble(etInitialAmount.getText().toString());
            double gstRate = Double.parseDouble(etGSTRate.getText().toString());

            double gstAmount = (initialAmount * gstRate) / (100 + gstRate);
            double cgstAmount = gstAmount / 2; // Assuming equal division for CGST and SGST
            double sgstAmount = gstAmount / 2;
            double netAmount = initialAmount - gstAmount;

            displayResult(netAmount, gstAmount, cgstAmount, sgstAmount);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid input. Please enter valid values.", Toast.LENGTH_LONG).show();
        }
    }

    private void displayResult(double netAmount, double gstAmount, double cgstAmount, double sgstAmount) {
        DecimalFormat currencyFormat = new DecimalFormat("₹#.00");

        String htmlTable = "<style>" +
                "table {" +
                "   width: 80%;" + // Adjust the width as needed
                "   margin-left: auto;" +
                "   margin-right: auto;" +
                "}" +
                "td, th {" +
                "   padding: 10px;" +
                "   text-align: center;" +
                "}" +
                "</style>" +
                "<table border='1'>" +
                "<tr><th>Category</th><th>Amount</th></tr>" +
                "<tr><td>Net Amount</td><td>" + currencyFormat.format(netAmount) + "</td></tr>" +
                "<tr><td>GST Amount</td><td>" + currencyFormat.format(gstAmount) + "</td></tr>" +
                "<tr><td>CGST</td><td>" + currencyFormat.format(cgstAmount) + "</td></tr>" +
                "<tr><td>SGST</td><td>" + currencyFormat.format(sgstAmount) + "</td></tr>" +
                "</table>";

        String resultHtml = "<html><head>" + htmlTable + "</head><body></body></html>";

        // Display the result in WebView
      ; // Assuming you have a WebView in your XML layout with the id 'webView'
        webView.loadData(resultHtml, "text/html", "UTF-8");
    }


}
