
describe("DcController", function(){
	var scope;
	var http;
	var ctrl;
	var $httpMock;
	var $scope;
	var $time;
	var createController;

	beforeEach(inject(function ($rootScope, $controller, $httpBackend,$timeout) {
		$scope=$rootScope.$new();
		$httpMock=$httpBackend;
		$time=$timeout;
		$httpMock.when('GET',configjson.restDcJsonFileUrl).respond(dcjson);
		createController = function() {
			var cntl=$controller('dcCtrl', {'$scope' : $scope });
			return cntl;
		};
	}));
	afterEach(function() {
		$httpMock.verifyNoOutstandingExpectation();
		$httpMock.verifyNoOutstandingRequest();
	});


	it("should load a config and a dc.json", function() { 
		$httpMock.expectGET('config.json').respond(configjson);
		$httpMock.expectGET(configjson.restDcJsonFileUrl);
		ctrl=createController();
		$httpMock.flush(); 
		expect($scope.config).toEqual(configjson);
		expect($scope.dc).toEqual(dcjson);
	});
	it("should create own baseUrl",function(){
		$httpMock.expectGET('config.json').respond(configBis);
		$httpMock.expectGET(configBis.restDcJsonFileUrl);
		ctrl=createController();
		$httpMock.flush();
		expect($scope.config.baseUrlPrefix).toMatch(/http:\/\/localhost:[0-9]{4}/);
	});
	it("should do a POST request ",function(){
		spyOn(Rainbow,"color");
		$httpMock.expectGET('config.json').respond(configjson);
		$httpMock.expectGET(configjson.restDcJsonFileUrl);
		ctrl=createController();
		$httpMock.flush(); 
		var doc=$scope.dc[0].documents[0];
		var url=$scope.config.baseUrlPrefix+dcjson[0].documents[0].url;
		$httpMock.expectPOST(url).respond(response(0));
		$scope.doMethod("POST",0,0);
		$httpMock.flush(); 
		$time.flush();
		var exp={ 
			'url' : 'http://localhost:9876//v0_0_1/content', 
			'body' : '', 
			'code' : 404, 
			'syntax' : 'javascript', 
			'headers' :"bla : bla\ncontent-type : application/javascript\n"
		};
		expect(Rainbow.color.calls.length).toEqual(1);
		console.log($scope.dc[0].documents[0]);
		expect(JSON.stringify($scope.dc[0].documents[0].result)).toEqual(JSON.stringify(exp));
	});
	it("should do a GET request with parameters and url rewriting",function(){
		$httpMock.expectGET('config.json').respond(configjson);
		$httpMock.expectGET(configjson.restDcJsonFileUrl);
		ctrl=createController();
		$httpMock.flush(); 
		var doc=$scope.dc[0].documents[1];
		var url=$scope.config.baseUrlPrefix+dcjson[0].documents[1].url;
		url=url.replace("{id}","urlwaarde")+"?test=paramwaarde";
		$httpMock.expectGET(url).respond(response(1));
		doc.parameters[0].value="urlwaarde";
		doc.parameters[1].value="paramwaarde";
		
		$scope.doMethod("GET",0,1);
		$httpMock.flush(); 
		
	});
});
