var TOOL_BAR = [ {
    id : 'view',
    text : '查看日志',
    iconCls : 'icon-search',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        if(selectedArray.length==1){
            $("#dom_var_window").window( {
                width : "800",
                height : "680",
                title : "查看【instId=" + selectedArray[0].instId + "，jobId=" + selectedArray[0].jobId + ",data_date=" + selectedArray[0].dataDate + "】",
                modal : true,
                minimizable : false,
                closed : true
            });
            $("#dom_var_window").window('open');
            $("#dom_var_window").window('refresh', "inst_log?instId="+selectedArray[0].instId);
        }else{
            $.messager.alert('提示', '请选择一条记录!', 'info');  //提示信息
        }
    }
}, {
    id : 'redo',
    text : '重调',
    iconCls : 'icon-redo',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        var ids;
        var _index = 0;
        $(selectedArray).each(function(index,obj){
            if (obj.status == 0) {
                if(_index==0){
                    ids = obj.instId;
                }else{
                    ids = ids + ',' + obj.instId;
                }
                _index = _index + 1;
            }
        })
        if(_index >0){
            var str = '选中了' + selectedArray.length + '条，' + _index + '错误实例条数据将被重调,是否继续?';
            $.messager.confirm('提示',str,function(flag){
                if(flag){
                    $.post('inst_redo?ids='+ids,{},function(f){
                        var obj = JSON.parse(f);
                        if(obj.ret==_index){
                            $.messager.alert('成功', '重调成功!', 'info');
                            $('#tt').datagrid('reload');
                        } else if (obj.ret > 0 ) {
                            $.messager.alert('成功', '重调' + _index + '条记录，成功重调' + obj.ret + '条记录', 'info');
                            $('#tt').datagrid('reload');
                        } else {
                            $.messager.alert('失败', '重调' + _index + '条记录，成功重调' + obj.ret + '条记录', 'error');
                        }
                    })
                }
            })
        }else{
            $.messager.alert('提示', '请至少选择一条失败的实例记录!', 'info');  //提示信息
        }
    }
},{
    id :"child",
    text : '查看后续作业',
    iconCls : 'icon-up',
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
    iconCls : 'icon-down',
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
        url : 'inst_list',
        closable : true,
        checkOnSelect : true,
        striped : true,
        rownumbers : true,
        pagination:true,
        pageSize:30,
        'toolbar' : TOOL_BAR,
        fitColumns : true,
        fit:false,
        loadFilter:pagerFilter,
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

            }},
            { field:'jobType',title:'作业类型',width:100,align:'center',formatter:function (val, row, index) {
                if (val ==1) return "数据源"
                if (val ==2) return "作业"
            }},
            { field:'jobName', title:'作业名称',width:180,align:'center'},
            { field:'jobCode', title:'作业编码',width:200,align:'center'}
        ]]
    });

    // 日期控件
    $("#dataDate").datebox({
        formatter:function(date){
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            var d = date.getDate();
            m = m < 10 ? '0'+ m : m;
            d = d < 10 ? '0' +d : d;
            return y+''+m + '' +d;
        }
    })

    // 下拉框控件
    $("#status").combobox({
        valueField: 'value',
        textField: 'label',
        value:-1,
        data:[{
            label:'全部',
            value:-1
        },{
            label:"运行中",
            value:2
        },{
            label:"已完成",
            value:1
        },{
            label:"失败",
            value:0
        }]
    })

    //清除条件
    $("#a_var_reset").bind('click',function(){
        $("#jobId").val('');
        $("#dataDate").datebox('setValue','');
        $("#status").combobox("setValue",'-1');
        $('#tt').datagrid('options').queryParams ={
            page:1,
            rows:30
        }
    })

    $("#a_var_search").bind('click', search)

    $("#jobId").bind("keyup", keyup)

})

function keyup(event) {
    if (event.keyCode == "13") {
        search();
    }
}

function search() {

    $('#tt').datagrid('options').queryParams ={
        page:1,
        rows:30
    }

    var jobId = $("#jobId").val();
    var dataDate = $("#dataDate").datebox("getValue");
    var status = $("#status").combobox("getValue");
    var queryParams = $('#tt').datagrid('options').queryParams;
    if ("" != jobId) {
        queryParams.jobId = jobId;
    }
    if ("" != dataDate) {
        queryParams.dataDate = dataDate;
    }
    if ("" != status) {
        queryParams.status = status;
    }
    $('#tt').datagrid('reload');
};