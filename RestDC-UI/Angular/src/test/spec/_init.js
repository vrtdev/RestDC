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
	{ "documents" : 
		[ 
			{ 
				"description" : "Insert or update content...",
				"parameters" : 
				[ 
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
			},
			{ 
				"parameters" : [ 
				{ 
					"name" : "id",
					"parameterLocation" : "PATH",
					"required" : true,
					"type" : { "typeName" : "String" }
				},
				{ 
					"name" : "test",
					"parameterLocation" : "PARAM",
					"required" : true,
					"type" : { "typeName" : "String" }
				}	 
				],
				"requestMethods" : [ "GET" ],
				"returnType" : { "typeName" : "AbstractContent" },
				"url" : "/v0_0_1/glyph/{id}"
			},
		]
	}
];
var responses=[
	{
		code:404,
		body:"",
		header:{'bla':'bla','content-type':'application/javascript'}
	}
];
var response=function(n){
	var res=responses[n];
	return function(){
		return [res.code,res.body,res.header]; 
	};
};