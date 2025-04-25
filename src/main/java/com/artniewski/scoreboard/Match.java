package com.artniewski.scoreboard;

public class Match {
    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private final long creationIndex;

    public Match(String homeTeam, String awayTeam, long creationIndex) {
        this.creationIndex = creationIndex;
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

    boolean involves(String teamName) {
        return homeTeam.equalsIgnoreCase(teamName) || awayTeam.equalsIgnoreCase(teamName);
    }

    int getTotalScore() {
        return homeScore + awayScore;
    }

    long getCreationIndex() {
        return creationIndex;
    }

    @Override
    public String toString() {
        return String.format("%s %d - %s %d",
                homeTeam, homeScore, awayTeam, awayScore);
    }
}
