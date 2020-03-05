package com.xiandao.android.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.ShopCar1Entity;
import com.xiandao.android.entity.StoreInfo;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Map;


/**
 * 购物车适配器
 */
public class ShopcatAdapter extends BaseExpandableListAdapter {
    private List<StoreInfo> groups;
    //这个String对应着StoreInfo的Id，也就是店铺的Id
    private Map<String, List<ShopCar1Entity>> childrens;
    private Context mcontext;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private GroupEditorListener groupEditorListener;
    private int count = 0;
//    private boolean flag = false; //组的编辑按钮是否可见，true可见，false不可见


    public ShopcatAdapter(List<StoreInfo> groups, Map<String, List<ShopCar1Entity>> childrens, Context mcontext) {
        this.groups = groups;
        this.childrens = childrens;
        this.mcontext = mcontext;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = groups.get(groupPosition).getId();
        return childrens.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ShopCar1Entity> childs = childrens.get(groups.get(groupPosition).getId());
        return childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.item_shopcat_group, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final StoreInfo group = (StoreInfo) getGroup(groupPosition);
        groupViewHolder.storeName.setText(group.getName());
        groupViewHolder.storeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());
            }
        });
        groupViewHolder.storeCheckBox.setChecked(group.isChoosed());

        /**【文字指的是组的按钮的文字】
         * 当我们按下ActionBar的 "编辑"按钮， 应该把所有组的文字显示"编辑",并且设置按钮为不可见
         * 当我们完成编辑后，再把组的编辑按钮设置为可见
         * 不懂，请自己操作淘宝，观察一遍
         */
        if (group.isActionBarEditor()) {
            group.setEditor(false);
//            groupViewHolder.storeEdit.setVisibility(View.GONE);
//            flag = false;
        } else {
//            flag = true;
//            groupViewHolder.storeEdit.setVisibility(View.VISIBLE);
        }

//        /**
//         * 思路:当我们按下组的"编辑"按钮后，组处于编辑状态，文字显示"完成"
//         * 当我们点击“完成”按钮后，文字显示"编辑“,组处于未编辑状态
//         */
//        if (group.isEditor()) {
//            groupViewHolder.storeEdit.setText("完成");
//        } else {
//            groupViewHolder.storeEdit.setText("编辑");
//        }
//
//        groupViewHolder.storeEdit.setOnClickListener(new GroupViewClick(group, groupPosition, groupViewHolder.storeEdit));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.item_shopcat_product, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        /**
         * 根据组的编辑按钮的可见与不可见，去判断是组对下辖的子元素编辑  还是ActionBar对组的下瞎元素的编辑
         * 如果组的编辑按钮可见，那么肯定是组对自己下辖元素的编辑
         * 如果组的编辑按钮不可见，那么肯定是ActionBar对组下辖元素的编辑
         */
//        if (flag) {
//            if (groups.get(groupPosition).isEditor()) {
//                childViewHolder.ll_shop_car_change_num.setVisibility(View.VISIBLE);
//                childViewHolder.delGoods.setVisibility(View.VISIBLE);
//                childViewHolder.tv_shop_car_count.setVisibility(View.GONE);
//                childViewHolder.tv_shop_car_price.setVisibility(View.GONE);
//            } else {
//                childViewHolder.delGoods.setVisibility(View.GONE);
//                childViewHolder.tv_shop_car_count.setVisibility(View.VISIBLE);
//                childViewHolder.tv_shop_car_price.setVisibility(View.VISIBLE);
//                childViewHolder.ll_shop_car_change_num.setVisibility(View.GONE);
//            }
//        } else {
        if (groups.get(groupPosition).isActionBarEditor()) {
            childViewHolder.delGoods.setVisibility(View.VISIBLE);
            childViewHolder.ll_shop_car_change_num.setVisibility(View.VISIBLE);
            childViewHolder.tv_shop_car_count.setVisibility(View.GONE);
            childViewHolder.tv_shop_car_price.setVisibility(View.GONE);
        } else {
            childViewHolder.delGoods.setVisibility(View.GONE);
            childViewHolder.tv_shop_car_count.setVisibility(View.VISIBLE);
            childViewHolder.tv_shop_car_price.setVisibility(View.VISIBLE);
            childViewHolder.ll_shop_car_change_num.setVisibility(View.GONE);
        }
