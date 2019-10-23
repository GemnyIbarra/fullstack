package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Score;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sun.text.resources.CollationData;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/games")
    public Map<String, Object> getAll(Authentication authentication) {
        Map<String, Object> dto =  new HashMap<>();

        if(isGuest(authentication)){
            dto.put("player","Guest");
        }else{
            Player  player = playerRepository.findByUserName(authentication.getName());
            dto.put("player",   player.PlayerToDTO());
        }

        dto.put("games", gameRepository.findAll().stream().map(Game -> GameToDTO(Game)).collect(Collectors.toList()));
        return dto;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    /*public List<Map<String, Object>> getGames() {
        return gameRepository.findAll().stream().map(Game -> GameToDTO(Game)).collect(Collectors.toList());
    }*/

    @RequestMapping(path= "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame (Authentication authentication){
        if(isGuest(authentication)){
            return new ResponseEntity<>(createMap("error","You must login!"), HttpStatus.FORBIDDEN);
        }else{
            Game game = new Game(new Date());
            gameRepository.save(game);

            Player ply = playerRepository.findByUserName(authentication.getName());

            GamePlayer gPlayer = new GamePlayer(game, ply);
            gamePlayerRepository.save(gPlayer);

            return new ResponseEntity<>(createMap("gpid", gPlayer.getId()), HttpStatus.CREATED);

        }

    }

    @RequestMapping(path= "/games/{id}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame (Authentication authentication,
                                                           @PathVariable Long id){
        if(isGuest(authentication)){
            return new ResponseEntity<>(createMap("error","You must login!"), HttpStatus.UNAUTHORIZED);
        }

        Game gamecito = gameRepository.findById(id).orElse(null);

        if(gamecito == null){
            return new ResponseEntity<>(createMap("error","Game doesn't exist"), HttpStatus.FORBIDDEN);
        }

        if(gamecito.getGamePlayers().size() >= 2){
            return new ResponseEntity<>(createMap("error","Game is full"), HttpStatus.FORBIDDEN);
        }

        Player ply = playerRepository.findByUserName(authentication.getName());

        if(gamecito.getGamePlayers().stream().map(
                gamePlayer -> gamePlayer.getPlayer().getUserName()).collect(Collectors.toList()).contains(ply.getUserName())){
            return new ResponseEntity<>(createMap("error","You're already in"), HttpStatus.FORBIDDEN);
        }

        GamePlayer gPlayer = new GamePlayer(gamecito, ply);
        gamePlayerRepository.save(gPlayer);

        return new ResponseEntity<>(createMap("gpid", gPlayer.getId()), HttpStatus.CREATED);
    }


    public Map<String, Object> createMap (String str, Object obj){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(str, obj);
        return map;
    }


    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String userName, @RequestParam String password) {

        if (userName.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(userName) != null) {
            return new ResponseEntity<>("UserName already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/leaderBoard")
    public  List<Map<String,Object>> getLeaderBoard(){
        return  playerRepository.findAll()
                .stream()
                .map(player  ->  player.makePlayerScoreDTO())
                .collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> getGameViewByGamePlayerId(@PathVariable Long nn) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(nn).get();

        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getDateGame());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(gamePlayer1 -> gamePlayer1.GamePlayerToDTO())
                .collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips()
                .stream()
                .map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList()));
        dto.put("salvoes", gamePlayer.getSalvoes()
               .stream()
                .map(salvo -> salvo.makeSalvoDTO())
                .collect(Collectors.toList()));
        return dto;
    }

    @JsonIgnore
    public Map<String, Object> GameToDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        Map<String,  Object> score = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getDateGame());
        dto.put("gamePlayer", getGamePlayerList(game.getGamePlayers()));

        if (isFinished(game.getScore())){
            dto.put("scores", game.getGamePlayers().stream().map(gp -> gp.getPlayer().makeScoredto(game)));
        }
        return dto;
    }

    public boolean isFinished(List<Score> sco){
        boolean flag = false;

        for(int i = 0; i < sco.size(); i++){

            if (sco.get(i).getFinishDate().equals("")){
                flag = false;
            }else{
                flag = true;
            }
        }
        return flag;
    }


    public List<Map<String, Object>> getGamePlayerList(List<GamePlayer> gamePlayers) {

        return gamePlayers.stream().map(GamePlayer -> GamePlayer.GamePlayerToDTO()).collect(Collectors.toList());

    }

    public List<Map<String, Object>> getScoreList(List<Score> score) {

        return score.stream().map(Score -> Score.makeScoreDTO()).collect(Collectors.toList());

    }

}
