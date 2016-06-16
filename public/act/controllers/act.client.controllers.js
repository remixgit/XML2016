angular.module('act').controller('actCtrl', ['$scope','$state','Propis',
    function($scope,$state,Propis) {
		$scope.addAct = function(){
			$state.go('view.addAct');
		}
		
		$scope.act = new Propis();
		
		$scope.sendAct = function(){
			if(!$scope.act.text){
				$scope.error = "Polje ne sme biti prazno. Molim vas unesite dokument.";
				return;
			}
			var text = $scope.act.text;
			$scope.error=""
			$scope.act.$save(function(response) {
				console.log(response);
				$scope.act.text="";
				
				if(response.map!=undefined)
				if(response.map.error!==""){
					$scope.error = response.map.error;
					$scope.act.text = text;
				
			}
				
			});
		}
		$scope.showForm = function(){
			$state.go("act");
		}
}]);