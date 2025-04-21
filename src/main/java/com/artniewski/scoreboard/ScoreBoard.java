package com.artniewski.scoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artniewski.scoreboard.exception.GameException;
import com.artniewski.scoreboard.exception.GameNotFoundException;

public class ScoreBoard {

    private final Map<String, Match> matches = new HashMap<>();

    public Match startGame(String homeTeamName, String awayTeamName) {
        validateTeamNames(homeTeamName, awayTeamName);
        validateTeamsNotAlreadyPlaying(homeTeamName, awayTeamName);
        return saveMatch(homeTeamName, awayTeamName);
    }

    public Match updateScore(String homeTeamName, String awayTeamName, int homeTeamScore, int awayTeamScore) {
        String matchId = composeMatchId(homeTeamName, awayTeamName);
        Match match = matches.get(matchId);
        if (match == null) {
            throw new GameNotFoundException("Game not found");
        }
        match.updateScore(homeTeamScore, awayTeamScore);
        return match;
    }

    public void finishGame(String homeTeamName, String awayTeamName) {
        validateTeamNames(homeTeamName, awayTeamName);
        String matchId = composeMatchId(homeTeamName, awayTeamName);
        Match match = matches.remove(matchId);
        if (match == null) {
            throw new GameNotFoundException("Game not found");
        }
    }

    public List<Match> getSummary() {
        return matches.values()
                .stream()
                .toList();
    }

    private Match saveMatch(String homeTeamName, String awayTeamName) {
        String matchId = composeMatchId(homeTeamName, awayTeamName);
        Match match = new Match(homeTeamName, awayTeamName);
        matches.put(matchId, match);
        return match;
    }

    private static String composeMatchId(String homeTeamName, String awayTeamName) {
        return homeTeamName.toLowerCase() + "#" + awayTeamName.toLowerCase();
    }

    private void validateTeamsNotAlreadyPlaying(String homeTeamName, String awayTeamName) {
        matches.values()
                .stream()
                .filter(m -> m.isInGame(homeTeamName) || m.isInGame(awayTeamName))
                .findFirst()
                .ifPresent(_ -> {
                    throw new GameException("One of the teams is already playing");
                });

    }

    private static void validateTeamNames(String homeTeamName, String awayTeamName) {
        validateTeamName(homeTeamName);
        validateTeamName(awayTeamName);
        if (homeTeamName.equalsIgnoreCase(awayTeamName)) {
            throw new GameException("Home and away teams cannot be the same");
        }
    }

    private static void validateTeamName(String homeTeamName) {
        if (homeTeamName == null || homeTeamName.isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }
    }
}
