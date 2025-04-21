package com.artniewski.scoreboard;

public class ScoreBoard {

    public Match startGame(String homeTeamName, String awayTeamName) {
        validateTeamNames(homeTeamName, awayTeamName);
        return new Match(homeTeamName, awayTeamName);
    }

    private static void validateTeamNames(String homeTeamName, String awayTeamName) {
        validate(homeTeamName);
        validate(awayTeamName);
        if (homeTeamName.trim().equalsIgnoreCase(awayTeamName.trim())) {
            throw new GameException("Home and away teams cannot be the same");
        }
    }

    private static void validate(String homeTeamName) {
        if (homeTeamName == null || homeTeamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }
    }
}
