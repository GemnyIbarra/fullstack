package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Player {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private long id;
        private String userName;

        @OneToMany(mappedBy = "player", fetch= FetchType.EAGER)
        private List<GamePlayer> gamePlayers;

        @OneToMany(mappedBy = "player", fetch= FetchType.EAGER)
        private List<Score> scores;

        private String password;


        public Player() { }

        public Player(String userN, String pass) {

            this.userName = userN;
            this.password = pass;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userN) {
            this.userName = userN;
        }

    public long getId() {
        return id;
    }

    public Map<String, Object> PlayerToDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("userName", this.getUserName());
        return dto;
    }

    public Double getTotalScore(){
        return this.getWinScoreCounter() * 1.0D + this.getTieScoreCounter() * 0.5D;
    }

    public float getWinScoreCounter(){
        return this.getScores().stream().filter(score -> score.getScore() == 1.0D).count();
    }

    public float getTieScoreCounter(){
        return this.getScores().stream().filter(score -> score.getScore() == 0.5D).count();
    }

    public float getLoseScoreCounter(){
        return this.getScores().stream().filter(score -> score.getScore() == 0.0D).count();
    }


    public List<Score> getScores() {
        return scores;
    }

    public void setScore(List<Score> scores) {
        this.scores = scores;
    }


    public Map<String,Object>   makePlayerScoreDTO(){
        Map<String,  Object>    dto =    new LinkedHashMap<>();
        Map<String,  Object>    score =    new LinkedHashMap<>();
        dto.put("id",   this.getId());
        dto.put("userName", this.getUserName());
        dto.put("scores", score);
        score.put("total", this.getTotalScore());
        score.put("won", this.getWinScoreCounter());
        score.put("lost", this.getLoseScoreCounter());
        score.put("tied", this.getTieScoreCounter());
        return  dto;
    }

    public Optional<Score> getScore(Game game){
        Optional<Score> score = this.getScores()
                .stream()
                .filter(score1 -> score1.getGame().getId()   ==  game.getId())
                .findFirst();
        return  score;
    }

    public Map<String, Object> makeScoredto(Game game){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player", this.PlayerToDTO());
        dto.put("scores", this.getScorePoints(game));
        return dto;
    }

    public Set<Double> getScorePoints(Game game){
        return this.getScores()
                .stream()
                .filter(score1 -> score1.getGame().getId()   ==  game.getId())
                .map(score -> score.getScore())
                .collect(Collectors.toSet());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
