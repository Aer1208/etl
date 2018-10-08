var jobParamIndex = undefined;
var jobRefIndex=undefined;
$(function () {
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
            }}
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
           { field:'refJobId',title:'依赖作业ID',width:80,sortable:true,align:'center',editor:{type:'validatebox',options:{
               required:true,
           }}},
           { field:'refType',title:'作业依赖类型',width:80,sortable:true,align:'center',editor:{type:'validatebox',options:{
               required:true,
           }}}
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
               $("#jobref_tt").datagrid('appendRow',{jobId:$('#jobId').val()});
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
       param.cmdType=1;
       param.maxInstance=3;
       var jobparamArray = $("#jobparam_tt").datagrid("getChanges");
       var jobrefArray = $("#jobref_tt").datagrid("getChanges");
       $.ajax({
           url:'submit_job',
           data:JSON.stringify({jobDef:param, jobParamDefs:jobparamArray,jobRefs:jobrefArray}),
           contentType:'application/json;charset=utf-8',
           type:"post",
           success:function(data) {
               if (data.status == 0) {
                   $.messager.alert("系统提示", "添加成功：添加作业定义记录" + data.addJobCnt + "条，添加参数记录" + data.addjobParamCnt + "条，添加依赖" + data.addjobRefCnt + "条", "INFO");
                   window.parent.closeTabs('新增作业')
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
});

function validateFrom() {
    var jobId=$("#jobId").val();
    var cmdName=$("#cmdName").val();
    var cmdPath=$("#cmdPath").val();

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
    return true;
}