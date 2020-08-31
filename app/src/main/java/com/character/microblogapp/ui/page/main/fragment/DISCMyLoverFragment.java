package com.character.microblogapp.ui.page.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MCharacterInfo2;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.util.PrefMgr;
import com.character.microblogapp.util.Toaster;
import com.nex3z.flowlayout.FlowLayout;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;


public class DISCMyLoverFragment extends BaseFragment {

    //
    @BindView(R.id.tv_char_01)
    TextView tv_char_01;

    @BindView(R.id.tv_char_02)
    TextView tv_char_02;


    @BindView(R.id.tv_charLover_01)
    TextView tv_charLover_01;

    @BindView(R.id.tv_charLover_02)
    TextView tv_charLover_02;

    @BindView(R.id.layout_result)
    LinearLayout layout_result;

    @BindView(R.id.tv_layer_01_char_01)
    TextView tv_layer_01_char_01;
    @BindView(R.id.tv_layer_01_char_02)
    TextView tv_layer_01_char_02;
    @BindView(R.id.tv_layer_02_char_01)
    TextView tv_layer_02_char_01;
    @BindView(R.id.tv_layer_02_char_02)
    TextView tv_layer_02_char_02;
    @BindView(R.id.tv_layer_03_char_01)
    TextView tv_layer_03_char_01;
    @BindView(R.id.tv_layer_03_char_02)
    TextView tv_layer_03_char_02;

    @BindArray(R.array.lover_d_array)
    String[] lover_d_array;
    @BindArray(R.array.lover_i_array)
    String[] lover_i_array;
    @BindArray(R.array.lover_s_array)
    String[] lover_s_array;
    @BindArray(R.array.lover_c_array)
    String[] lover_c_array;

    @BindView(R.id.iv_topBg)
            ImageView iv_topBg;
    PrefMgr m_prefMgr;

    String type = "";
    int nowPage = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createView(inflater, container, R.layout.fragment_disc_mylover);
        SharedPreferences prefs = getActivity().getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        m_prefMgr = new PrefMgr(prefs);
        type = MyInfo.getInstance().character;
        type = type.toUpperCase();
        if(type.length() > 2){
            type = type.substring(0,2);
        }
//You need to add the following line for this solution to work; thanks skayred

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

        getInfo();

        return mRoot;
    }


    @Override
    protected void initUI() {


    }


    @OnClick(R.id.rlt_back)
    public void rlt_backClicked() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.selectLine(0);
        mainActivity.selectTab(8);


    }

    @OnClick(R.id.layout_char_01)
    public void layout_char_01Clicked() {
        String selectChar =    tv_layer_01_char_01.getText().toString()+tv_layer_01_char_02.getText().toString() + "";
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.charInfoPageNum = 2;
        mainActivity.charListType = selectChar;
        mainActivity.selectLine(0);
        mainActivity.selectTab(14);
    }
    @OnClick(R.id.layout_char_02)
    public void layout_char_02Clicked() {
        String selectChar =    tv_layer_02_char_01.getText().toString()+tv_layer_02_char_02.getText().toString() + "";
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.charInfoPageNum = 2;
        mainActivity.charListType = selectChar;
        mainActivity.selectLine(0);
        mainActivity.selectTab(14);
    }
    @OnClick(R.id.layout_char_03)
    public void layout_char_03Clicked() {
        String selectChar =    tv_layer_03_char_01.getText().toString()+tv_layer_03_char_02.getText().toString() + "";
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.charInfoPageNum = 2;
        mainActivity.charListType = selectChar;
        mainActivity.selectLine(0);
        mainActivity.selectTab(14);
    }
