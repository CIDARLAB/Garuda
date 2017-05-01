/**
 * Created by krishna on 4/30/17.
 */
//Knockout stuff

function UCFItemViewModel(){
    var id = ko.observable();

    var name = ko.observable();
}

function PredictViewModel(){
    var self = this;

    self.ucfid = ko.observable("");

    self.netlist =  ko.observable("");

    self.ucfs = ko.observableArray([]);

    self.predict = function(){
        $.post('/api/v1/cello/predict', {
            ucfid: self.ucfid ,
            netlist: self.netlist
        }, function(response){
            //Update the prediction output
            console.log(response);
        });
    }

    self.getUCFs = function(){
        //Download UCFs
        $.get('/api/v1/cello/ucfs', function(response){
            console.log("Calling get UCFs");
            for(var index = 0; index<response.length; index++){
                var ucfitem = new UCFItemViewModel();
                ucfitem.id = response[index].id;
                ucfitem.name = response[index].name;
                self.ucfs.push(ucfitem);
            }
        });

    }


    self.initPage = function(){
        console.log("Loading the page");
        self.getUCFs();
    }

}

var pageViewModel = new PredictViewModel();
ko.applyBindings(pageViewModel);

pageViewModel.initPage();
