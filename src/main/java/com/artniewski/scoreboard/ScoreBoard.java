package com.artniewski.scoreboard;

public class ScoreBoard {
    public Match startGame(String homeTeam, String awayTeam) {

        return new Match(homeTeam, awayTeam);
    }
}
