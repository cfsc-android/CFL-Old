package com.xiandao.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xiandao.android.R;
import com.xiandao.android.adapter.AbstractSpinerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: EditAndSpiner
 * @Description: 可输入和下拉
 */
public class EditAndSpiner extends RelativeLayout implements OnClickListener, AbstractSpinerAdapter.IOnItemSelectListener {
    private EditText tv_value;
    private ImageView bt_dropdown;
    private SpinerPopWindow mpopwindow;

    /**
     * @return the mpopwindow
     */
    public SpinerPopWindow getSpinerPopWindow() {
        return mpopwindow;
    }

    private List<String> spinerlist = new ArrayList<String>();
    private Context context;

    /**
     * 构造方法
     */
    public EditAndSpiner(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.spiner_edit_no_backgroud, this, true);
        tv_value = (EditText) findViewById(R.id.edit_value);
        bt_dropdown = (ImageView) findViewById(R.id.bt_dropdown);
        bt_dropdown.setOnClickListener(this);
        mpopwindow = new SpinerPopWindow(context);

        this.context = context;
        mpopwindow.setItemListener(this);
        tv_value.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_value.setText("");
                }
            }
        });
    }

    public void setdata(List<String> list) {
        spinerlist = list;
        mpopwindow.refreshData(list, 0);
    }

    @Override
    public void onItemClick(int pos) {
        setvalue(pos);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_dropdown:
                showSpinWindow();
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

    private void setvalue(int pos) {
        if (pos >= 0 && pos <= spinerlist.size()) {
            String value = spinerlist.get(pos);
            if (value != null) {
                tv_value.setText(value);
            }
        }
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

    public void setEditDisable() {
        tv_value.setKeyListener(null);
        tv_value.setEnabled(false);
    }

    /**
     * @return the tv_value
     */
    public EditText getTv_value() {
        return tv_value;
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
}
