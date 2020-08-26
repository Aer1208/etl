/**
 * 定义Cron对象
 * @param cycle 运行周期
 * @param unit  运行单位
 * @constructor
 */
function Cron() {}
Cron.prototype.cycleUnit=1
Cron.prototype.jobCycle=1
Cron.prototype.second = "01"
Cron.prototype.minute = "01"
Cron.prototype.hour="01"
Cron.prototype.day="*"
Cron.prototype.month="*"
Cron.prototype.year="*"

Cron.prototype.getCronExp = function () {
    if (this.cycleUnit == 0 ) {
        this.hour="*/" + this.jobCycle
    }else if (this.cycleUnit ==1 ) {
        this.hour="01"
    }else if (this.cycleUnit == 2 ) {
        this.day = "01"
    } else if (this.cycleUnit == 3) {
        this.month = "01"
        this.day = "02"
    } else if (this.cycleUnit == 4) {
        this.hour = "*"
        this.minute = "*/" + this.jobCycle
    }
    return this.second + " " + this.minute + " " + this.hour + " " + this.day  + " " + this.month + " " + this.year
}

//----------------------------------------------------COMBOBOX----------------------------------------------------------//
// 作业调度周期表达式选项
var cycleOption={
    valueField: 'value',
    textField: 'label',
    value:-1,
    data:[{
        label:'请选择',
        value:-1
    },{
        label:"小时",
        value:0
    },{
        label:"天",
        value:1
    },{
        label:"周",
        value:5
    },{
        label:"月",
        value:2
    },{
        label:"季",
        value:6
    },{
        label:"年",
        value:3
    },{
        label:'分',
        value:4
    }],
    onSelect:function (item) {
        var cron = new Cron();
        cron.jobCycle = $("#jobCycle").val()
        cron.cycleUnit= item.value;
        $("#cronDesc").val(cron.getCronExp())
    }
}

var validOption={
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
}

var refTypeOption={
    valueField: 'value',
    textField: 'label',
    value:1,
    data:[{
        label:"直接依赖",
        value:1
    },{
        label:"天依赖小时",
        value:2
    },{
        label:"月依赖天",
        value:3
    },{
        label:"年依赖月",
        value:4
    },{
        label:"周依赖日",
        value:5
    },{
        label:"季依赖月",
        value:6
    }]
}
var weekOption={
    valueField: 'value',
    textField: 'label',
    value:0,
    data:[{
        label:"星期日",
        value:0
    },{
        label:"星期一",
        value:1
    },{
        label:"星期二",
        value:2
    },{
        label:"星期三",
        value:3
    },{
        label:"星期四",
        value:4
    },{
        label:"星期五",
        value:5
    },{
        label:"星期六",
        value:6
    }]
}

//-------------------------------------------------共用函数------------------------------------------------------//
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

function searchInst() {
    var dataDate = $("#searchDataDate").datebox("getValue");
    var queryParams = $('#jobtt').datagrid('options').queryParams={
        page:1,
        rows:20
    };
    if ("" != dataDate) {
        queryParams.dataDate = dataDate;
    }

    $("#jobtt").datagrid("reload");
    $('#jobtt').datagrid('options').queryParams={
        page:1,
        rows:20
    };
}

function searchKey() {
    var searchKey = $("#searchKey").val();
    var queryParams = $('#tt').datagrid('options').queryParams={
        page:1,
        rows:30
    };
    if (searchKey.trim() != "" ) {
        queryParams.key = searchKey;
    }
    $("#tt").datagrid("reload");
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