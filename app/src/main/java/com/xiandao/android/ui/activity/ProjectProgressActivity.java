package com.xiandao.android.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiandao.android.R;
import com.xiandao.android.adapter.NoticeListAdapter;
import com.xiandao.android.entity.NoticeEntity;
import com.xiandao.android.entity.NoticeListEntity;
import com.xiandao.android.net.ApiHttpResult;
import com.xiandao.android.net.HttpUtils;
import com.xiandao.android.net.NetUtil;
import com.xiandao.android.ui.BaseActivity;
import com.xiandao.android.utils.Tools;

import org.xutils.event.annotation.ContentView;
import org.xutils.event.annotation.Event;
import org.xutils.event.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_project_progress)
public class ProjectProgressActivity extends BaseActivity {
    @ViewInject(R.id.tv_title_name)
    private TextView tvTitleName;
    @ViewInject(R.id.lv_project_progress)
    private ListView lv_project_progress;

    private ArrayList<NoticeEntity> noticeList = new ArrayList<>();
    private NoticeListAdapter mNoticeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitleName.setText("工程进度");
        mNoticeListAdapter=new NoticeListAdapter(this,noticeList);
        lv_project_progress.setAdapter(mNoticeListAdapter);
        lv_project_progress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoticeEntity selectNotice = noticeList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("title","工程进度");
                bundle.putString("noticeId", selectNotice.getNoticeId());
                openActivity(NoticeDetailActivity.class, bundle);

    //                NoticeEntity selectNotice = noticeList.get(position);
    //                String noticeId = selectNotice.getNoticeId();
    //                String titleName = selectNotice.getTitle();
    //                String url = selectNotice.getDetailUrl();
    //                String publisherName = selectNotice.getPublisherName();
    //                String publishTime = selectNotice.getPublishTime();
    //                int praises = selectNotice.getPraises();
    //                int reads = selectNotice.getReads();
    //                String resources = selectNotice.getResources();
    //                List<NoticeEntity.ResourceEntity> resourceList=selectNotice.getResourceList();
    //                startNoticeDetail(noticeId, titleName, url, publisherName, publishTime, praises, reads, resources,resourceList);
            }
        });
        getData();
    }
    /**
     * @author TanYong
     * create at 2017/6/14 17:07
     * TODO：跳转到详情
     */
    private void startNoticeDetail(String noticeId, String titleName, String url, String publisherName, String publishTime, int praises, int reads, String resources,List<NoticeEntity.ResourceEntity> resourceList) {
        Bundle bundle = new Bundle();
        bundle.putString("noticeId", noticeId);
        bundle.putString("titleName", titleName);
        bundle.putString("url", url);
        bundle.putString("publisherName", publisherName);
        bundle.putString("publishTime", publishTime);
        bundle.putInt("praises", praises);
        bundle.putInt("reads", reads);
        bundle.putString("resources", resources);
        bundle.putSerializable("resourceList", (Serializable) resourceList);
        openActivity(NoticeDetailActivity.class, bundle);
    }

    private void getData(){
        ApiHttpResult.queryTurnsAndHottopics(this, new String[]{"receive"},
                new Object[]{1},
                new HttpUtils.DataCallBack() {
                    @Override
                    public void callBack(Object o) {
                        if (o != null) {
                            NoticeListEntity noticeListEntity = (NoticeListEntity) o;
                            if (noticeListEntity != null && noticeListEntity.getResultCode().equals("0")) {
                                ArrayList<NoticeEntity> resultNoticeList = noticeListEntity.getNoticeList();
                                if (resultNoticeList != null && resultNoticeList.size() > 0) {
                                    for (int i = 0; i < resultNoticeList.size(); i++) {
                                        if(resultNoticeList.get(i).getNoticeType()==5){
                                            noticeList.add(resultNoticeList.get(i));
                                        }
                                    }

                                    mNoticeListAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Tools.showPrompt(noticeListEntity.getMsg());
                            }
                        } else {
                            NetUtil.toNetworkSetting(ProjectProgressActivity.this);
                        }
                    }
                });
    }

    @Event({R.id.iv_title_left})
    private void onClickEvent(View v){
        switch (v.getId()){
            case R.id.iv_title_left:
                finish();
                break;
        }
    }
}
