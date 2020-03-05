package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.entity.LoginUserEntity;
import com.xiandao.android.entity.OverallSituationEntity;
import com.xiandao.android.entity.QueryRoomsResultEntity;
import com.xiandao.android.entity.SpinnerEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.FileManagement;
import com.xiandao.android.utils.Tools;
import com.xiandao.android.view.MySpiner;
import com.xiandao.android.view.alertview.AlertView;
import com.xiandao.android.view.alertview.OnItemClickListener;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类描述的是:创建常用地址activity
 *
 * @author TanYong
 *         create at 2017/5/11 21:27
 */
@ContentView(R.layout.activity_create_common_address)
public class CreateCommonAddressActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.iv_title_left)
    private ImageView ivTitleLeft;
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.btn_create_common_address_ok)
    private TextView btn_create_common_address_ok;
    //楼盘
    @ViewInject(R.id.ms_create_common_address_projects)
    private MySpiner ms_create_common_address_projects;
    //楼栋
    @ViewInject(R.id.ms_create_common_address_buildings)
    private MySpiner ms_create_common_address_buildings;
    //单元
    @ViewInject(R.id.ms_create_common_address_cells)
    private MySpiner ms_create_common_address_cells;
    //房屋
    @ViewInject(R.id.ms_create_common_address_rooms)
    private MySpiner ms_create_common_address_rooms;

    private LoginUserEntity userEntity;

    private List<SpinnerEntity> projects = new ArrayList<>();//楼盘数据
    private List<SpinnerEntity> buildings = new ArrayList<>();//楼栋数据
    private List<SpinnerEntity> cells = new ArrayList<>();//单元数据
    private List<SpinnerEntity> rooms = new ArrayList<>();//房间数据

    private int selectProjectId;//被选中的楼盘ID
    private int selectBuildingId;//被选中的楼栋ID
    private int selectCellId;//被选中的单元ID
    private int selectRoomId;//被选中的房间ID
    private String selectProjectName;//被选中的楼盘Name
    private String selectBuildingName;//被选中的楼栋Name
    private String selectCellName;//被选中的单元Name
    private String selectRoomName;//被选中的房间Name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    private void init() {
        userEntity = FileManagement.getLoginUserEntity();
        tvTitleName.setText("新建常用地址");
        ivTitleLeft.setOnClickListener(this);
        btn_create_common_address_ok.setOnClickListener(this);
        queryProjects();

        ms_create_common_address_projects.setOnSelectItemValue(new MySpiner.GetSeleceValue() {
            @Override
            public void getSeleceValue() {
                selectProjectName = ms_create_common_address_projects.getSelectItemValue();
                for (SpinnerEntity entity : projects) {
                    if (selectProjectName.equals(entity.getName())) {
                        selectProjectId = Integer.valueOf(entity.getId());
                        queryBuildings();
                    }
                }
            }
        });
        ms_create_common_address_buildings.setOnSelectItemValue(new MySpiner.GetSeleceValue() {
            @Override
            public void getSeleceValue() {
                selectBuildingName = ms_create_common_address_buildings.getSelectItemValue();
                for (SpinnerEntity entity : buildings) {
                    if (selectBuildingName.equals(entity.getName())) {
                        selectBuildingId = Integer.valueOf(entity.getId());
                        queryCells();
                    }
                }
            }
        });
        ms_create_common_address_cells.setOnSelectItemValue(new MySpiner.GetSeleceValue() {
            @Override
            public void getSeleceValue() {
                selectCellName = ms_create_common_address_cells.getSelectItemValue();
                for (SpinnerEntity entity : cells) {
                    if (selectCellName.equals(entity.getName())) {
                        selectCellId = Integer.valueOf(entity.getId());
                        queryRooms();
                    }
                }
            }
        });
        ms_create_common_address_rooms.setOnSelectItemValue(new MySpiner.GetSeleceValue() {
            @Override
            public void getSeleceValue() {
                selectRoomName = ms_create_common_address_rooms.getSelectItemValue();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.btn_create_common_address_ok:
                createCommonAddress();
                break;
        }
    }

    /**
     * @author TanYong
     * create at 2017/5/18 16:16
     * TODO：查询楼盘
     */
    private void queryProjects() {
        ApiHttpResult.queryProjects(this, new String[]{"ownerId", "appMobile"},
                new Object[]{userEntity.getUserId(), userEntity.getMobileNumber()}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        if (o != null) {
                            QueryRoomsResultEntity roomsResultEntity = (QueryRoomsResultEntity) o;
                            if (roomsResultEntity.getEntities() != null && roomsResultEntity.getEntities().size() > 0) {
                                if (projects.size() > 0)
                                    projects.clear();
                                projects.addAll(roomsResultEntity.getEntities());
                                List<String> projectsName = new ArrayList<String>();
                                for (SpinnerEntity entity : projects) {
                                    projectsName.add(entity.getName());
                                }
                                ms_create_common_address_projects.setdata(projectsName);
                                ms_create_common_address_projects.onItemClick(0);
                            } else {
                                Tools.showPrompt(roomsResultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(CreateCommonAddressActivity.this);
                        }
                    }
                });
    }

    /**
     * @author TanYong
     * create at 2017/5/18 16:16
     * TODO：查询楼栋
     */
    private void queryBuildings() {
        ApiHttpResult.queryBuildings(this, new String[]{"ownerId", "appMobile", "projectId"},
                new Object[]{userEntity.getUserId(), userEntity.getMobileNumber(), selectProjectId}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        if (o != null) {
                            QueryRoomsResultEntity roomsResultEntity = (QueryRoomsResultEntity) o;
                            if (roomsResultEntity.getEntities() != null && roomsResultEntity.getEntities().size() > 0) {
                                if (buildings.size() > 0)
                                    buildings.clear();
                                buildings.addAll(roomsResultEntity.getEntities());
                                List<String> buildingsName = new ArrayList<String>();
                                for (SpinnerEntity entity : buildings) {
                                    buildingsName.add(entity.getName());
                                }
                                ms_create_common_address_buildings.setdata(buildingsName);
                                ms_create_common_address_buildings.onItemClick(0);
                            } else {
                                Tools.showPrompt(roomsResultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(CreateCommonAddressActivity.this);
                        }
                    }
                });
    }

    /**
     * @author TanYong
     * create at 2017/5/18 16:16
     * TODO：查询单元
     */
    private void queryCells() {
        ApiHttpResult.queryCells(this, new String[]{"ownerId", "appMobile", "buildingId"},
                new Object[]{userEntity.getUserId(), userEntity.getMobileNumber(), selectBuildingId}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        if (o != null) {
                            QueryRoomsResultEntity roomsResultEntity = (QueryRoomsResultEntity) o;
                            if (roomsResultEntity.getEntities() != null && roomsResultEntity.getEntities().size() > 0) {
                                if (cells.size() > 0)
                                    cells.clear();
                                cells.addAll(roomsResultEntity.getEntities());
                                List<String> cellsName = new ArrayList<String>();
                                for (SpinnerEntity entity : cells) {
                                    cellsName.add(entity.getName());
                                }
                                ms_create_common_address_cells.setdata(cellsName);
                                ms_create_common_address_cells.onItemClick(0);
                            } else {
                                Tools.showPrompt(roomsResultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(CreateCommonAddressActivity.this);
                        }
                    }
                });
    }

    /**
     * @author TanYong
     * create at 2017/5/18 16:16
     * TODO：查询房间
     */
    private void queryRooms() {
        ApiHttpResult.queryRooms(this, new String[]{"ownerId", "appMobile", "cellId"},
                new Object[]{userEntity.getUserId(), userEntity.getMobileNumber(), selectCellId}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        if (o != null) {
                            QueryRoomsResultEntity roomsResultEntity = (QueryRoomsResultEntity) o;
                            if (roomsResultEntity.getEntities() != null && roomsResultEntity.getEntities().size() > 0) {
                                if (rooms.size() > 0)
                                    rooms.clear();
                                rooms.addAll(roomsResultEntity.getEntities());
                                List<String> roomsName = new ArrayList<String>();
                                for (SpinnerEntity entity : rooms) {
                                    roomsName.add(entity.getName());
                                }
                                ms_create_common_address_rooms.setdata(roomsName);
                                ms_create_common_address_rooms.onItemClick(0);
                            } else {
                                Tools.showPrompt(roomsResultEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(CreateCommonAddressActivity.this);
                        }
                    }
                });
    }

    /**
     * @author TanYong
     * create at 2017/5/19 14:47
     * TODO：创建常用地址
     */
    private void createCommonAddress() {
        ApiHttpResult.createCommonAddress(this, new String[]{"userId", "appMobile", "houses", "building", "cells", "roomNumber"},
                new Object[]{userEntity.getUserId(), userEntity.getMobileNumber(), selectProjectName,
                        selectBuildingName, selectCellName, selectRoomName}, new HttpUtils.DataCallBack<Object>() {
                    @Override
                    public void callBack(Object o) {
                        if (o != null) {
                            OverallSituationEntity entity = (OverallSituationEntity) o;
                            if ("0".equals(entity.getResultCode())) {
                                new AlertView("温馨提示", Tools.getStringValue("常用地址创建成功"),
                                        null, new String[]{"知道了"}, null, CreateCommonAddressActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(Object o, int position) {
                                        CreateCommonAddressActivity.this.finish();
                                    }
                                }).setCancelable(false).show();
                            } else {
                                Tools.showPrompt(entity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(CreateCommonAddressActivity.this);
                        }
                    }
                });
    }

}
