$(function() {
    loadData();
});

$(function() {
    $.get('/api/games')
            .done(function(data) {
                if(data.player.id){
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
        $.post("/api/login", { userName: document.getElementById("userN").value, password: document.getElementById("pass").value }).done(function(data){
            $.get('/api/games')
                            .done(function(data) {
                                //if(data.player != "Guest"){
                                    $.get('/api/game_view/1')
                                            .done(function(data) {
                                                loadTables(data);
                                            })
                                            .fail(function( jqXHR, textStatus ) {
                                              //alert( "Failed: " + textStatus );
                                            });
                                //}
                            })
                            .fail(function( jqXHR, textStatus ) {
                              //alert( "Failed: " + textStatus );
                            });

                    document.getElementById("userLogged").innerHTML = "Welcome "+document.getElementById("userN").value;
                    document.getElementById("logoutBtn").style.visibility = "visible";
                    document.getElementById("btnLog").style.visibility = "hidden";

        }).fail(function( jqXHR, textStatus ) {
                                        alert( "Wrong credentials");
                                      });

};

function logout(){
    $.post("/api/logout").done(function() { console.log("logged out"); });
    window.location.reload();
    document.getElementById("logoutBtn").style.visibility = "hidden";
    document.getElementById("btnLog").style.visibility = "visible";
}

function singup(){
    $.post("/api/players", { userName: document.getElementById("userNameSU").value, password: document.getElementById("passSU").value }).done(function(){
         $("#myModal .close").click();
        alert( "Successful Register!");

    }).fail(function( jqXHR, textStatus ) {
        alert(jqXHR.responseText);
     });
}


function putShip(){
  $.post({
  url: "/api/games/players/14/ships",
  data: JSON.stringify([{type: "Battleship", location: ["H2", "H3", "H4"]}]),
  dataType: "text",
  contentType: "application/json"
})
.done(function (response, status, jqXHR) {
  alert( "Ship added: " + response );
})
.fail(function (jqXHR, status, httpError) {
  alert("Failed to add ship: " + textStatus + " " + httpError);
});
}




function toMainPage(){
    window.location.href = 'http://localhost:8080/web/games.html';
}


//qué es el game/view/1?




