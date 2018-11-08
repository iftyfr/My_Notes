package com.example.ifty.mynotes;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

public class CreditActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    private static final int CALL_PHONE_PERMISSION_CODE = 1;
    private static final int SEND_SMS_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.credit_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void callPhone(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" +"+8801516172736"));
            startActivity(callIntent);
        }
        else {
            requestCallphonePermission();
        }

    }

    private void requestCallphonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("To make a call this permission is needed.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CreditActivity.this,new String[] {Manifest.permission.CALL_PHONE},CALL_PHONE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CALL_PHONE},CALL_PHONE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CALL_PHONE_PERMISSION_CODE:
                if (grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Perission Denied", Toast.LENGTH_SHORT).show();
                }

            case SEND_SMS_PERMISSION_CODE:if (grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Perission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void sendSMS(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "+8801516172736"));
            startActivity(smsIntent);
        }
        else {
            requestSendSMSPermission();
        }

    }

    private void requestSendSMSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("To send a SMS this permission is needed.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CreditActivity.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }

        else {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_CODE);
        }
    }

    public void sendEmail(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ifyfr6@gmail.com"});
        emailIntent.setType("message/rfc822");
        try {
            startActivity(Intent.createChooser(emailIntent, "Choose an email client: (ex: Gmail app)"));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_LONG).show();
        }
    }
}
