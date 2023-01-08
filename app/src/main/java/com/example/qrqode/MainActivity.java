package com.example.qrqode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrqode.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //view Object
    private Button btnScan;
    private TextView txtName,txtKelas,txtNIM;
    //QR Scanning Object
    private IntentIntegrator QRScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //view Object
        btnScan = (Button)findViewById(R.id.btnScan);

        txtName = (TextView) findViewById(R.id.txtName);
        txtKelas = (TextView) findViewById(R.id.txtclass);
        txtNIM = (TextView) findViewById(R.id.nim);
        //Initial
        QRScan = new IntentIntegrator(this);
        //Action Object
        btnScan.setOnClickListener(this);
    }
    //Get Result Scanning


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scanning result not found", Toast.LENGTH_LONG).show();
            } else if (Patterns.EMAIL_ADDRESS.matcher(result.getContents()).matches()) {
                // Open an email composer with the scanned email address as the recipient
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + result.getContents()));
                startActivity(emailIntent);
            } else if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                // Open the URL in the device's web browser
                Intent visiturl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visiturl);
            } else if (Patterns.PHONE.matcher(result.getContents()).matches()) {
                // Attempt to make a phone call to the scanned number
                String telp = String.valueOf(result.getContents());
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + telp));
                startActivity(callIntent);
            } else if (result.getContents().startsWith("geo:") || result.getContents().startsWith("google.navigation:")) {
                // Open the map location in the device's map app
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(mapIntent);
            } else {
                try {
                    // Parse the result as a JSON object and display the contents
                    JSONObject obj = new JSONObject(result.getContents());
                    txtName.setText(obj.getString("name"));
                    txtKelas.setText(obj.getString("class"));
                    txtNIM.setText(obj.getString("nim"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    @Override
    public void onClick(View view) {
        //I QRcode
        QRScan.initiateScan();
    }
}