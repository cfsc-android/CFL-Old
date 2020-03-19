package com.xiandao.android.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiandao.android.R;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.PersonalInfomation;
import com.xiandao.android.entity.RoomInfoEntity;
import com.xiandao.android.entity.eventbus.EventBusMessage;
import com.xiandao.android.entity.eventbus.NickNameEventBusData;
import com.xiandao.android.entity.smart.WorkflowType;
import com.xiandao.android.ui.BaseLazyFragment;
import com.xiandao.android.ui.activity.CarManageActivity;
import com.xiandao.android.ui.activity.CommentActivity;
import com.xiandao.android.ui.activity.ExpressSearchActivity;
import com.xiandao.android.ui.activity.FaceCollectionActivity;
import com.xiandao.android.ui.activity.FaceCollectionListActivity;
import com.xiandao.android.ui.activity.HouseHoldActivity;
import com.xiandao.android.ui.activity.MyComplainActivity;
import com.xiandao.android.ui.activity.MyRepairsActivity;
import com.xiandao.android.ui.activity.PersonalInformationActivity;
import com.xiandao.android.ui.activity.SetMineActivity;
import com.xiandao.android.ui.activity.SetttingActivity;
import com.xiandao.android.ui.activity.UnLock;
import com.xiandao.android.ui.activity.WaitingForDevelopmentActivity;
import com.xiandao.android.ui.activity.WorkflowListActivity;
import com.xiandao.android.utils.Constants;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.GlideImageLoader;
import com.xiandao.android.utils.PhotoUtils;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.utils.XUtilsImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Date;

public class NewMineFragment extends BaseLazyFragment implements View.OnClickListener {
    private ImageView iv_new_mine_head;
    private TextView tv_new_mine_name, tv_new_mine_gongdan, tv_new_mine_tousu, tv_new_mine_address, tv_new_mine_car, tv_new_mine_bill,
             tv_new_nime_wallet, tv_new_mine_data, tv_new_mine_setting, tv_new_mine_face,tv_new_mine_express;
    private LinearLayout ll_new_mine_head;

    private LoginUserEntity userEntity;
    private ArrayList<RoomInfoEntity> roomInfoEntity;

    private boolean bind;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(FileManagement.getTokenInfo().equals("third")){
//            bind=false;
//        }else{
            bind=true;
//        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void injectView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_new_mine, null);
        setContentView(view);
        initView(view);
//        initData();
        if(!TextUtils.isEmpty(FileManagement.getUserInfoEntity().getAvatarResource())){
            Glide.with(this)
                    .load(FileManagement.getUserInfoEntity().getAvatarResource())
                    .error(R.drawable.ic_default_img)
                    .circleCrop()
                    .into(iv_new_mine_head);
        }
        tv_new_mine_name.setText(FileManagement.getUserInfoEntity().getNickName());
        tv_new_mine_address.setText(FileManagement.getUserInfoEntity().getAncestor());
        tv_new_mine_address.setTextColor(getResources().getColor(R.color.white));
    }

