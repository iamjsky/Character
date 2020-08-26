package com.character.microblogapp.ui.page.setting;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.BlockInfo;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MBlock;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.SwitchAdapter;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;
import com.kakao.util.helper.log.Logger;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

public class BlockActivity extends BaseActivity {

    @BindView(R.id.btnBack)
    ImageButton btnBack;

    @BindView(R.id.btnMenu)
    ImageButton btnMenu;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvContent)
    RecyclerView rvContent;

    private SwitchAdapter adapter;
    private ArrayList<BlockInfo> arrContent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_block);
    }

    @Override
    protected void initUI() {
        super.initUI();

        tvTitle.setText("연락처 지인차단");
        btnMenu.setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);
        adapter = new SwitchAdapter(BlockActivity.this, arrContent, new SwitchAdapter.OnItemClickListener() {
            @Override
            public void onDetail(int pos, boolean status) {
                apiSet(arrContent.get(pos).phone, pos, status);
            }
        });

        rvContent.setAdapter(adapter);
        rvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        checkPermission();
    }

    @OnClick(R.id.btn_all_block)
    void onClickAll() {
        for (int i = 0 ; i < arrContent.size(); i ++) {
            if (!arrContent.get(i).isBlock) {
                apiSet(arrContent.get(i).phone, i, true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_PHONE_STATE_REQUEST) {
            if (permissions.length == grantResults.length) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }

                checkPermission();
            }
        }
    }

    private void checkPermission() {
        if (!Util.checkPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS})) {
            requestPermission(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS}, PERMISSIONS_PHONE_STATE_REQUEST);
            return;
        }

//        test
//        for (int i = 0; i < 5; i++) {
//            BlockInfo info = new BlockInfo();
//
//            info.phone = "01230123" + i;
//            info.isBlock = false;
//
//            arrContent.add(info);
//        }
//        loadAddressDataList("");
        phoneBook();
        apiList();
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    private void loadAddressDataList(String keyword) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        String selection = null;

        if (!keyword.isEmpty()) {
            selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                    + " like '%" + keyword + "%'";
        }

        Cursor contactCursor = managedQuery(uri, projection, selection,
                selectionArgs, sortOrder);

        int idx = 0;
        if (contactCursor.moveToFirst()) {
            do {
                String phonenumber = contactCursor.getString(1).replaceAll("-",
                        "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(" ", "");

                BlockInfo data = new BlockInfo();
                data.phone = phonenumber;
                data.isBlock = false;

                arrContent.add(data);

            } while (contactCursor.moveToNext());
        }

    }

    private Cursor getURI() {
        // 주소록 URI
        Uri people = ContactsContract.Contacts.CONTENT_URI;

        // 검색할 컬럼 정하기
        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER};

        // 쿼리 날려서 커서 얻기
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        // managedquery 는 activity 메소드이므로 아래와 같이 처리함
        return getContentResolver().query(people, projection, null, selectionArgs, sortOrder);
        // return managedQuery(people, projection, null, selectionArgs, sortOrder);
    }

    public void phoneBook() {
        showProgress(this);
        Cursor cursor = getURI();                    // 전화번호부 가져오기

        int end = cursor.getCount();                // 전화번호부의 갯수 세기
        Logger.d("ANDROES", "end = " + end);

        String[] name = new String[end];    // 전화번호부의 이름을 저장할 배열 선언
        String[] phone = new String[end];    // 전화번호부의 이름을 저장할 배열 선언
        int count = 0;

        if (cursor.moveToFirst()) {
            // 컬럼명으로 컬럼 인덱스 찾기
            int idIndex = cursor.getColumnIndex("_id");

            do {

                // 요소값 얻기
                int id = cursor.getInt(idIndex);
                String phoneChk = cursor.getString(2);
                if (phoneChk.equals("1")) {
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = " + id, null, null);

                    while (phones.moveToNext()) {
                        phone[count] = phones
                                .getString(phones
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                }
                name[count] = cursor.getString(1);

                // 개별 연락처 삭제
                // rowNum = getBaseContext().getContentResolver().delete(RawContacts.CONTENT_URI, RawContacts._ID+ " =" + id,null);

                // LogCat에 로그 남기기
                // Logger.i("ANDROES", "id=" + id +", name["+count+"]=" + name[count]+", phone["+count+"]=" + phone[count]);

                BlockInfo data = new BlockInfo();
                data.phone = phone[count];
                data.user_name = name[count];
                data.isBlock = false;
                if (data.phone != null && !data.phone.trim().isEmpty())
                    arrContent.add(data);

                count++;

            } while (cursor.moveToNext() || count > end);
        }

        hideProgress();
    }

    void apiList() {
        showProgress(this);
        Net.instance().api.get_block_user_list(MyInfo.getInstance().uid)
                .enqueue(new Net.ResponseCallBack<MBlock>() {
                    @Override
                    public void onSuccess(MBlock response) {
                        super.onSuccess(response);
                        hideProgress();

                        for (MBlock.Result info : response.arr_list) {
                            for (BlockInfo data : arrContent) {
                                if (info.phone.equals(data.phone)) {
                                    data.isBlock = true;
                                    break;
                                }
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(BlockActivity.this, "오류입니다.");
                        }
                    }
                });
    }

    void apiSet(String phone, final int position, final boolean status) {
        showProgress(this);
        Net.instance().api.set_block_user(MyInfo.getInstance().uid, phone, status ? 1 : 0)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        hideProgress();

                        arrContent.get(position).isBlock = status;
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        hideProgress();

                        if (response.resultcode == 500) {
                            networkErrorOccupied(response);
                        } else {
                            Toaster.showShort(BlockActivity.this, "오류입니다.");
                        }
                    }
                });
    }

}
