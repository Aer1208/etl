var TOOL_BAR = [ {
    id : 'add',
    text : '新增',
    iconCls : 'icon-add',
    handler : function() {
        $("#eventId").val("");
        $("#jobId").val("");
        $("#dataDate").datebox("setValue","");
        $("#refJobId").val("");
        openWind('add_window','新增队列');
    }
},{
    id :"remove",
    text : '删除',
    iconCls : 'icon-remove',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        var ids;
        $(selectedArray).each(function(index,obj){
            if(index==0){
                ids = obj.eventId;
            }else{
                ids = ids + ',' + obj.eventId;
            }
        })
        if(selectedArray.length>=1) {
            var str = '选中了' + selectedArray.length + '条，' + '这些队列将被删除,是否继续?';
            $.messager.confirm('提示',str,function(flag) {
                if (flag) {
                    $.post("event_delete", {ids: ids}, function (data, staus, xhr) {
                        if (staus == "success") {
                            $.messager.alert("系统提示", "删除队列成功", "INFO");
                            $("#tt").datagrid("reload");
                            closeWind("add_window");
                        } else {
                            $.messager.alert("系统提示", "删除失败，返回结果：" + data, "ERROR");
                        }
                    })
                }
            });
        }else {
            $.messager.alert('提示', '请至少选择一条记录!', 'info');  //提示信息
        }
    }
},{
    id :"edit",
    text : '修改',
    iconCls : 'icon-edit',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行

        if(selectedArray.length==1) {
            $("#eventId").val(selectedArray[0].eventId);
            $("#jobId").val(selectedArray[0].jobId);
            $("#instId").val(selectedArray[0].instId);
            $("#dataDate").datebox("setValue",selectedArray[0].dataDate);
            $("#refJobId").val(selectedArray[0].refJobId);
            openWind('add_window','修改队列');
        }else {
            $.messager.alert('提示', '请选择一条记录!', 'info');  //提示信息
        }
    }
}];

function openWind(id,title) {
    $('#' + id).window({
        width: "400",
        height: "300",
        title: title,
        modal: true,
        minimizable: false,
        closed: true
    });
    $('#' +id).window("open");
}

function closeWind(id) {
    $('#' +id).window("close");
}

$(function() {

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

    $('#save').click(function() {
        var url = "event_add";
        var param = {}
        var eventId = $("#eventId").val();
        var jobId = $("#jobId").val();
        var dataDate = $("#dataDate").datebox("getValue");
        var refJobId = $("#refJobId").val();
        var instId = $("#instId").val();

        if (jobId != "") {
            param.jobId = jobId;
        }else {
            $.messager.alert("系统提示","jobId不能为空","ERROR");
            return ;
        }

        if (dataDate != "") {
            param.dataDate = dataDate;
        }else {
            $.messager.alert("系统提示","dataDate不能为空","ERROR");
            return ;
        }

        if (refJobId != "") {
            param.refJobId = refJobId;
        } else {
            $.messager.alert("系统提示","依赖作业ID不能为空","ERROR");
            return ;
        }
        if (instId > 0) {
            param.instId = instId;
        }

        if (eventId > 0) {
            param.eventId = eventId;
        }

        $.post(url,param,function (data, status, xhr) {
            if (status == "success") {
                $.messager.alert("系统提示",data.type + "事件成功","INFO");
                $("#tt").datagrid("reload");
                closeWind("add_window");
            }
        })

    });

    $('#btnCancel').click(function () {
        closeWind('add_window')
    });

    $("#tt").datagrid( {
        url : 'event_list',
        closable : true,
        checkOnSelect : true,
        striped : true,
        rownumbers : true,
        'toolbar' : TOOL_BAR,
        fitColumns : true,
        fit:false,
        columns:[[
            { checkbox:true},
            { field:'eventId',title:'事件ID',width:80,sortable:true,align:'center'},
            { field:'jobId',title:'作业编号',width:80,sortable:true,align:'center'},
            { field:'instId',title:'触发实例ID',width:80,sortable:true,align:'center'},
            { field:'dataDate',title:'数据日期',width:120,sortable:true,align:'center'},
            { field:'refJobId',title:'依赖作业',width:120,align:'center'}
        ]]
    });
});
