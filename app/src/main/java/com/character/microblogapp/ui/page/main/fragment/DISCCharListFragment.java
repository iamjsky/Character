package com.character.microblogapp.ui.page.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MCharacterInfo2;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.util.PrefMgr;
import com.character.microblogapp.util.Toaster;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;


public class DISCCharListFragment extends BaseFragment {

    //



    PrefMgr m_prefMgr;

    String parentFrag = "";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createView(inflater, container, R.layout.fragment_disc_charlist);
        SharedPreferences prefs = getActivity().getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        m_prefMgr = new PrefMgr(prefs);
//You need to add the following line for this solution to work; thanks skayred

        parentFrag = getTag();
        mRoot.setFocusableInTouchMode(true);
        mRoot.requestFocus();

        mRoot.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        rlt_backClicked();
                        return true;
                    }
                }

                return false;
            }
        });



        return mRoot;
    }


    @Override
    protected void initUI() {


    }


    @OnClick(R.id.rlt_back)
    public void rlt_backClicked() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.selectLine(0);
        if(parentFrag.equals("CharInfoFragment")){
            mainActivity.selectTab(8);
        }else if(parentFrag.equals("DISCMyLoverFragment")){
            mainActivity.selectTab(15);
        }



    }

    @OnClick({R.id.tv_showChar_01, R.id.tv_showChar_02, R.id.tv_showChar_03, R.id.tv_showChar_04
            , R.id.tv_showChar_05, R.id.tv_showChar_06, R.id.tv_showChar_07, R.id.tv_showChar_08
            , R.id.tv_showChar_09, R.id.tv_showChar_10, R.id.tv_showChar_11, R.id.tv_showChar_12
            , R.id.tv_showChar_13, R.id.tv_showChar_14, R.id.tv_showChar_15, R.id.tv_showChar_16 })
   public void tv_showCharClicked(View view){
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        switch (view.getId()){

            case R.id.tv_showChar_01:

                mainActivity.charListType = "D";
                break;
            case R.id.tv_showChar_02:
                mainActivity.charListType = "DI";
                break;
            case R.id.tv_showChar_03:
                mainActivity.charListType = "DS";
                break;
            case R.id.tv_showChar_04:
                mainActivity.charListType = "DC";
                break;
            case R.id.tv_showChar_05:
                mainActivity.charListType = "I";
                break;
            case R.id.tv_showChar_06:
                mainActivity.charListType = "ID";
                break;
            case R.id.tv_showChar_07:
                mainActivity.charListType = "IS";
                break;
            case R.id.tv_showChar_08:
                mainActivity.charListType = "IC";
                break;
            case R.id.tv_showChar_09:
                mainActivity.charListType = "S";
                break;
            case R.id.tv_showChar_10:
                mainActivity.charListType = "SD";
                break;
            case R.id.tv_showChar_11:
                mainActivity.charListType = "SI";
                break;
            case R.id.tv_showChar_12:
                mainActivity.charListType = "SC";
                break;
            case R.id.tv_showChar_13:
                mainActivity.charListType = "C";
                break;
            case R.id.tv_showChar_14:
                mainActivity.charListType = "CD";
                break;
            case R.id.tv_showChar_15:
                mainActivity.charListType = "CI";
                break;
            case R.id.tv_showChar_16:
                mainActivity.charListType = "CS";
                break;


        }
        mainActivity.selectLine(0);
        mainActivity.selectTab(14);

    }


}
