package com.character.microblogapp.ui.page.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MEnergy;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.ChartAdapter;
import com.character.microblogapp.ui.adapter.EnergyAdapter;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.widget.Toast;

public class EnergyActivity extends BaseActivity implements BillingProcessor.IBillingHandler{

    @BindView(R.id.btnBack)
    ImageButton btnBack;



    @BindView(R.id.tvCount)
    TextView tvCount;



    @BindView(R.id.rvContent)
    RecyclerView rvContent;

    private EnergyAdapter adapter;
    private ArrayList<String> arrContent = new ArrayList<>();

    int receiveCount = 0;

    // 결제 관련
    private BillingProcessor bp;
    public static ArrayList<SkuDetails> products;

    int m_nSelectPayType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_energy);

        bp = new BillingProcessor(this, billingKey, this);
    }

    @Override
    protected void initUI() {
        super.initUI();

        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname");




        arrContent.add("");
        arrContent.add("");
        arrContent.add("");
        arrContent.add("");
        arrContent.add("");
        arrContent.add("");
        arrContent.add("");

        adapter = new EnergyAdapter(this, arrContent, new EnergyAdapter.OnItemClickListener() {
            @Override
            public void onDetail(int pos) {
                apiEnergy(pos + 1);
            }
        });

        rvContent.setAdapter(adapter);

        LinearLayoutManager layout_manager = new LinearLayoutManager(this);
        rvContent.setLayoutManager(layout_manager);

        apiInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }



    void apiInfo() {
        showProgress(this);
        Net.instance().api.get_my_energy(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MEnergy>() {
                    @Override
                    public void onSuccess(MEnergy response) {
                        super.onSuccess(response);
                        hideProgress();

                        tvCount.setText(Util.numberFormatString(response.energy));
                        receiveCount = response.energy;
                        MyInfo.getInstance().energy = receiveCount;
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(EnergyActivity.this, "오류입니다.");
                        }
                    }
                });
    }

    void apiEnergy(int type) {
        m_nSelectPayType = type;
        if (PAY_TEST) {
            String pay_uid = "10000000000";
            afterPayment(pay_uid, type);
        } else {
            switch (type) {
                case 1:
                    purchaseProduct(PURCHARSE_ITEM_50);
                    break;
                case 2:
                    purchaseProduct(PURCHARSE_ITEM_100);
                    break;
                case 3:
                    purchaseProduct(PURCHARSE_ITEM_200);
                    break;
                case 4:
                    purchaseProduct(PURCHARSE_ITEM_300);
                    break;
                case 5:
                    purchaseProduct(PURCHARSE_ITEM_500);
                    break;
                case 6:
                    purchaseProduct(PURCHARSE_ITEM_1000);
                    break;
                case 7:
                    purchaseProduct(PURCHARSE_ITEM_3000);
                    break;
            }
        }
    }

    private void afterPayment(String paymentId, int type) {
        showProgress(this);
        Net.instance().api.purchase_energy(MyInfo.getInstance().uid, paymentId, type)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

//                        Intent intent = new Intent(EnergyActivity.this, ReceiveEnergyActivity.class);
//                        intent.putExtra("count", receiveCount);
//                        startActivity(intent);
                        Toaster.showShort(EnergyActivity.this, "결제되었습니다.");
                        apiInfo();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(EnergyActivity.this, "오류입니다.");
                        }
                    }
                });
    }

    //////////////////////////////////////////////////////////
    // 결제하기

    public void purchaseProduct(final String productId) {
        if (bp.isPurchased(productId)) {
            bp.consumePurchase(productId);
        }
        bp.purchase(this, productId); // 결제창 띄움
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        // 구매한 아이템 정보
        final SkuDetails sku = bp.getPurchaseListingDetails(productId);

//        final String proId = productId;
//        String purchaseMessage = sku.title + " 구매에 성공하였습니다.";
//        Toast.makeText(EnergyActivity.this, purchaseMessage, Toast.LENGTH_SHORT).show();

        // 구매 처리
        int amount = 0;
        try {
            // 사용자의 하트 100개를 추가
            afterPayment(productId, m_nSelectPayType);
        } catch (Exception e) {
            Toast.makeText(this, "오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        if (errorCode != 200) {
            String errorMessage = "결제 에러" + " (" + errorCode + ")";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBillingInitialized() {

    }
}
