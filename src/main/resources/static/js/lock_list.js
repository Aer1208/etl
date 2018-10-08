var TOOL_BAR = [{
    id :"remove",
    text : '删除',
    iconCls : 'icon-remove',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        var lockStr;
        $(selectedArray).each(function(index,obj){
            if(index==0){
                lockStr = obj.lockObj;
            }else{
                lockStr = lockStr + ',' + obj.lockObj;
            }
        })
        if(selectedArray.length>=1) {
            var str = '选中了' + selectedArray.length + '条，' + '这些锁对象将被删除,是否继续?';
            $.messager.confirm('提示',str,function(flag) {
                if (flag) {
                    $.post("delete_lock", {lockStr: lockStr}, function (data, staus, xhr) {
                        if (staus == "success") {
                            $.messager.alert("系统提示", "删除锁对象成功", "INFO");
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
}];


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

    $("#tt").datagrid( {
        url : 'lock_list',
        closable : true,
        checkOnSelect : true,
        striped : true,
        rownumbers : true,
        'toolbar' : TOOL_BAR,
        fitColumns : true,
        fit:false,
        columns:[[
            { checkbox:true},
            { field:'lockObj',title:'锁对象',width:80,sortable:true,align:'center'},
            { field:'writeLockCnt',title:'写锁层数',width:80,sortable:true,align:'center'},
            { field:'readLockCnt',title:'读锁层数',width:120,sortable:true,align:'center'}
        ]]
    });
});
