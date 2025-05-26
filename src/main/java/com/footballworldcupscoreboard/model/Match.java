package com.footballworldcupscoreboard.model;


import java.time.Clock;
import java.time.Instant;
import java.util.Objects;


public record Match(
        HomeTeam homeTeam,
        AwayTeam awayTeam,
        int homeTeamScore,
        int awayTeamScore,
        Instant addedAt
) {
    public Match(HomeTeam homeTeam, AwayTeam awayTeam, Clock clock) {
        this(homeTeam, awayTeam, 0, 0, clock);
    }

    public Match(HomeTeam homeTeam, AwayTeam awayTeam, int homeTeamScore, int awayTeamScore, Clock clock) {
        this(homeTeam, awayTeam, homeTeamScore, awayTeamScore, Instant.now(clock));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;
        Match other = (Match) o;
        return homeTeam.equals(other.homeTeam) &&
                awayTeam.equals(other.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, awayTeam);
    }


    @Override
    public String toString() {
        return homeTeam + " " + homeTeamScore + " - " + awayTeamScore + " " + awayTeam;
    }

    public int getTotalScore() {
        return homeTeamScore + awayTeamScore;
    }
}