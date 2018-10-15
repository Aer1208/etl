var TOOL_BAR = [ {
    id : 'add',
    text : '新增',
    iconCls : 'icon-add',
    handler : function() {
        $("#type").val("add");
        $("#dsId").attr("readOnly",false);
        $("#dsId").val('');
        $("#dsName").val('');
        $("#srcTabName").val('');
        $("#srcDbName").val('');
        $("#srcDbType").val('MYSQL');
        $("#srcServIp").val('');
        $("#srcServPort").val('3306');
        $("#targetPath").val('');
        $("#cronDesc").val('00 00 7 * * *');
        $("#fieldDel").val('\\001');
        $("#exportCols").val('');
        $("#whereExp").val('');
        $("#priorty").val('');
        $("#dsValid").combobox("setValue",1);
        $("#jobCycle").val('1')
        $("#cycleUnit").combobox("setValue",1)
        openWind('ds_from_window','新增数据源');
    }
},{
    id :"edit",
    text : '修改',
    iconCls : 'icon-edit',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        if(selectedArray.length==1) {
            $("#type").val("update");
            $("#dsId").attr("readOnly",true);
            $("#dsId").val(selectedArray[0].dsId);
            $("#dsName").val(selectedArray[0].dsName);
            $("#srcTabName").val(selectedArray[0].srcTabName);
            $("#srcDbName").val(selectedArray[0].srcDbName);
            $("#srcDbType").val(selectedArray[0].srcDbType);
            $("#srcServIp").val(selectedArray[0].srcServIp);
            $("#srcServPort").val(selectedArray[0].srcServPort);
            $("#targetPath").val(selectedArray[0].targetPath);
            $("#cronDesc").val(selectedArray[0].cronDesc);
            $("#fieldDel").val(selectedArray[0].fieldDel);
            $("#exportCols").val(selectedArray[0].exportCols);
            $("#whereExp").val(selectedArray[0].whereExp);
            $("#priorty").val(selectedArray[0].priorty);
            $("#jobCycle").val(selectedArray[0].jobCycle);
            $("#cycleUnit").combobox("setValue",selectedArray[0].cycleUnit);
            $("#dsValid").combobox("setValue",selectedArray[0].dsValid);
            openWind('ds_from_window','修改数据源');
        }else {
            $.messager.alert('提示', '请选择一条记录!', 'info');  //提示信息
        }
    }
},{
    id :"valid",
    text : '置为生效',
    iconCls : 'icon-valid',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        var ids;
        var _index = 0;
        $(selectedArray).each(function(index,obj){
            if (obj.dsValid == 0) {
                if(_index==0){
                    ids = obj.dsId;
                }else{
                    ids = ids + ',' + obj.dsId;
                }
                _index= _index + 1;
            }
        });
        if(selectedArray.length>=1 && _index > 0) {
            $.messager.confirm('提示','确定将选中的' + selectedArray.length + '条，其中' + _index + '条数据源修改为生效',function(flag) {
               if(flag) {

                   if (_index > 0) {
                       $.post('ds_change_valid',{ids:ids,valid:1},function (data, status,xhr) {
                           if (status == "success") {
                               $.messager.alert("系统提示","置为生效成功","INFO");
                               $("#tt").datagrid("reload");
                           } else {
                               $.messager.alert("系统提示","置为生效失败，失败原因" + data.toString(),"ERROR");
                           }
                       })
                   }
               }
            });
        }else {
            $.messager.alert('提示', '请至少选择一条无效的数据源记录!', 'info');  //提示信息
        }
    }
},{
    id :"invalid",
    text : '置为失效',
    iconCls : 'icon-invalid',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        var _index = 0;
        $(selectedArray).each(function(index,obj){
            if (obj.dsValid != 0) {
                if(_index==0){
                    ids = obj.dsId;
                }else{
                    ids = ids + ',' + obj.dsId;
                }
                _index= _index + 1;
            }
        });
        if(selectedArray.length>=1 && _index > 0) {
            $.messager.confirm('提示','确定将选中的' + selectedArray.length + '条，其中' + _index + '条数据源修改为失效',function(flag) {
                if(flag) {
                    $.post('ds_change_valid',{ids:ids,valid:0},function (data, status,xhr) {
                        if (status == "success") {
                            $.messager.alert("系统提示","置为生效成功","INFO");
                            $("#tt").datagrid("reload");
                        } else {
                            $.messager.alert("系统提示","置为生效失败，失败原因" + data.toString(),"ERROR");
                        }
                    })
                }
            });
        }else {
            $.messager.alert('提示', '请至少选择一条有效的记录!', 'info');  //提示信息
        }
    }
},{
    id:'refresh',
    text:'刷新数据源',
    iconCls:'icon-reload',
    handler:function () {
        $.messager.confirm('提示','确定刷新调度',function(flag) {
            if(flag) {
                $.post('ds_refresh', {}, function (data, status, xhr) {
                    if (status == "success") {
                        $.messager.alert('提示', '刷新调度成功!，目前已有的调度数=' + data.dsSize, 'info');  //提示信息
                    } else {
                        $.messager.alert('提示', '刷新调度失败!', 'error');  //提示信息
                    }
                })
            }
        })
    }
},{
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
            $.messager.confirm('提示','确定启动调度,[dsId=' + selectedArray[0].dsId + ',dsName=' + selectedArray[0].dsName + ']',function(flag) {
                if(flag) {
                    $.post('ds_start',{dsId:selectedArray[0].dsId},function (data, status, xhr) {
                        if (status =="success") {
                            $.messager.alert('提示', '启动调度成功!', 'info');  //提示信息
                            $("#tt").datagrid("reload");
                        } else {
                            $.messager.alert('提示', '启动失败!', 'error');  //提示信息
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
            $.messager.confirm('提示','确定停止调度,[dsId=' + selectedArray[0].dsId + ',dsName=' + selectedArray[0].dsName + ']',function(flag) {
               if(flag) {
                   $.post('ds_stop',{dsId:selectedArray[0].dsId},function (data, status, xhr) {
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
}];

function openWind(id,title) {
    $('#' + id).window({
        width: "500",
        height: "600",
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

function checkNotNull(fieldValue, fieldName, param) {
    if (fieldValue != "") {
        return false;
    }else {
        $.messager.alert("系统提示", fieldName + "不能为空","ERROR");
        return true;
    }
}

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

    $('#dsValid').combobox(validOption);

    $("#cycleUnit").combobox(cycleOption)

    $('#save').click(function() {
        var url = "ds_add";
        var param = {}
        var dsId = $("#dsId").val().trim();
        var dsName = $("#dsName").val().trim();
        var srcTabName = $("#srcTabName").val().trim();
        var srcDbName = $("#srcDbName").val().trim();
        var srcDbType = $("#srcDbType").val().trim();
        var srcServIp = $("#srcServIp").val().trim();
        var srcServPort = $("#srcServPort").val().trim();
        var targetPath = $("#targetPath").val().trim();
        var cronDesc = $("#cronDesc").val().trim();
        var fieldDel = $("#fieldDel").val().trim();
        var exportCols = $("#exportCols").val().trim();
        var whereExp = $("#whereExp").val().trim();
        var priorty = $("#priorty").val().trim();
        var jobCycle=$("#jobCycle").val().trim();
        var cycleUnit=$("#cycleUnit").combobox("getValue");
        var dsValid = $("#dsValid").combobox("getValue");

        param.dsId=dsId;
        if (checkNotNull(dsId, '数据源ID',param.dsId)) {
            return ;
        }

        param.srcTabName=srcTabName;
        if (checkNotNull(srcTabName, '接入表名',param.srcTabName)) {
            return ;
        }
        param.srcDbName=srcDbName;
        if (checkNotNull(srcDbName, '源数据库',param.srcDbName)) {
            return ;
        }
        param.srcDbType = "MYSQL";
        if (srcDbType != "") {
            param.srcDbType = srcDbType;
        }
        param.srcServIp=srcServIp;
        if (checkNotNull(srcServIp, '源数据库IP',param.srcServIp)) {
            return ;
        }
        param.srcServPort = "3306";
        if (srcServPort != "") {
            param.srcServPort = srcServPort;
        }
        param.targetPath=targetPath;
        if (checkNotNull(targetPath, '导出目标路径（hdfs）',param.targetPath)) {
            return ;
        }
        param.jobCycle=jobCycle
        if (checkNotNull(jobCycle,'作业周期不能为空', param.jobCycle)) {
            return ;
        }
        param.cycleUnit=cycleUnit;
        if(checkNotNull(cycleUnit,'作业周期单位不能为空',param.cycleUnit)) {
            return ;
        }
        param.cronDesc="00 00 7 * * *"
        if(cronDesc != "") {
            param.cronDesc = cronDesc;
        }

        param.fieldDel = "\\001";
        if (fieldDel != "") {
            param.fieldDel = fieldDel;
        }

        if (exportCols != "") {
            param.exportCols=exportCols;
        }

        if (whereExp != "" ) {
            param.whereExp=whereExp;
        }

        param.priorty=100;
        if (priorty != "") {
            param.priorty = priorty;
        }
        param.dsValid =1;
        if (dsValid != "") {
            param.dsValid = dsValid;
        }

        if (dsName != "") {
            param.dsName = dsName;
        }else {
            param.dsName = srcTabName;
        }

        // 提交类型
        param.type = $("#type").val();

        $.post(url,param,function (data, status, xhr) {
            if (status == "success") {
                $.messager.alert("系统提示",$("#type").val() + "数据源成功","INFO");
                $("#tt").datagrid("reload");
                closeWind("ds_from_window");
            }
        })

    });

    $('#btnCancel').click(function () {
        closeWind('ds_from_window')
    });


    $("#tt").datagrid( {
        url : 'ds_list',
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
                else if (val == 4) return "分";
            }},
            { field:'dsValid', title:'是否有效',width:80,align:'center',formatter:function(val, row,index) {
                if (val == 1) return "<span style=\"color:green\" >有效</span>";
                else  return "<span style=\"color:red\" >无效</span>";
            }},
            {field:"scheduleStatus", title:'是否启动调度', width:80, align:'center', formatter:function (val, row, index) {
                if (val == 1) return "<span style=\"color:green\" >已启动</span>"
                else return "<span style=\"color:red\" >未启动</span>";
            }}
        ]]
    });
});
