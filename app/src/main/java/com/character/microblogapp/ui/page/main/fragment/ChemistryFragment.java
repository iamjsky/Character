package com.character.microblogapp.ui.page.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.util.PrefMgr;
import com.character.microblogapp.util.Toaster;

import butterknife.BindView;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ChemistryFragment extends BaseFragment {

    @BindView(R.id.layout_charSelectPopup)
    LinearLayout layout_charSelectPopup;

    @BindView(R.id.layout_main)
            LinearLayout layout_main;
    @BindView(R.id.layout_result)
    LinearLayout layout_result;

    PrefMgr m_prefMgr;
    int popUpState = 0;

    /**
     * d i s c -> 0 1 2 3
     */
    private int selectedCharMan = -1;
    private int selectedCharWoman = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createView(inflater, container, R.layout.fragment_chemistry);
        SharedPreferences prefs = getActivity().getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        m_prefMgr = new PrefMgr(prefs);
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
        layout_main.setVisibility(View.VISIBLE);
        layout_result.setVisibility(View.GONE);
    }


    @OnClick(R.id.rlt_back)
    public void rlt_backClicked() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.selectLine(0);
        if(popUpState == 0){
            selectedCharMan = -1;
            selectedCharWoman = -1;
            mainActivity.selectTab(0);

        }else{
            layout_charSelectPopup.setVisibility(View.GONE);
            popUpState = 0;
            selectedCharMan = -1;
            selectedCharWoman = -1;
        }

    }

    @OnClick(R.id.btn_checkChemistry)
    public void btn_checkChemistryClicked() {
        if(selectedCharMan != -1 && selectedCharWoman != -1){
            ChemistryModel chemistryModel = (ChemistryModel) getChemistryResult();

            layout_main.setVisibility(View.GONE);
            layout_result.setVisibility(View.VISIBLE);

        }else{
            Toaster.showShort(mParent, "DISC 유형을 모두 선택해 주세요.");
        }

    }

    @OnClick(R.id.layout_charMan)
    public void layout_charManClicked() {
        popUpState = 1;
        layout_charSelectPopup.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.layout_charWoman)
    public void layout_charWomanClicked() {
        popUpState = 2;
        layout_charSelectPopup.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_d)
    public void tv_dClicked(){
        if(popUpState == 1){
          selectedCharMan = 0;
        }else{
          selectedCharWoman = 0;
        }
    }
    @OnClick(R.id.tv_i)
    public void tv_iClicked(){
        if(popUpState == 1){
            selectedCharMan = 1;
        }else{
            selectedCharWoman = 1;
        }
    }
    @OnClick(R.id.tv_s)
    public void tv_sClicked(){
        if(popUpState == 1){
            selectedCharMan = 2;
        }else{
            selectedCharWoman = 2;
        }
    }
    @OnClick(R.id.tv_c)
    public void tv_cClicked(){
        if(popUpState == 1){
            selectedCharMan = 3;
        }else{
            selectedCharWoman = 3;
        }
    }

    private ChemistryModel getChemistryResult(){
        int gender = MyInfo.getInstance().gender;
        int percent = 0;
        String desc = "";
        if(gender == 1){
            if(selectedCharMan == 0){
                switch (selectedCharWoman){
                    case 0:
                        desc = getString(R.string.dd);
                        percent = 95;
                        break;

                    case 1:
                        desc = getString(R.string.di);
                        percent = 80;
                        break;
                    case 2:
                        desc = getString(R.string.ds);
                        percent = 70;
                        break;
                    case 3:
                        desc = getString(R.string.dc);
                        percent = 10;
                        break;

                }
            }else if(selectedCharMan == 1){
                switch (selectedCharWoman){
                    case 0:
                        desc = getString(R.string.id);
                        percent = 80;
                        break;
                    case 1:
                        desc = getString(R.string.ii);
                        percent = 99;
                        break;
                    case 2:
                        desc = getString(R.string.is);
                        percent = 60;
                        break;
                    case 3:
                        desc = getString(R.string.ic);
                        percent = 5;
                        break;

                }
            }else if(selectedCharMan == 2){
                switch (selectedCharWoman){
                    case 0:
                        desc = getString(R.string.sd);
                        percent = 40;
                        break;
                    case 1:
                        desc = getString(R.string.si);
                        percent = 60;
                        break;
                    case 2:
                        desc = getString(R.string.ss);
                        percent = 99;
                        break;
                    case 3:
                        desc = getString(R.string.sc);
                        percent = 90;
                        break;

                }
            }else if(selectedCharMan == 3){
                switch (selectedCharWoman){
                    case 0:
                        desc = getString(R.string.cd);
                        percent = 20;
                        break;
                    case 1:
                        desc = getString(R.string.ci);
                        percent = 10;
                        break;
                    case 2:
                        desc = getString(R.string.cs);
                        percent = 90;
                        break;
                    case 3:
                        desc = getString(R.string.cc);
                        percent = 80;
                        break;

                }
            }
        }else if(gender == 2){
            if(selectedCharMan == 0){
                switch (selectedCharWoman){
                    case 0:
                        desc = getString(R.string.dd);
                        percent = 98;
                        break;
                    case 1:
                        desc = getString(R.string.di);
                        percent = 80;
                        break;
                    case 2:
                        desc = getString(R.string.ds);
                        percent = 40;
                        break;
                    case 3:
                        desc = getString(R.string.dc);
                        percent = 20;
                        break;

                }
            }else if(selectedCharMan == 1){
                switch (selectedCharWoman){
                    case 0:
                        desc = getString(R.string.id);
                        percent = 80;
                        break;
                    case 1:
                        desc = getString(R.string.ii);
                        percent = 99;
                        break;
                    case 2:
                        desc = getString(R.string.is);
                        percent = 40;
                        break;
                    case 3:
                        desc = getString(R.string.ic);
                        percent = 10;
                        break;

                }
            }else if(selectedCharMan == 2){
                switch (selectedCharWoman){
                    case 0:
                        desc = getString(R.string.sd);
                        percent = 70;
                        break;
                    case 1:
                        desc = getString(R.string.si);
                        percent = 60;
                        break;
                    case 2:
                        desc = getString(R.string.ss);
                        percent = 99;
                        break;
                    case 3:
                        desc = getString(R.string.sc);
                        percent = 90;
                        break;

                }
            }else if(selectedCharMan == 3){
                switch (selectedCharWoman){
                    case 0:
                        desc = getString(R.string.cd);
                        percent = 10;
                        break;
                    case 1:
                        desc = getString(R.string.ci);
                        percent = 5;
                        break;
                    case 2:
                        desc = getString(R.string.cs);
                        percent = 90;
                        break;
                    case 3:
                        desc = getString(R.string.cc);
                        percent = 80;
                        break;

                }
            }
        }

        ChemistryModel chemistryModel = new ChemistryModel("D", "D",
                getDISCColor(selectedCharMan), getDISCColor(selectedCharWoman), percent+"%", getPercentColor(percent), getPercentImg(percent), desc);
        return chemistryModel;

    }

    class ChemistryModel{
        public String charMan;
        public String charWoman;
        public int manColor;
        public int womanColor;
        public String percent;
        public int percentColor;
        public Drawable imgRes;
        public String desc;

        public ChemistryModel(String charMan, String charWoman, int manColor,
                              int womanColor, String percent, int percentColor, Drawable imgRes, String desc) {
            this.charMan = charMan;
            this.charWoman = charWoman;
            this.manColor = manColor;
            this.womanColor = womanColor;
            this.percent = percent;
            this.percentColor = percentColor;
            this.imgRes = imgRes;
            this.desc = desc;
        }
    }
    private int getDISCColor(int type) {
        int discColor;
        switch (type) {

            case 0:
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.char_d_color);

                break;
            case 1:
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.char_i_color);

                break;
            case 2:
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.char_s_color);
                break;
            case 3:
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.char_c_color);

                break;

            default:
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.color_char_gray);


        }
        return discColor;
    }
    private Drawable getPercentImg(int percent) {
        Drawable drawable = null;
        switch (percent) {

            case 5:
                drawable = getResources().getDrawable(R.drawable.relation_img_5);
                break;
            case 10:
                drawable = getResources().getDrawable(R.drawable.relation_img_10);
                break;
            case 20:
                drawable = getResources().getDrawable(R.drawable.relation_img_20);
                break;
            case 40:
                drawable = getResources().getDrawable(R.drawable.relation_img_40);
                break;
            case 60:
                drawable = getResources().getDrawable(R.drawable.relation_img_60);
                break;
            case 70:
                drawable = getResources().getDrawable(R.drawable.relation_img_70);
                break;
            case 80:
                drawable = getResources().getDrawable(R.drawable.relation_img_80);
                break;
            case 90:
                drawable = getResources().getDrawable(R.drawable.relation_img_90);
                break;
            case 95:
                drawable = getResources().getDrawable(R.drawable.relation_img_95);
                break;
            case 98:
                drawable = getResources().getDrawable(R.drawable.relation_img_98);
                break;
            case 99:
                drawable = getResources().getDrawable(R.drawable.relation_img_99);
                break;

            default:


        }
        return drawable;
    }

    private int getPercentColor(int percent) {
        int percentColor;
       if(percent >= 90) {
           percentColor = ContextCompat.getColor(getApplicationContext(), R.color.chemistry_percent_color_90_99);
       }else if(percent >= 80) {
           percentColor = ContextCompat.getColor(getApplicationContext(), R.color.chemistry_percent_color_60_80);
       }else if(percent >= 20) {
           percentColor = ContextCompat.getColor(getApplicationContext(), R.color.chemistry_percent_color_20_40);
       }else if(percent >= 5) {
           percentColor = ContextCompat.getColor(getApplicationContext(), R.color.chemistry_percent_color_5_10);
       }else{
           percentColor = ContextCompat.getColor(getApplicationContext(), R.color.color_char_gray);
       }




        return percentColor;
    }
}
