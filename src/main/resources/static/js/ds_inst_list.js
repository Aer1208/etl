var TOOL_BAR = [ {
    id : 'view',
    text : '查看作业全部实例',
    iconCls : 'icon-search',
    float:'left',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        if(selectedArray.length==1){
            $("#dom_var_window").window( {
                width : "900",
                height : "600",
                title : "查看-" + selectedArray[0].dsId  ,
                modal : true,
                minimizable : false,
                closed : true
            });
            $("#jobtt").datagrid({
                url:"inst_list?jobId="+selectedArray[0].dsId,
                checkOnSelect : true,
                striped : true,
                rownumbers : true,
                pagination:true,
                pageSize:30,
                toolbar: ['-',{
                    id:"searchTxt",
                    text:'<span>数据日期：</span><input id="searchDataDate" type="text" style="padding: 1px;height: 100%" />'
                },{
                    id:'searchBtn',
                    text:'查询',
                    iconCls:'icon-search',
                    handler:function () {
                        searchInst();
                    }
                },'-'],
                loadFilter:pagerFilter,
                fitColumns : true,
                fit:false,
                columns:[[
                    { checkbox:true},
                    { field:'instId',title:'实例ID',width:80,sortable:true,align:'center'},
                    { field:'jobId',title:'作业ID',width:80,sortable:true,align:'center'},
                    { field:'dataDate',title:'数据日期',width:80,align:'center'},
                    { field:'startTime',title:'开始日期',width:120,align:'center',formatter:function(val,row,index){
                        var unixTimestamp = new Date(val);
                        return unixTimestamp.format('yyyy-MM-dd hh:mm:ss');
                    }},
                    { field:'endTime',title:'结束日期',width:120,align:'center',formatter:function(val,row,index){
                        var unixTimestamp = new Date(val);
                        return unixTimestamp.format('yyyy-MM-dd hh:mm:ss');
                    }},
                    { field:'runtime',title:'运行时长(s)',width:100,align:'center',formatter:function (val,row,index) {
                        return (row["endTime"] - row["startTime"])/1000;
                    }},
                    { field:'status',title:'状态',width:100,align:'center',formatter:function(val,row,index){
                        if (val == 1)
                            return "<span style=\"color:green\" >已完成</span>";
                        if (val == 0)
                            return "<span style=\"color:red\" >失败</span>";
                        if (val == 2)
                            return "<span style=\"color:cyan\" >运行中</span>";

                    }}
                ]]
            });
            $("#dom_var_window").window('open');
            $("#searchDataDate").bind("keypress",function (event) {
                if (event.keyCode == "13") {
                    searchInst();
                }
            });
            $("#searchDataDate").datebox({
                formatter:function(date){
                    var y = date.getFullYear();
                    var m = date.getMonth()+1;
                    var d = date.getDate();
                    m = m < 10 ? '0'+ m : m;
                    d = d < 10 ? '0' +d : d;
                    return y+''+m + '' +d;
                }
            })
            //$("#dom_var_window").window('refresh', "inst_job?jobId="+selectedArray[0].dsId);
        }else{
            $.messager.alert('提示', '请选择一条记录!', 'info');  //提示信息
        }
    }
},{
    id :"child",
    text : '查看后续作业',
    float:'left',
    iconCls : 'icon-search',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        if(selectedArray.length==1) {
            $("#child_job_window").window({
                width: "900",
                height: "680",
                title: "查看" + selectedArray[0].dsId + '后续作业',
                modal: true,
                minimizable: false,
                closed: true
            });
            $("#childjobtt").treegrid({
                url:"next_job?jobId="+selectedArray[0].dsId,
                fitColumns : true,
                fit:false,
                idField:"jobId",
                treeField:"jobId",
                columns:[[
                    { field:'jobId',title:'作业ID',width:40,sortable:true,align:'center'},
                    { field:'jobName',title:'作业名',width:120,sortable:true,align:'center'},
                    { field:'cmdName',title:'作业程序',width:120,sortable:true,align:'center'}
                ]]
            });
            $("#child_job_window").window('open');
        }else {
            $.messager.alert('提示', '请选择一条记录!', 'info');  //提示信息
        }
    }
},'-',{
    id:'searchBtn0',
    text:'查询',
    float:'right',
    iconCls:'icon-search',
    handler:searchKey
},{
    id:"searchTxt0",
    float:'right',
    text:'<span>搜索关键字:</span><input id="searchKey" type="text" style="padding: 1px;height: 100%" />'
}];
Date.prototype.format = function(format) {
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
}
$(function() {
    $("#tt").datagrid( {
        url : 'ds_inst_list',
        closable : true,
        checkOnSelect : true,
        striped : true,
        rownumbers : true,
        pagination:true,
        pageSize:40,
        'toolbar' : TOOL_BAR,
        fitColumns : true,
        fit:false,
        loadFilter:pagerFilter,
        columns:[[
            { checkbox:true},
            { field:'dsId',title:'数据源编号',width:80,sortable:true,align:'center'},
            { field:'dsName',title:'数据源名称',width:80,sortable:true,align:'center'},
            { field:'srcTabName',title:'接入表名',width:80,align:'center'},
            { field:'srcDbName',title:'数据库名',width:80,align:'center'},
            { field:'srcDbType',title:'数据库类型',width:80,align:'center'},
            { field:'srcServIp',title:'服务器IP',width:100,align:'center'},
            { field:'srcServPort',title:'数据库端口',width:80,align:'center'},
            { field:'targetPath',title:'导出路径',width:100,align:'center'},
            { field:'cronDesc', title:'接入调度',width:100,align:'center'},
            { field:'fieldDel', title:'字段分隔符',width:80,align:'center'},
            { field:'exportCols', title:'接入字段',width:80,align:'center'},
            { field:'whereExp', title:'接入条件',width:80,align:'center'},
            { field:'priorty', title:'优先级',width:80,align:'center'},
            { field:'jobCycle', title:'作业周期',width:80,align:'center'},
            { field:'cycleUnit', title:'周期单位',width:80,align:'center',formatter:function(val, row,index) {
                if (val == 0) return "小时";
                else if (val == 1) return "天";
                else if (val == 2) return "月";
                else if (val == 3) return "年";
                else if (val == 4) return "分"
            }},
            { field:'dsValid', title:'是否有效',width:80,align:'center',formatter:function(val, row,index) {
                if (val == 1) return "<span style=\"color:green\" >有效</span>";
                else  return "<span style=\"color:red\" >无效</span>";
            }},
            { field:'lastStatus', title:'最后运行状态',width:80,align:'center',formatter:function(val,row,index){
                if (val == 1)
                    return "<span style=\"color:green\" >已完成</span>";
                if (val == 0)
                    return "<span style=\"color:red\" >失败</span>";
                if (val == 2)
                    return "<span style=\"color:cyan\" >运行中</span>";
                if (val == -1)
                    return "<span style=\"color:darkred\" >未运行</span>";

            }},
            { field:'lastDataDate', title:'最后运行日期',width:80,align:'center'}
        ]]
    });
    $("#searchKey").bind("keypress",function (event) {
        if (event.keyCode == 13) {
            searchKey();
        }
    })
});