package com.myloan.planner2024.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.widget.TextView;


import com.myloan.planner2024.ChromeUtils;
import com.google.android.material.navigation.NavigationView;
import com.myloan.planner2024.MainActivity;
import com.myloan.planner2024.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Home extends Fragment {

    Button btnemi,btnrd,btnfd,btncisi,btnppf;
    NavController navController;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    CardView cardcompare,cardcomputeloanamount;
    TextView tvgetduration,tvgetinterest,tvgetmoratorium;

    NavigationView navigationView;
    TextView tvgstcalc,tvvatcalc;
    ProgressDialog progressDialog;
    LinearLayout ll_Gstcal,ll_Vatcalc,ll_Emical,ll_devops,ll_privacypolicy,ll_share,ll_fdcalc;

    private int retryAttempt;
    private void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_home, container, false);
        changeStatusBarColor(Color.parseColor("#03A9F4"));
       btnemi=view.findViewById(R.id.btnemi);
       btncisi=view.findViewById(R.id.btnsici);
       btnfd=view.findViewById(R.id.btnfd);
       btnrd=view.findViewById(R.id.btnrd);
       btnppf=view.findViewById(R.id.btnppf);
       tvgetduration=view.findViewById(R.id.tvcalduration);
       tvgetmoratorium=view.findViewById(R.id.tvgetmoratorium);
       cardcompare=view.findViewById(R.id.cardcompare);
       tvgetinterest=view.findViewById(R.id.tvgetinterest);
        tvgstcalc=view.findViewById(R.id.tvgstcalc);
        tvvatcalc=view.findViewById(R.id.tvvatcalc);
        cardcomputeloanamount=view.findViewById(R.id.cardcomputeloanamount);
        drawerLayout = getActivity().findViewById(R.id.drawer);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.baseline_menu_24);
        navigationView = getActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
       ll_Gstcal = headerView.findViewById(R.id.ll_GstCalc);
       ll_devops=headerView.findViewById(R.id.ll_devops);
       ll_Emical=headerView.findViewById(R.id.ll_emicalc);
       ll_Vatcalc=headerView.findViewById(R.id.ll_VatCalc);
       ll_privacypolicy=headerView.findViewById(R.id.ll_privacy);
       ll_share=headerView.findViewById(R.id.ll_share);
       ll_fdcalc=headerView.findViewById(R.id.ll_fdcalc);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progressbar);




        ll_Gstcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.close();
                navController.navigate(R.id.GSTCalc);
            }
        });
        ll_Vatcalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.close();
                navController.navigate(R.id.vatCal);
            }
        });
        ll_Emical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.close();
               navigateToDestination(R.id.EMI); //inter3
            }
        });

        ll_privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.close();
                navController.navigate(R.id.action_home2_to_privacy_policy);
            }
        });
        ll_devops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  drawerLayout.close();

                String url = "http://www.reemzetdeveloper.in";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        ll_fdcalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.close();
                navigateToDestination(R.id.fdcalculation);//interone
            }
        });






        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });
       btnemi.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               navigateToDestination(R.id.action_home2_to_EMI);
           }
       });
       cardcompare.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               navigateToDestination(R.id.action_home2_to_CompareEmi);
           }
       });
        cardcomputeloanamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDestination(R.id.action_home2_to_LoanAmount);
            }
        });
        tvgetduration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDestination(R.id.action_home2_to_tenure);
            }
        });
        tvgetinterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });
        tvgetmoratorium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });

        btncisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");

            }
        });

        btnfd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });

        btnrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });

        btnppf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // navigateToDestination(R.id.action_home2_to_PPFCalc);
            }
        });
        tvgstcalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });
        tvvatcalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChromeUtils.openUrlInChrome(getActivity(), "https://nationkhabar.in/");
            }
        });


    return view;
    }






    private void loadAndNavigateToDestination(final int destinationFragmentId, String adUnitId) {
       // progressDialog.show();
        navigateToDestination(destinationFragmentId);
       /* MaxInterstitialAd interstitialAd = new MaxInterstitialAd(adUnitId, getActivity());
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
                }, 4000);
            }


            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                // Handle error, you might want to hide the loading indicator and show a message
                progressDialog.dismiss();
                navigateToDestination(destinationFragmentId);
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
        interstitialAd.loadAd();*/
    }

    private void navigateToDestination(int destinationFragmentId) {
        navController.navigate(destinationFragmentId);
    }








    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showAppBar();
    }





}