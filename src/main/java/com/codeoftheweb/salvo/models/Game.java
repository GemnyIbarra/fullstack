package com.codeoftheweb.salvo.models;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Date dateGame;

    @OneToMany(mappedBy = "game", fetch= FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch= FetchType.EAGER)
    private List<Score> score;

    public Game() {
        this.dateGame = new Date();
    }

    public Game(Date dateG) {
        this.dateGame = dateG;
    }

    public long getId() {
        return id;
    }

    public Date getDateGame() {
        return dateGame;
    }

    public void setDateGame(Date dateGame) {
        this.dateGame = dateGame;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public List<Score> getScore() {
        return score;
    }

    public void setScore(List<Score> score) {
        this.score = score;
    }



}
