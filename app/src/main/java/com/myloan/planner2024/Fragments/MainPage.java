package com.myloan.planner2024.Fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;


import com.myloan.planner2024.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class MainPage extends Fragment {

    LinearLayout llrateus,llprivacy,llshare;
    NavController navController;

    ProgressDialog progressDialog;
    Button btnstart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_main_page, container, false);
        changeStatusBarColor(Color.parseColor("#03A9F4"));

        llrateus=view.findViewById(R.id.llrateus);
        llprivacy=view.findViewById(R.id.llprivacy);
        llshare=view.findViewById(R.id.llshare);
        btnstart=view.findViewById(R.id.btnstart);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progressbar);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();




    btnstart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        navigateToDestination(R.id.home2);
        }
    });
        llshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent with ACTION_SEND
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");

                // Add the content to share (app link)
                String appPackageName = getActivity().getPackageName(); // Replace with your app's package name
                String appLink = "https://play.google.com/store/apps/details?id=" + appPackageName;

                shareIntent.putExtra(Intent.EXTRA_TEXT, "Explore the versatility of our incredible app, the 'EMI Calculator App'! \uD83D\uDE80 This all-in-one tool is your go-to solution for various financial calculations, including EMI calculations, tax estimations, and banking scenarios. \uD83D\uDCB0\uD83D\uDCCA" + appLink);

                // Add the app logo as an attachment
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/share_image.png";
                OutputStream out = null;
                File file = new File(path);
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // Launch the sharing activity
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        llprivacy.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navController.navigate(R.id.action_mainPage_to_privacy_policy);
        }
    });
        llrateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace "com.your.app.package" with your app's package name
                String appPackageName = getActivity().getPackageName();

                try {
                    // Open the Play Store app with the specified app package
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException e) {
                    // If the Play Store app is not available, open the app in a web browser
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });





        return  view;
    }




    private void navigateToDestination(int destinationFragmentId) {
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