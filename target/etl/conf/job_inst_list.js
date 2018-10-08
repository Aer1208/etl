var TOOL_BAR = [ {
    id : 'view',
    text : '查看作业全部实例',
    iconCls : 'icon-search',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        if(selectedArray.length==1){
            $("#dom_var_window").window( {
                width : "900",
                height : "680",
                title : "查看-" + selectedArray[0].jobId  ,
                modal : true,
                minimizable : false,
                closed : true
            });
            $("#jobtt").datagrid({
                url:"inst_job?jobId="+selectedArray[0].jobId,
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
            //$("#dom_var_window").window('refresh', "inst_job?jobId="+selectedArray[0].dsId);
        }else{
            $.messager.alert('提示', '请选择一条记录!', 'info');  //提示信息
        }
    }
},{
    id :"child",
    text : '查看后续作业',
    iconCls : 'icon-search',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        if(selectedArray.length==1) {
            $("#child_job_window").window({
                width: "900",
                height: "680",
                title: "查看" + selectedArray[0].jobId + '后续作业',
                modal: true,
                minimizable: false,
                closed: true
            });
            $("#childjobtt").treegrid({
                url:"next_job?jobId="+selectedArray[0].jobId,
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
},{
    id :"parent",
    text : '查看前置作业',
    iconCls : 'icon-search',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        if(selectedArray.length==1) {
            $("#parent_job_window").window({
                width: "900",
                height: "680",
                title: "查看" + selectedArray[0].jobId + '前置作业',
                modal: true,
                minimizable: false,
                closed: true
            });
            $("#parentjobtt").treegrid({
                url:"pre_job?jobId="+selectedArray[0].jobId,
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
            $("#parent_job_window").window('open');
        }else {
            $.messager.alert('提示', '请选择一条记录!', 'info');  //提示信息
        }
    }
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
        url : 'job_inst_list',
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
            { field:'jobId',title:'作业编号',width:80,sortable:true,align:'center'},
            { field:'jobName',title:'作业名称',width:120,sortable:true,align:'center'},
            { field:'cmdName',title:'作业脚本',width:120,align:'center'},
            { field:'cmdPath',title:'脚本路径',width:120,align:'center'},
            { field:'jobGroup',title:'作业组',width:80,align:'center'},
            { field:'priorty', title:'优先级',width:80,align:'center'},
            { field:'jobValid', title:'是否有效',width:80,align:'center',formatter:function(val, row,index) {
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
});

function pagerFilter(data){
    var dg = $(this);
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        onSelectPage:function(pageNum, pageSize){
            opts.pageNumber = pageNum;
            opts.pageSize = pageSize;
            pager.pagination('refresh',{pageNo:pageNum,pageSize:pageSize});
            dg.datagrid('loadData',data);
        }
    });

    return data;
}
