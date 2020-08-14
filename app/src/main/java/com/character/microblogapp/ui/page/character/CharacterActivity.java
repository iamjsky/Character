package com.character.microblogapp.ui.page.character;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.character.microblogapp.R;
import com.character.microblogapp.model.MCharacter;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.BaseFragment;
import com.character.microblogapp.ui.page.character.fragment.CharacterFragment;
import com.character.microblogapp.ui.page.profile.ProfileDISCInfoActivity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class CharacterActivity extends BaseActivity {



    @BindView(R.id.flContainer)
    FrameLayout flContainer;

    CharacterFragment characterFragment;
//
//    @BindView(R.id.btn_character_before)
//    Button btnCharacterBefore;

    @BindView(R.id.tv_bottomCount)
    TextView tv_bottomCount;
    @BindView(R.id.tv_bottomTotalCount)
    TextView tv_bottomTotalCount;
    @BindView(R.id.tv_bottomCountText)
    TextView tv_bottomCountText;

    RoundCornerProgressBar pb_countBar;


//    @BindView(R.id.tv_test_result)
//    TextView tvTestResult;

    public int mViewIndex = 0;

    int dSum = 0;
    int iSum = 0;
    int cSum = 0;
    int sSum = 0;

    String oldAdd = "";

    int go = 0;
    int totalCount = -1;
   public ArrayList<MCharacter.Character> character_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_character);
    }

    @Override
    protected void initUI() {
        super.initUI();

        go = getIntent().getIntExtra("go", 0);

//        btnCharacterBefore.setVisibility(View.GONE);
        pb_countBar = (RoundCornerProgressBar) findViewById(R.id.pb_countBar);
        initData();
    }

    void initData() {
        showProgress(this);
        Net.instance().api.get_character_quiz_list()
                .enqueue(new Net.ResponseCallBack<MCharacter>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onSuccess(MCharacter response) {
                        super.onSuccess(response);
                        hideProgress();

                        character_list = response.character;

                        mViewIndex = 0;
                        showCharacterFragment();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        }
                    }
                });
    }

    private String calcCharacter(int d, int i, int s, int c) {
        String ret = "";

        do {

            if (d >= 11 && i >= 11 && s <= 6 && c <= 6) {
                ret = i > d ? "ID" : "DI";
                break;
            }

            if (d >= 11 && s >= 11 && i <= 6 && c <= 6) {
                ret = s > d ? "SD" : "DS";
                break;
            }

            if (d >= 11 && c >= 11 && i <= 6 && s <= 6) {
                ret = c > d ? "CD" : "DC";
                break;
            }

            if(i >= 11 && s >= 11 && d <= 6 && c <= 6) {
                ret = s > i ? "SI" : "IS";
                break;
            }

            if(i >= 11 && c >= 11 && d <= 6 && s <= 6) {
                ret = c > i ? "CI" : "IC";
                break;
            }

            if(s >= 11 && c >= 11 && d <= 6 && i <= 6) {
                ret = c > s ? "CS" : "SC";
                break;
            }


            if (d >= 11 && i >= 7) {
                if(i >= 7 && i <= 10 && s <= 6 && c <= 6){
                    ret = "Di";
                    break;
                } else if(i >= 7 && i <= 10 && s >= 7 && s <= 10 && c <= 6){
                    ret = s > i ? "Dsi" : "Dis";
                    break;
                } else if(i >= 7 && i <= 10 && c >= 7 && c <= 10 && s <= 6){
                    ret = c > i ? "Dci" : "Dic";
                    break;
                }
            }

            if (d >= 11 && s >= 7) {
                if(s >= 7 && s <= 10 && c >= 7 && c <= 10 && i <= 6){
                    ret = c > s ? "Dcs" : "Dsc";
                    break;
                } else if(s >= 7 && s <= 10 && i <= 6 && c <= 6){
                    ret = "Ds";
                    break;
                }
            }

            if (d >= 11 && c >= 7) {
                if(c >= 7 && c <= 10 && i <= 6 && s <= 6){
                    ret = "Dc";
                    break;
                }
            }

            if (i >= 11 && d >= 7) {
                if(d >= 7 && d <= 10 && s <= 6 && c <= 6){
                    ret = "Id";
                    break;
                } else if(d >= 7 && d <= 10 && s >= 7 && s <= 10 && c <= 6){
                    ret = s > d ? "Isd" : "Ids";
                    break;
                } else if(d >= 7 && d <= 10 && c >= 7 && c <= 10 && s <= 6){
                    ret = c > d ? "Icd" : "Idc";
                    break;
                }
            }

            if (i >= 11 && s >= 7) {
                if(s >= 7 && s <= 10 && c >= 7 && c <= 10 && d <= 6){
                    ret = c > s ? "Ics" : "Isc";
                    break;
                } else if(s >= 7 && s <= 10 && d <= 6 && c <= 6){
                    ret = "Is";
                    break;
                }
            }

            if (i >= 11 && c >= 7) {
                if(c >= 7 && c <= 10 && d <= 6 && s <= 6 ){
                    ret = "Ic";
                    break;
                }
            }

            if (s >= 11 && d >= 7) {
                if(d >= 7 && d <= 10 && i >= 7 && i <= 10 && c <= 6){
                    ret = i > d ? "Sid" : "Sdi";
                    break;
                } else if(d >= 7 && d <= 10 && c >= 7 && c <= 10 && i <= 6){
                    ret = c > d ? "Scd" : "Sdc";
                    break;
                } else if(d >= 7 && d <= 10 && i <= 6 && c <= 6){
                    ret = "Sd";
                    break;
                }
            }

            if (s >= 11 && i >= 7) {
                if(i >= 7 && i <= 10 && c >= 7 && c <= 10 && d <= 6){
                    ret = c > i ? "Sci" : "Sic";
                    break;
                } else if(i >= 7 && i <= 10 && d <= 6 && c <= 6){
                    ret = "Si";
                    break;
                }
            }

            if (s >= 11 && c >= 7) {
                if(c >= 7 && c <= 10 && d <= 6 && i <= 6){
                    ret =  "Sc";
                    break;
                }
            }

            if (c >= 11 && d >= 7) {
                if(d >= 7 && d <= 10 && i >= 7 && i <= 10 && s <= 6){
                    ret = i > d ? "Cid" : "Cdi";
                    break;
                } else if(d >= 7 && d <= 10 && s >= 7 && s <= 10 && i <= 6){
                    ret = s > d ? "Csd" : "Cds";
                    break;
                } else if(d >= 7 && d <= 10 && i <= 6 && s <= 6){
                    ret = "Cd";
                    break;
                }
            }

            if (c >= 11 && i >= 7) {
                if(i >= 7 && i <= 10 && s >= 7 && s <= 10 && d <= 6){
                    ret = s > i ? "Csi" : "Cis";
                    break;
                } else if(i >= 7 && i <= 10 && d <= 6 && s <= 6){
                    ret = "Ci";
                    break;
                }
            }

            if (c >= 11 && s >= 7) {
                if(s >= 7 && s <= 10 && d <= 6 && i <= 6){
                    ret = "Cs";
                    break;
                }
            }

            if (d == 10 && i == 6 && s == 6 && c == 6) {
                ret = "Disc";
                break;
            }

            if (i == 10 && d == 6 && s == 6 && c == 6) {
                ret = "Idsc";
                break;
            }

            if (s == 10 && i == 6 && d == 6 && c == 6) {
                ret = "Sdic";
                break;
            }

            if (c == 10 && i == 6 && s == 6 && d == 6) {
                ret = "Cdis";
                break;
            }

            if (d >= 6 && d <= 8 && i >= 6 && i <= 8 && s >= 6 && s <= 8 && c >= 6 && c <= 8) {
                ret = "disc";
                break;
            }

            if( d >= 7 && d <= 10 && i >= 7 && i <= 10){
                if(s <= 6 && c <= 6) {
                    ret = i > d ? "id" : "di";
                    break;
                }
            }

            if(d >= 7 && d <= 10 && s >= 7 && s <= 10) {
                if(i <= 6 && c <= 6){
                    ret = s > d ? "sd" : "ds";
                    break;
                }
            }

            if (d >= 7 && d <= 10 && c >= 7 && c <= 10) {
                if(i <= 6 && s <= 6){
                    ret = c > d ? "cd" : "dc";
                    break;
                }
            }

            if(i >= 7 && i <= 10 && s >= 7 && s <= 10) {
                if(d <= 6 && c <= 6){
                    ret = s > i ? "si" : "is";
                    break;
                }
            }

            if(i >= 7 && i <= 10 && c >= 7 && c <= 10) {
                if(d <= 6 && s <= 6) {
                    ret = c > i ? "ci" : "ic";
                    break;
                }
            }

            if(s >= 7 && s <= 10 && c >= 7 && c <= 10) {
                if(d <= 6 && i <= 6) {
                    ret = c > s ? "cs" : "sc";
                    break;
                }
            }


            if (d >= 7 && d <= 10 && i >= 7 && i <= 10) {
                if (s >= 7 && s <= 10 && c <= 6) {
                    if(s > d && d >= i){
                        ret = "sdi";
                        break;
                    } else if( s > i && i > d) {
                        ret = "sid";
                        break;
                    } else if(d >= s && s > i) {
                        ret = "dsi";
                        break;
                    } else if( d >= i && i >= s) {
                        ret = "dis";
                        break;
                    } else if( i > d && d >= s) {
                        ret = "ids";
                        break;
                    } else if(i >= s && s > d ) {
                        ret = "isd";
                        break;
                    }
                } else if (c >= 7 && c <= 10 && s <= 6) {
                    if(c > i && i > d){
                        ret = "cid";
                        break;
                    } else if(c > d && d >= i) {
                        ret = "cdi";
                        break;
                    } else if(d >= c && c > i) {
                        ret = "dci";
                        break;
                    } else if(d >= i && i >= c) {
                        ret = "dic";
                        break;
                    } else if(i > d && d >= c) {
                        ret = "idc";
                        break;
                    } else if(i >= c && c > d) {
                        ret = "icd";
                        break;
                    }
                }
            }

            if (d >= 7 && d <= 10 && s >= 7 && s <= 10) {
                if (c >= 7 && c <= 10 && i <= 6) {
                    if(c > d && d >= s){
                        ret = "cds";
                        break;
                    } else if(c > s && s > d){
                        ret = "csd";
                        break;
                    } else if(d >= c && c > s){
                        ret = "dcs";
                        break;
                    } else if(d >= s && s >= c) {
                        ret = "dsc";
                        break;
                    } else if(s > d && d >= c) {
                        ret = "sdc";
                        break;
                    } else if(s >= c && c > d){
                        ret = "scd";
                        break;
                    }
                }
            }

            if(i >= 7 && i <= 10 && s >= 7 && s <= 10 && c >= 7 && c <= 10){
                if(d <= 6 ){
                    if(i >= s && s >= c){
                        ret = "isc";
                        break;
                    } else if(i >= c && c > s) {
                        ret = "ics";
                        break;
                    } else if(s > i && i >= c){
                        ret = "sic";
                        break;
                    } else if(s >= c && c > i) {
                        ret = "sci";
                        break;
                    } else if(c > i && i >= s){
                        ret = "cis";
                        break;
                    } else if(c > s && s > i){
                        ret = "csi";
                        break;
                    }
                }
            }

            if(d >= 11 && d <= 19 && i <= 6 && s <= 6 && c <= 6 ){
                ret = "D=";
                break;
            }

            if (d >= 20 && i <= 6 && s <= 6 && c <= 6) {
                ret = "D";
                break;
            }

            if(i >= 11 && i<= 19 && d <= 6 && s <= 6 && c <= 6){
                ret = "I=";
                break;
            }

            if (i >= 20 && d <= 6 && s <= 6 && c <= 6) {
                ret = "I";
                break;
            }

            if(s >= 11 && s <= 19 && d <= 6 && i <= 6 && c <= 6) {
                ret = "S=";
                break;
            }
            if (s >= 20 && d <= 6 && i <= 6 && c <= 6) {
                ret = "S";
                break;
            }

            if (c >= 11 && c <= 19 && d <= 6 && i <= 6 && s <= 6){
                ret = "C=";
                break;
            }

            if (c >= 20 && d <= 6 && i <= 6 && s <= 6) {
                ret = "C";
                break;
            }

        } while (false);

        return ret;
    }

    public void showCharacterNext(MCharacter.Answer answer) {
        switch (answer.type) {
            case "D":
            case "d":
                dSum++;
                break;
            case "I":
            case "i":
                iSum++;
                break;
            case "S":
            case "s":
                sSum++;
                break;
            case "C":
            case "c":
                cSum++;
                break;
        }
        oldAdd = answer.type;

        if (mViewIndex == character_list.size() - 1) {
//            SparseArray<String> map = new SparseArray<>(4);
//            map.put(dSum, "D");
//            map.put(iSum, "I");
//            map.put(sSum, "S");
//            map.put(cSum, "C");
//
//            int[] indices = new int[4];
//            indices[0] = dSum;
//            indices[1] = iSum;
//            indices[2] = cSum;
//            indices[3] = sSum;
//
//            Arrays.sort(indices);
//
//            String testRes = map.get(indices[3]);
//            if (indices[2] > 0 && indices[2] != indices[3]) {
//                testRes = testRes + map.get(indices[2]);
//            }

            Intent intent = new Intent(this, CharacterConfirmActivity.class);
            intent.putExtra("result", calcCharacter(dSum, iSum, sSum, cSum));
            intent.putExtra("go", go);
            startActivity(intent);
            finish();

            if (go == 1) {
                finish();
            }

            return;
        }

//        tvTestResult.setText("D : " + dSum + " 개 - I : " + iSum + " 개 - S : " + sSum + " 개 - C : " + cSum + " 개" );

        mViewIndex++;
        showCharacterFragment();
    }

    @OnClick(R.id.rlInfo)
    public void onInfo() {
        Intent intent = new Intent(this, ProfileDISCInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_character_next)
    public void showCharacterBefore() {
        if (mViewIndex == 0) {
            return;
        }

        switch (oldAdd) {
            case "D":
                dSum--;
                break;
            case "I":
                iSum--;
                break;
            case "S":
                sSum--;
                break;
            case "C":
                cSum--;
                break;
        }

        mViewIndex--;
        showCharacterFragment();
    }

    @SuppressLint("DefaultLocale")
    public void showCharacterFragment() {


        characterFragment = CharacterFragment.newInstance();
        characterFragment.setData(character_list.get(mViewIndex));
        showFragment(characterFragment);
    }

    private void showFragment(BaseFragment p_fragment) {
        FragmentTransaction w_ft = getSupportFragmentManager().beginTransaction();
        w_ft.replace(R.id.flContainer, p_fragment);
        w_ft.addToBackStack(null);
        w_ft.commit();
    }

    @OnClick(R.id.rlt_back)
    void go_back() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mViewIndex == 0) {
            if (go == 0) {
                setFinishAppWhenPressedBackKey();
                super.onBackPressed();
            } else {
                finish();
            }
        } else {
            showCharacterBefore();
        }
    }

    public void setCountProgressBar(){
        int index = mViewIndex+1;

        if(totalCount == -1){
            totalCount = character_list.size();
            tv_bottomTotalCount.setText(totalCount+"");
            pb_countBar.setMax(totalCount);
        }

        pb_countBar.setProgress(index);
        tv_bottomCount.setText(index+"");
        tv_bottomCountText.setText(index+"번째");
    }
}
