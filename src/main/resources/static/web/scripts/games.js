var url = "http://localhost:8080/api/games";
var player = [];

fetch(url)
  .then((resp) => resp.json()) // Transform the data into json
  .then(function(data) {

        $.each(data, function (i, item) {

            $.each(item.gamePlayer, function (j, item2) {
                var itemP = item2.player.email;
                if(item2.player.email){
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

            $('#gameDetails').append($('<li>').html('Game ID: '+ item.id + ', Date: '
            + item.created + ' Player 1: '+ player[0] + ' vs Player 2: ' + player[1]));
            player = [];
        });

    });