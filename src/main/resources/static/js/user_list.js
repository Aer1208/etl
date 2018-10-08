var TOOL_BAR = [ {
    id : 'add',
    text : '新增',
    iconCls : 'icon-add',
    handler : function() {
        $("#userId").val("");
        $("#loginName").val("");
        $("#userName").val("");
        $("#partName").combobox("setValue","");
        openWind('add_window','新增用户');
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
                ids = obj.userId;
            }else{
                ids = ids + ',' + obj.userId;
            }
        })
        if(selectedArray.length>=1) {
            var str = '选中了' + selectedArray.length + '条，' + '这些用户将被删除,是否继续?';
            $.messager.confirm('提示',str,function(flag) {
                if (flag) {
                    $.post("user_delete", {ids: ids}, function (data, staus, xhr) {
                        if (staus == "success") {
                            $.messager.alert("系统提示", "删除用户成功", "INFO");
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
            $("#userId").val(selectedArray[0].userId);
            $("#loginName").val(selectedArray[0].loginName);
            $("#userName").val(selectedArray[0].userName);
            $("#partName").combobox("setValue", selectedArray[0].roleId);

            openWind('add_window','修改用户');
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

function resetPwd(userId) {
    $.post("reset_pwd",{userId:userId},function (data, status, xhr) {
        if (data.ret == 1) {
            $.messager.alert("系统提示","密码重置成功","INFO");
        }
    })
}

$(function() {

    $("#partName").combobox({
        url:'role_list',
        valueField: 'roleId',
        textField: 'roleName'
    })

    $('#save').click(function() {
        var url = "user_add";
        var param = {}
        var userId = $("#userId").val();
        var loginName = $("#loginName").val();
        var userName = $("#userName").val();
        var roleId = $("#partName").combobox("getValue");
        var partName=$("#partName").combobox("getText");

        if (loginName != "") {
            param.loginName = loginName;
        }else {
            $.messager.alert("系统提示","登录账号不能为空","ERROR");
            return ;
        }

        if (userName != "") {
            param.userName = userName;
        }else {
            $.messager.alert("系统提示","用户名不能为空","ERROR");
            return ;
        }

        if (partName != "") {
            param.partName = partName;
            param.roleId=roleId;
        }else {
            $.messager.alert("系统提示","部门不能为空","ERROR");
            return ;
        }

        if (userId > 0) {
            param.userId = userId;
        }

        $.post(url,param,function (data, status, xhr) {
            if (status == "success") {
                $.messager.alert("系统提示",data.type + "用户成功","INFO");
                $("#tt").datagrid("reload");
                closeWind("add_window");
            }
        })

    });

    $('#btnCancel').click(function () {
        closeWind('add_window')
    });

    $("#tt").datagrid( {
        url : 'user_list',
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
            { field:'userId',title:'用户ID',width:80,sortable:true,align:'center'},
            { field:'userName',title:'用户名',width:80,sortable:true,align:'center'},
            { field:'loginName',title:'登录账号',width:120,sortable:true,align:'center'},
            { field:'partName',title:'用户部门',width:120,align:'center'},
            { field:'roleId',title:'角色ID',width:120,align:'center'},
            {field:'_operation',title:'操作',width:100, align:'center',formatter:function(value,rows, index){
                return '<a href="#" class="easyui-button" data-options="iconCls:icon-reset" onclick="resetPwd('+rows.userId+')">重置密码</a>'
            }}
        ]]
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
});
