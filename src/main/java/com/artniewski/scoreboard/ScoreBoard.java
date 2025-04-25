package com.artniewski.scoreboard;

import static java.util.Comparator.comparingInt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.artniewski.scoreboard.exception.GameException;
import com.artniewski.scoreboard.exception.GameNotFoundException;

public class ScoreBoard {

    private final AtomicLong creationCounter = new AtomicLong();

    private final Map<String, Match> matches = new HashMap<>();

    public Match startGame(String homeTeamName, String awayTeamName) {
        validateTeamNames(homeTeamName, awayTeamName);
        validateTeamsNotAlreadyPlaying(homeTeamName, awayTeamName);
        return saveMatch(homeTeamName, awayTeamName);
    }

    public Match updateScore(String homeTeamName, String awayTeamName, int homeTeamScore, int awayTeamScore) {
        validateTeamNames(homeTeamName, awayTeamName);
        String matchId = composeMatchId(homeTeamName, awayTeamName);
        return Optional.ofNullable(matches.get(matchId))
                .map(match -> match.updateScore(homeTeamScore, awayTeamScore))
                .orElseThrow(() -> new GameNotFoundException("Game not found"));
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
        return matches.values().stream()
                .sorted(comparingInt(Match::getTotalScore)
                        .thenComparingLong(Match::getCreationIndex)
                        .reversed())
                .collect(Collectors.toList());
    }

    private Match saveMatch(String homeTeamName, String awayTeamName) {
        String matchId = composeMatchId(homeTeamName, awayTeamName);
        long creationIndex = creationCounter.incrementAndGet();
        Match match = new Match(homeTeamName, awayTeamName, creationIndex);
        matches.put(matchId, match);
        return match;
    }

    private static String composeMatchId(String homeTeamName, String awayTeamName) {
        return homeTeamName.toLowerCase() + "#" + awayTeamName.toLowerCase();
    }

    private void validateTeamsNotAlreadyPlaying(String homeTeamName, String awayTeamName) {
        matches.values()
                .stream()
                .filter(m -> m.involves(homeTeamName) || m.involves(awayTeamName))
                .findFirst()
                .ifPresent(__ -> {
                    throw new GameException("One of the teams is already playing");
                });

    }

    private static void validateTeamNames(String homeTeamName, String awayTeamName) {
        if (homeTeamName == null || awayTeamName == null || homeTeamName.isEmpty() || awayTeamName.isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }
        if (homeTeamName.equalsIgnoreCase(awayTeamName)) {
            throw new GameException("Home and away teams cannot be the same");
        }
    }

}
