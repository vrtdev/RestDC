
describe("DcController", function(){
	var scope;
	var http;
	var ctrl;
	var $httpMock;
	var $scope;
	var createController;
	var configjson={
		"restDcJsonFileUrl":"/rest-dc/dc.json", 
		"baseUrlPrefix":"/",
		"defaultAcceptType":"application/json"
	};
	var configBis={
		"restDcJsonFileUrl":"/rest-dc/dc.json", 
		"defaultAcceptType":"application/json"
	};
	var dcjson=[ 
		{ "documents" : [ 
				{ 
					"description" : "Insert or update content...",
		          			"parameters" : [ 
		          				{ 
		          					"description" : "The content to insert or update, when id is null an insert is assumed...",
		                				"name" : "content",
		                				"parameterLocation" : "BODY",
		                				"required" : true,
		                				"type" : { "typeName" : "AbstractContent" }
		              			},
		              			{ 
		              				"name" : "request",
		                				"required" : true,
		                				"type" : { "typeName" : "HttpServletRequest" }
		              			}
		            		],
	          				"requestMethods" : [ "PUT","POST"],
	          				"returnType" : { "typeName" : "void" },
	          				"acceptTypes":["text/html"],
	          				"url" : "/v0_0_1/content"
	        			}
	        		]
        		}
        	];


	beforeEach(inject(function ($rootScope, $controller, $httpBackend) {
		$scope=$rootScope.$new();
		$httpMock=$httpBackend;
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
		console.log("---------------------");
		$httpMock.expectGET('config.json').respond(configjson);
		$httpMock.expectGET(configjson.restDcJsonFileUrl);
		ctrl=createController();
		$httpMock.flush(); 
		var doc=$scope.dc[0].documents[0];
		$httpMock.expectPOST($scope.config.baseUrlPrefix+dcjson[0].documents[0].url).respond(404, '',{'bla':'bla','content-type':'application/javascript'});
		$scope.doMethod("POST",0,0);
		$httpMock.flush(); 
		console.log($scope.dc[0].documents[0]);
		expect($scope.dc[0].documents[0].result).toEqual("*****");
	});
});