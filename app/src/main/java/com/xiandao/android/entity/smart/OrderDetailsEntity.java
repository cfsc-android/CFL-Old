package com.xiandao.android.entity.smart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Loong on 2020/2/19.
 * Version: 1.0
 * Describe: 工单详情实体类
 */
public class OrderDetailsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private OrderEntity workOrder;
    private List<WorkflowOrderEntity> WorkOrderDetailsVo;

    public OrderEntity getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(OrderEntity workOrder) {
        this.workOrder = workOrder;
    }

    public List<WorkflowOrderEntity> getWorkOrderDetailsVo() {
        return WorkOrderDetailsVo;
    }

    public void setWorkOrderDetailsVo(List<WorkflowOrderEntity> workOrderDetailsVo) {
        WorkOrderDetailsVo = workOrderDetailsVo;
    }
}
