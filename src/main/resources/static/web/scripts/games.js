var url = "http://localhost:8080/api/games";
var player = [];

fetch(url)
  .then((resp) => resp.json()) // Transform the data into json
  .then(function(data) {

        $.each(data.games, function (i, item) {

            $.each(item.gamePlayer, function (j, item2) {
                var itemP = item2.player.userName;
                if(item2.player.userName){
                    player.push(itemP);
                }
            });

            if(item.gamePlayer.length == 0){
                player.push('N/A');
                player.push('N/A');
            }

            if(item.gamePlayer.length == 1){
                 player.push('N/A');
            }

            $('#gameDetails').append($('<a>').attr("href", "http://localhost:8080/web/game.html?gp="+item.id).addClass("text-white").html('Game ID: '+ item.id + ', Date: '
            + item.created + ' Player 1: '+ player[0] + ' vs Player 2: ' + player[1]));



            player = [];
        });

    });