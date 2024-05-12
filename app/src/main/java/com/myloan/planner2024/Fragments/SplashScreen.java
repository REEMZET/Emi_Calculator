package com.myloan.planner2024.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myloan.planner2024.MainActivity;
import com.myloan.planner2024.R;


import android.os.Handler;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public class SplashScreen extends Fragment {
    NavController navController;

    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progressbar);



        return view;
    }

    private void navigateToHome() {
        // Get the NavController

    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) requireActivity()).hideAppBar();

        // Use a Handler to introduce a delay of 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToDestination(R.id.mainPage);
            }
        }, 2000);
    }



    /*private void loadAndShowInterstitialAd(final int destinationFragmentId) {
        progressDialog.show();
        interstitialAd = new InterstitialAd(getActivity(), "1542157703201770_1542161933201347");

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Ad displayed, you can hide the loading indicator if needed
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Ad dismissed, you can handle this event if needed
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Handle error, you might want to hide the loading indicator and show a message
                progressDialog.dismiss();
                navigateToDestination(destinationFragmentId);
                Toast.makeText(getActivity(), adError.getErrorMessage().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded, you can show it now
                interstitialAd.show();
                progressDialog.dismiss();
                navigateToDestination(destinationFragmentId);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked, you can handle this event if needed
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged, you can handle this event if needed
            }
        };

        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }*/

    private void navigateToDestination(int destinationFragmentId) {

        NavController navController = NavHostFragment.findNavController(this);

        // Pop the splash screen from the back stack
        navController.popBackStack(R.id.splashScreen, true);

        // Navigate to the home fragment
        navController.navigate(destinationFragmentId);
    }
}
