package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @OneToMany(mappedBy = "gamePlayer", fetch= FetchType.EAGER)
    private List<Ship> ships;

    @OneToMany(mappedBy = "gamePlayer")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Salvo> salvoes;

    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player) {
        this.joinDate = new Date();
        this.game = game;
        this.player = player;
    }

    public long getId() {
        return id;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public Map<String, Object> GamePlayerToDTO() {
        Map<String, Object> dto = new LinkedHashMap();

        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().PlayerToDTO());

        return dto;
    }

    public List<Salvo> getSalvoes() {
        return salvoes;
    }

    @JsonIgnore
    public Map<String,Object> getScores() {
        Map<String,  Object> dto = player.makePlayerScoreDTO();
        return dto;
    }

}
