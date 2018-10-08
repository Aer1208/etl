var TOOL_BAR = [ {
    id : 'add',
    text : '新增',
    iconCls : 'icon-add',
    handler : function() {

       $("#rolePerm").combotree({
           url:'get_all_menus?roleId=0',
           cascadeCheck : false,
           multiple: true
        });

        $("#roleId").val("");
        $("#roleName").val("");
        $("#roleCode").val("");
        openWind('add_window','新增角色');
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
                    $.post("role_delete", {ids: ids}, function (data, staus, xhr) {
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
            $("#roleId").val(selectedArray[0].roleId);
            $("#roleName").val(selectedArray[0].roleName);
            $("#roleCode").val(selectedArray[0].roleCode);

            $("#rolePerm").combotree({
                url:'get_all_menus?roleId=' +  selectedArray[0].roleId,
                cascadeCheck : false,
                multiple: true
            });

            openWind('add_window','修改角色');

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

    $("#partName").combobox({
        url:'role_list',
        valueField: 'roleId',
        textField: 'roleName'
    });

    $('#save').click(function() {
        var url = "role_add";
        var param = {}
        var roleId = $("#roleId").val();
        var roleName = $("#roleName").val();
        var roleCode = $("#roleCode").val();
        var roleTree = $("#rolePerm").combotree("tree");
        var rolePerm = roleTree.tree('getChecked');

        var perms = [];

        $.each(rolePerm,function (i,n) {
            var parent = roleTree.tree('getParent', n.target);
            perms.push(n.id);
            if (parent != null && perms.indexOf(parent.id) == -1) {
                perms.push(parent.id);
            }
        });

        var permIds = perms.join(",");

        if (roleName != "") {
            param.roleName = roleName;
        }else {
            $.messager.alert("系统提示","角色名不能为空","ERROR");
            return ;
        }

        if (roleCode != "") {
            param.roleCode = roleCode;
        }else {
            $.messager.alert("系统提示","角色编码不能为空","ERROR");
            return ;
        }

        if (roleId > 0) {
            param.roleId = roleId;
        }

        param.permIds=permIds;
        param.status=1;

        $.post(url,param,function (data, status, xhr) {
            if (status == "success") {
                $.messager.alert("系统提示",data.type + "角色成功","INFO");
                $("#tt").datagrid("reload");
                closeWind("add_window");
            }
        })

    });

    $('#btnCancel').click(function () {
        closeWind('add_window')
    });

    $("#tt").datagrid( {
        url : 'role_list2',
        closable : true,
        checkOnSelect : true,
        striped : true,
        rownumbers : true,
        'toolbar' : TOOL_BAR,
        fitColumns : true,
        fit:false,
        loadFilter:pagerFilter,
        columns:[[
            { checkbox:true},
            { field:'roleId',title:'角色ID',width:80,sortable:true,align:'center'},
            { field:'roleName',title:'角色名',width:80,sortable:true,align:'center'},
            { field:'roleCode',title:'角色编码',width:120,sortable:true,align:'center'},
            { field:'status',title:'角色状态',width:120,align:'center',formatter: function(data,index){
                if (data == 1) {
                    return "<span style='color:green'>有效</span>"
                } else if (data == 0) {
                    return "<span style='color:red'>无效</span>"
                }
                return data;
            }}
        ]]
    });

    function pagerFilter(data){
        alert("run here");
        var result= {};
        result.rows  = data;
        return result;
    }
});
