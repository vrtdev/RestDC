	function dcCtrl($scope,$http,$timeout,$document){

    	var syntax={"html":"html","javascript":"javascript","jscript":"javascript","json":"javascript"};

	$scope.dc=[];

	$http.get('config.json').then(function(result){
		$scope.config=result.data;
		$http.get($scope.config.restDcJsonFileUrl)
			.then(function(res){
          				$scope.dc = res.data;
        		});
		if(typeof $scope.config.baseUrlPrefix==="undefined" || $scope.config.baseUrlPrefix===null || $scope.config.baseUrlPrefix.length===0){
			var thisUrl=window.location.href.split("#")[0];
			var path=window.location.pathname;
			var pelem=path.split("/");
			pelem.pop();
			pelem.pop();
			var newpath=pelem.join("/");
			$scope.config.baseUrlPrefix=window.location.origin+newpath;
		}
		if($scope.config.baseUrlPrefix.indexOf('/')===0){
			$scope.config.baseUrlPrefix=window.location.origin+$scope.config.baseUrlPrefix;
		}
		
	});
	console.log($document);
	// TODO : fix for Angular unstable
	if  (!$scope.$$phase) {
		$scope.$apply();
	}

	$scope.doMethod = function(method,itemnr,docnr){
		var httpMethod = method.toUpperCase();
		var mydoc = $scope.dc[itemnr].documents[docnr];
		var acceptType=mydoc.selectedAcceptType || $scope.config.defaultAcceptType;
		var url=$scope.config.baseUrlPrefix+mydoc.url;
		mydoc.result={};
		var hasParms=false;
		var parm={};
		var paramstring="?";
		var data=null;
		var fd=null;
		for(var p=0;p< mydoc.parameters.length;p++){
			var param=mydoc.parameters[p];
			switch(param.parameterLocation){
				case "PARAMETERS":
					parm[param.name]=param.value;
					hasParms=true;
					paramstring+=(paramstring=="?"?"":"&")+encodeURIComponent(param.name)+"="+encodeURIComponent(param.value);
					break;
				case "BODY":
					data=param.value;
					break;
				case "PATH":
					url=url.replace("{"+param.name+"}",param.value);
					break;
			}
			if(param.type.typeName=="MultipartFile"){
				if(fd===null){
					fd = new FormData();
				}
				fd.append(param.name,document.getElementById(itemnr+"-"+docnr+"-"+param.name).files[0]);
			}
		}
		var config={"method":httpMethod};
		if(fd!==null){
			data=fd;
			config.withCredentials= true;
			config.headers={"Content-Type": undefined };
			config.transformRequest=angular.identity;
		}
		config.headers=config.headers||{};
		config.headers.Accept=acceptType;
		mydoc.result.url=url+(paramstring=='?'?'':paramstring);
		config.url=url;
		if(hasParms){
			config.params=parm;
		}
		config.data=data;
		$http(config).success(function(data, status, headers, config){
			console.log("SUCCESS");
			insertResults(mydoc.result,data, status, headers, config);
		}).error(function(data, status, headers, config){
			console.log("Not so much");
			insertResults(mydoc.result,data, status, headers, config);
		});

		// TODO : fix for Angular unstable
		if(!$scope.$$phase) {
			$scope.$apply();
		}
	};

	var insertResults=function(result,data, status, headers, config){
		console.log("got executed");
		result.body=data;
		result.code=status;
		var header=headers();
		var ctype=header["content-type"].toLowerCase();
		result.syntax='generic';
		for(var key in syntax){
			if(ctype.indexOf(key)>-1){
				result.syntax=syntax[key];
				break;
			}
		}
		result.headers=flatten(header);
		$timeout(Rainbow.color,500);
		// setTimeout(Rainbow.color, 500);
	};

	var flatten = function(obj){
		var result="";
		for(var key in obj){
			if(obj.hasOwnProperty(key)){
				result+=key+' : '+obj[key]+"\n";
			}
		}
		return result;
	};
}