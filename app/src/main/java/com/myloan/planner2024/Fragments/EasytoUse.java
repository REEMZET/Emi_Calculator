package com.myloan.planner2024.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.myloan.planner2024.MainActivity;
import com.myloan.planner2024.R;



public class EasytoUse extends Fragment {

    NavController navController;
   
    ProgressDialog progressDialog;
    Button btneasyskip;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_easyto_use, container, false);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progressbar);
        btneasyskip=view.findViewById(R.id.btneasyskip);
        btneasyskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.home2);
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



   /* private void loadAndShowInterstitialAd(final int destinationFragmentId) {
        progressDialog.show();
        interstitialAd = new InterstitialAd(getActivity(), "384092747424862_384093387424798"); //intertwo

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
