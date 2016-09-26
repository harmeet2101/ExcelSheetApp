package com.example.vinove.excelsheetapp.Activities;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vinove.excelsheetapp.Model.PaperSheet;
import com.example.vinove.excelsheetapp.Poi.ExcelSheet;
import com.example.vinove.excelsheetapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SurveyActivity extends FragmentActivity implements ScreenSlidePageFragment.IDataSource{

    private   int NUM_PAGES=0;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private PaperSheet[] ps;
    private Button leftControllButton,rightControllButton;
    private boolean is_SdCard_Present;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_survey_slide_pane);
        mPager = (ViewPager) findViewById(R.id.pager);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            ps= (PaperSheet[]) bundle.getSerializable("data");
            NUM_PAGES=ps.length-1;

        }
        is_SdCard_Present=checkSdcard();
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), ps);
        mPagerAdapter.notifyDataSetChanged();
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    leftControllButton.setVisibility(View.INVISIBLE);
                else
                    leftControllButton.setVisibility(View.VISIBLE);

                if (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1) {
                    rightControllButton.setText("Submit");
                } else
                    rightControllButton.setText("Next");

            }
        });
        leftControllButton=(Button)findViewById(R.id.left);
        rightControllButton=(Button)findViewById(R.id.right);
        leftControllButton.setVisibility(View.INVISIBLE);

        leftControllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });
        rightControllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_SdCard_Present)
                takeScreenshot();
                if(!rightControllButton.getText().toString().equalsIgnoreCase("Submit")) {

                    /*if(resPos==mPager.getCurrentItem()){
                        Toast.makeText(getBaseContext(), pageFragment.sendDataToActivity(mPager.getCurrentItem()), Toast.LENGTH_SHORT).show();
                    }*/
                    String data=    ps[mPager.getCurrentItem()+1].getResponseTypes();
                    String res=pageFragment.sendDataToActivity(data);
                    Toast.makeText(getBaseContext(), res, Toast.LENGTH_SHORT).show();
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                }else {
                    ExcelSheet excelSheet=new ExcelSheet(getBaseContext());
                    excelSheet.writeExcelSheet("/storage/sdcard0/"+getAppLabel());
                }
            }
        });

    }

    private ScreenSlidePageFragment pageFragment;
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        PaperSheet[] ps;

        public ScreenSlidePagerAdapter(FragmentManager fm,PaperSheet[] ps) {
            super(fm);
            this.ps=ps;
        }

        @Override
        public Fragment getItem(int position) {
            pageFragment=ScreenSlidePageFragment.create(position);
            return pageFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public String getQuestions(int position) {

        return this.ps[position].getQuestions();
    }

    @Override
    public String getResponseType(int position) {
        return this.ps[position].getResponseTypes();
    }


    @Override
    public ArrayList<String> getValidRespList(int position) {
        return this.ps[position].getValidRespList();
    }


    public void takeScreenshot()
    {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap=rootView.getDrawingCache();
        saveBitmap(bitmap,mPager.getCurrentItem()+1,rootView);
    }


    public String getAppLabel() {
        PackageManager packageManager = getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(SurveyActivity.this.getApplicationInfo().packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "Unknown");
    }

    public void saveBitmap(Bitmap bitmap,int position,View view)
    {
        File imagePath = new File("/storage/sdcard0/"+getAppLabel()+"/screenshots");
        imagePath.mkdirs();
        File imageFile = new File(imagePath,"Question-"+position+".jpg");
        FileOutputStream fos;
        try
        {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            view.setDrawingCacheEnabled(false);
        }
        catch (FileNotFoundException e)
        {
            Log.e("GREC", e.getMessage(), e);
        }
        catch (IOException e)
        {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    public boolean checkSdcard(){
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        return isSDPresent;
    }

    @Override
    public void setResponses(String answer,int position) {
        result=answer;
        resPos=position;
    }

    private String result;
    private int resPos;
}
