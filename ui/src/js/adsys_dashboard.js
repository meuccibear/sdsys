function ajaxDashboardRequest(params) {
	ajaxGet("dashboard/dashboardinfo", null, function(result){
		if(result.success)
		{
			init(result.data);
		} 
	}, null);

}


function init(data){
	// Donut Chart
	$('#DEVICES').html(data.DEVICES);
	$('#ProgramCount').html(data.ProgramCount);
	$('#cupUsage').html(data.cupUsage+'%');
	$('#memoryUsage').html(data.memoryUsage+'%');
   var m1 = new Morris.Donut({
        element: 'donut-chart2',
        data: [
          {label: "在线", value: data.ONLINE},
          {label: "离线", value: data.OFFLINE}
        ],
        colors: ['#D9534F','#1CAF9A']
    });

    var d = new Date(new Date().getFullYear(), new Date().getMonth()+1, 0).getDate();
    var ldate =[];
    for(var i=1;i<=d;i++){
    	ldate.push({key:i+'',a:0});
    	// console.log(i);
    }
    $.each(data.DeployDeviceNumOfDay||[], function(i, item){
    	ldate[item.d_day-1].a = item.devices;
    });
    // ldate[10].a=9;


    var m2 = new Morris.Line({
        // ID of the element in which to draw the chart.
        element: 'basicflot',
        // Chart data records -- each entry in this array corresponds to a point on
        // the chart.
        data: ldate,
        xkey: 'key',
        ykeys: ['a', 'b'],
        labels: ['Series A', 'Series B'],
        gridTextColor: 'rgba(255,0,255,0.5)',
        lineColors: ['#fdd2a4', '#fdd2a4'],
        //lineWidth: '2px',
        hideHover: 'always',
        parseTime:false,
        smooth: false,
        grid: true,
        units:"台",
        ymax:"auto 4",
        onlyIntegers:true
   });
}

function getLisence(){
	ajaxGet("customer/license",null,function(result){
		if (result.success)
		{
			var retJ = result.data;
			if (retJ)
			{
				$("#license").html(retJ);		
			}
		}	
	}, null);
}
