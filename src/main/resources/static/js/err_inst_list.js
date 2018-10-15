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
}];
$(function() {

    $("#tt").datagrid( {
        url : 'err_inst_list',
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
            { field:'instId',title:'实例ID',width:80,sortable:true,align:'center'},
            { field:'status',title:'状态',width:80,sortable:true,align:'center',formatter:function(val,row,index){
                if (val == 1)
                    return "<span style=\"color:green\" >重调中</span>";
                if (val == 0)
                    return "<span style=\"color:red\" >失败</span>";
            }},
            { field:'jobType',title:'数据日期',width:120,sortable:true,align:'center',formatter:function (val, row, index) {
                if (val ==1) return "数据源"
                if (val ==2) return "作业"
            }},
            { field:'jobId',title:'优先级',width:120,align:'center'},
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
            { field:'jobCycle', title:'作业周期',width:80,align:'center'},
            { field:'cycleUnit', title:'周期单位',width:80,align:'center',formatter:function (val,row,index) {
                if(val == 0) return "小时"
                else if (val == 1) return "天"
                else if (val == 2) return "月"
                else if (val == 3) return "年"
                else if (val == 4) return "分"
            }}
        ]]
    });
});
