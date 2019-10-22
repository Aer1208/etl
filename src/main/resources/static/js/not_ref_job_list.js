var TOOL_BAR = [{
    id:'start',
    text:'启动调度',
    iconCls:'icon-start',
    handler:function () {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        if(selectedArray.length==1) {
            if (selectedArray[0].scheduleStatus == 1) {
                $.messager.alert('提示', '调度已启动!', 'info');  //提示信息
                return ;
            }
            $.messager.confirm('提示','确定启动调度,[jobId=' + selectedArray[0].jobId + ',jobName=' + selectedArray[0].jobName + ']',function(flag) {
                if(flag) {
                    $.post('startNotRefSchd',{jobId:selectedArray[0].jobId},function (data, status, xhr) {
                        if (status =="success" && data.ret == "success") {
                            $.messager.alert('提示', '启动调度成功!', 'info');  //提示信息
                            $("#tt").datagrid("reload");
                        } else {
                            $.messager.alert('提示', '启动失败!,失败原因：' + data.msg, 'error');  //提示信息
                        }
                    })
                }
            });

        }else {
            $.messager.alert('提示', '请选择一条记录!', 'info');  //提示信息
        }
    }
},{
    id:'stop',
    text:'停止调度',
    iconCls:'icon-stop',
    handler:function () {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        if(selectedArray.length==1) {
            if (selectedArray[0].scheduleStatus != 1) {
                $.messager.alert('提示', '调度已停止!', 'info');  //提示信息
                return ;
            }
            $.messager.confirm('提示','确定停止调度,[jobId=' + selectedArray[0].jobId + ',jobName=' + selectedArray[0].jobName + ']',function(flag) {
                if(flag) {
                    $.post('stopNotRefSchd',{jobId:selectedArray[0].jobId},function (data, status, xhr) {
                        if (status =="success") {
                            $.messager.alert('提示', '停止调度成功!', 'info');  //提示信息
                            $("#tt").datagrid("reload");
                        } else {
                            $.messager.alert('提示', '停止失败!', 'error');  //提示信息
                        }
                    })
                }
            });

        }else {
            $.messager.alert('提示', '请选择一条记录!', 'info');  //提示信息
        }
    }
},{
    text:'-',
    float:"left"
},{
    id:'searchBtn',
    float:"right",
    iconCls:'icon-search',
    handler:function () {
        searchJob();
    }
},{
    id:"searchTxt",
    float:"right",
    text:'<input id="searchJobTxt" type="text" style="padding: 1px;height: 100%" />'
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
        url : 'not_ref_job_list',
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
            { field:'cronDesc', title:'作业调度',width:80,align:'center'},
            { field:'jobCycle', title:'作业周期',width:80,align:'center'},
            { field:'cycleUnit', title:'周期单位',width:80,align:'center',formatter:function (val,row,index) {
                if(val == 0) return "小时";
                else if (val == 1) return "天";
                else if (val == 2) return "月";
                else if (val == 3) return "年";
                else if (val == 4) return "分";
            }},
            { field:'jobValid', title:'是否有效',width:80,align:'center',formatter:function(val, row,index) {
                if (val == 1) return "<span style=\"color:green\" >有效</span>";
                else  return "<span style=\"color:red\" >无效</span>";
            }},
            {field:"scheduleStatus", title:'是否启动调度', width:80, align:'center', formatter:function (val, row, index) {
                if (val == 1) return "<span style=\"color:green\" >已启动</span>"
                else return "<span style=\"color:red\" >未启动</span>";
            }}
        ]]
    });

    $("#searchJobTxt").bind("keypress",function (event) {
        if (event.keyCode == "13") {
            searchJob();
        }
    });

    $("#searchJobBtn").click(function () {
        searchJob();
    })
});

function searchJob() {
    var searchJobTxt = $("#searchJobTxt").val();
    var queryParams = $('#tt').datagrid('options').queryParams={
        page:1,
        rows:40
    };
    if ("" != searchJobTxt) {
        queryParams.key = searchJobTxt;
    }

    $("#tt").datagrid("reload");
}