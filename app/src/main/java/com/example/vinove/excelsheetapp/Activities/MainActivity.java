package com.example.vinove.excelsheetapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vinove.excelsheetapp.Model.PaperSheet;
import com.example.vinove.excelsheetapp.Poi.ExcelSheet;
import com.example.vinove.excelsheetapp.R;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private Button surveybutton;
    private ExcelSheet excelSheet;
    private ProgressDialog progressDialog;
    private PaperSheet[] ps;
    public void onCreate(Bundle bundle){

        super.onCreate(bundle);
        setContentView(R.layout.layout_main);
        surveybutton=(Button)findViewById(R.id.startSurvey);
        surveybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSurvey();
            }
        });

    }


    public void startSurvey(){
        try {
            InputStream inputStream=getAssets().open("Input.xlsx");
            excelSheet=new ExcelSheet(this);
            new MyTask().execute(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class MyTask extends AsyncTask<InputStream,Void,Boolean>{


        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(MainActivity.this,ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Reading file");
            progressDialog.setMessage("Please wait a moment...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            if(aBoolean) {
                Intent intent = new Intent(getBaseContext(), SurveyActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("data",ps);
                intent.putExtras(bundle);
                startActivity(intent);
            }else
                Toast.makeText(getBaseContext(),"error reading file",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(InputStream... params) {

            boolean result=false;
            if(params[0]!=null){
                ps=excelSheet.readExcelSheet(params[0]);
                if(ps!=null && ps.length>0)
                    result=true;
            }
            return result;
        }
    }

}
