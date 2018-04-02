package com.a4.cis350.when2meetmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class IndividualFragment extends Fragment {

    Button calAvailInd;

    public IndividualFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // creating the button for users to enter their availabilities for the event
        calAvailInd = getView().findViewById(R.id.calAvailIndiv);
        calAvailInd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent showCal = new Intent(getActivity().getApplicationContext(), PopupCalActivity.class);
                startActivity(showCal);
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_individual, container, false);
    }


}
