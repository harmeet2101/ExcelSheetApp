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
import android.widget.AdapterView;
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


public class ScreenSlidePageFragment extends Fragment {


    public static final String ARG_PAGE = "page";
    private int mPageNumber;
    private IDataSource iDataSource;


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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);

        TextView textView=(TextView)rootView.findViewById(R.id.text1);
        textView.setText(textView.getText().toString()+(getPageNumber()+1)+": "+iDataSource.getQuestions(getPageNumber()+1));
        String responseType=iDataSource.getResponseType(getPageNumber()+1);
        ArrayList<String> respList=iDataSource.getValidRespList(getPageNumber() + 1);
        HashMap<String,ArrayList<String>> map=new HashMap<>();
        map.put(responseType,respList);
        LinearLayout linearLayout=(LinearLayout)rootView.findViewById(R.id.linear);

        switch (responseType){

            case "Select":
                Spinner spinner=new Spinner(getActivity());
                spinner.setId(android.R.id.content);
                List<String> list=map.get("Select");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
                linearLayout.addView(spinner);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item=parent.getItemAtPosition(position).toString();
                        iDataSource.setResponses(item,mPageNumber);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
            case "Radio":
                final RadioGroup radioGroup=new RadioGroup(getActivity());
                linearLayout.addView(radioGroup);
                List<String> list1=map.get("Radio");
                RadioButton product=null;
                for (int i=0;i<list1.size();i++){
                    product = new RadioButton(getActivity());
                    radioGroup.addView(product);
                    product.setText(list1.get(i));
                    product.setId(i+1);
                }
                /*int id=radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) getActivity().findViewById(id);
                String item = radioButton.getText().toString();
                iDataSource.setResponses(item, mPageNumber);*/

                break;
            case "Number":
                EditText editText=new EditText(getActivity());
                editText.setHint("Enter Number");
                editText.setId(android.R.id.content);
                linearLayout.addView(editText);
                iDataSource.setResponses("" + editText.getText().toString(), mPageNumber);
                break;
            case "Checkbox":
                List<String> list2=map.get("Checkbox");
                for (int i=0;i<list2.size();i++){
                    CheckBox checkBox=new CheckBox(getActivity());
                    checkBox.setId(i+1);
                    checkBox.setText(list2.get(i));
                    linearLayout.addView(checkBox);
                }
                break;
        }
        return rootView;
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


}
