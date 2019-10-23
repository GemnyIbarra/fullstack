$(function() {
    loadData()
});

function updateViewGames(data) {
  var htmlList = data.map(function (game) {
      return  '<li class="list-group-item">' + new Date(game.created).toLocaleString() + ' ' + game.gamePlayers.map(function(element) { return element.player.email}).join(', ')  +'</li>';
  }).join('');
  document.getElementById("game-list").innerHTML = htmlList;
}

function updateViewLBoard(data) {
  var htmlList = data.map(function (score) {
      return  '<tr><td>' + score.userName + '</td>'
              + '<td>' + score.scores.total + '</td>'
              + '<td>' + score.scores.won + '</td>'
              + '<td>' + score.scores.lost + '</td>'
              + '<td>' + score.scores.tied + '</td></tr>';
  }).join('');
  document.getElementById("leader-list").innerHTML = htmlList;
}

function loadData() {
  $.get("/api/games")
    .done(function(data) {
      updateViewGames(data.games);
    })
    .fail(function( jqXHR, textStatus ) {
      alert( "Failed: " + textStatus );
    });

  $.get("/api/leaderBoard")
    .done(function(data) {
      updateViewLBoard(data);
    })
    .fail(function( jqXHR, textStatus ) {
      alert( "Failed: " + textStatus );
    });
}