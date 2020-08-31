package com.character.microblogapp.ui.page.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
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

    @BindView(R.id.iv_percent)
    ImageView iv_percent;
    @BindView(R.id.tv_charMan_01)
    TextView tv_charMan_01;
    @BindView(R.id.tv_charMan_02)
    TextView tv_charMan_02;
    @BindView(R.id.tv_charWoman_01)
    TextView tv_charWoman_01;
    @BindView(R.id.tv_charWoman_02)
    TextView tv_charWoman_02;
    @BindView(R.id.tv_charPercent)
    TextView tv_charPercent;
    @BindView(R.id.tv_desc)
    TextView tv_desc;

    @BindView(R.id.layout_charMan)
    LinearLayout layout_charMan;
    @BindView(R.id.layout_charWoman)
    LinearLayout layout_charWoman;
    @BindView(R.id.tv_charSelectMan)
    TextView tv_charSelectMan;
    @BindView(R.id.tv_charSelectWoman)
    TextView tv_charSelectWoman;

    @BindView(R.id.iv_topBg)
    ImageView iv_topBg;
    PrefMgr m_prefMgr;
    /**
     * 0 main
     * 1 popup man select
     * 2 woman select
     * 3 result
     */
    int pageState = 0;

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
        layout_charSelectPopup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                pageState = 0;
                selectedCharMan = -1;
                selectedCharWoman = -1;
                tv_charSelectMan.setText("");
                tv_charSelectWoman.setText("");
                layout_charMan.setVisibility(View.VISIBLE);
                layout_charWoman.setVisibility(View.VISIBLE);
                tv_charSelectMan.setVisibility(View.GONE);
                tv_charSelectWoman.setVisibility(View.GONE);
                layout_charSelectPopup.setVisibility(View.GONE);
                return true;
            }
        });
        return mRoot;
    }


    @Override
    protected void initUI() {
        layout_main.setVisibility(View.VISIBLE);
        tv_charSelectMan.setText("");
        tv_charSelectWoman.setText("");
        layout_charMan.setVisibility(View.VISIBLE);
        layout_charWoman.setVisibility(View.VISIBLE);
        iv_topBg.setBackground(getDISCBackground("I", 3));
        iv_topBg.setVisibility(View.VISIBLE);
        tv_charSelectMan.setVisibility(View.GONE);
        tv_charSelectWoman.setVisibility(View.GONE);
        layout_result.setVisibility(View.GONE);
    }


    @OnClick(R.id.rlt_back)
    public void rlt_backClicked() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.selectLine(0);
        if (pageState == 0) {
            selectedCharMan = -1;
            selectedCharWoman = -1;
            mainActivity.selectTab(0);

        } else if (pageState == 3) {
            iv_topBg.setBackground(getDISCBackground("I", 3));
            iv_topBg.setVisibility(View.VISIBLE);
            selectedCharMan = -1;
            selectedCharWoman = -1;
            layout_main.setVisibility(View.VISIBLE);
            layout_result.setVisibility(View.GONE);
            pageState = 0;

        } else {
            iv_topBg.setBackground(getDISCBackground("I", 3));
            iv_topBg.setVisibility(View.VISIBLE);
            layout_charSelectPopup.setVisibility(View.GONE);
            pageState = 0;
            selectedCharMan = -1;
            selectedCharWoman = -1;
        }

        tv_charSelectMan.setText("");
        tv_charSelectWoman.setText("");
        layout_charMan.setVisibility(View.VISIBLE);
        layout_charWoman.setVisibility(View.VISIBLE);
        tv_charSelectMan.setVisibility(View.GONE);
        tv_charSelectWoman.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_share)
    void btn_shareClicked() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);

        intent.setType("text/plain");


        String URL = "http://personalitism.com/measurement_";


        String char_01 = tv_charMan_01.getText().toString() + "";
        String char_02 = tv_charWoman_01.getText().toString() + "";
        String charType = (char_01 + char_02 + "").toLowerCase();
        String gender = "_";
        if (MyInfo.getInstance().gender == 1) {
            gender = gender + "m";
        } else if (MyInfo.getInstance().gender == 2) {
            gender = gender + "w";
        }
        intent.putExtra(Intent.EXTRA_TEXT, "\"너와 나의 성격 코드는 잘 맞을까?\"\n\"우리의 성격 궁합 점수는?\"\n" + URL + charType + gender);


        Intent chooser = Intent.createChooser(intent, "\"너와 나의 성격 코드는 잘 맞을까?\"\n\"우리의 성격 궁합 점수는?\"\n" + URL + charType + gender);
        startActivity(chooser);


    }


    @OnClick(R.id.btn_checkChemistry)
    public void btn_checkChemistryClicked() {
        if (selectedCharMan != -1 && selectedCharWoman != -1) {
            ChemistryModel chemistryModel = (ChemistryModel) getChemistryResult();

            iv_percent.setImageDrawable(chemistryModel.imgRes);
            tv_charMan_01.setText(chemistryModel.charMan);
            tv_charMan_01.setTextColor(chemistryModel.manColor);
            tv_charMan_02.setText(chemistryModel.charMan);
            tv_charMan_02.setTextColor(chemistryModel.manColor);

            tv_charWoman_01.setText(chemistryModel.charWoman);
            tv_charWoman_01.setTextColor(chemistryModel.womanColor);
            tv_charWoman_02.setText(chemistryModel.charWoman);
            tv_charWoman_02.setTextColor(chemistryModel.womanColor);

            tv_charPercent.setText(chemistryModel.percent);
            tv_charPercent.setTextColor(chemistryModel.percentColor);


            Spannable sb = new SpannableString(chemistryModel.desc);
            for (int i = 0; i < chemistryModel.desc.length(); i++) {
                char temp = (chemistryModel.desc.charAt(i));
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
            tv_desc.setText(sb);
            layout_main.setVisibility(View.GONE);
            layout_result.setVisibility(View.VISIBLE);
            iv_topBg.setBackground(getDISCBackground(chemistryModel.charMan, 3));
            iv_topBg.setVisibility(View.VISIBLE);
            pageState = 3;
        } else {
            Toaster.showShort(mParent, "DISC 유형을 모두 선택해 주세요.");
        }

    }

    @OnClick(R.id.layout_charManArea)
    public void layout_charManClicked() {
        pageState = 1;
        layout_charSelectPopup.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.layout_charWomanArea)
    public void layout_charWomanClicked() {
        pageState = 2;
        layout_charSelectPopup.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_d)
    public void tv_dClicked() {
        if (pageState == 1) {
            selectedCharMan = 0;
            tv_charSelectMan.setText(getDISCChar(selectedCharMan));
            tv_charSelectMan.setTextColor(getDISCColor(selectedCharMan));
            layout_charMan.setVisibility(View.GONE);
            tv_charSelectMan.setVisibility(View.VISIBLE);

        } else {
            selectedCharWoman = 0;
            tv_charSelectWoman.setText(getDISCChar(selectedCharWoman));
            tv_charSelectWoman.setTextColor(getDISCColor(selectedCharWoman));
            layout_charWoman.setVisibility(View.GONE);
            tv_charSelectWoman.setVisibility(View.VISIBLE);
        }
        pageState = 0;
        layout_charSelectPopup.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_i)
    public void tv_iClicked() {
        if (pageState == 1) {
            selectedCharMan = 1;
            tv_charSelectMan.setText(getDISCChar(selectedCharMan));
            tv_charSelectMan.setTextColor(getDISCColor(selectedCharMan));
            layout_charMan.setVisibility(View.GONE);
            tv_charSelectMan.setVisibility(View.VISIBLE);
        } else {
            selectedCharWoman = 1;
            tv_charSelectWoman.setText(getDISCChar(selectedCharWoman));
            tv_charSelectWoman.setTextColor(getDISCColor(selectedCharWoman));
            layout_charWoman.setVisibility(View.GONE);
            tv_charSelectWoman.setVisibility(View.VISIBLE);
        }
        pageState = 0;

        layout_charSelectPopup.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_s)
    public void tv_sClicked() {
        if (pageState == 1) {
            selectedCharMan = 2;
            tv_charSelectMan.setText(getDISCChar(selectedCharMan));
            tv_charSelectMan.setTextColor(getDISCColor(selectedCharMan));
            layout_charMan.setVisibility(View.GONE);
            tv_charSelectMan.setVisibility(View.VISIBLE);
        } else {
            selectedCharWoman = 2;
            tv_charSelectWoman.setText(getDISCChar(selectedCharWoman));
            tv_charSelectWoman.setTextColor(getDISCColor(selectedCharWoman));
            layout_charWoman.setVisibility(View.GONE);
            tv_charSelectWoman.setVisibility(View.VISIBLE);
        }
        pageState = 0;
        layout_charSelectPopup.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_c)
    public void tv_cClicked() {
        if (pageState == 1) {
            selectedCharMan = 3;
            tv_charSelectMan.setText(getDISCChar(selectedCharMan));
            tv_charSelectMan.setTextColor(getDISCColor(selectedCharMan));
            layout_charMan.setVisibility(View.GONE);
            tv_charSelectMan.setVisibility(View.VISIBLE);
        } else {
            selectedCharWoman = 3;
            tv_charSelectWoman.setText(getDISCChar(selectedCharWoman));
            tv_charSelectWoman.setTextColor(getDISCColor(selectedCharWoman));
            layout_charWoman.setVisibility(View.GONE);
            tv_charSelectWoman.setVisibility(View.VISIBLE);
        }
        pageState = 0;
        layout_charSelectPopup.setVisibility(View.GONE);
    }

    private ChemistryModel getChemistryResult() {
        int gender = MyInfo.getInstance().gender;
        int percent = 0;
        String desc = "";
        if (gender == 1) {
            if (selectedCharMan == 0) {
                switch (selectedCharWoman) {
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
            } else if (selectedCharMan == 1) {
                switch (selectedCharWoman) {
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
            } else if (selectedCharMan == 2) {
                switch (selectedCharWoman) {
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
            } else if (selectedCharMan == 3) {
                switch (selectedCharWoman) {
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
        } else if (gender == 2) {
            if (selectedCharMan == 0) {
                switch (selectedCharWoman) {
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
            } else if (selectedCharMan == 1) {
                switch (selectedCharWoman) {
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
            } else if (selectedCharMan == 2) {
                switch (selectedCharWoman) {
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
            } else if (selectedCharMan == 3) {
                switch (selectedCharWoman) {
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


        ChemistryModel chemistryModel = new ChemistryModel(getDISCChar(selectedCharMan), getDISCChar(selectedCharWoman),
                getDISCColor(selectedCharMan), getDISCColor(selectedCharWoman), percent + "%", getPercentColor(percent), getPercentImg(percent), desc);
        return chemistryModel;

    }

    class ChemistryModel {
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

    private String getDISCChar(int type) {
        String disc = "";
        switch (type) {

            case 0:
                disc = "D";

                break;
            case 1:
                disc = "I";

                break;
            case 2:
                disc = "S";
                break;
            case 3:
                disc = "C";

                break;


        }

        return disc;
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
        if (percent >= 90) {
            percentColor = ContextCompat.getColor(getApplicationContext(), R.color.chemistry_percent_color_90_99);
        } else if (percent >= 80) {
            percentColor = ContextCompat.getColor(getApplicationContext(), R.color.chemistry_percent_color_60_80);
        } else if (percent >= 20) {
            percentColor = ContextCompat.getColor(getApplicationContext(), R.color.chemistry_percent_color_20_40);
        } else if (percent >= 5) {
            percentColor = ContextCompat.getColor(getApplicationContext(), R.color.chemistry_percent_color_5_10);
        } else {
            percentColor = ContextCompat.getColor(getApplicationContext(), R.color.color_char_gray);
        }


        return percentColor;
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

}
