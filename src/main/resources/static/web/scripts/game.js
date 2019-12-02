var gamesInfo;
var currentlyGamePlayer;

$(function() {
    loadData();
});

$(function() {
    $.get('/api/games')
            .done(function(data) {
                if(data.player.id){
                    currentlyGamePlayer = data.player.id;
                    document.getElementById("logoutBtn").style.visibility = "visible";
                    document.getElementById("btnLog").style.visibility = "hidden";
                }else{
                    document.getElementById("logoutBtn").style.visibility = "hidden";
                    document.getElementById("btnLog").style.visibility = "visible";
                }
            })
            .fail(function( jqXHR, textStatus ) {
              //alert( "Failed: " + textStatus );
            });
});

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};

function loadTables(data){

    let playerInfo;
    let secondPlayer;

                if(data.gamePlayers.length == 1){
                    secondPlayer = 'N/A';
                }else{
                    secondPlayer = data.gamePlayers[1].player.userName;
                }

                if(data.gamePlayers[0].id == getParameterByName('gp'))
                    playerInfo = [data.gamePlayers[0].player.userName, secondPlayer];
                else
                    playerInfo = [secondPlayer, data.gamePlayers[0].player.userName];

                $('#playerInfo').text(playerInfo[0] + '(you) vs ' + playerInfo[1]);

                data.ships.forEach(function(ship){
                    ship.location.forEach(function(location){
                        $('#'+location).addClass('ship-piece');
                    })
                });


                    data.salvoes.forEach(function(salvo){
                         for(j = 0; j < salvo.location.length; j++){
                            data.ships.forEach(function(ship){
                                for(i = 0; i < ship.location.length; i++){
                                    if(ship.location[i] == salvo.location[j]){
                                         $('#'+salvo.location[j]+'_2').removeClass();
                                         $('#'+salvo.location[j]+'_2').addClass('ship-piece-hited');
                                    }else{
                                        if($('#'+salvo.location[j]+'_2').hasClass('.ship-piece-hited')){
                                             console.log("");
                                        }else{
                                            $('#'+salvo.location[j]+'_2').addClass('salvo');
                                        }
                                    }
                                }
                            });
                         }
                     });

}

function loadData(){

    $.get('/api/game_view/'+getParameterByName('gp'))
        .done(function(data) {
            gamesInfo = data;
            loadTables(data);
        })
        .fail(function( jqXHR, textStatus ) {
          //alert( "Failed: " + textStatus );
        });
};

function openForm() {
  document.getElementById("myForm").style.display = "block";
}

function closeForm() {
  document.getElementById("myForm").style.display = "none";
}

function login(){
        closeForm();
        $.post("/api/login", { userName: document.getElementById("userN").value, password: document.getElementById("pass").value })
        .done(function(data){
            getInfo();
                document.getElementById("userLogged").innerHTML = "Welcome "+document.getElementById("userN").value;
                document.getElementById("logoutBtn").style.visibility = "visible";
                document.getElementById("btnLog").style.visibility = "hidden";
        }).fail(function( jqXHR, textStatus ) {
                                        alert( "Wrong credentials");
                                      });

};

function getInfo(){
    $.get('/api/games')
        .done(function(data) {
            loadData();
        })
        .fail(function( jqXHR, textStatus ) {
          //alert( "Failed: " + textStatus );
        });
}

function logout(){
    $.post("/api/logout").done(function() { console.log("logged out"); });
    window.location.reload();
    toMainPage();
    document.getElementById("logoutBtn").style.visibility = "hidden";
    document.getElementById("btnLog").style.visibility = "visible";
}

function singup(){
    $.post("/api/players", { userName: document.getElementById("userNameSU").value, password: document.getElementById("passSU").value }).done(function(){
         $("#myModal.close").click();
        alert( "Successful Register!");

    }).fail(function( jqXHR, textStatus ) {
        alert(jqXHR.responseText);
     });
}



function toMainPage(){
    window.location.href = 'http://localhost:8080/web/games.html';
}

function shoot(){

    var shoot1 = obtenerPosicion("bomb1");
    var shoot2 = obtenerPosicion("bomb2");
    var shoot3 = obtenerPosicion("bomb3");
    var shoot4 = obtenerPosicion("bomb4");
    var shoot5 = obtenerPosicion("bomb5");

    var shoots = [];

    var locationList;

    if(shoot1 != ''){
        shoots.push(shoot1);
    }
    if(shoot2 != ''){
        shoots.push(shoot2);
    }
    if(shoot3 != ''){
        shoots.push(shoot3);
    }
    if(shoot4 != ''){
        shoots.push(shoot4);
    }
    if(shoot5 != ''){
        shoots.push(shoot5);
    }

    $.post({
      url: "/api/games/players/"+getParameterByName('gp')+"/salvoes",
      data: JSON.stringify({turn: getTurn(), location: shoots}),
      dataType: "text",
      contentType: "application/json"
    })
    .done(function (response, status, jqXHR) {
      alert( "BOOM! " + response );

      loadData();
    })
    .fail(function (jqXHR, status, httpError) {
      alert(jqXHR.responseText);
    });
}


const obtenerPosicion = function (bombNumber) {

    var bomb = new Object();
    bomb["name"] = $("#" + bombNumber).attr('id');

    var loc = $("#" + bombNumber).parent("td").attr("id");

    var locEnd;

    if(loc){
        locEnd = loc.substr(0, 2);
    }else{
        locEnd = '';
    }



    return locEnd;
}

function getTurn(){
  var arr=[]
  var turn = 0;
  gamesInfo.salvoes.map(function(salvo){
    if(salvo.player == currentlyGamePlayer.id){
      arr.push(salvo.turn);
    }
  })
  turn = Math.max.apply(Math, arr);

  if (turn == -Infinity){
    return 1;
  } else {
    return turn + 1;
  }

}




