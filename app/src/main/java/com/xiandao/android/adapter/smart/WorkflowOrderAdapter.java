package com.xiandao.android.adapter.smart;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiandao.android.R;
import com.xiandao.android.entity.smart.ResourceEntity;
import com.xiandao.android.entity.smart.WorkflowOrderEntity;
import com.xiandao.android.view.imagepreview.ImagePreviewListAdapter;
import com.xiandao.android.view.imagepreview.ImageViewInfo;
import com.xiandao.android.view.imagepreview.PreviewBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Created by Loong on 2020/2/17.
 * Version: 1.0
 * Describe: 工单工作流适配器
 */
public class WorkflowOrderAdapter extends BaseQuickAdapter<WorkflowOrderEntity, BaseViewHolder> {
    private Activity activity;
    private Map<Integer, GridLayoutManager> lms=new HashMap<>();
    private Map<Integer, ImagePreviewListAdapter> las=new HashMap<>();
    private GridLayoutManager mGridLayoutManager;
    public WorkflowOrderAdapter(Activity activity, int layoutResId, @Nullable List<WorkflowOrderEntity> data) {
        super(layoutResId, data);
        this.activity=activity;
    }

    @Override
    protected void convert(@NonNull final BaseViewHolder helper, WorkflowOrderEntity item) {
        ImageView avatar= helper.getView(R.id.item_workflow_avatar);
        if(TextUtils.isEmpty(item.getAvatarUrl())){
            Glide.with(activity)
                    .load(R.drawable.ic_launcher)
                    .circleCrop()
                    .error(R.drawable.ic_no_img)
                    .into(avatar);
        }else{
            Glide.with(activity)
                    .load(item.getAvatarUrl())
                    .circleCrop()
                    .error(R.drawable.ic_no_img)
                    .into(avatar);
        }
        helper.setText(R.id.item_workflow_user_name,item.getHandlerName());
        helper.setText(R.id.item_workflow_user_role,item.getShortDesc());
        helper.setText(R.id.item_workflow_content,item.getRemark());
        helper.setText(R.id.item_workflow_time,item.getCreateTime());


        RecyclerView pic=helper.getView(R.id.item_workflow_pic);
        lms.put(helper.getAdapterPosition(),new GridLayoutManager(activity,3));
//        final GridLayoutManager mGridLayoutManager = new GridLayoutManager(activity,3);
        pic.setLayoutManager(lms.get(helper.getAdapterPosition()));
        final List<ImageViewInfo> list=new ArrayList<>();
        List<ResourceEntity> picData=item.getResourceValues();
        if(picData!=null){
            for (int i = 0; i < picData.size(); i++) {
                list.add(new ImageViewInfo(picData.get(i).getUrl()));
            }
        }
//        ImagePreviewListAdapter imageAdapter=new ImagePreviewListAdapter(activity,R.layout.item_workflow_image_perview_list,list);
        las.put(helper.getAdapterPosition(),new ImagePreviewListAdapter(activity,R.layout.item_workflow_image_perview_list,list));
        pic.setAdapter(las.get(helper.getAdapterPosition()));
        pic.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                GridLayoutManager mGridLayoutManager=lms.get(helper.getAdapterPosition());
                for (int i = mGridLayoutManager.findFirstVisibleItemPosition(); i < adapter.getItemCount(); i++) {
                    View itemView = mGridLayoutManager.findViewByPosition(i);
                    Rect bounds = new Rect();
                    if (itemView != null) {
                        ImageView imageView = itemView.findViewById(R.id.iiv_item_image_preview);
                        imageView.getGlobalVisibleRect(bounds);
                    }
                    ImagePreviewListAdapter imageAdapter=las.get(helper.getAdapterPosition());
                    //计算返回的边界
                    imageAdapter.getItem(i).setBounds(bounds);
                }
                PreviewBuilder.from(activity)
                        .setImgs(list)
                        .setCurrentIndex(position)
                        .setSingleFling(true)
                        .setType(PreviewBuilder.IndicatorType.Number)
                        .start();
            }
        });
    }



}
