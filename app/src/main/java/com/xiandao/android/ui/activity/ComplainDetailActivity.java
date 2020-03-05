package com.xiandao.android.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiandao.android.R;
import com.xiandao.android.entity.smart.BaseEntity;
import com.xiandao.android.entity.smart.ComplainDetailsEntity;
import com.xiandao.android.entity.smart.ComplainEntity;
import com.xiandao.android.entity.smart.OrderDetailsEntity;
import com.xiandao.android.entity.smart.ResourceEntity;
import com.xiandao.android.entity.smart.WorkflowComplainEntity;
import com.xiandao.android.http.JsonParse;
import com.xiandao.android.http.MyCallBack;
import com.xiandao.android.http.XUtils;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.ui.fragment.ComplainDetailFragment;
import com.xiandao.android.ui.fragment.ComplainPlanFragment;
import com.xiandao.android.view.NoUnderlineSpan;
import com.xiandao.android.view.imagepreview.ImagePreviewListAdapter;
import com.xiandao.android.view.imagepreview.ImageViewInfo;
import com.xiandao.android.view.imagepreview.PreviewBuilder;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.xiandao.android.base.Config.BASE_URL;
import static com.xiandao.android.base.Config.WORKORDER;


/**
 * 此类描述的是:投诉详情activity
 *
 * @author TanYong
 *         create at 2017/6/2 10:05
 */
@ContentView(R.layout.activity_complain_detail_layout)
public class ComplainDetailActivity extends BaseActivity {
    @ViewInject(R.id.toolbar_tv_title)
    TextView toolbar_title;
    @ViewInject(R.id.toolbar_tv_action)
    TextView toolbar_tv_action;
    @ViewInject(R.id.toolbar_btn_action)
    ImageButton toolbar_btn_action;

    @ViewInject(R.id.complain_detail_user_avatar)
    private ImageView complain_detail_user_avatar;
    @ViewInject(R.id.complain_detail_user_name)
    private TextView complain_detail_user_name;
    @ViewInject(R.id.complain_detail_user_room)
    private TextView complain_detail_user_room;
    @ViewInject(R.id.complain_detail_complain_type)
    private TextView complain_detail_complain_type;
    @ViewInject(R.id.complain_detail_contact_tel)
    private TextView complain_detail_contact_tel;
    @ViewInject(R.id.complain_detail_remark_text)
    private TextView complain_detail_remark_text;
    @ViewInject(R.id.complain_detail_remark_time)
    private TextView complain_detail_remark_time;


    @ViewInject(R.id.complain_detail_workflow_ll)
    private LinearLayout complain_detail_workflow_ll;
    @ViewInject(R.id.complain_detail_workflow_action_fl)
    private FrameLayout complain_detail_workflow_action_fl;

    private String complainId;
    private NoUnderlineSpan mNoUnderlineSpan;
    private List<WorkflowComplainEntity> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar_title.setText("投诉详情");
        toolbar_btn_action.setVisibility(View.GONE);
        toolbar_tv_action.setText("进度");
        toolbar_tv_action.setVisibility(View.VISIBLE);