    private void initData(){
        /** 测试人脸采集 zxl 2019-4-8 */
        if(!bind){
            if(FileManagement.getLoginType().equals("wx")){
                XUtilsImageUtils.display(iv_new_mine_head,
                        FileManagement.getWXLogin().getIconurl(),
                        true);
                /** 注释 zxl 2019-4-8 */
                tv_new_mine_name.setText(FileManagement.getWXLogin().getName());
            }else{
                XUtilsImageUtils.display(iv_new_mine_head,
                        FileManagement.getQQLogin().getIconurl(),
                        true);
                /** 注释 zxl 2019-4-8 */
                tv_new_mine_name.setText(FileManagement.getQQLogin().getName());
            }
            tv_new_mine_address.setText("未绑定业主，无法获取房屋信息");
            tv_new_mine_address.setTextColor(getResources().getColor(R.color.payment_btn));
        }else{
            userEntity = FileManagement.getLoginUserEntity();
            roomInfoEntity = FileManagement.getRoomInfo();
            if (userEntity != null) {
                XUtilsImageUtils.display(iv_new_mine_head,
                        Constants.BASEHOST+userEntity.getHeadImageUrl(),
                        true);
                tv_new_mine_name.setText(userEntity.getNickName());
            }
            tv_new_mine_address.setText(FileManagement.getUserInfoEntity().getAncestor());
            tv_new_mine_address.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void initView(View view) {
        iv_new_mine_head = (ImageView) view.findViewById(R.id.iv_new_mine_head);
        tv_new_mine_address = (TextView) view.findViewById(R.id.tv_new_mine_address);

        ll_new_mine_head = (LinearLayout) view.findViewById(R.id.ll_new_mine_head);
        ll_new_mine_head.setOnClickListener(this);
        tv_new_mine_name = (TextView) view.findViewById(R.id.tv_new_mine_name);

        tv_new_mine_gongdan = (TextView) view.findViewById(R.id.tv_new_mine_gongdan);
        tv_new_mine_gongdan.setOnClickListener(this);
        tv_new_mine_tousu = (TextView) view.findViewById(R.id.tv_new_mine_tousu);
        tv_new_mine_tousu.setOnClickListener(this);
        tv_new_mine_car = (TextView) view.findViewById(R.id.tv_new_mine_car);
        tv_new_mine_car.setOnClickListener(this);
        tv_new_mine_bill = (TextView) view.findViewById(R.id.tv_new_mine_bill);
        tv_new_mine_bill.setOnClickListener(this);
        tv_new_nime_wallet = (TextView) view.findViewById(R.id.tv_new_nime_wallet);
        tv_new_nime_wallet.setOnClickListener(this);
        tv_new_mine_data = (TextView) view.findViewById(R.id.tv_new_mine_data);
        tv_new_mine_data.setOnClickListener(this);
        tv_new_mine_setting = (TextView) view.findViewById(R.id.tv_new_mine_setting);
        tv_new_mine_setting.setOnClickListener(this);
        tv_new_mine_face= (TextView) view.findViewById(R.id.tv_new_mine_face);
        tv_new_mine_face.setOnClickListener(this);
        tv_new_mine_express= (TextView) view.findViewById(R.id.tv_new_mine_express);
        tv_new_mine_express.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessage message){
        if("headerImg".equals(message.getMessage())){
            userEntity=FileManagement.getLoginUserEntity();
            XUtilsImageUtils.display(iv_new_mine_head,
                    Constants.BASEHOST+userEntity.getHeadImageUrl(),
                    true);
        }else if("bind".equals(message.getMessage())){
            Log.e("bind","Mine_bind");
            bind=true;
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle=new Bundle();
        switch (v.getId()) {
            case R.id.ll_new_mine_head://更换头像与昵称
                openActivity(PersonalInformationActivity.class);
//                openActivityResult(SetMineActivity.class);
                break;
            case R.id.tv_new_mine_gongdan://我的工单
                if(bind){
                    bundle.putSerializable("workflowType", WorkflowType.Order);
                    openActivity(WorkflowListActivity.class,bundle);
                }else{
                    EventBus.getDefault().post(new EventBusMessage<>("unbind"));
                }
                break;
            case R.id.tv_new_mine_tousu://我的投诉
                if(bind){
                    bundle.putSerializable("workflowType", WorkflowType.Complain);
                    openActivity(WorkflowListActivity.class,bundle);
                }else{
                    EventBus.getDefault().post(new EventBusMessage<>("unbind"));
                }
                break;
            case R.id.tv_new_mine_car:
                if(bind){
                    openActivity(CarManageActivity.class);
                }else{
                    EventBus.getDefault().post(new EventBusMessage<>("unbind"));
                }
                break;
            case R.id.tv_new_mine_bill:
                startActivity(new Intent(getActivity(), WaitingForDevelopmentActivity.class).putExtra("title", "账单明细"));
                break;
            case R.id.tv_new_nime_wallet:
//                openActivity(CommentActivity.class);
//                startActivity(new Intent(getActivity(), WaitingForDevelopmentActivity.class).putExtra("title", "评价"));
                openActivity(HouseHoldActivity.class);
                break;
            case R.id.tv_new_mine_data:
                openActivity(PersonalInformationActivity.class);
                break;
            case R.id.tv_new_mine_setting:
                openActivity(SetttingActivity.class);
//                startActivity(new Intent(getActivity(), WaitingForDevelopmentActivity.class).putExtra("title", "设置"));
                break;
//            case R.id.tv_my_order://我的订单
//                openActivity(MyOrderActivity.class);
//                break;
//            case R.id.tv_my_shop_address://我的收货地址
//                openActivity(MyReceivingAddressActivity.class);
//                break;
            case R.id.tv_new_mine_face:
//                openActivity(FaceCollectionActivity.class);
                if(bind){
                    openActivity(FaceCollectionListActivity.class);
                }else{
                    EventBus.getDefault().post(new EventBusMessage<>("unbind"));
                }
                break;
            case R.id.tv_new_mine_express:
                openActivity(ExpressSearchActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (100 == resultCode) {
            userEntity = FileManagement.getLoginUserEntity();
            tv_new_mine_name.setText(userEntity.getNickName());
            ImageLoader.getInstance().clearDiskCache();
            PhotoUtils.loadRoundImage(userEntity.getHeadImageUrl(), iv_new_mine_head, 200, 1);
        }
    }
}
