var jobParamIndex = undefined;
var jobRefIndex=undefined;
$(function () {
    $("#cycleUnit").combobox(cycleOption)
   $("#jobparam_tt").datagrid({
       singleSelect:true,
       columns:[[
           { checkbox:true},
           { field:'jobId',title:'作业编号',width:80,sortable:true,align:'center',editor:{type:'validatebox',options:{
               required:true,
           }}},
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
                   label:"数字",
                   value:0
               },{
                   label:"字符串",
                   value:1
               }]
           }}},
           { field:'paramValid',title:'有效标志',width:80,sortable:true,align:'center',editor:{type:'combobox',options:validOption}
           }
       ]],
       toolbar:[{
           iconCls:'icon-add',
           handler:function(){
               if ($('#jobId').val().trim() == "") {
                   $.messager.alert("系统提示","请填写作业ID","WARN");
                   return ;
               }
               if (jobParamIndex != undefined) {
                   if($("#jobparam_tt").datagrid("validateRow",jobParamIndex)) {
                       $("#jobparam_tt").datagrid('endEdit', jobParamIndex);
                   }else {
                       $.messager.alert("系统提示","请先完成上一次编辑","INFO");
                       return ;
                   }
               }
               $("#jobparam_tt").datagrid('appendRow',{jobId:$('#jobId').val(),paramType:1,paramValid:1});
               $("#jobparam_tt").datagrid('beginEdit',$("#jobparam_tt").datagrid("getRows").length-1);
               jobParamIndex = $("#jobparam_tt").datagrid("getRows").length-1;
           }
       },{
           iconCls:'icon-remove',
           handler:function () {
               var jobParamSelects = $("#jobparam_tt").datagrid("getSelections");
               if (jobParamSelects.length >=1 ) {
                   var selectIndex = $("#jobparam_tt").datagrid("getRowIndex", jobParamSelects[0]);
                   $("#jobparam_tt").datagrid("deleteRow", selectIndex);
               }else {
                   $.messager.alert("系统提示","请选择一行参数进行删除","INFO");
               }
           }
       },{
           iconCls:'icon-save',
           handler:function () {
               if($("#jobparam_tt").datagrid("validateRow",jobParamIndex)) {
                   $("#jobparam_tt").datagrid('endEdit',jobParamIndex);
                   jobParamIndex = undefined;
               }else {
                   $.messager.alert("系统提示","编辑的行校验不通过，请检查编辑行","INFO");
               }
           }
       }],
       onDblClickRow:function (index,data) {
           if($("#jobparam_tt").datagrid("validateRow",jobParamIndex)) {
               $("#jobparam_tt").datagrid('endEdit',jobParamIndex);
               $("#jobparam_tt").datagrid('beginEdit',index);
               jobParamIndex =  index;
           }else {
               $.messager.alert("系统提示","请先完成上一次编辑","INFO");
               return ;
           }
       }
   });
   $("#jobref_tt").datagrid({
       singleSelect:true,
       columns:[[
           { checkbox:true},
           { field:'jobId',title:'作业编号',width:80,sortable:true,align:'center',editor:{type:'validatebox',options:{
               required:true,
           }}},
           { field:'refJobId',title:'依赖作业ID',width:300,sortable:true,align:'center',editor:{type:'combobox',options:{
               url:'get_writer_lock',
               textField:'lockObj',
               valueField:'jobId',
               mode:'local',
               hasDownArrow:false,
               filter: function(q, row){
                   var opts = $(this).combobox('options');
                   var rowText = row[opts.textField].toUpperCase();
                   return rowText.indexOf(q.toUpperCase()) == 0 || row[opts.valueField].toString().indexOf(q) ==0;
               }
           }}},
           { field:'refType',title:'作业依赖类型',width:80,sortable:true,align:'center',editor:{type:'combobox',options:refTypeOption}}
       ]],
       toolbar:[{
           iconCls:'icon-add',
           handler:function(){
               if ($('#jobId').val().trim() == "") {
                   $.messager.alert("系统提示","请填写作业ID","WARN");
                   return ;
               }
               if (jobRefIndex != undefined) {
                   if($("#jobref_tt").datagrid("validateRow",jobRefIndex)) {
                       $("#jobref_tt").datagrid('endEdit', jobRefIndex);
                   }else {
                       $.messager.alert("系统提示","请先完成上一次编辑","INFO");
                       return ;
                   }
               }
               $("#jobref_tt").datagrid('appendRow',{jobId:$('#jobId').val(),refType:1});
               $("#jobref_tt").datagrid('beginEdit',$("#jobref_tt").datagrid("getRows").length-1);
               jobRefIndex = $("#jobref_tt").datagrid("getRows").length-1;
           }
       },{
           iconCls:'icon-remove',
           handler:function () {
               var jobRefSelects = $("#jobref_tt").datagrid("getSelections");
               if (jobRefSelects.length >=1 ) {
                   var selectIndex = $("#jobref_tt").datagrid("getRowIndex", jobRefSelects[0]);
                   $("#jobref_tt").datagrid("deleteRow", selectIndex);
               }else {
                   $.messager.alert("系统提示","请选择一行参数进行删除","INFO");
               }
           }
       },{
           iconCls:'icon-save',
           handler:function () {
               if($("#jobref_tt").datagrid("validateRow",jobRefIndex)) {
                   $("#jobref_tt").datagrid('endEdit',jobRefIndex);
                   jobRefIndex = undefined;
               }else {
                   $.messager.alert("系统提示","编辑的行校验不通过，请检查编辑行","INFO");
               }
           }
       }],
       onDblClickRow:function (index,data) {
           if($("#jobref_tt").datagrid("validateRow",jobRefIndex)) {
               $("#jobref_tt").datagrid('endEdit',jobRefIndex);
               $("#jobref_tt").datagrid('beginEdit',index);
               jobRefIndex =  index;
           }else {
               $.messager.alert("系统提示","请先完成上一次编辑","INFO");
               return ;
           }
       }
   });

   $("#save").click(function(){
       if (!validateFrom()) return ;
       var param = {}
       param.jobValid=1;
       param.jobId=$("#jobId").val();
       param.jobName=$("#jobName").val();
       param.cmdName=$("#cmdName").val();
       param.cmdPath=$("#cmdPath").val();
       param.jobGroup=$("#jobGroup").val();
       param.priorty=$("#priorty").val();
       param.cronDesc=$("#cronDesc").val();
       param.jobCycle=$("#jobCycle").val();
       param.cycleUnit=$("#cycleUnit").combobox("getValue");
       param.cmdType=1;
       param.maxInstance=3;
       var jobparamArray = $("#jobparam_tt").datagrid("getRows");
       var jobrefArray = $("#jobref_tt").datagrid("getRows");
       var jobLockObj = {}
       jobLockObj.jobId=$("#jobId").val();
       jobLockObj.lockObj = $("#tableName").val();
       jobLockObj.lockType=1;
       // alert(JSON.stringify({jobDef:param, jobParamDefs:jobparamArray,jobRefs:jobrefArray,jobLockObj:jobLockObj}));
       $.ajax({
           url:'submit_job',
           data:JSON.stringify({jobDef:param, jobParamDefs:jobparamArray,jobRefs:jobrefArray,jobLockObj:jobLockObj}),
           contentType:'application/json;charset=utf-8',
           type:"post",
           success:function(data) {
               if (data.status == 0) {
                   $.messager.alert("系统提示", "添加成功：添加作业定义记录" + data.addJobCnt + "条，添加参数记录" + data.addjobParamCnt + "条，添加依赖" + data.addjobRefCnt + "条", "INFO",function(){
                       $.messager.confirm("系统提示","是否关闭新增作业页面",function (flag) {
                           if (flag) {
                               window.parent.closeTabs('新增作业');
                           }
                       })
                   });
               } else {
                   $.messager.alert("系统提示", "添加失败：" + data.ret , "INFO");
               }
           },
           error:function (data) {
               $.messager.alert("系统提示","失败：" +JSON.stringify(data),"INFO");
           }
       })
   });

   $("#btnCancel").click(function () {
       window.parent.closeTabs('新增作业')
   })

    $("#jobType").combobox({
        valueField: 'label',
        textField: 'value',
        data:[{
            label:1,
            value:"基础模型作业"
        },{
            label:2,
            value:"清洗转换"
        }],
        onSelect:function (row) {
            if ($('#jobId').val() == "") {
                $.messager.alert("系统提示","请先输入作业号","ERROR");
                return ;
            }
            $("#jobparam_tt").datagrid('loadData',{total:0,rows:[]});
            if (row.label == 2) {
                $("#cleanTr").show();
                $("#sqoopToHiveTr").hide();
            } else {
                $("#cleanTr").hide();
                $("#sqoopToHiveTr").hide();
                $("#jobparam_tt").datagrid("appendRow",{
                    jobId:$('#jobId').val(),
                    paramType:0,
                    paramValid:1,
                    paramSeq:1,
                    paramId:'job_seq',
                    paramName:'作业运行步骤',
                    paramDef:1
                });
                $("#jobparam_tt").datagrid("appendRow",{
                    jobId:$('#jobId').val(),
                    paramType:0,
                    paramValid:1,
                    paramSeq:2,
                    paramId:'vDataDate',
                    paramName:'数据日期',
                    paramDef:'${vDataDate}'
                });
            }
        }
    });
   
   $("#genCleanParam").click(function () {
       var cycleUnit = $("#cycleUnit").combobox("getValue");
       var dataDate="${vDataHour}"
       if (cycleUnit == 1 ) {
           dataDate="${vDataDate}"
       }else if (cycleUnit == 2) {
           dataDate="${vMonth}"
       }else if (cycleUnit == 3) {
           dataDate="${vYear}"
       }
       var srcTableName = $("#srcTableName").val();
       var objTableName= $("#objTableName").val();
       var dbName = $("#dbName").val();
       $("#jobparam_tt").datagrid('loadData',{rows:[
           {
               jobId:$('#jobId').val(),
               paramType:0,
               paramValid:1,
               paramSeq:1,
               paramId:'job_seq',
               paramName:'作业运行步骤',
               paramDef:1
           },{
               jobId:$('#jobId').val(),
               paramType:0,
               paramValid:1,
               paramSeq:2,
               paramId:'cleanFile',
               paramName:'清洗配置文件',
               paramDef:srcTableName + ".xml"
           },{
               jobId:$('#jobId').val(),
               paramType:1,
               paramValid:1,
               paramSeq:3,
               paramId:'input',
               paramName:'输入路径',
               paramDef:'/user/dw/extract/'+srcTableName+'/'+ dataDate
           },{
               jobId:$('#jobId').val(),
               paramType:1,
               paramValid:1,
               paramSeq:4,
               paramId:'output',
               paramName:'输出路径',
               paramDef:'/user/dw/tran/'+srcTableName+'/'+ dataDate
           },{
               jobId:$('#jobId').val(),
               paramType:1,
               paramValid:1,
               paramSeq:5,
               paramId:'dbName',
               paramName:'数据库',
               paramDef:dbName
           },{
               jobId:$('#jobId').val(),
               paramType:1,
               paramValid:1,
               paramSeq:6,
               paramId:'table_name',
               paramName:'目标表名',
               paramDef:objTableName
           },{
               jobId:$('#jobId').val(),
               paramType:1,
               paramValid:1,
               paramSeq:7,
               paramId:'loadType',
               paramName:'装载类型',
               paramDef:'1'
           }
       ]});
   })
});

function validateFrom() {
    var jobId=$("#jobId").val();
    var cmdName=$("#cmdName").val();
    var cmdPath=$("#cmdPath").val();
    var tableName=$("#tableName").val();
    var jobCycle = $("#jobCycle").val();
    var cycleUnit = $("#cycleUnit").combobox("getValue");

    var jobparamArray = $("#jobparam_tt").datagrid("getRows");
    var jobrefArray = $("#jobref_tt").datagrid("getRows");

    if (jobId == "") {
        $.messager.alert("系统提示","作业ID不能为空","WARN")
        return false;
    }
    if (cmdName == "") {
        $.messager.alert("系统提示","作业脚本不能为空","WARN")
        return false;
    }
    if (cmdPath == "") {
        $.messager.alert("系统提示","作业路径不能为空","WARN")
        return false;
    }
    if (tableName == "") {
        $.messager.alert("系统提示","表名不能为空", "WARN")
        return false;
    }
    if (jobCycle == "") {
        $.messager.alert("系统提示","作业运行周期不能为空", "WARN")
        return false;
    }
    if(cycleUnit == "" || cycleUnit == -1) {
        $.messager.alert("系统提示","请选择作业运行周期单位", "WARN")
        return false
    }
    return true;
}