//        }

        final ShopCar1Entity child = (ShopCar1Entity) getChild(groupPosition, childPosition);
        if (child != null) {
            childViewHolder.tv_shop_car_goods_name.setText(child.getName());
            childViewHolder.tv_shop_car_discountprice.setText("￥" + child.getDiscountprice() + "");
            childViewHolder.edittext_num.setText(String.valueOf(child.getCount()));

            if (child.getResourceList() != null && child.getResourceList().size() > 0) {
                PhotoUtils.loadImage(child.getResourceList().get(0).getUrl(), childViewHolder.iv_shop_car1);
            }

            childViewHolder.tv_shop_car_size.setText(Tools.getStringValue(child.getSize()));
            //设置打折前的原价
            SpannableString spannableString = new SpannableString("￥" + child.getPrice() + "");
            StrikethroughSpan span = new StrikethroughSpan();
            spannableString.setSpan(span, 0, spannableString.length() - 1 + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            //避免无限次的append
            if (childViewHolder.tv_shop_car_price.length() > 0) {
                childViewHolder.tv_shop_car_price.setText("");
            }
            childViewHolder.tv_shop_car_price.setText(spannableString);
            childViewHolder.tv_shop_car_count.setText("x" + child.getCount() + "");
            childViewHolder.cb_shop_car1.setChecked(child.isChoosed());
            childViewHolder.cb_shop_car1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    child.setChoosed(((CheckBox) v).isChecked());
                    childViewHolder.cb_shop_car1.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());
                }
            });
            childViewHolder.text_num_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition, childPosition, childViewHolder.edittext_num, childViewHolder.cb_shop_car1.isChecked());
                }
            });
            childViewHolder.text_num_subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition, childPosition, childViewHolder.edittext_num, childViewHolder.cb_shop_car1.isChecked());
                }
            });
            childViewHolder.edittext_num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(groupPosition, childPosition, childViewHolder.edittext_num, childViewHolder.cb_shop_car1.isChecked(), child);
                }
            });
            childViewHolder.delGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.childDelete(groupPosition, childPosition);
                }
            });
        }
        return convertView;
    }

    /**
     * @param groupPosition
     * @param childPosition
     * @param showCountView
     * @param isChecked
     * @param child
     */
    private void showDialog(final int groupPosition, final int childPosition, final View showCountView, final boolean isChecked, final ShopCar1Entity child) {
        final AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(mcontext);
        View view = LayoutInflater.from(mcontext).inflate(R.layout.dialog_change_num, null);
        final AlertDialog dialog = alertDialog_Builder.create();
        dialog.setView(view);//errored,这里是dialog，不是alertDialog_Buidler
        count = Integer.valueOf(child.getCount());
        final EditText num = (EditText) view.findViewById(R.id.dialog_num);
        num.setText(count + "");
        //自动弹出键盘
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Tools.showKeyboard(mcontext, showCountView);
            }
        });
        final TextView increase = (TextView) view.findViewById(R.id.dialog_increaseNum);
        final TextView DeIncrease = (TextView) view.findViewById(R.id.dialog_reduceNum);
        final TextView pButton = (TextView) view.findViewById(R.id.dialog_Pbutton);
        final TextView nButton = (TextView) view.findViewById(R.id.dialog_Nbutton);
        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(num.getText().toString().trim());
                if (number == 0) {
                    dialog.dismiss();
                } else {
                    Log.i("数量=", number + "");
                    num.setText(String.valueOf(number));
                    child.setCount(number + "");
                    modifyCountInterface.doUpdate(groupPosition, childPosition, showCountView, isChecked);
                    dialog.dismiss();
                }
            }
        });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                num.setText(String.valueOf(count));
            }
        });
        DeIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1) {
                    count--;
                    num.setText(String.valueOf(count));
                }
            }
        });
        dialog.show();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public GroupEditorListener getGroupEditorListener() {
        return groupEditorListener;
    }

    public void setGroupEditorListener(GroupEditorListener groupEditorListener) {
        this.groupEditorListener = groupEditorListener;
    }

    public CheckInterface getCheckInterface() {
        return checkInterface;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public ModifyCountInterface getModifyCountInterface() {
        return modifyCountInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }


    static class GroupViewHolder {
        @ViewInject(R.id.store_checkBox)
        CheckBox storeCheckBox;
        @ViewInject(R.id.store_name)
        TextView storeName;
//        @ViewInject(R.id.store_edit)
//        TextView storeEdit;

        public GroupViewHolder(View view) {
            x.view().inject(this, view);
        }
    }

    /**
     * 店铺的复选框
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素的位置
         * @param isChecked     组元素的选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变触发的事件
         *
         * @param groupPosition 组元素的位置
         * @param childPosition 子元素的位置
         * @param isChecked     子元素的选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }


    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素的位置
         * @param childPosition 子元素的位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        void doUpdate(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删除子Item
         *
         * @param groupPosition
         * @param childPosition
         */
        void childDelete(int groupPosition, int childPosition);
    }

    /**
     * 监听编辑状态
     */
    public interface GroupEditorListener {
        void groupEditor(int groupPosition);
    }

    /**
     * 使某个小组处于编辑状态
     */
    private class GroupViewClick implements View.OnClickListener {
        private StoreInfo group;
        private int groupPosition;
        private TextView editor;

        public GroupViewClick(StoreInfo group, int groupPosition, TextView editor) {
            this.group = group;
            this.groupPosition = groupPosition;
            this.editor = editor;
        }

        @Override
        public void onClick(View v) {
            if (editor.getId() == v.getId()) {
                groupEditorListener.groupEditor(groupPosition);
                if (group.isEditor()) {
                    group.setEditor(false);
                } else {
                    group.setEditor(true);
                }
                notifyDataSetChanged();
            }
        }
    }


    static class ChildViewHolder {
        @ViewInject(R.id.cb_shop_car1)
        CheckBox cb_shop_car1;
        @ViewInject(R.id.iv_shop_car1)
        ImageView iv_shop_car1;
        @ViewInject(R.id.tv_shop_car_goods_name)
        TextView tv_shop_car_goods_name;
        @ViewInject(R.id.tv_shop_car_size)
        TextView tv_shop_car_size;
        @ViewInject(R.id.tv_shop_car_discountprice)
        TextView tv_shop_car_discountprice;
        @ViewInject(R.id.tv_shop_car_price)
        TextView tv_shop_car_price;
        @ViewInject(R.id.tv_shop_car_count)
        TextView tv_shop_car_count;
        @ViewInject(R.id.ll_shop_car_data)
        LinearLayout ll_shop_car_data;
        @ViewInject(R.id.text_num_subtract)
        Button text_num_subtract;
        @ViewInject(R.id.edittext_num)
        EditText edittext_num;
        @ViewInject(R.id.text_num_add)
        Button text_num_add;
        //        @ViewInject(R.id.tv_shop_car_size_1)
//        TextView tv_shop_car_size_1;
        @ViewInject(R.id.del_goods)
        TextView delGoods;
        @ViewInject(R.id.ll_shop_car_change_num)
        LinearLayout ll_shop_car_change_num;

        public ChildViewHolder(View view) {
            x.view().inject(this, view);
        }
    }
}
