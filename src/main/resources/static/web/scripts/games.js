var url = "http://localhost:8080/api/games";
var player = [];
var flagAvailable;
var flagSamePlayer = false;

$(function() {
    loadDataGames();
});

    function loadDataGames(){
        $('#gameDetails').empty();

        fetch(url)
          .then((resp) => resp.json()) // Transform the data into json
          .then(function(data) {

                $.each(data.games, function (i, item) {

                    $.each(item.gamePlayer, function (j, item2) {
                        var itemP = item2.player.userName;
                        if(item2.player.userName){
                            player.push(itemP);
                        }

                        if(item2.player.userName == data.player.userName){
                            flagSamePlayer = true;
                        }
                    });


                    if(item.gamePlayer.length == 0){
                        player.push('N/A');
                        player.push('N/A');
                    }

                    if(item.gamePlayer.length == 1){
                        flagAvailable = true;
                        player.push('N/A');
                    }

                    $('#gameDetails').append($('<a>').attr("href", "http://localhost:8080/web/game.html?gp="+item.id).addClass("text-white").html('Game ID: '+ item.id + ', Date: '
                    + item.created + ' Player 1: '+ player[0] + ' vs Player 2: ' + player[1]));

                    if(flagAvailable == true && data.player != 'Guest' && flagSamePlayer == false ){
                        $('#gameDetails').append($('<button>').attr("type", "button").attr("id", "btngp"+item.id)
                        .attr("onclick", "joinGame(this)")
                        .addClass("btn btn-outline-success").html("Join Game"));

                    }

                    if(data.player != 'Guest'){
                        document.getElementById("cgbtn").style.visibility = "visible";
                    }

                    flagSamePlayer = false;
                    flagAvailable = false;
                    player = [];
                });

            });

    };


    function createGame(){

        $.post("/api/games").done(function(data) {

            loadDataGames();

            alert( "Game Created!");

        }).fail(function( jqXHR, textStatus ) {
                  alert(jqXHR.responseText);
               });
    }

    function joinGame(event){

        var gamePlayerId = event.id.substring(5);

        console.log(gamePlayerId);

        $.post('/api/games/'+gamePlayerId+'/players').done(function(data){

            loadDataGames();

            alert( "You're in!");

        }).fail(function( jqXHR, textStatus ) {
                           alert(jqXHR.responseText);
                        });

    }

    function loginG(){
            closeForm();
            $.post("/api/login", { userName: document.getElementById("userN").value, password: document.getElementById("pass").value }).done(function(data){
                loadDataGames();

                        document.getElementById("userLogged").innerHTML = "Welcome "+document.getElementById("userN").value;
                        document.getElementById("logoutBtn").style.visibility = "visible";
                        document.getElementById("btnLog").style.visibility = "hidden";
                        document.getElementById("cgbtn").style.visibility = "visible";

            }).fail(function( jqXHR, textStatus ) {
                                            alert( "Wrong credentials");
                                          });

    };