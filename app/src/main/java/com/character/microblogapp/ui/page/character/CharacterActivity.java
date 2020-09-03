package com.character.microblogapp.ui.page.character;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_character);
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

    class CharModel implements Comparable<CharModel>{
        public int charCount;
        public String charText;

        public CharModel(int charCount, String charText){
            this.charCount = charCount;
            this.charText = charText;
        }

        public int getCharCount() {
            return charCount;
        }



        @Override
        public int compareTo(CharModel s) {
            if (this.charCount < s.getCharCount()) {
                return -1;
            } else if (this.charCount > s.getCharCount()) {
                return 1;
            }
            return 0;
        }


    }
    private String calcNewCharacter(int d, int i, int s, int c) {
        String ret = "";

//        Log.e("char_debug", "disc count list : " + d + " / " + i + " / " + s + " / " + c);

        //d
        if(d >= 20){
            ret = "D";
        }
        //i
        else if(i >= 20){
            ret = "I";
        }
        //s
        else if(s >= 20) {
            ret = "S";
        }
        //c
        else if(c >= 20) {
            ret = "C";
        } else {

            ArrayList<CharModel> charModelList = new ArrayList<>();
            CharModel charModel_D = new CharModel(d, "D");
            charModelList.add(charModel_D);
            CharModel charModel_I = new CharModel(i, "I");
            charModelList.add(charModel_I);
            CharModel charModel_S = new CharModel(s, "S");
            charModelList.add(charModel_S);
            CharModel charModel_C = new CharModel(c, "C");
            charModelList.add( charModel_C);

            Collections.sort(charModelList, Collections.reverseOrder());
//            Log.e("char_debug", "disc sort list : " + charModelList.get(0).charText + charModelList.get(0).charCount
//                    + " / " + charModelList.get(1).charText + charModelList.get(1).charCount
//                    + " / " + charModelList.get(2).charText + charModelList.get(2).charCount
//                    + " / " + charModelList.get(3).charText + charModelList.get(3).charCount);

//            Log.e("char_debug", "1순위 : " + charModelList.get(0).charText + charModelList.get(0).charCount
//             + " / 2순위 : " + charModelList.get(1).charText + charModelList.get(1).charCount);

            String charFirst = charModelList.get(0).charText;
            String charSecond = charModelList.get(1).charText;

//            Log.e("char_debug", "char result : " + charFirst + charSecond);
            ret = charFirst + charSecond + "";
        }









//            //di
//            if(d >= i && i >= s && i >= c){
//               ret = "DI";
//               break;
//            }
//            //ds
//            if(d >= s && s >= i && s >= c) {
//                ret = "DS";
//                break;
//            }
//            //dc
//            if(d >= c && c >= i && c >= s) {
//                ret = "DC";
//                break;
//            }
//
//
//            //id
//            if(i >= d && d >= s && d >= c) {
//                ret = "ID";
//                break;
//            }
//            //is
//            if(i >= s && s >= d && s >= c) {
//                ret = "IS";
//                break;
//            }
//            //ic
//            if(i >= c && c >= d && c >= s) {
//                ret = "IC";
//                break;
//            }
//
//
//            //sd
//            if(s >= d && d >= i && d >= c) {
//                ret = "SD";
//                break;
//            }
//            //si
//            if(s >= i && i >= d && i >= c) {
//                ret = "SI";
//                break;
//            }
//            //sc
//            if(s >= c && c >= d && c >= i) {
//                ret = "SC";
//                break;
//            }
//
//
//            //cd
//            if(c >= d && d >= i && d >= s) {
//                ret = "CD";
//                break;
//            }
//            //cs
//            if(c >= s && s >= d && s >= i) {
//                ret = "CD";
//                break;
//            }
//        } while (false);


        return ret;
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

            if (i >= 11 && s >= 11 && d <= 6 && c <= 6) {
                ret = s > i ? "SI" : "IS";
                break;
            }

            if (i >= 11 && c >= 11 && d <= 6 && s <= 6) {
                ret = c > i ? "CI" : "IC";
                break;
            }

            if (s >= 11 && c >= 11 && d <= 6 && i <= 6) {
                ret = c > s ? "CS" : "SC";
                break;
            }


            if (d >= 11 && i >= 7) {
                if(i >= 7 && i <= 10 && s <= 6 && c <= 6){
                    ret = "DI";
                    break;
                } else if(i >= 7 && i <= 10 && s >= 7 && s <= 10 && c <= 6){
                    ret = s > i ? "DS" : "DI";
                    break;
                } else if(i >= 7 && i <= 10 && c >= 7 && c <= 10 && s <= 6){
                    ret = c > i ? "DC" : "DI";
                    break;
                }
            }

            if (d >= 11 && s >= 7) {
                if(s >= 7 && s <= 10 && c >= 7 && c <= 10 && i <= 6){
                    ret = c > s ? "DC" : "DS";
                    break;
                } else if(s >= 7 && s <= 10 && i <= 6 && c <= 6){
                    ret = "DS";
                    break;
                }
            }

            if (d >= 11 && c >= 7) {
                if(c >= 7 && c <= 10 && i <= 6 && s <= 6){
                    ret = "DC";
                    break;
                }
            }

            if (i >= 11 && d >= 7) {
                if(d >= 7 && d <= 10 && s <= 6 && c <= 6){
                    ret = "ID";
                    break;
                } else if(d >= 7 && d <= 10 && s >= 7 && s <= 10 && c <= 6){
                    ret = s > d ? "IS" : "ID";
                    break;
                } else if(d >= 7 && d <= 10 && c >= 7 && c <= 10 && s <= 6){
                    ret = c > d ? "IC" : "ID";
                    break;
                }
            }

            if (i >= 11 && s >= 7) {
                if(s >= 7 && s <= 10 && c >= 7 && c <= 10 && d <= 6){
                    ret = c > s ? "IC" : "IS";
                    break;
                } else if(s >= 7 && s <= 10 && d <= 6 && c <= 6){
                    ret = "IS";
                    break;
                }
            }

            if (i >= 11 && c >= 7) {
                if(c >= 7 && c <= 10 && d <= 6 && s <= 6 ){
                    ret = "IC";
                    break;
                }
            }

            if (s >= 11 && d >= 7) {
                if(d >= 7 && d <= 10 && i >= 7 && i <= 10 && c <= 6){
                    ret = i > d ? "SI" : "SD";
                    break;
                } else if(d >= 7 && d <= 10 && c >= 7 && c <= 10 && i <= 6){
                    ret = c > d ? "SC" : "SD";
                    break;
                } else if(d >= 7 && d <= 10 && i <= 6 && c <= 6){
                    ret = "SD";
                    break;
                }
            }

            if (s >= 11 && i >= 7) {
                if(i >= 7 && i <= 10 && c >= 7 && c <= 10 && d <= 6){
                    ret = c > i ? "SC" : "SI";
                    break;
                } else if(i >= 7 && i <= 10 && d <= 6 && c <= 6){
                    ret = "SI";
                    break;
                }
            }

            if (s >= 11 && c >= 7) {
                if(c >= 7 && c <= 10 && d <= 6 && i <= 6){
                    ret =  "SC";
                    break;
                }
            }

            if (c >= 11 && d >= 7) {
                if(d >= 7 && d <= 10 && i >= 7 && i <= 10 && s <= 6){
                    ret = i > d ? "CI" : "CD";
                    break;
                } else if(d >= 7 && d <= 10 && s >= 7 && s <= 10 && i <= 6){
                    ret = s > d ? "CS" : "CD";
                    break;
                } else if(d >= 7 && d <= 10 && i <= 6 && s <= 6){
                    ret = "CD";
                    break;
                }
            }

            if (c >= 11 && i >= 7) {
                if(i >= 7 && i <= 10 && s >= 7 && s <= 10 && d <= 6){
                    ret = s > i ? "CS" : "CI";
                    break;
                } else if(i >= 7 && i <= 10 && d <= 6 && s <= 6){
                    ret = "CI";
                    break;
                }
            }

            if (c >= 11 && s >= 7) {
                if(s >= 7 && s <= 10 && d <= 6 && i <= 6){
                    ret = "CS";
                    break;
                }
            }

            if (d == 10 && i == 6 && s == 6 && c == 6) {
                ret = "DI";
                break;
            }

            if (i == 10 && d == 6 && s == 6 && c == 6) {
                ret = "ID";
                break;
            }

            if (s == 10 && i == 6 && d == 6 && c == 6) {
                ret = "SD";
                break;
            }

            if (c == 10 && i == 6 && s == 6 && d == 6) {
                ret = "CD";
                break;
            }

            if (d >= 6 && d <= 8 && i >= 6 && i <= 8 && s >= 6 && s <= 8 && c >= 6 && c <= 8) {
                ret = "DI";
                break;
            }

            if( d >= 7 && d <= 10 && i >= 7 && i <= 10){
                if(s <= 6 && c <= 6) {
                    ret = i > d ? "ID" : "DI";
                    break;
                }
            }

            if(d >= 7 && d <= 10 && s >= 7 && s <= 10) {
                if(i <= 6 && c <= 6){
                    ret = s > d ? "SD" : "DS";
                    break;
                }
            }

            if (d >= 7 && d <= 10 && c >= 7 && c <= 10) {
                if(i <= 6 && s <= 6){
                    ret = c > d ? "CD" : "DC";
                    break;
                }
            }

            if(i >= 7 && i <= 10 && s >= 7 && s <= 10) {
                if(d <= 6 && c <= 6){
                    ret = s > i ? "SI" : "IS";
                    break;
                }
            }

            if(i >= 7 && i <= 10 && c >= 7 && c <= 10) {
                if(d <= 6 && s <= 6) {
                    ret = c > i ? "CI" : "IC";
                    break;
                }
            }

            if(s >= 7 && s <= 10 && c >= 7 && c <= 10) {
                if(d <= 6 && i <= 6) {
                    ret = c > s ? "CS" : "SC";
                    break;
                }
            }


            if (d >= 7 && d <= 10 && i >= 7 && i <= 10) {
                if (s >= 7 && s <= 10 && c <= 6) {
                    if(s > d && d >= i){
                        ret = "SD";
                        break;
                    } else if( s > i && i > d) {
                        ret = "SI";
                        break;
                    } else if(d >= s && s > i) {
                        ret = "DS";
                        break;
                    } else if( d >= i && i >= s) {
                        ret = "DI";
                        break;
                    } else if( i > d && d >= s) {
                        ret = "ID";
                        break;
                    } else if(i >= s && s > d ) {
                        ret = "IS";
                        break;
                    }
                } else if (c >= 7 && c <= 10 && s <= 6) {
                    if(c > i && i > d){
                        ret = "CI";
                        break;
                    } else if(c > d && d >= i) {
                        ret = "CD";
                        break;
                    } else if(d >= c && c > i) {
                        ret = "DC";
                        break;
                    } else if(d >= i && i >= c) {
                        ret = "DI";
                        break;
                    } else if(i > d && d >= c) {
                        ret = "ID";
                        break;
                    } else if(i >= c && c > d) {
                        ret = "IC";
                        break;
                    }
                }
            }

            if (d >= 7 && d <= 10 && s >= 7 && s <= 10) {
                if (c >= 7 && c <= 10 && i <= 6) {
                    if(c > d && d >= s){
                        ret = "CD";
                        break;
                    } else if(c > s && s > d){
                        ret = "CS";
                        break;
                    } else if(d >= c && c > s){
                        ret = "DC";
                        break;
                    } else if(d >= s && s >= c) {
                        ret = "DS";
                        break;
                    } else if(s > d && d >= c) {
                        ret = "SD";
                        break;
                    } else if(s >= c && c > d){
                        ret = "SC";
                        break;
                    }
                }
            }

            if(i >= 7 && i <= 10 && s >= 7 && s <= 10 && c >= 7 && c <= 10){
                if(d <= 6 ){
                    if(i >= s && s >= c){
                        ret = "IS";
                        break;
                    } else if(i >= c && c > s) {
                        ret = "IC";
                        break;
                    } else if(s > i && i >= c){
                        ret = "SI";
                        break;
                    } else if(s >= c && c > i) {
                        ret = "SC";
                        break;
                    } else if(c > i && i >= s){
                        ret = "CI";
                        break;
                    } else if(c > s && s > i){
                        ret = "CS";
                        break;
                    }
                }
            }

            if (d >= 11 && d <= 19 && i <= 6 && s <= 6 && c <= 6) {
                ret = "D";
                break;
            }

            if (d >= 20 && i <= 6 && s <= 6 && c <= 6) {
                ret = "D";
                break;
            }

            if (i >= 11 && i <= 19 && d <= 6 && s <= 6 && c <= 6) {
                ret = "I";
                break;
            }

            if (i >= 20 && d <= 6 && s <= 6 && c <= 6) {
                ret = "I";
                break;
            }

            if (s >= 11 && s <= 19 && d <= 6 && i <= 6 && c <= 6) {
                ret = "S";
                break;
            }
            if (s >= 20 && d <= 6 && i <= 6 && c <= 6) {
                ret = "S";
                break;
            }

            if (c >= 11 && c <= 19 && d <= 6 && i <= 6 && s <= 6) {
                ret = "C";
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
            intent.putExtra("result", calcNewCharacter(dSum, iSum, sSum, cSum));
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

    public void setCountProgressBar() {
        int index = mViewIndex + 1;

        if (totalCount == -1) {
            totalCount = character_list.size();
            tv_bottomTotalCount.setText(totalCount + "");
            pb_countBar.setMax(totalCount);
        }

        pb_countBar.setProgress(index);
        tv_bottomCount.setText(index + "");
        tv_bottomCountText.setText(index + "번째");
    }

    class Disc {
        int typeCount;
        String typeText;

        public int getTypeCount() {
            return typeCount;
        }

        public void setTypeCount(int typeCount) {
            this.typeCount = typeCount;
        }

        public String getTypeText() {
            return typeText;
        }

        public void setTypeText(String typeText) {
            this.typeText = typeText;
        }
    }
}
