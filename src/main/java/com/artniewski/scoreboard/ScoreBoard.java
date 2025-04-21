package com.artniewski.scoreboard;

public class ScoreBoard {
    public Match startGame(String homeTeamName, String awayTeamName) {
        validate(homeTeamName);
        validate(awayTeamName);
        return new Match(homeTeamName, awayTeamName);
    }

    private static void validate(String homeTeamName) {
        if (homeTeamName == null || homeTeamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }
    }
}
