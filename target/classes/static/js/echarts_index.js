
$(function () {
    // 路径配置
    require.config({
        paths: {
            echarts: '../echarts/'
        }
    });
// 使用
    require(
        [
            'echarts',
            'echarts/chart/bar',
            'echarts/chart/line',
            'echarts/chart/pie'
        ],
        drawEcharts
    );
})

function drawEcharts(ec){
    drawTop(ec);
    drawRunTime(ec);
    drawPieJobs(ec,'昨日平台全量作业监控','/allJobs','main3');
    drawPieJobs(ec,'昨日平台数据源监控','/ds','main4');
    drawPieJobs(ec,'昨日平台普通作业监控','/job','main5');
}
function drawTop(ec){
    var myBarChart = ec.init(document.getElementById("main"));
    myBarChart.showLoading();    //数据加载完之前先显示一段简单的loading动画
    var names=[];    //类别数组（实际用来盛放X轴坐标值）
    var nums=[];    //销量数组（实际用来盛放Y坐标值）
    $.post('/topn',{dataDate:getYeastoday(), top:10},function (data) {
        $.each(data,function (i,n) {
            names.push(n.jobId);
            nums.push(n.runTime);
        });
        myBarChart.hideLoading();    //隐藏加载动画
        var option = {
            title : {
                text: '昨日运行实例TOP10'
            },
            tooltip: {
                show: true
            },
            legend: {
                data:['昨日运行实例TOP10']
            },
            xAxis : [
                {
                    type : 'category',
                    data : names
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    name:'运行时长（秒）'
                }
            ],
            series : [
                {
                    "name":"昨日运行实例TOP10",
                    "type":"bar",
                    "data":nums
                }
            ]
        };
        myBarChart.setOption(option,true); //当setOption第二个参数为true时，会阻止数据合并
    })
}


function drawRunTime(ec){
    var myLineChart = ec.init(document.getElementById('main2'));
    myLineChart.showLoading();    //数据加载完之前先显示一段简单的loading动画
    var dataDates = [];
    var runTimes = [];
    var runJobs = []
    $.post("/getRunTime", {}, function (data) {
        $.each(data, function (i,n) {
            dataDates.push(n.dataDate);
            runTimes.push(n.runTime);
            runJobs.push(n.runJobCnt);
        })
        myLineChart.hideLoading();
        var option2 = {
            title : {
                text: '最近7日系统运行作业监控'
            },
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['平均运行时长','成功运行作业数']
            },
            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : dataDates
                }
            ],
            yAxis : [
                {
                    type : 'value',
                    name:'运行时长（秒）'
                },{
                    type:'value',
                    name:'成功运行作业数（个）'
                }
            ],
            series : [
                {
                    name:'平均运行时长',
                    type:'line',
                    data:runTimes,
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name: '平均值'}
                        ]
                    }
                },
                {
                    name:'成功运行作业数',
                    type:'line',
                    yAxisIndex: 1,
                    data:runJobs
                }
            ]
        };
        myLineChart.setOption(option2,true);
    })
}

function drawPieJobs(ec,text,url,id) {
    var allJobsChart = ec.init(document.getElementById(id));
    $.post(url,{},function (data) {
        var option = {
            title : {
                text: text,
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient : 'vertical',
                x : 'left',
                data:['应完成实例数','已完成实例数','已失败实例数']
            },
            calculable : true,
            series : [
                {
                    name:text,
                    type:'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:data
                }
            ]
        };
        allJobsChart.setOption(option, true)
    });
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

function getYeastoday() {
    var day1 = new Date();
    day1.setTime(day1.getTime()-24*60*60*1000);
    var s1 = day1.format('yyyyMMdd');
    return s1;
}