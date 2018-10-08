var TOOL_BAR = [ {
    id : 'add',
    tips:'新增一个作业',
    iconCls : 'icon-add',
    handler : function() {
        var subtitle = '新增作业';
        if(!$('#tabs').tabs('exists',subtitle)){
            $('#tabs').tabs('add',{
                title:subtitle,
                content:'<iframe scrolling="auto" frameborder="0"  src="add_job" style="width:100%;height:100%;"></iframe>',
                closable:true,
                icon:'icon-add',
                height:'100%'
            });
        }else{
            $('#tabs').tabs('select',subtitle);
        }
    }
},{
    id :"edit",
    tips:'修改选中的作业',
    iconCls : 'icon-edit',
    handler : function() {
        var selectedArray = $('#tt').datagrid('getSelections');  //返回被选择的行
        if(selectedArray.length==1) {
            var data = selectedArray[0]
            var subtitle = '修改作业-' + data.jobId
            if(!$('#tabs').tabs('exists',subtitle)){
                $('#tabs').tabs('add',{
                    title:subtitle,
                    content:createEditFrame(data),
                    closable:true,
                    icon:'icon-edit',
                    height:'100%'
                });
                loadEditJobDetail(data);
            }else{
                $('#tabs').tabs('select',subtitle);
            }
        }else {
            $.messager.alert('提示', '请选择一条记录!', 'info');  //提示信息
        }
    }
},{
    id:'refresh',
    tips:'刷新后台内存中作业的数据',
    iconCls:'icon-reload',
    handler:function () {
        $.post('refresh_job',{},function (data, status, xhr) {
            if (status == "success") {
                $.messager.alert("系统提示","刷新参数成功","INFO");
            }else {
                $.messager.alert("系统提示","刷新参数失败","INFO");
            }
        })
    }
},{
    text:'<input id="searchJobTxt" type="text" />'
},{
    text:'<a id="searchJobBtn" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" >查找</a>'
}];

