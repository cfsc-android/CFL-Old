package com.xiandao.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiandao.android.R;
import com.xiandao.android.adapter.AbstractSpinerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MySpinerNoBackgroud extends RelativeLayout implements OnClickListener, AbstractSpinerAdapter.IOnItemSelectListener {
    private TextView tv_value;
    private ImageButton bt_dropdown;
    private SpinerPopWindow mpopwindow;
    private List<String> spinerlist = new ArrayList<String>();
    private Context context;
    private boolean precondition = true;// 前置条件是否满足
    private String preconditionStirng;// 前置条件提醒
    private int addressType = 0;

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    private int grouption;// 专柜组id

    /**
     * @param context
     */
    public MySpinerNoBackgroud(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_spiner_no_backgroud, this, true);
        tv_value = (TextView) findViewById(R.id.tv_value);
        bt_dropdown = (ImageButton) findViewById(R.id.bt_dropdown);
        bt_dropdown.setOnClickListener(this);
        tv_value.setOnClickListener(this);
        mpopwindow = new SpinerPopWindow(context);

        this.context = context;
        mpopwindow.setItemListener(this);
        tv_value.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_value.setText("");
                } else {
                    mGetSeleceValue.getSeleceValue();
                }
            }
        });
    }

    public void setdata(List<String> list) {
        spinerlist = list;
        mpopwindow.refreshData(list, 0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_dropdown:
                if (precondition) {
                    Log.e("", "showSpinWindow");
                    showSpinWindow();
                } else {
                }
                break;
            case R.id.tv_value:
                if (precondition) {
                    showSpinWindow();
                } else {
                    Toast.makeText(context, preconditionStirng, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }

    private void showSpinWindow() {
        mpopwindow.setWidth(tv_value.getWidth());
        if (spinerlist.size() < 6) {
            mpopwindow.setHeight(tv_value.getHeight() * spinerlist.size() + 50);
        } else {
            mpopwindow.setHeight(tv_value.getHeight() * 6 + 20);
        }

        mpopwindow.showAsDropDown(tv_value);
    }

    public void setPrecondition(boolean bool) {
        precondition = bool;
    }

    public void setAddressType(int type, int grouption) {
        addressType = type;
        this.grouption = grouption;
    }

    @Override
    public void onItemClick(int pos) {
        setvalue(pos);

    }

    public void settext(String text) {
        tv_value.setText(text);
    }

    private void setvalue(int pos) {
        if (pos >= 0 && pos <= spinerlist.size()) {
            String value = spinerlist.get(pos);
            if (value != null) {
                tv_value.setText(value);
            }
        }
        if (this.mGetSeleceValue != null)
            this.mGetSeleceValue.getSeleceValue();
    }

    public void setDefault(int position) {
        setvalue(position);
    }

    public void clearValue() {
        tv_value.setText("");
    }

    public String getValue() {
        if (tv_value.getText() == null) {
            return null;
        }
        return tv_value.getText().toString();
    }

    /**
     * @param hint
     * @Title: setHint
     * @Description: TODO 为下拉框添加hint
     * @return: void
     */
    public void setHint(String hint) {
        tv_value.setHint(hint);
    }

    public void setPreconditionStirng(String p) {
        preconditionStirng = p;
    }

    public void setUse(boolean bool) {
        tv_value.setFocusable(bool);
        tv_value.setClickable(bool);
        tv_value.setEnabled(bool);
        bt_dropdown.setFocusable(bool);
        bt_dropdown.setClickable(bool);
        bt_dropdown.setEnabled(bool);
    }

    private GetSeleceValue mGetSeleceValue;

    public void setOnSelectItemValue(GetSeleceValue mGetSeleceValue) {
        this.mGetSeleceValue = mGetSeleceValue;
    }

    public String getSelectItemValue() {
        return tv_value.getText().toString();
    }

    /**
     * 获取选中的item value
     */
    public interface GetSeleceValue {
        public abstract void getSeleceValue();
    }
}
