var TOOL_BAR = [ {
    id : 'add',
    text : '新增',
    iconCls : 'icon-add',
    handler : function() {
        $("#menuName").val("");
        $("#menuIcon").val("");
        $("#permission").val("");
        $("#menuUrl").val("");
        $("#status").combobox("setValue","1");
        $("#menuType").combobox("setValue","");
        $("#parentId").combobox("setValue","");
        openWind('add_window','新增菜单');
    }
},{
    id :"remove",
    text : '删除',
    iconCls : 'icon-remove',
    handler : function() {
        var selectedArray = $('#tt').treegrid('getSelections');  //返回被选择的行
        var ids;
        $(selectedArray).each(function(index,obj){
            if(index==0){
                ids = obj.userId;
            }else{
                ids = ids + ',' + obj.userId;
            }
        })
        if(selectedArray.length>=1) {
            var str = '选中了' + selectedArray.length + '条，' + '这些菜单将被删除,是否继续?';
            $.messager.confirm('提示',str,function(flag) {
                if (flag) {
                    $.post("menu_delete", {ids: ids}, function (data, staus, xhr) {
                        if (staus == "success") {
                            $.messager.alert("系统提示", "删除菜单成功", "INFO");
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
        var selectedArray = $('#tt').treegrid('getSelections');  //返回被选择的行

        if(selectedArray.length==1) {
            $("#menuId").val(selectedArray[0].menuId);
            $("#menuName").val(selectedArray[0].menuName);
            $("#menuIcon").val(selectedArray[0].menuIcon);
            $("#permission").val(selectedArray[0].permission);
            $("#menuUrl").val(selectedArray[0].url);
            $("#status").combobox("setValue",selectedArray[0].status);
            $("#menuType").combobox("setValue",selectedArray[0].permType);
            $("#parentId").combobox("setValue",selectedArray[0].parentId);

            openWind('add_window','修改菜单');
        }else {
            $.messager.alert('提示', '请选择一条记录!', 'info');  //提示信息
        }
    }
}];

function openWind(id,title) {
    $('#' + id).window({
        width: "400",
        height: "350",
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

    $("#status").combobox({
        data:[{
            text:'有效',
            value:1
        },{
            text:'无效',
            value:0
        }]
    });
    $("#menuType").combobox({
        data:[{
            text:'菜单权限',
            value:'menu'
        },{
            text:'按钮权限',
            value:'button'
        }]
    });
    $("#parentId").combobox({
        url:'get_root_menus',
        textField:'menuName',
        valueField:'menuId'
    });

    $('#save').click(function() {
        var url = "menu_add";
        var param = {}
        var menuId=$("#menuId").val();
        var menuName = $("#menuName").val();
        var menuIcon = $("#menuIcon").val();
        var permission = $("#permission").val();
        var menuUrl = $("#menuUrl").val();
        var status = $("#status").combobox("getValue");
        var menuType=$("#menuType").combobox("getValue");
        var parentId=$("#parentId").combobox("getValue");

        if (menuName != "") {
            param.menuName = menuName;
        }else {
            $.messager.alert("系统提示","菜单名称不能为空","ERROR");
            return ;
        }

        if (menuId > 0) {
            param.menuId = menuId;
        }
        param.menuIcon=menuIcon;
        param.permission=permission;
        param.url=menuUrl;
        param.status=status;
        param.permType=menuType;
        if (parentId == "") {
            param.parentId=0;
        }else {
            param.parentId=parentId;
        }

        $.post(url,param,function (data, status, xhr) {
            if (status == "success") {
                $.messager.alert("系统提示",data.type + "菜单成功","INFO");
                $("#tt").datagrid("reload");
                closeWind("add_window");
            }
        })

    });

    $('#btnCancel').click(function () {
        closeWind('add_window')
    });

    $("#tt").treegrid({
        url : 'menu_list',
        fitColumns : true,
        fit:false,
        idField:"menuId",
        treeField:"menuName",
        toolbar:TOOL_BAR,
        singleSelect:true,
        columns:[[
            { field:'menuName',title:'菜单名称',width:80,sortable:true,align:'left'},
            { field:'menuId',title:'菜单ID',width:80,sortable:true,align:'center'},
            { field:'menuIcon',title:'菜单图标',width:120,sortable:true,align:'center'},
            { field:'url',title:'菜单URL',width:120,align:'center'},
            { field:'permission',title:'菜单权限',width:120,align:'center'},
            {field:'status',title:'菜单状态',width:100, align:'center',formatter:function(value,rows, index){
               if (value == 1) {
                   return "<span style='color:green'>有效</span>"
               } else if (value == 0) {
                   return "<span style='color:red'>无效</span>"
               }
            }},
            {field:'permType',title:'菜单类型',width:100,align:'center',formatter:function (value, rows, index) {
                if (value == "menu") {
                    return "菜单权限";
                } else if (value == "button") {
                    return "按钮权限"
                }
            }}
        ]]
    });
});