$(function() {
    $("#tt").datagrid( {
        url : 'job_list',
        closable : true,
        checkOnSelect : true,
        singleSelect:true,
        striped : true,
        rownumbers : true,
        pagination:true,
        pageSize:40,
        toolbar : TOOL_BAR,
        fitColumns : true,
        fit:false,
        loadFilter:pagerFilter,
        columns:[[
            { field:'jobId',title:'作业编号',width:80,sortable:true,align:'center'},
            { field:'jobName',title:'作业名称',width:120,sortable:true,align:'center'},
            { field:'jobValid', title:'是否有效',width:80,align:'center',formatter:function(val, row,index) {
                if (val == 1) return "<span style=\"color:green\" >有效</span>";
                else  return "<span style=\"color:red\" >无效</span>";
            }}
        ]],
        onDblClickRow:function (index, data) {
            var subtitle = '查看作业-' + data.jobId
            if(!$('#tabs').tabs('exists',subtitle)){
                $('#tabs').tabs('add',{
                    title:subtitle,
                    content:createFrame(data),
                    closable:true,
                    icon:'icon-print',
                    height:'100%'
                });
                loadJobDetail(data);
            }else{
                $('#tabs').tabs('select',subtitle);
            }
        }
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
    var jobId = $("#searchJobTxt").val();
    var queryParams = $('#tt').datagrid('options').queryParams={
        page:1,
        rows:40
    };
    if ("" != jobId) {
        queryParams.jobId = jobId;
    }

    $("#tt").datagrid("reload");
}

function createFrame(data) {
    var html="<div title=\"查看作业-" + data.jobId + "\" style=\"padding:20px;overflow:hidden; \" >\n" +
        "            <div class=\"easyui-panel\"title=\"作业定义表\" data-options=\"collapsed:true\">\n" +
        "                <table id=\"jobdef_tt-" + data.jobId + "\" class=\"easyui-datagrid\" />\n" +
        "            </div>\n" +
        "            <br/>\n" +
        "            <div class=\"easyui-panel\" title=\"作业参数定义表\">\n" +
        "                <table id=\"jobparam_tt-" + data.jobId + "\" class=\"easyui-datagrid\" />\n" +
        "            </div>\n" +
        "            <br/>\n" +
        "            <div class=\"easyui-panel\" title=\"作业依赖表\">\n" +
        "                <table id=\"jobref_tt-" + data.jobId+ "\" class=\"easyui-datagrid\" />\n" +
        "            </div>\n" +
        "        </div>"
    return html;
}

function closeTabs(title) {
    $('#tabs').tabs('close',title)
}

function loadJobDetail(data) {
    $("#jobdef_tt-" + data.jobId).datagrid({
        url:'job_byid?jobId='+data.jobId,
        columns:[[
            { checkbox:true},
            { field:'jobId',title:'作业编号',width:80,sortable:true,align:'center'},
            { field:'jobName',title:'作业名称',width:150,sortable:true,align:'center'},
            { field:'cmdName',title:'作业脚本',width:150,align:'center'},
            { field:'cmdPath',title:'脚本路径',width:80,align:'center'},
            { field:'jobGroup',title:'作业组',width:80,align:'center'},
            { field:'priorty', title:'优先级',width:80,align:'center'},
            { field:'jobValid', title:'是否有效',width:80,align:'center',formatter:function(val, row,index) {
                if (val == 1) return "<span style=\"color:green\" >有效</span>";
                else  return "<span style=\"color:red\" >无效</span>";
            }}
        ]]
    });

    $("#jobparam_tt-" + data.jobId).datagrid({
        url:'jobparam_byid?jobId=' + data.jobId,
        columns:[[
            { checkbox:true},
            { field:'jobId',title:'作业编号',width:80,sortable:true,align:'center'},
            { field:'paramSeq',title:'参数序号',width:80,sortable:true,align:'center'},
            { field:'paramId',title:'参数ID',width:80,sortable:true,align:'center'},
            { field:'paramName',title:'参数名称',width:80,sortable:true,align:'center'},
            { field:'paramDef',title:'参数定义',width:280,sortable:true,align:'left'},
            { field:'paramType',title:'参数类型',width:80,sortable:true,align:'center'},
            { field:'paramValid',title:'有效标志',width:80,sortable:true,align:'center'}
        ]]
    });

    $("#jobref_tt-" + data.jobId).datagrid({
        url:'jobref_byid?jobId=' + data.jobId,
        columns:[[
            { checkbox:true},
            { field:'jobId',title:'作业编号',width:80,sortable:true,align:'center'},
            { field:'refJobId',title:'依赖作业ID',width:80,sortable:true,align:'center'},
            { field:'refType',title:'作业依赖类型',width:80,sortable:true,align:'center'}
        ]]
    })
}

function createEditFrame(data) {
    var html="<div title=\"修改作业-" + data.jobId + "\" style=\"padding:20px;overflow:hidden; \" >\n" +
        "            <div class=\"easyui-panel\"title=\"作业定义表\" data-options=\"collapsed:true\">\n" +
        "                <table id=\"edit_jobdef_tt-" + data.jobId + "\" class=\"easyui-datagrid\" />\n" +
        "            </div>\n" +
        "            <br/>\n" +
        "            <div class=\"easyui-panel\" title=\"作业参数定义表\">\n" +
        "                <table id=\"edit_jobparam_tt-" + data.jobId + "\" class=\"easyui-datagrid\" />\n" +
        "            </div>\n" +
        "            <br/>\n" +
        "            <div class=\"easyui-panel\" title=\"作业依赖表\">\n" +
        "                <table id=\"edit_jobref_tt-" + data.jobId+ "\" class=\"easyui-datagrid\" />\n" +
        "            </div>\n" +
        "        </div>"
    return html;
}

function loadEditJobDetail(data) {
    $("#edit_jobdef_tt-" + data.jobId).datagrid({
        url:'job_byid?jobId='+data.jobId,
        columns:[[
            { checkbox:true},
            { field:'jobId',title:'作业编号',width:80,sortable:true,align:'center'},
            { field:'jobName',title:'作业名称',width:150,sortable:true,align:'center',editor:{type:'validatebox',options:{
                required:true,
            }}},
            { field:'cmdName',title:'作业脚本',width:150,align:'center',editor:{type:'validatebox',options:{
                required:true,
            }}},
            { field:'cmdPath',title:'脚本路径',width:80,align:'center',editor:{type:'validatebox',options:{
                required:true,
            }}},
            { field:'jobGroup',title:'作业组',width:80,align:'center',editor:{type:'validatebox',options:{
                required:true,
            }}},
            { field:'priorty', title:'优先级',width:80,align:'center',editor:{type:'validatebox',options:{
                required:true,
            }}},
            { field:'jobValid', title:'是否有效',width:80,align:'center',formatter:function(val, row,index) {
                if (val == 1) return "<span style=\"color:green\" >有效</span>";
                else  return "<span style=\"color:red\" >无效</span>";
            },editor:{type:'combobox',options:{
                valueField: 'value',
                textField: 'label',
                value:1,
                data:[{
                    label:"有效",
                    value:1
                },{
                    label:"无效",
                    value:0
                }]
            }}}
        ]],
        onDblClickRow:function (index,row) {
            $("#edit_jobdef_tt-" + data.jobId).datagrid('beginEdit',index);
        },
        toolbar:[{
            iconCls:'icon-save',
            handler:function(){
                $("#edit_jobdef_tt-" + data.jobId).datagrid("endEdit",0);
                var jobDef = $("#edit_jobdef_tt-" + data.jobId).datagrid("getChanges")[0]
                if (jobDef == undefined) {
                    $.messager.alert("系统提示","你还没有编辑","INFO");
                    return null;
                }
                $.ajax({
                    url:'update_job',
                    data:JSON.stringify(jobDef),
                    contentType:'application/json;charset=utf-8',
                    type:"post",
                    success:function(data) {
                        $("#edit_jobdef_tt-" + data.jobId).datagrid("acceptChanges");
                        $.messager.alert("系统提示","修改作业成功","INFO");
                    },
                    error:function (data) {
                        $.messager.alert("系统提示","失败：" +JSON.stringify(data),"INFO");
                    }
                })
            }
        }]
    });

    var editJobParamIndex = undefined;
    var editJobRefIndex = undefined;
    $("#edit_jobparam_tt-" + data.jobId).datagrid({
        url:'jobparam_byid?jobId=' + data.jobId,
        singleSelect:true,
        columns:[[
            { checkbox:true},
            { field:'jobId',title:'作业编号',width:80,sortable:true,align:'center'},
            { field:'paramSeq',title:'参数序号',width:80,sortable:true,align:'center',editor:{type:'validatebox',options:{
                required:true,
            }}},
            { field:'paramId',title:'参数ID',width:80,sortable:true,align:'center',editor:{type:'validatebox',options:{
                required:true,
            }}},
            { field:'paramName',title:'参数名称',width:80,sortable:true,align:'center',editor:{type:'validatebox',options:{
                required:true,
            }}},
            { field:'paramDef',title:'参数定义',width:280,sortable:true,align:'left',editor:{type:'validatebox',options:{
                required:true,
            }}},
            { field:'paramType',title:'参数类型',width:80,sortable:true,align:'center',editor:{type:'combobox',options:{
                valueField: 'value',
                textField: 'label',
                value:1,
                data:[{
                    label:"字符串",
                    value:1
                },{
                    label:"数字",
                    value:0
                }]
            }},formatter:function(val, row,index) {
                if (val == 1) return "<span style=\"color:green\" >字符串</span>";
                else  return "<span style=\"color:green\" >数字</span>";
            }},
            { field:'paramValid',title:'有效标志',width:80,sortable:true,align:'center',editor:{type:'combobox',options:{
                valueField: 'value',
                textField: 'label',
                value:1,
                data:[{
                    label:"有效",
                    value:1
                },{
                    label:"无效",
                    value:0
                }]
            }},formatter:function(val, row,index) {
                if (val == 1) return "<span style=\"color:green\" >有效</span>";
                else  return "<span style=\"color:red\" >无效</span>";
            }}
        ]],
        onDblClickRow:function (index,row) {
            if (editJobParamIndex == undefined) {
                $("#edit_jobparam_tt-" + data.jobId).datagrid('beginEdit',index);
                editJobParamIndex = index;
            }

            if ($("#edit_jobparam_tt-" + data.jobId).datagrid("validateRow",editJobParamIndex)) {
                $("#edit_jobparam_tt-" + data.jobId).datagrid('endEdit',editJobParamIndex);
                $("#edit_jobparam_tt-" + data.jobId).datagrid('beginEdit',index);
                editJobParamIndex = index;
            } else {
                $.messager.alert("系统提示","请先完成上一条参数的修改","INFO");
            }
        },
        toolbar:[{
            iconCls:'icon-add',
            handler:function () {
                if ($("#edit_jobparam_tt-" + data.jobId).datagrid("validateRow",editJobParamIndex)) {
                    $("#edit_jobparam_tt-" + data.jobId).datagrid('endEdit',editJobParamIndex);
                    $("#edit_jobparam_tt-" + data.jobId).datagrid('appendRow',{jobId:data.jobId,paramType:1,paramValid:1});
                    editJobParamIndex = $("#edit_jobparam_tt-" + data.jobId).datagrid("getRows").length-1;
                    $("#edit_jobparam_tt-" + data.jobId).datagrid('beginEdit',editJobParamIndex);
                } else {
                    $.messager.alert("系统提示","请先完成上一条参数的修改","INFO");
                }
            }
        },{
            iconCls:'icon-remove',
            handler:function () {
                var jobParamSelects = $("#edit_jobparam_tt-" + data.jobId).datagrid("getSelections");
                if (jobParamSelects.length >=1 ) {
                    var selectIndex = $("#edit_jobparam_tt-" + data.jobId).datagrid("getRowIndex", jobParamSelects[0]);
                    $("#edit_jobparam_tt-"+ data.jobId).datagrid("deleteRow", selectIndex);
                }else {
                    $.messager.alert("系统提示","请选择一行参数进行删除","INFO");
                }
            }
        },{
            iconCls:'icon-save',
            handler:function () {
                if ($("#edit_jobparam_tt-" + data.jobId).datagrid("validateRow",editJobParamIndex)) {
                    $("#edit_jobparam_tt-" + data.jobId).datagrid('endEdit', editJobParamIndex);
                    $.ajax({
                        url:'update_param',
                        data:JSON.stringify({inserted:$("#edit_jobparam_tt-" + data.jobId).datagrid("getChanges","inserted"),
                            deleted:$("#edit_jobparam_tt-" + data.jobId).datagrid("getChanges","deleted"),
                            updated:$("#edit_jobparam_tt-" + data.jobId).datagrid("getChanges","updated")}),
                        contentType:'application/json;charset=utf-8',
                        type:'post',
                        success:function(data) {
                            if (data.status == 0) {
                                $.messager.alert("系统提示", "修改参数成功：\n  新增记录数：" + data.insertedCnt + "\n  修改记录数：" + data.updatedCnt + "\n  删除记录数：" + data.deletedCnt, "INFO");
                            }else if (data.status == -1) {
                                $.messager.alert("系统提示", "修改参数失败：" + data.ret, "INFO");
                            }
                            $("#edit_jobparam_tt-" + data.jobId).datagrid("acceptChanges");
                            $("#edit_jobparam_tt-" + data.jobId).datagrid("reload");
                        },
                        error:function (data) {
                            $.messager.alert("系统提示","失败：" +JSON.stringify(data),"INFO");
                        }

                    })
                } else {

                }
            }
        }]
    });

    $("#edit_jobref_tt-" + data.jobId).datagrid({
        url:'jobref_byid?jobId=' + data.jobId,
        singleSelect:true,
        columns:[[
            { checkbox:true},
            { field:'jobId',title:'作业编号',width:80,sortable:true,align:'center'},
            { field:'refJobId',title:'依赖作业ID',width:80,sortable:true,align:'center',editor:{type:'validatebox',options:{
                required:true,
            }}},
            { field:'refType',title:'作业依赖类型',width:80,sortable:true,align:'center',editor:{type:'validatebox',options:{
                required:true,
            }}}
        ]],
        toolbar:[{
            iconCls:'icon-add',
            handler:function () {
                if ($("#edit_jobref_tt-" + data.jobId).datagrid("validateRow",editJobRefIndex)) {
                    $("#edit_jobref_tt-" + data.jobId).datagrid('endEdit',editJobRefIndex);
                    $("#edit_jobref_tt-" + data.jobId).datagrid('appendRow',{jobId:data.jobId,refType:1});
                    editJobRefIndex = $("#edit_jobref_tt-" + data.jobId).datagrid("getRows").length-1;
                    $("#edit_jobref_tt-" + data.jobId).datagrid('beginEdit',editJobRefIndex);
                } else {
                    $.messager.alert("系统提示","请先完成上一条依赖的修改","INFO");
                }
            }
        },{
            iconCls:'icon-remove',
            handler:function () {
                var jobRefSelects = $("#edit_jobref_tt-" + data.jobId).datagrid("getSelections");
                if (jobRefSelects.length >=1 ) {
                    var selectIndex = $("#edit_jobref_tt-" + data.jobId).datagrid("getRowIndex", jobRefSelects[0]);
                    $("#edit_jobref_tt-"+ data.jobId).datagrid("deleteRow", selectIndex);
                }else {
                    $.messager.alert("系统提示","请选择一行依赖进行删除","INFO");
                }
            }
        },{
            iconCls:'icon-save',
            handler:function () {
                if ($("#edit_jobref_tt-" + data.jobId).datagrid("validateRow",editJobRefIndex)) {
                    $("#edit_jobref_tt-" + data.jobId).datagrid('endEdit', editJobRefIndex);
                    $.ajax({
                        url:'update_ref',
                        data:JSON.stringify({inserted:$("#edit_jobref_tt-" + data.jobId).datagrid("getChanges","inserted"),
                            deleted:$("#edit_jobref_tt-" + data.jobId).datagrid("getChanges","deleted"),
                            updated:$("#edit_jobref_tt-" + data.jobId).datagrid("getChanges","updated")}),
                        contentType:'application/json;charset=utf-8',
                        type:'post',
                        success:function(data) {
                            if (data.status == 0) {
                                $.messager.alert("系统提示", "修改依赖成功：\n  新增记录数：" + data.insertedCnt + "\n  删除记录数：" + data.deletedCnt, "INFO");
                            }else if (data.status == -1) {
                                $.messager.alert("系统提示", "修改依赖失败：" + data.ret, "INFO");
                            }
                            $("#edit_jobref_tt-" + data.jobId).datagrid("acceptChanges");
                            $("#edit_jobref_tt-" + data.jobId).datagrid("reload");
                        },
                        error:function (data) {
                            $.messager.alert("系统提示","失败：" +JSON.stringify(data),"INFO");
                        }

                    })
                } else {

                }
            }
        }]
    })
}

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
