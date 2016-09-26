/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vinove.excelsheetapp.Activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vinove.excelsheetapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ScreenSlidePageFragment extends Fragment{


    public static final String ARG_PAGE = "page";
    private int mPageNumber;
    private IDataSource iDataSource;
    private ViewGroup rootView;

    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            iDataSource = (IDataSource) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + ScreenSlidePageFragment.IDataSource.class.getSimpleName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        /*Toast.makeText(getActivity(), "onCreate()" + mPageNumber, Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

         rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);
        initView(rootView);

        return rootView;
    }


    public void onResume(){

        super.onResume();
        /*Toast.makeText(getActivity(), "onResume()" + mPageNumber, Toast.LENGTH_SHORT).show();*/
    }

    public void onPause(){
        super.onPause();
        /*Toast.makeText(getActivity(), "onPause()" + mPageNumber, Toast.LENGTH_SHORT).show();*/
    }

    public int getPageNumber() {
        return mPageNumber;
    }


    public interface IDataSource{

        String getQuestions(int position);
        String getResponseType(int position);
        ArrayList<String> getValidRespList(int position);
        void setResponses(String answer,int position);
    }


    public void initView(View rootView){
        TextView textView=(TextView)rootView.findViewById(R.id.text1);
        textView.setText(textView.getText().toString()+(getPageNumber()+1)+": "+iDataSource.getQuestions(getPageNumber()+1));
        String responseType=iDataSource.getResponseType(getPageNumber() + 1);
        ArrayList<String> respList=iDataSource.getValidRespList(getPageNumber() + 1);
        HashMap<String,ArrayList<String>> map=new HashMap<>();
        map.put(responseType, respList);

        LinearLayout linearLayout=(LinearLayout)rootView.findViewById(R.id.linear);
        switch (responseType){

            case "Select":
                    spinnerViewHolder=new SpinnerViewHolder(linearLayout,map);
                break;
            case "Radio":
                radioViewHolder=new RadioViewHolder(linearLayout,map);
                break;
            case "Number":
                editViewHolder=new EditViewHolder(linearLayout);
                break;
            case "Checkbox":
               checkboxViewHolder= new CheckboxViewHolder(linearLayout,map);
                break;
        }
    }

    EditViewHolder editViewHolder;
    SpinnerViewHolder spinnerViewHolder;
    RadioViewHolder radioViewHolder;
    CheckboxViewHolder checkboxViewHolder;

    public class EditViewHolder{

        public EditText editText;

        public EditViewHolder(String responseType){}
        public EditViewHolder(LinearLayout linearLayout){
            editText=new EditText(getActivity());
            editText.setHint("Enter Number");
            editText.setId(android.R.id.content);
            linearLayout.addView(editText);
        }

        public String getData(){
            return editText.getText().toString();
        }

    }
    public class SpinnerViewHolder{
        public Spinner spinner;

        public SpinnerViewHolder(){}

        public SpinnerViewHolder(LinearLayout linearLayout,HashMap<String,ArrayList<String>> map){
            spinner=new Spinner(getActivity());
            spinner.setId(android.R.id.content);
            List<String> list=map.get("Select");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            linearLayout.addView(spinner);
           /* spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    iDataSource.setResponses(getData(),getPageNumber());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
        }
        public String getData(){
            if(spinner.isSelected())
           return spinner.getSelectedItem().toString();
            else
                return null;
        }
    }

    public class RadioViewHolder{

        public  RadioGroup radioGroup;
        public RadioButton product=null;
        public RadioViewHolder(){}

        public RadioViewHolder(LinearLayout linearLayout,HashMap<String,ArrayList<String>> map){

            radioGroup=new RadioGroup(getActivity());
            linearLayout.addView(radioGroup);
            List<String> list1=map.get("Radio");

            for (int i=0;i<list1.size();i++){
                product = new RadioButton(getActivity());
                product.setText(list1.get(i));
                product.setId(i+1);
                radioGroup.addView(product);
            }

        }

    }

    public class CheckboxViewHolder{

        public CheckBox checkBox;
        public CheckboxViewHolder(){}

        public CheckboxViewHolder(LinearLayout linearLayout,HashMap<String,ArrayList<String>> map){
            List<String> list2=map.get("Checkbox");
            for (int i=0;i<list2.size();i++){
                checkBox=new CheckBox(getActivity());
                checkBox.setId(i+1);
                checkBox.setText(list2.get(i));
                linearLayout.addView(checkBox);
            }
        }
    }





    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public String sendDataToActivity(String response){

        switch (response){
            case "Select":
               return "spinner";
            case "Radio":
               return "Radio";
            case "Number":
             return editViewHolder.getData();
            case "Checkbox":
                return "Checkbox";
        }
        return response;
    }
}
