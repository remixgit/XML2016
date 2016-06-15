angular.module('main')
  .controller('MainController', ['$scope', '$rootScope', '$timeout','$location','$state','$window','Main','Search','Propis','Xhtml', 'Pdf',
    function($scope,$rootScope, $timeout,$location,$state,$window, Main, Search,Propis, Xhtml, Pdf){

        //go on mainPage
        //$state.go("main");
        //show documents
        $scope.propose = function(){
            $state.go('main.act');
        }
    
        $scope.search = function(){
            $state.go('main.search');
        }

        $scope.find = function(){
            $scope.acts = Main.query(
                function(response){
                     $scope.listForShowing = $scope.acts;
            });
           
        };

        $scope.showAct = function(uri){
            console.log(uri);
        };
        $scope.criteria = "";


        $scope.searchActs = function(){
            
            if(!$scope.criteria){
                $scope.error = "Morate uneti tekst pretrage.";
                return;
            }
            var searchCriteria = new Search({
                criteria: $scope.criteria
            });
            searchCriteria.$save(function(response){
                if(response.map){
                    if(response.map.error){
                        if($scope.results) $scope.results = [];
                        $scope.error = response.map.error;
                        return;                   
                    }
                }
                else{
                    $scope.error = "";
                    $scope.results = response.myArrayList;

                }
            })
        };

        $scope.selected = "";

        $scope.filter = function(){

            $scope.listForShowing = [];

            for(var i = 0; i < $scope.acts.length; i++){
                    if($scope.acts[i].status == $scope.selected){
                        $scope.listForShowing.push($scope.acts[i]);
                    }
            }

        };
        
        
        
        $scope.withdrawAct = function(act){
        	act.uri=act.uri.replace('/acts/','');
        	act.uri=act.uri.replace('.xml','');
        	
        	var index = $scope.acts.indexOf()
        	
        	 propis = Propis.get({actId:act.uri},function(response){ 
        	response.$update({actId:act.uri},function(response) {
        		act.status="povucen";
				console.log(response);
				//Popraviti ovo
				//if(response.map.error)
				//	$scope.error = response.map.error;
			});
        	 });
        };
        $scope.xhtml = function(uri){
            var xhtml = new Xhtml();
            xhtml.$get({uri: uri}, function(response){
                $scope.xhtmlDoc = response.map.html;
                console.log(response);
            });
        }
        $scope.pdf = function(uri){
            var pdf = new Pdf();
            pdf.$get({uri: uri}, function(response){
                $scope.path = response.map.path;
                $window.location.href = 'http://localhost:9000'+response.map.path;
            });
        }
    }])
    .controller('SearchResultsController', ['$scope', '$rootScope', '$timeout','$location','$state','Main','Search',
    function($scope,$rootScope, $timeout,$location,$state, Main, Search){

        $scope.results = $scope.$parent.results
    }])
    .controller('filterActsController', ['$scope', '$rootScope', '$timeout','$location','$state','Main','Search',
    function($scope,$rootScope, $timeout,$location,$state, Main, Search){

        $scope.acts = $scope.$parent.acts;

        $scope.selected = "";

        $scope.filter = function(){

            $scope.$parent.listForShowing = [];

            for(var i = 0; i < $scope.acts.length; i++){
                    if($scope.acts[i].status == $scope.selected){
                       $scope.$parent.listForShowing.push($scope.acts[i]);
                    }
            }

        }
    }]);

