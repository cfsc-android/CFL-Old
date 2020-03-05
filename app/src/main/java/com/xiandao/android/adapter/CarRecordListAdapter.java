package com.xiandao.android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.CarRecord;
import com.xiandao.android.entity.HikCarCrossRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.xiandao.android.R.drawable.bg_car_record_mode_fail;
import static com.xiandao.android.R.drawable.bg_car_record_mode_success;

/**
 * Created by ZXL on 2019/4/2.
 * Describe:
 */
public class CarRecordListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<HikCarCrossRecord> carRecordArrayList;

    public CarRecordListAdapter(Context context, ArrayList<HikCarCrossRecord> carRecordArrayList) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.carRecordArrayList = carRecordArrayList;
    }

    @Override
    public int getCount() {
        return carRecordArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return carRecordArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CarRecordListAdapter.CarRecordHolder carRecordHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.car_record_list_item, null);
            carRecordHolder = new CarRecordListAdapter.CarRecordHolder();
            carRecordHolder.tvCarCode = (TextView) convertView.findViewById(R.id.tv_car_record_car_code);
            carRecordHolder.tvInTime = (TextView) convertView.findViewById(R.id.tv_car_record_in_time);
            carRecordHolder.tvOutTime = (TextView) convertView.findViewById(R.id.tv_car_record_out_time);
            carRecordHolder.tvTiemLength = (TextView) convertView.findViewById(R.id.tv_car_record_time_length);
            carRecordHolder.tvCrossMode= (TextView) convertView.findViewById(R.id.tv_car_record_cross_mode);
            convertView.setTag(carRecordHolder);
        } else {
            carRecordHolder = (CarRecordListAdapter.CarRecordHolder) convertView.getTag();
        }
        HikCarCrossRecord data = carRecordArrayList.get(position);
        String crossTime=data.getCreateTime().replace("T"," ").replace("+08:00","");
        carRecordHolder.tvCarCode.setText(data.getPlateNo());
        carRecordHolder.tvInTime.setText(data.getVehicleOut()==0?crossTime:"");
        carRecordHolder.tvOutTime.setText(data.getVehicleOut()==1?crossTime:"");
        String parkingTimeLength="";
        if(data.getVehicleOut()==1){
            String lastIntime=getParkingTime(position);
            if(!"".equals(lastIntime)){
                try {
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date in_date=sdf.parse(lastIntime);
                    Date out_date=sdf.parse(crossTime);
                    long time=out_date.getTime()-in_date.getTime();
                    Log.e("getParkingTime",position+"__"+time);
                    if(time/1000/60>=60){
                        parkingTimeLength=(time/1000/60)%60==0?time/1000/60/60+"小时":time/1000/60/60+"小时"+(time/1000/60)%60+"分钟";
                    }else{
                        parkingTimeLength=time/1000/60+"分钟";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Log.e("getParkingTime",position+"__"+getParkingTime(position));
        }
        carRecordHolder.tvTiemLength.setText(parkingTimeLength);
        carRecordHolder.tvCrossMode.setText(getReleaseModeCh(data.getReleaseMode()));
        if(data.getReleaseMode()==0 || data.getReleaseMode()== 30){
            carRecordHolder.tvCrossMode.setTextColor(context.getResources().getColor(R.color.follow_background));
            carRecordHolder.tvCrossMode.setBackgroundResource(R.drawable.bg_car_record_mode_fail);
        }else{
            carRecordHolder.tvCrossMode.setTextColor(context.getResources().getColor(R.color.car_lock_theme));
            carRecordHolder.tvCrossMode.setBackgroundResource(R.drawable.bg_car_record_mode_success);
        }
        return convertView;
    }

    private String getReleaseModeCh(int mode){
        String result="其它";
        switch (mode){
            case 0:
                result="禁止放行";
                break;
            case 1:
                result="固定车包期";
                break;
            case 2:
                result="临时车入场";
                break;
            case 10:
                result="离线出场";
                break;
            case 11:
                result="缴费出场";
                break;
            case 12:
                result="预付费出场";
                break;
            case 13:
                result="免费出场";
                break;
            case 30:
                result="非法卡不放行";
                break;
            case 35:
                result="群组车放行";
                break;
        }
        return result;
    }

    private String getParkingTime(int position){
        String result="";
        if(position<carRecordArrayList.size()-1){
            if(carRecordArrayList.get(position+1).getVehicleOut()==0){
                result=carRecordArrayList.get(position+1).getCreateTime();
            }else{
                result=getParkingTime(position+1);
            }
        }
        if(!"".equals(result)){
            result=result.replace("T"," ").replace("+08:00","");
        }
        return result;
    }

    public class CarRecordHolder {
        TextView tvCarCode;//汽车牌照
        TextView tvInTime;//入场时间
        TextView tvOutTime;//出场时间
        TextView tvTiemLength;//停车时长
        TextView tvCrossMode;
    }
}