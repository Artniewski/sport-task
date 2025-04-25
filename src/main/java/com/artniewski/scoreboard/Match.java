package com.artniewski.scoreboard;

public class Match {
    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;

    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    Match updateScore(int homeTeamScore, int awayTeamScore) {
        if (homeTeamScore < 0 || awayTeamScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative");
        }
        this.homeScore = homeTeamScore;
        this.awayScore = awayTeamScore;
        return this;
    }

    boolean isInGame(String teamName) {
        return homeTeam.equalsIgnoreCase(teamName) || awayTeam.equalsIgnoreCase(teamName);
    }
}
