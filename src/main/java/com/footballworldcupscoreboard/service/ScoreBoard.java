package com.footballworldcupscoreboard.service;

import com.footballworldcupscoreboard.model.Match;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ScoreBoard {
    private final List<Match> FinishedMatches = new ArrayList<>();
    private List<Match> matchList = new ArrayList<>();

    public void startGame(Match match) {
        if (matchList.contains(match)) {
            throw new IllegalArgumentException("The match already exist");
        }
        matchList.add(match);
    }

    public boolean updateGame(Match match, int newHomeTeamScore, int newAwayTeamScore) {
        if (newHomeTeamScore < 0 || newAwayTeamScore < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        AtomicBoolean updated = new AtomicBoolean(false);
        matchList = matchList.stream()
                .map(m -> {
                    if (m.equals(match)) {
                        updated.set(true);
                        return new Match(m.homeTeam(), m.awayTeam(), newHomeTeamScore, newAwayTeamScore, m.addedAt());
                    } else {
                        return m;
                    }
                })
                .collect(Collectors.toList());

        return updated.get();
    }

    public boolean deleteGameAfterGameEnding(Match match) {
        boolean finishedmatch = matchList.remove(match);
        if (finishedmatch) {
            FinishedMatches.add(match);
        }
        return finishedmatch;
    }

    public List<Match> getAllMatches() {
        return new ArrayList<>(matchList);
    }


    public List<Match> getSummary() {
        return matchList.stream()
                .sorted(Comparator
                        .comparingInt(Match::getTotalScore)
                        .thenComparing(Match::addedAt)
                        .reversed())
                .collect(Collectors.toList());
    }

    public List<Match> getSummaryOfFinishedMatches() {
        return FinishedMatches.stream()
                .sorted(Comparator
                        .comparingInt(Match::getTotalScore)
                        .thenComparing(Match::addedAt)
                        .reversed())
                .collect(Collectors.toList());
    }
}

