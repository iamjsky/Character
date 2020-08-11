package com.character.microblogapp.ui.page.model;

import android.content.Intent;
import android.os.Bundle;

import com.character.microblogapp.R;
import com.character.microblogapp.data.Common;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MEnergy;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.model.MUserList;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.dialog.ConfirmDialog2;
import com.character.microblogapp.ui.dialog.SelectDialog2;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.ui.page.intro.SelectLoveActivity;
import com.character.microblogapp.ui.page.intro.SelectTypeActivity;
import com.character.microblogapp.ui.page.setting.EnergyActivity;
import com.character.microblogapp.ui.page.setting.TypeActivity;
import com.character.microblogapp.util.Toaster;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindArray;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class ModelFindActivity extends BaseActivity {

    int m_nSelectModelType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_model_find);
    }

    @Override
    protected void initUI() {
        super.initUI();
    }

    private void transformUserList(int type, MUserList response) {
        m_nSelectModelType = type;
        String strType = "";
        switch (type) {
            case 12:
                strType = "연애스타일";
                break;
            case 1:
                strType = "상위 20% 이성";
                break;
            case 2:
                strType = "선호하는 성격";
                break;
            case 3:
                strType = "체형별";
                break;
            case 4:
                strType = "뉴페이스";
                break;
            case 5:
                strType = "동네 친구";
                break;
            case 6:
                strType = "지금 접속중";
                break;
            case 7:
                strType = "종교";
                break;
            case 8:
                strType = "음주 습관";
                break;
            case 9:
                strType = "키";
                break;
            case 10:
                strType = "관심사";
                break;
            case 11:
                strType = "직업";
                break;
        }

        Common.g_modelType = type;
        Common.g_models.clear();
        if (response.arr_list != null) {
            for (int i = 0; i < response.arr_list.length; i++) {
                Common.g_models.add(response.arr_list[i]);
                Common.g_models.get(i).pay_title = strType;
            }
        }

        setResult(RESULT_OK);
        finish();
    }

    private MUserList makeDummyDataForTest() {

        MUserList result = new MUserList();

        result.arr_list = new MUserList.User[6];
        for (int i = 0; i < 6; i++) {

            MUserList.User user = new MUserList.User();
            user.uid = i;
            user.nickname = "tester" + i;
            user.address = "address" + i;
            user.age = (20 + i);
            user.gender = 1;
            user.character = "DS";
            user.ideal_character = "강한 주도형\n강한 안정형";
            user.school = "서울대";
            user.job = "대학생";
            user.height = 167;
            user.profile = new String[]{"http://192.168.56.1/Character/Source/Admin/temp/temp_item_1.png", "http://192.168.56.1/Character/Source/Admin/temp/temp_item_2.png"};
            user.isPublic = true;

            result.arr_list[i] = user;
        }

        result.page_cnt = 2;

        return result;
    }

    @OnClick(R.id.rlt_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.rlt_different_opposite)
        //상위 20%이상 이성
    void goDifferentOppsite() {

        ConfirmDialog2.show(this, "상위 20% 이성", "에너지 50개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {
                m_nSelectModelType = 1;
                if (MyInfo.getInstance().energy >= 50) {

                    calcEnergy(50, 2);

                } else {

                    ConfirmDialog2.show(ModelFindActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ModelFindActivity.this, EnergyActivity.class);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    @BindArray(R.array.character1)
    String[] character1;

    @BindArray(R.array.character2)
    String[] character2;

    @OnClick(R.id.rtl_like_character)
    void goLikeCharacter() {

        ConfirmDialog2.show(this, "선호하는 성격", "에너지 15개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {
                m_nSelectModelType = 2;
                if (MyInfo.getInstance().energy >= 15) {

                    calcEnergy(15, 3);

                } else {

                    ConfirmDialog2.show(ModelFindActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ModelFindActivity.this, EnergyActivity.class);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    @OnClick(R.id.rlt_body)
    void goBody() {

        ConfirmDialog2.show(this, "체형별", "에너지 15개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {
                m_nSelectModelType = 3;
                if (MyInfo.getInstance().energy >= 15) {

                    calcEnergy(15, 4);

                } else {

                    ConfirmDialog2.show(ModelFindActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ModelFindActivity.this, EnergyActivity.class);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    @OnClick(R.id.rlt_new_pay)
    void goNewPay() {
        ConfirmDialog2.show(this, "뉴페이스", "에너지 20개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {
                m_nSelectModelType = 4;
                if (MyInfo.getInstance().energy >= 20) {
                    calcEnergy(20, 5);
                } else {

                    ConfirmDialog2.show(ModelFindActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ModelFindActivity.this, EnergyActivity.class);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    @OnClick(R.id.rlt_neighbor)
    void goNeighbor() {

        ConfirmDialog2.show(this, "동네 친구", "에너지 20개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {
                m_nSelectModelType = 5;
                if (MyInfo.getInstance().energy >= 20) {
                    calcEnergy(20, 6);
                } else {

                    ConfirmDialog2.show(ModelFindActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ModelFindActivity.this, EnergyActivity.class);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    @OnClick(R.id.rlt_now_connect)
    void goNowConnect() {
        ConfirmDialog2.show(this, "지금 접속중", "에너지 15개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {
                m_nSelectModelType = 6;
                if (MyInfo.getInstance().energy >= 15) {
                    calcEnergy(15, 7);
                } else {

                    ConfirmDialog2.show(ModelFindActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ModelFindActivity.this, EnergyActivity.class);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    @OnClick(R.id.rlt_region)
    void ogRegion() {

        ConfirmDialog2.show(this, "종교", "에너지 15개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {
                m_nSelectModelType = 7;
                if (MyInfo.getInstance().energy >= 15) {
                    calcEnergy(15, 8);
                } else {

                    ConfirmDialog2.show(ModelFindActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ModelFindActivity.this, EnergyActivity.class);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    @OnClick(R.id.rlt_drink)
    void goDrink() {

        ConfirmDialog2.show(this, "음주습관", "에너지 15개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {
                m_nSelectModelType = 8;
                if (MyInfo.getInstance().energy >= 15) {

                    calcEnergy(15, 9);

                } else {

                    ConfirmDialog2.show(ModelFindActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ModelFindActivity.this, EnergyActivity.class);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    @OnClick(R.id.rlt_tall)
    void goTall() {

        ConfirmDialog2.show(this, "키", "에너지 15개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {
                m_nSelectModelType = 9;
                if (MyInfo.getInstance().energy >= 15) {

                    calcEnergy(15, 10);
                } else {

                    ConfirmDialog2.show(ModelFindActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ModelFindActivity.this, EnergyActivity.class);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    @OnClick(R.id.rlt_interest)
    void goInterest() {

        ConfirmDialog2.show(this, "관심사", "에너지 15개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {
                m_nSelectModelType = 10;
                if (MyInfo.getInstance().energy >= 15) {
                    calcEnergy(15, 11);
                } else {

                    ConfirmDialog2.show(ModelFindActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ModelFindActivity.this, EnergyActivity.class);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    @OnClick(R.id.rlt_job)
    void goJob() {
        ConfirmDialog2.show(this, "직업", "에너지 15개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {
                m_nSelectModelType = 11;
                if (MyInfo.getInstance().energy >= 15) {

                    calcEnergy(15, 12);

                } else {

                    ConfirmDialog2.show(ModelFindActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ModelFindActivity.this, EnergyActivity.class);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    @OnClick(R.id.rlt_romance_style)
    void goRomanceStyle() {
        ConfirmDialog2.show(this, "연애스타일", "에너지 15개를 사용할까요?", new ConfirmDialog2.ActionListener() {
            @Override
            public void onOk() {

                m_nSelectModelType = 12;
                if (MyInfo.getInstance().energy >= 15) {

                    calcEnergy(15, 1);

                } else {

                    ConfirmDialog2.show(ModelFindActivity.this, "에너지가 부족해요!!", String.format(getString(R.string.confirm_get_energy), (10 - MyInfo.getInstance().energy)),
                            new ConfirmDialog2.ActionListener() {
                                @Override
                                public void onOk() {
                                    Intent goEnergy = new Intent(ModelFindActivity.this, EnergyActivity.class);
                                    startActivity(goEnergy);
                                }
                            });
                }
            }
        });
    }

    private void calcEnergy(int count, final int mType) {
        showProgress(this);
        Net.instance().api.calc_point(MyInfo.getInstance().uid, count,
                mType == 1 ? "이상형 소개(연애스타일)" :
                mType == 2 ? "이상형 소개(상위 20%이성)" :
                mType == 3 ? "이상형 소개(선호하는 성격)" :
                mType == 4 ? "이상형 소개(체형별)" :
                mType == 5 ? "이상형 소개(뉴페이스)" :
                mType == 6 ? "이상형 소개(동네 친구)" :
                mType == 7 ? "이상형 소개(지금 접속중)" :
                mType == 8 ? "이상형 소개(종교)" :
                mType == 9 ? "이상형 소개(음주 습관)" :
                mType == 10 ? "이상형 소개(키)" :
                mType == 11 ? "이상형 소개(관심사)" : "직업")
                .enqueue(new Net.ResponseCallBack<MEnergy>() {
                    @Override
                    public void onSuccess(MEnergy response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().energy = response.energy;

                        if (mType == 1) {
                            Intent intent = new Intent(ModelFindActivity.this, SelectTypeActivity.class);
                            intent.putExtra("type", "love");
                            intent.putExtra("data", "");
                            intent.putExtra("max_choose", 2);
                            startActivityForResult(intent, 0x0110);
                        } else if (mType == 2) {
                            reqModelFind(1);
                        } else if (mType == 3) {

                            ArrayList<String> arrayList1 = new ArrayList<>();
                            arrayList1.addAll(Arrays.asList(character2));

                            ArrayList<String> arrayList2 = new ArrayList<>();
                            arrayList2.addAll(Arrays.asList(character1));

                            new SelectDialog2(ModelFindActivity.this, arrayList1, arrayList2, mType, new SelectDialog2.Callback() {
                                @Override
                                public void onConfirm(String data1, String data2) {
                                    if (data2.equals("-"))
                                        data2 = "";
                                    reqModelFind(2, data1, data2, null);
                                }

                                @Override
                                public void onCancel() {
                                    restoreEnergy();
                                }
                            }).show();

                        } else if (mType == 4) {
                            Intent intent = new Intent(ModelFindActivity.this, SelectTypeActivity.class);
                            if (MyInfo.getInstance().gender == 1) {
                                intent.putExtra("type", "body_male");
                            } else {
                                intent.putExtra("type", "body_female");
                            }
                            intent.putExtra("data", "");
                            intent.putExtra("max_choose", 2);
                            startActivityForResult(intent, 0x0110);
                        } else if (mType == 5) {
                            reqModelFind(4);
                        } else if (mType == 6) {
                            reqModelFind(5);
                        } else if (mType == 7) {
                            reqModelFind(6);
                        } else if (mType == 8) {
                            Intent intent = new Intent(ModelFindActivity.this, SelectTypeActivity.class);
                            intent.putExtra("type", "belief");
                            intent.putExtra("data", "");
                            intent.putExtra("max_choose", 2);
                            startActivityForResult(intent, 0x0110);
                        } else if (mType == 9) {
                            Intent intent = new Intent(ModelFindActivity.this, SelectTypeActivity.class);
                            intent.putExtra("type", "drinking");
                            intent.putExtra("data", "");
                            intent.putExtra("max_choose", 2);
                            startActivityForResult(intent, 0x0110);
                        } else if (mType == 10) {
                            ArrayList<String> arrayList1 = new ArrayList<>();
                            ArrayList<String> arrayList2 = new ArrayList<>();

                            for (int i = 140; i <= 200; i++) {
                                arrayList1.add(String.valueOf(i));
                                arrayList2.add(String.valueOf(i));
                            }

                            new SelectDialog2(ModelFindActivity.this, arrayList1, arrayList2, mType,new SelectDialog2.Callback() {
                                @Override
                                public void onConfirm(String data1, String data2) {
                                    reqModelFind(9, data1, data2, null);
                                }

                                @Override
                                public void onCancel() {
                                    restoreEnergy();
                                }


                            }).show();
                        } else if (mType == 11) {

                            Intent intent = new Intent(ModelFindActivity.this, SelectTypeActivity.class);
                            intent.putExtra("type", "hobby");
                            intent.putExtra("data", "");
                            intent.putExtra("max_choose", 2);
                            startActivityForResult(intent, 0x0110);

                        } else if (mType == 12) {
                            Intent intent = new Intent(ModelFindActivity.this, SelectTypeActivity.class);
                            intent.putExtra("type", "job");
                            intent.putExtra("data", "");
                            intent.putExtra("max_choose", 2);
                            startActivityForResult(intent, 0x0110);
                        }
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();
                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(ModelFindActivity.this, response.res_msg);
                        }
                    }
                });
    }

    private void reqModelFind(final int type) {

        m_nSelectModelType = type;
        showProgress(this);

        Net.instance().api.get_model_find(MyInfo.getInstance().uid, type, null, null, null)
                .enqueue(new Net.ResponseCallBack<MUserList>() {
                    @Override
                    public void onSuccess(MUserList response) {
                        super.onSuccess(response);
                        hideProgress();
                        transformUserList(type, response);
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();
                        if (IS_UITEST) {
                            transformUserList(type, makeDummyDataForTest());
                        } else {
                            if (response.resultcode == 500) {
                                networkErrorOccupied(response);
                            }
                        }

                        restoreEnergy();
                    }
                });
    }

    private void reqModelFind(final int type, String data1, String data2, String data3) {

        m_nSelectModelType = type;
        showProgress(this);

        Net.instance().api.get_model_find(MyInfo.getInstance().uid, type, data1, data2, null)
                .enqueue(new Net.ResponseCallBack<MUserList>() {
                    @Override
                    public void onSuccess(MUserList response) {
                        super.onSuccess(response);
                        hideProgress();
                        transformUserList(type, response);
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();
                        if (IS_UITEST) {
                            transformUserList(type, makeDummyDataForTest());
                        } else {
                            if (response.resultcode == 500) {
                                networkErrorOccupied(response);
                            }
                        }

                        restoreEnergy();

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0x0110 && resultCode == RESULT_OK) {
            String type = data.getStringExtra("type");
            String selected = data.getStringExtra("data");

            if (selected == null || selected.length() == 0) {
                restoreEnergy();
                return;
            }

            String[] tmp = selected.split(",");

            switch (type) {
                case "hobby":
                    if (tmp.length == 1) {
                        reqModelFind(10, tmp[0], null, null);
                    } else if (tmp.length == 2) {
                        reqModelFind(10, tmp[0], tmp[1], null);
                    } else {
                        reqModelFind(10, tmp[0], tmp[1], tmp[2]);
                    }
                    break;
                case "love":
                    if (tmp.length == 1) {
                        reqModelFind(12, tmp[0], null, null);
                    } else {
                        reqModelFind(12, tmp[0], tmp[1], null);
                    }
                    break;
                case "body_male":
                case "body_female":
                    if (tmp.length == 1) {
                        reqModelFind(3, tmp[0], null, null);
                    } else {
                        reqModelFind(3, tmp[0], tmp[1], null);
                    }
                    break;
                case "belief":
                    if (tmp.length == 1) {
                        reqModelFind(7, tmp[0], null, null);
                    } else {
                        reqModelFind(7, tmp[0], tmp[1], null);
                    }
                    break;
                case "drinking":
                    if (tmp.length == 1) {
                        reqModelFind(8, tmp[0], null, null);
                    } else {
                        reqModelFind(8, tmp[0], tmp[1], null);
                    }
                    break;
                case "job":
                    if (tmp.length == 1) {
                        reqModelFind(11, tmp[0], null, null);
                    } else {
                        reqModelFind(11, tmp[0], tmp[1], null);
                    }
                    break;
            }
        } else {
            if (m_nSelectModelType > 0 ) {
                restoreEnergy();
            }
        }
    }

    // 에너지 복구
    private void restoreEnergy() {
        Net.instance().api.restore_my_energy(MyInfo.getInstance().uid, m_nSelectModelType)
                .enqueue(new Net.ResponseCallBack<MEnergy>() {
                    @Override
                    public void onSuccess(MEnergy response) {
                        super.onSuccess(response);
                        hideProgress();

                        MyInfo.getInstance().energy = response.energy;
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(ModelFindActivity.this, "오류입니다.");
                        }
                    }
                });
    }
}
