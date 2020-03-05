package com.xiandao.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiandao.android.R;
import com.xiandao.android.entity.ThemeEntity;
import com.xiandao.android.ui.activity.FindDetailsActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.GlideImageLoader;
import com.xiandao.android.utils.XUtilsImageUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.LinearViewHolder> {
    private Context mContext;
    private AdapterView.OnItemClickListener mListener;
    private List<ThemeEntity> themeEntities;

    public StaggeredGridAdapter(Context mContext, List<ThemeEntity> themeEntities) {
        this.mContext = mContext;
        this.themeEntities = themeEntities;
    }

    @Override
    public StaggeredGridAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_staggere_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(StaggeredGridAdapter.LinearViewHolder holder, final int position) {
        ThemeEntity themeEntity = themeEntities.get(position);
        if(themeEntity.getPicList()!=null&&themeEntity.getPicList().size()>0){
            Glide.with(mContext)
                    .load(Constants.BASEHOST + "/" + themeEntity.getPicList().get(0).getUrl())
                    .placeholder(R.drawable.pic_find_2)
                    .error(R.drawable.pic_find_2)
                    .into(holder.mImageView);
        }else{
            holder.mImageView.setVisibility(View.GONE);
        }

        holder.tv_theme_content.setText(themeEntity.getContent());
        holder.tv_theme_nick_name.setText(themeEntity.getOwnerName());
        if (themeEntity.getUpNumber() != null && !"".equals(themeEntity.getUpNumber())) {
            holder.tv_theme_count.setText(themeEntity.getUpNumber());
            if(Integer.parseInt(themeEntity.getUpNumber())>0){
                holder.iv_theme_up.setImageResource(R.drawable.btn_find_like_sel);
            }else{
                holder.iv_theme_up.setImageResource(R.drawable.btn_find_like_nor);
            }
        } else {
            holder.tv_theme_count.setText("0");
            holder.iv_theme_up.setImageResource(R.drawable.btn_find_like_nor);
        }
        if(themeEntity.getOwnerFace()!=null&&!"".equals(themeEntity.getOwnerFace())){
            XUtilsImageUtils.display(holder.iv_owner_face,Constants.BASEHOST+"/"+themeEntity.getOwnerFace(),true);
        }else{
            holder.iv_owner_face.setImageResource(R.drawable.iocn_find_user);
        }
        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FindDetailsActivity.class);
                intent.putExtra("themeEntity", themeEntities.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return themeEntities.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_item;
        private ImageView mImageView,iv_owner_face,iv_theme_up;
        private TextView tv_theme_content, tv_theme_nick_name, tv_theme_count;

        public LinearViewHolder(View itemView) {
            super(itemView);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            mImageView = (ImageView) itemView.findViewById(R.id.iv);
            tv_theme_content = (TextView) itemView.findViewById(R.id.tv_theme_content);
            tv_theme_nick_name = (TextView) itemView.findViewById(R.id.tv_theme_nick_name);
            tv_theme_count = (TextView) itemView.findViewById(R.id.tv_theme_count);
            iv_owner_face= (ImageView) itemView.findViewById(R.id.iv_owner_face);
            iv_theme_up= (ImageView) itemView.findViewById(R.id.iv_theme_up);
        }
    }
}
