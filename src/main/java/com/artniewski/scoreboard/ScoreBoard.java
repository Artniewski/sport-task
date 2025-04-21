package com.artniewski.scoreboard;

import java.util.HashMap;
import java.util.Map;

public class ScoreBoard {

    private final Map<String, Match> matches = new HashMap<>();

    public Match startGame(String homeTeamName, String awayTeamName) {
        validateTeamNames(homeTeamName, awayTeamName);
        Match match = new Match(homeTeamName, awayTeamName);
        matches.values()
                .stream()
                .filter(m -> m.getHomeTeam().equalsIgnoreCase(homeTeamName)
                        || m.getAwayTeam().equalsIgnoreCase(homeTeamName)
                        || m.getHomeTeam().equalsIgnoreCase(awayTeamName)
                        || m.getAwayTeam().equalsIgnoreCase(awayTeamName))
                .findFirst()
                .ifPresent(m -> {
                    throw new GameException("Cannot start a game with a team already in play");
                });
        String matchId = homeTeamName.toLowerCase() + "#" + awayTeamName.toLowerCase();
        matches.put(matchId, match);
        return match;
    }

    private static void validateTeamNames(String homeTeamName, String awayTeamName) {
        validate(homeTeamName);
        validate(awayTeamName);
        if (homeTeamName.equalsIgnoreCase(awayTeamName)) {
            throw new GameException("Home and away teams cannot be the same");
        }
    }

    private static void validate(String homeTeamName) {
        if (homeTeamName == null || homeTeamName.isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }
    }
}
