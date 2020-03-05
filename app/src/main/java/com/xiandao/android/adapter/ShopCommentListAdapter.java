package com.xiandao.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.GoodsCommentOwnersEntity;
import com.xiandao.android.entity.ShopCommentEntity;
import com.xiandao.android.utils.Tools;

import java.util.ArrayList;

/**
 * 此类描述的是:商品评价列表adapter
 *
 * @author TanYong
 *         create at 2017/6/14 17:28
 */
public class ShopCommentListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<ShopCommentEntity> shopCommentEntityArrayList;

    public ShopCommentListAdapter(Context context, ArrayList<ShopCommentEntity> shopCommentEntityArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.shopCommentEntityArrayList = shopCommentEntityArrayList;
    }

    @Override
    public int getCount() {
        return shopCommentEntityArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return shopCommentEntityArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShopCommentHolder shopCommentHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_shop_comment, null);
            shopCommentHolder = new ShopCommentHolder();
            shopCommentHolder.tv_buyer_name = (TextView) convertView.findViewById(R.id.tv_buyer_name);
            shopCommentHolder.tv_shop_comment_time = (TextView) convertView.findViewById(R.id.tv_shop_comment_time);
            shopCommentHolder.tv_shop_comment_content = (TextView) convertView.findViewById(R.id.tv_shop_comment_content);
            shopCommentHolder.tv_shop_comment_level = (TextView) convertView.findViewById(R.id.tv_shop_comment_level);
            shopCommentHolder.iv_star_1 = (ImageView) convertView.findViewById(R.id.iv_star_1);
            shopCommentHolder.iv_star_2 = (ImageView) convertView.findViewById(R.id.iv_star_2);
            shopCommentHolder.iv_star_3 = (ImageView) convertView.findViewById(R.id.iv_star_3);
            shopCommentHolder.iv_star_4 = (ImageView) convertView.findViewById(R.id.iv_star_4);
            shopCommentHolder.iv_star_5 = (ImageView) convertView.findViewById(R.id.iv_star_5);
            convertView.setTag(shopCommentHolder);
        } else {
            shopCommentHolder = (ShopCommentHolder) convertView.getTag();
        }
        ShopCommentEntity entity = shopCommentEntityArrayList.get(position);
        if (null != entity) {
            GoodsCommentOwnersEntity owners = entity.getOwners();
            if (null != owners) {
                if (!Tools.isEmpty(owners.getName())) {
                    String name = owners.getName();
                    String buyerName = "";
                    for (int i = 0; i < name.length(); i++) {
                        if (i == 0) {
                            buyerName = name.substring(0, 1);
                        } else {
                            buyerName = buyerName + "*";
                        }
                    }
                    shopCommentHolder.tv_buyer_name.setText(buyerName);
                }
            }
            shopCommentHolder.tv_shop_comment_time.setText(Tools.getStringValue(entity.getCommentdatetime()));
            shopCommentHolder.tv_shop_comment_content.setText(Tools.getStringValue(entity.getContent()));
            int stargrade = Integer.valueOf(entity.getStargrade());
            switch (stargrade) {
                case 1:
                    shopCommentHolder.iv_star_1.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_2.setImageResource(R.mipmap.star_gray);
                    shopCommentHolder.iv_star_3.setImageResource(R.mipmap.star_gray);
                    shopCommentHolder.iv_star_4.setImageResource(R.mipmap.star_gray);
                    shopCommentHolder.iv_star_5.setImageResource(R.mipmap.star_gray);
                    break;
                case 2:
                    shopCommentHolder.iv_star_1.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_2.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_3.setImageResource(R.mipmap.star_gray);
                    shopCommentHolder.iv_star_4.setImageResource(R.mipmap.star_gray);
                    shopCommentHolder.iv_star_5.setImageResource(R.mipmap.star_gray);
                    break;
                case 3:
                    shopCommentHolder.iv_star_1.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_2.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_3.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_4.setImageResource(R.mipmap.star_gray);
                    shopCommentHolder.iv_star_5.setImageResource(R.mipmap.star_gray);
                    break;
                case 4:
                    shopCommentHolder.iv_star_1.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_2.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_3.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_4.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_5.setImageResource(R.mipmap.star_gray);
                    break;
                case 5:
                    shopCommentHolder.iv_star_1.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_2.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_3.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_4.setImageResource(R.mipmap.star_yellow);
                    shopCommentHolder.iv_star_5.setImageResource(R.mipmap.star_yellow);
                    break;
                default:
                    break;
            }
            int commentGrage = Integer.valueOf(entity.getCommentgrade());
            switch (commentGrage) {
                case 0:
                    shopCommentHolder.tv_shop_comment_level.setText("评价等级：差评");
                    break;
                case 1:
                    shopCommentHolder.tv_shop_comment_level.setText("评价等级：中评");
                    break;
                case 2:
                    shopCommentHolder.tv_shop_comment_level.setText("评价等级：好评");
                    break;
                default:
                    break;
            }
        }
        return convertView;
    }

    public class ShopCommentHolder {
        TextView tv_buyer_name;
        TextView tv_shop_comment_time;
        TextView tv_shop_comment_content;
        TextView tv_shop_comment_level;
        ImageView iv_star_1;
        ImageView iv_star_2;
        ImageView iv_star_3;
        ImageView iv_star_4;
        ImageView iv_star_5;
    }
}