//    @OnClick(R.id.layout_charListArea)
//    public void layout_charListAreaClicked() {
//        MainActivity mainActivity = (MainActivity) getActivity();
//        mainActivity.parentFrag = "DISCMyLoverFragment";
//        mainActivity.selectLine(0);
//        mainActivity.selectTab(16);
//    }


    void setUI() {
        if (result == null) {
            return;
        }

        nowPage = 0;

        //add
        String personality1 = result.personality1 + "";
        String personality2 = result.personality2 + "";

        String gender1 = result.gender1 + "";
        String gender2 = result.gender2 + "";


        tv_char_01.setText(personality1);
        tv_char_01.setTextColor(getDISCColor(personality1));
        tv_charLover_01.setText(setDISCCharColor(gender1));
        tv_charLover_01.setVisibility(View.VISIBLE);

        if (!personality2.equals("")) {

            tv_char_02.setText(personality2);
            tv_char_02.setTextColor(getDISCColor(personality2));
            tv_charLover_02.setText(setDISCCharColor(gender2));
            tv_char_02.setVisibility(View.VISIBLE);
            tv_charLover_02.setVisibility(View.VISIBLE);
        } else {

            tv_char_02.setVisibility(View.GONE);
            tv_charLover_02.setVisibility(View.GONE);
        }

        String[] loverArray = getLoverArray(personality1 + personality2 + "");
        for (int i = 0; i < loverArray.length; i++) {
            String[] charArray = loverArray[i].split("/");
            if (i == 0) {
                tv_layer_01_char_01.setText(charArray[0] + "");
                tv_layer_01_char_01.setTextColor(getDISCColor(charArray[0] + ""));
                if (charArray.length > 1) {
                    tv_layer_01_char_02.setText(charArray[1] + "");
                    tv_layer_01_char_02.setTextColor(getDISCColor(charArray[1] + ""));
                }


            } else if (i == 1) {
                tv_layer_02_char_01.setText(charArray[0] + "");
                tv_layer_02_char_01.setTextColor(getDISCColor(charArray[0] + ""));
                if (charArray.length > 1) {
                    tv_layer_02_char_02.setText(charArray[1] + "");
                    tv_layer_02_char_02.setTextColor(getDISCColor(charArray[1] + ""));
                }


            } else if (i == 2) {
                tv_layer_03_char_01.setText(charArray[0] + "");
                tv_layer_03_char_01.setTextColor(getDISCColor(charArray[0] + ""));
                if (charArray.length > 1) {
                    tv_layer_03_char_02.setTextColor(getDISCColor(charArray[1] + ""));
                    tv_layer_03_char_02.setText(charArray[1] + "");
                }


            }


        }
        iv_topBg.setBackground(getDISCBackground(personality1, 3));
        iv_topBg.setVisibility(View.VISIBLE);
        layout_result.setVisibility(View.VISIBLE);


    }
    private Drawable getDISCBackground(String type, int layout) {
        Drawable drawable = null;
        switch (type) {

            case "D":
                if (layout == 0) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_d_01);
                } else if (layout == 1) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_d_02);
                } else if (layout == 2) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_d_02);
                } else if (layout == 3) {
                    drawable = getResources().getDrawable(R.drawable.bg_d_img);
                } else if (layout == 4) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_d_01);
                } else if (layout == 5) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_panel_d_01);
                } else if (layout == 6) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_share_d);
                }


                break;
            case "I":
                if (layout == 0) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_i_01);
                } else if (layout == 1) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_i_02);
                } else if (layout == 2) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_i_02);
                } else if (layout == 3) {
                    drawable = getResources().getDrawable(R.drawable.bg_i_img);
                } else if (layout == 4) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_i_01);
                } else if (layout == 5) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_panel_i_01);
                } else if (layout == 6) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_share_i);
                }


                break;
            case "C":
                if (layout == 0) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_c_01);
                } else if (layout == 1) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_c_02);
                } else if (layout == 2) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_c_02);
                } else if (layout == 3) {
                    drawable = getResources().getDrawable(R.drawable.bg_c_img);
                } else if (layout == 4) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_c_01);
                } else if (layout == 5) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_panel_c_01);
                } else if (layout == 6) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_share_c);
                }


                break;
            case "S":
                if (layout == 0) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_s_01);
                } else if (layout == 1) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_s_02);
                } else if (layout == 2) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_s_02);
                } else if (layout == 3) {
                    drawable = getResources().getDrawable(R.drawable.bg_s_img);
                } else if (layout == 4) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_keyword_s_01);
                } else if (layout == 5) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_charinfo_panel_s_01);
                } else if (layout == 6) {
                    drawable = getResources().getDrawable(R.drawable.bg_rounded_share_s);
                }

                break;


        }
        return drawable;
    }
    private Spannable setDISCCharColor(String desc){
        Spannable sb = new SpannableString(desc);
        for (int i = 0; i < desc.length(); i++) {
            char temp = (desc.charAt(i));
            switch (temp) {
                case 'D':
                    sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_D_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;
                case 'I':
                    sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_I_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;
                case 'S':
                    sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_S_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;
                case 'C':
                    sb.setSpan(new ForegroundColorSpan(Constant.CHARACTER_C_COLOR), i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;
            }

        }

        return sb;
    }


    private String[] getLoverArray(String type) {
        String upperType = type.toUpperCase();
        String typeToArrayString = "";
        String[] result;

        switch (upperType) {
            case "D":
                typeToArrayString = lover_d_array[0];
                break;
            case "DI":
                typeToArrayString = lover_d_array[1];
                break;
            case "DS":
                typeToArrayString = lover_d_array[2];
                break;
            case "DC":
                typeToArrayString = lover_d_array[3];
                break;
            case "I":
                typeToArrayString = lover_i_array[0];
                break;
            case "ID":
                typeToArrayString = lover_i_array[1];
                break;
            case "IS":
                typeToArrayString = lover_i_array[2];
                break;
            case "IC":
                typeToArrayString = lover_i_array[3];
                break;
            case "S":
                typeToArrayString = lover_s_array[0];
                break;
            case "SD":
                typeToArrayString = lover_s_array[1];
                break;
            case "SI":
                typeToArrayString = lover_s_array[2];
                break;
            case "SC":
                typeToArrayString = lover_s_array[3];
                break;
            case "C":
                typeToArrayString = lover_c_array[0];
                break;
            case "CD":
                typeToArrayString = lover_c_array[1];
                break;
            case "CI":
                typeToArrayString = lover_c_array[2];
                break;
            case "CS":
                typeToArrayString = lover_c_array[3];
                break;


        }

        result = typeToArrayString.split(",");

        return result;
    }


    MCharacterInfo2.Info result = null;

    private int getDISCColor(String type) {
        int discColor;
        switch (type) {

            case "D":
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.char_d_color);

                break;
            case "I":
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.char_i_color);

                break;
            case "C":
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.char_c_color);

                break;
            case "S":
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.char_s_color);
                break;
            default:
                discColor = ContextCompat.getColor(getApplicationContext(), R.color.color_char_gray);


        }
        return discColor;
    }


    private void getInfo() {

        Net.instance().api.get_character_info_style2(type)
                .enqueue(new Net.ResponseCallBack<MCharacterInfo2>() {
                    @Override
                    public void onSuccess(MCharacterInfo2 response) {

                        if (response.info != null) {
                            result = response.info;
                            Log.e("char_debug", "type : " + type + " / info : " + result);
                            setUI();
                        }
                    }

                    @Override
                    public void onFailure(MError response) {

                        Toaster.showShort(getActivity(), "등록된 데이터가 없습니다.");
                    }
                });
    }


}
