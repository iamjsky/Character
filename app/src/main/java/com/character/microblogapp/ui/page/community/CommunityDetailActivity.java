package com.character.microblogapp.ui.page.community;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.character.microblogapp.R;
import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.model.MBase;
import com.character.microblogapp.model.MComment;
import com.character.microblogapp.model.MCommunityInfo;
import com.character.microblogapp.model.MError;
import com.character.microblogapp.net.Net;
import com.character.microblogapp.ui.adapter.CommunityCommentAdapter;
import com.character.microblogapp.ui.page.BaseActivity;
import com.character.microblogapp.util.GlideUtil;
import com.character.microblogapp.util.Toaster;
import com.character.microblogapp.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
public class CommunityDetailActivity extends BaseActivity {

    int uid = 0;

    @BindView(R.id.imv_profile)
    ImageView imvProfile;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_nickname)
    TextView tvNickname;

    @BindView(R.id.tv_time)
    TextView tvTime;

    /*@BindView(R.id.vpBanner)
    ViewPager vpBanner;

    @BindView(R.id.ciBanner)
    CirclePageIndicator ciBanner;*/

    @BindView(R.id.imv_image)
    ImageView imvImage;

    @BindView(R.id.imv_image1)
    ImageView imvImage1;

    @BindView(R.id.imv_image2)
    ImageView imvImage2;

    @BindView(R.id.tv_content)
    TextView tvContent;

    @BindView(R.id.tv_comment_count)
    TextView tvCommentCount;

    @BindView(R.id.rcv_comment)
    RecyclerView rcvComment;

    @BindView(R.id.et_comment)
    EditText etComment;

    @BindView(R.id.btnMore)
    Button btnMore;

    @BindView(R.id.rlBg)
    RelativeLayout rlBg;

    private CommunityCommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_community_detail);
    }

    @Override
    protected void initUI() {
        super.initUI();

        uid = getIntent().getIntExtra("id", 0);

        adapter = new CommunityCommentAdapter(this, new ArrayList<MComment.Comment>(), new CommunityCommentAdapter.OnItemClickListener() {
            @Override
            public void onDelete(int pos) {
                MComment.Comment comment = (MComment.Comment) adapter.getItem(pos);
                if (comment != null) {
                    communityDelete(comment.uid, pos);
                }
            }
        });
        rcvComment.setAdapter(adapter);

        getCommunityDetail();

        initCommentList();
    }

    void communityDelete(int comment_id, final int pos) {
        Net.instance().api.remove_community_comment(MyInfo.getInstance().uid, uid, comment_id)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        Toaster.showShort(CommunityDetailActivity.this, "삭제되었습니다");
                        adapter.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        tvCommentCount.setText(String.format("댓글 %s개", response.res_msg));
                        tvCommentCount.setVisibility(response.res_msg.equals("0") ? View.GONE : View.VISIBLE);
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        networkErrorOccupied(response);
                    }
                });
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        finish();
    }

    @OnClick({R.id.btnMenu, R.id.rlBg})
    void onMenu(View view) {
        if (view.getId() == R.id.rlBg) {
            rlBg.setVisibility(View.GONE);
        } else {
            rlBg.setVisibility(rlBg.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
    }

    @OnClick(R.id.btnMore)
    void onMore() {
        rlBg.setVisibility(View.GONE);
        if (btnMore.getText().toString().equals("신고하기")) {
            new AlertDialog.Builder(this).setTitle("신고").setMessage("해당 게시글을 신고하시겠습니까?").setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    blame();
                }
            }).setNegativeButton(R.string.cancel, null).show();
        } else {
            new AlertDialog.Builder(this).setTitle("삭제").setMessage("해당 게시글을 삭제하시겠습니까?").setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    remove();
                }
            }).setNegativeButton(R.string.cancel, null).show();
        }
    }

    @OnClick(R.id.tv_comment_save)
    void onCommentSave() {
        String comment = etComment.getText().toString();
        if (comment.isEmpty()) {
            Toaster.showShort(this, "댓글을 입력해주세요");
            return;
        }

        Net.instance().api.add_community_comment(
                MyInfo.getInstance().uid,
                uid,
                comment,
                0)
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        etComment.setText("");
                        hideSoftKeyboard(CommunityDetailActivity.this);
                        Toaster.showShort(CommunityDetailActivity.this, "등록되었습니다");
                        initCommentList();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        networkErrorOccupied(response);
                    }
                });
    }

    void remove() {
        Net.instance().api.remove_community(
                MyInfo.getInstance().uid,
                uid
                )
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        Toaster.showShort(CommunityDetailActivity.this, "삭제되었습니다");
                        finish();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        if (response.resultcode == 11) {
                            Toaster.showShort(CommunityDetailActivity.this, "댓글이 있는 커뮤니티는 관리자만 삭제가능합니다.");
                        } else {
                            networkErrorOccupied(response);
                        }
                    }
                });
    }

    void blame() {
        Net.instance().api.add_report_community(
                MyInfo.getInstance().uid,
                uid
        )
                .enqueue(new Net.ResponseCallBack<MBase>() {
                    @Override
                    public void onSuccess(MBase response) {
                        super.onSuccess(response);
                        Toaster.showShort(CommunityDetailActivity.this, "신고되었습니다");
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        networkErrorOccupied(response);
                    }
                });
    }

    void getCommunityDetail() {
        Net.instance().api.get_community_detail_info(MyInfo.getInstance().uid, uid)
                .enqueue(new Net.ResponseCallBack<MCommunityInfo>() {
                    @Override
                    public void onSuccess(MCommunityInfo response) {
                        super.onSuccess(response);
                        tvTitle.setText(response.info.title);
                        tvNickname.setText(response.info.nickname);
                        tvTime.setText(response.info.reg_time);
                        btnMore.setText(response.info.usr_uid == MyInfo.getInstance().uid ? "삭제하기" : "신고하기");

                        GlideUtil.loadRoundImage(imvProfile, response.info.profile, Util.dp2px(getApplicationContext(), 17));

                        if (response.info.add_file.size() > 0) {
                            GlideUtil.loadImage(imvImage, response.info.add_file.get(0));
                            if (response.info.add_file.size() > 1) {
                                GlideUtil.loadImage(imvImage1, response.info.add_file.get(1));
                                if (response.info.add_file.size() > 2) {
                                    GlideUtil.loadImage(imvImage2, response.info.add_file.get(2));
                                } else {
                                    imvImage2.setVisibility(View.GONE);
                                }
                            } else {
                                imvImage1.setVisibility(View.GONE);
                                imvImage2.setVisibility(View.GONE);
                            }
                        } else {
                            imvImage.setVisibility(View.GONE);
                            imvImage1.setVisibility(View.GONE);
                            imvImage2.setVisibility(View.GONE);
                        }

                        tvContent.setText(response.info.content);
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        networkErrorOccupied(response);
                    }
                });
    }

    int pageNum = 1;
    int pageCount = 0;
    boolean loadingEnd = false;
    boolean lockRcv = false;

    void initCommentList() {
        pageNum = 1;
        pageCount = 0;
        loadingEnd = false;
        lockRcv = false;

        getCommentList();
    }

    void getCommentList() {
        lockRcv = true;

        Net.instance().api.get_community_comment_list(MyInfo.getInstance().uid, uid)
                .enqueue(new Net.ResponseCallBack<MComment>() {
                    @Override
                    public void onSuccess(MComment response) {
                        super.onSuccess(response);

                        if (response.total_cnt > 0) {
                            tvCommentCount.setVisibility(View.VISIBLE);
                            tvCommentCount.setText(String.format("댓글 %d개", response.total_cnt));
                        } else {
                            tvCommentCount.setVisibility(View.GONE);
                        }

                        adapter.clear();
                        adapter.addAll(response.comment);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(MError response) {
                        super.onFailure(response);
                        networkErrorOccupied(response);
                    }
                });
    }

}
