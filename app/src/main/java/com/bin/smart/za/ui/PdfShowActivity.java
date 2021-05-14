package com.bin.smart.za.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.bin.smart.za.R;
import com.bin.smart.za.ui.StudentActivity.VideoAndTextDetails;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdfShowActivity extends AppCompatActivity
{
    private TextView text1Pdf;
    private PDFView pdfView;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_show);
        text1Pdf = findViewById(R.id.text1_pdf);
        pdfView = (PDFView) findViewById(R.id.pdf_view);

          String value=loadData("uri_pdf");
          System.out.println("********************/n = " + value);
          text1Pdf.setText(value);
          String uri = text1Pdf.getText().toString();
        new RetrivePdfStream().execute(uri);

    }


        class RetrivePdfStream extends AsyncTask<String,Void, InputStream>
        {

            @Override
            protected InputStream doInBackground(String... strings) {
                InputStream inputStream = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

                    if (urlConnection.getResponseCode() == 200)
                    {
                        inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    }
                }
                catch (IOException e)
                {
                    return null;
                }
                return inputStream;
            }

            @Override
            protected void onPostExecute(InputStream inputStream) {
                pdfView.fromStream(inputStream).load();
            }
        }

    public String loadData(String key)
    {
        sharedPreferences = PdfShowActivity.this.getSharedPreferences(
                "Garage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = sharedPreferences.getString(key , "000");
        return value;

    }

    }
