package com.myloan.planner2024.Fragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


import com.myloan.planner2024.MainActivity;
import com.myloan.planner2024.R;




public class Secure extends Fragment {

    NavController navController;
    ProgressDialog progressDialog;
    Button btnsecureskip;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_secure, container, false);
        changeStatusBarColor(getResources().getColor(R.color.white));
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progressbar);
        btnsecureskip=view.findViewById(R.id.btnsecureskip);

        btnsecureskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              navigateToDestination(R.id.easytoUse);
            }
        });

        return view;
    }

    private void navigateToHome() {
        // Get the NavController

    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) requireActivity()).hideAppBar();
    }


    /*private void loadAndNavigateToDestination(final int destinationFragmentId, String adUnitId) {
        progressDialog.show();

        MaxInterstitialAd interstitialAd = new MaxInterstitialAd(adUnitId, getActivity());

        interstitialAd.setListener(new MaxAdListener() {

            @Override
            public void onAdLoaded(MaxAd ad) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        interstitialAd.showAd();
                        navigateToDestination(destinationFragmentId);
                    }
                }, 3500);
            }


            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                // Handle error, you might want to hide the loading indicator and show a message
                progressDialog.dismiss();
                navigateToDestination(destinationFragmentId);
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
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


    private void navigateToDestination(int destinationFragmentId) {

        NavController navController = NavHostFragment.findNavController(this);

        // Pop the splash screen from the back stack
        navController.popBackStack(R.id.splashScreen, true);

        // Navigate to the home fragment
        navController.navigate(destinationFragmentId);
    }
    private void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
