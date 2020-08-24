package com.character.microblogapp.ui.page.intro.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class IntroPageItem03Fragment extends BaseFragment {
    private int frag_num;
    public static IntroPageItem03Fragment instance;




    public IntroPageItem03Fragment() {
    }

    // newInstance constructor for creating fragment with arguments
    public static IntroPageItem03Fragment newInstance(int num) {
        IntroPageItem03Fragment fragment = new IntroPageItem03Fragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frag_num = getArguments().getInt("num", 0);

    }   //  onCreate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_intro_page_03, container, false);
        ButterKnife.bind(this, view);
        instance = this;




        return view;

    }   //  onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);
    }   //  onViewCreated

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null) {
            return;
        }


    }   //  onResume

    @Override
    public void onStop() {
        super.onStop();
    }






}