        complainId=getIntent().getExtras().getString("complain_id");
        getData();
        mNoUnderlineSpan = new NoUnderlineSpan();
    }

    @Event({R.id.toolbar_btn_back,R.id.toolbar_tv_action})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.toolbar_btn_back:
                finish();
                break;
            case R.id.toolbar_tv_action:
                Bundle bundle=new Bundle();
                bundle.putSerializable("complainWorkflowList", (Serializable) data);
                openActivity(WorkflowStepActivity.class,bundle);
                break;
        }
    }

    private void getData(){
        startProgressDialog("");
        XUtils.Get(BASE_URL+WORKORDER+"work/complaintOwner/getUserComplaintDetails/"+complainId,null,new MyCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtils.d(result);
                BaseEntity<ComplainDetailsEntity> baseEntity= JsonParse.parse(result,ComplainDetailsEntity.class);
                if(baseEntity.isSuccess()){
                    initView(baseEntity.getResult().getComplaint());
                    initWorkFlow(baseEntity.getResult().getCfcComplaintDetailsVo());
                    data.addAll(baseEntity.getResult().getCfcComplaintDetailsVo());
                }else{
                    showToast(baseEntity.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                showToast(ex.getMessage());
            }

            @Override
            public void onFinished() {
                super.onFinished();
                stopProgressDialog();
            }
        });
    }


    private void initView(ComplainEntity complainEntity){
        if(TextUtils.isEmpty(complainEntity.getCreatorAvatarUrl())){
            Glide.with(this)
                    .load(R.drawable.icon_user_default)
                    .error(R.drawable.ic_no_img)
                    .circleCrop()
                    .into(complain_detail_user_avatar);
        }else{
            Glide.with(this)
                    .load(complainEntity.getCreatorAvatarUrl())
                    .error(R.drawable.ic_no_img)
                    .circleCrop()
                    .into(complain_detail_user_avatar);
        }
        complain_detail_user_name.setText(complainEntity.getHouseholdName());
        complain_detail_user_room.setText(complainEntity.getRoomNameAll());
        complain_detail_complain_type.setText(complainEntity.getComplaintTypeName());
        if (!TextUtils.isEmpty(complainEntity.getHouseholdMobile())) {
            complain_detail_contact_tel.setText(complainEntity.getHouseholdMobile());
        } else {
            complain_detail_contact_tel.setVisibility(View.GONE);
        }
        if (complain_detail_contact_tel.getText() instanceof Spannable) {
            Spannable s = (Spannable) complain_detail_contact_tel.getText();
            s.setSpan(mNoUnderlineSpan, 0, s.length(), Spanned.SPAN_MARK_MARK);
        }
        complain_detail_remark_text.setText(complainEntity.getContent());
        complain_detail_remark_time.setText(complainEntity.getComplainTime());
    }

    private void initWorkFlow(final List<WorkflowComplainEntity> list){
        for (int i = 0; i < list.size(); i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.item_workflow_list,null);
            ImageView item_workflow_avatar=v.findViewById(R.id.item_workflow_avatar);
            TextView item_workflow_user_name=v.findViewById(R.id.item_workflow_user_name);
            TextView item_workflow_user_role=v.findViewById(R.id.item_workflow_user_role);
            TextView item_workflow_tel=v.findViewById(R.id.item_workflow_tel);
            TextView item_workflow_content=v.findViewById(R.id.item_workflow_content);
            RecyclerView item_workflow_pic=v.findViewById(R.id.item_workflow_pic);
            TextView item_workflow_node=v.findViewById(R.id.item_workflow_node);
            TextView item_workflow_time=v.findViewById(R.id.item_workflow_time);
            WorkflowComplainEntity item=list.get(i);
            if(TextUtils.isEmpty(item.getAvatarUrl())){
                Glide.with(this)
                        .load(R.drawable.ic_launcher)
                        .circleCrop()
                        .error(R.drawable.ic_no_img)
                        .into(item_workflow_avatar);
            }else{
                Glide.with(this)
                        .load(item.getAvatarUrl())
                        .circleCrop()
                        .error(R.drawable.ic_no_img)
                        .into(item_workflow_avatar);
            }
            item_workflow_user_name.setText(item.getHandlerName());
            item_workflow_user_role.setText(item.getShortDesc());
            if (!TextUtils.isEmpty(item.getMobile())) {
                item_workflow_tel.setText(item.getMobile());
            } else {
                item_workflow_tel.setVisibility(View.GONE);
            }
            if (item_workflow_tel.getText() instanceof Spannable) {
                Spannable s = (Spannable) item_workflow_tel.getText();
                s.setSpan(mNoUnderlineSpan, 0, s.length(), Spanned.SPAN_MARK_MARK);
            }
            item_workflow_content.setText(item.getComment());
            item_workflow_node.setText(item.getNodeName());
            item_workflow_time.setText(item.getCreateTime());
            final List<ImageViewInfo> data=new ArrayList<>();
            List<ResourceEntity> picData=item.getResourceValue();
            if(picData!=null){
                for (int j = 0; j < picData.size(); j++) {
                    data.add(new ImageViewInfo(picData.get(j).getUrl()));
                }
            }
            final ImagePreviewListAdapter imageAdapter=new ImagePreviewListAdapter(this,R.layout.item_workflow_image_perview_list,data);
            final GridLayoutManager mGridLayoutManager = new GridLayoutManager(this,3);
            item_workflow_pic.setLayoutManager(mGridLayoutManager);
            item_workflow_pic.setAdapter(imageAdapter);
            item_workflow_pic.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    for (int k = mGridLayoutManager.findFirstVisibleItemPosition(); k < adapter.getItemCount(); k++) {
                        View itemView = mGridLayoutManager.findViewByPosition(k);
                        Rect bounds = new Rect();
                        if (itemView != null) {
                            ImageView imageView = itemView.findViewById(R.id.iiv_item_image_preview);
                            imageView.getGlobalVisibleRect(bounds);
                        }
                        //计算返回的边界
                        imageAdapter.getItem(k).setBounds(bounds);
                    }
                    PreviewBuilder.from(ComplainDetailActivity.this)
                            .setImgs(data)
                            .setCurrentIndex(position)
                            .setSingleFling(true)
                            .setType(PreviewBuilder.IndicatorType.Number)
                            .start();
                }
            });
            complain_detail_workflow_ll.addView(v);
        }



    }
}
