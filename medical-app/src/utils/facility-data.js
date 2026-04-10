// 应急设施详情数据
export const facilityData = {
  "fire-extinguisher-1": {
    name: "1号灭火器箱",
    location: "2号楼一层楼梯口",
    type: "ABC干粉灭火器",
    capacity: "4公斤",
    checkDate: "2023年10月15日",
    description: "适用于扑灭固体物质、液体和气体火灾，以及电器火灾。使用时拔掉保险销，握住喷管对准火焰根部，按下压把喷射。"
  },
  "fire-extinguisher-2": {
    name: "2号灭火器箱",
    location: "3号楼二层走廊中部",
    type: "ABC干粉灭火器",
    capacity: "4公斤",
    checkDate: "2023年10月15日",
    description: "适用于扑灭固体物质、液体和气体火灾，以及电器火灾。使用时拔掉保险销，握住喷管对准火焰根部，按下压把喷射。"
  },
  "first-aid": {
    name: "园区急救点",
    location: "1号楼一层大厅",
    equipment: ["急救箱", "AED自动除颤器", "担架", "氧气瓶"],
    service: "提供基础急救处理，可联系值班医护人员",
    hours: "24小时开放"
  },
  "security-room": {
    name: "主保安室",
    location: "园区主入口右侧",
    function: ["24小时安保值班", "监控中心", "访客登记", "应急物资储备"],
    staff: "4名安保人员轮班",
    equipment: ["对讲机", "应急照明", "防爆设备", "消防应急包"]
  },
  "evacuation-exit": {
    name: "主要疏散出口",
    location: "4号楼西侧",
    width: "1.5米",
    capacity: "可同时容纳30人通过",
    direction: "通向园区紧急集合点",
    notes: "出口处设有应急照明和疏散指示标志，保持24小时畅通"
  }
}