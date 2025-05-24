package com.footballworldcupscoreboard.service;
import com.footballworldcupscoreboard.model.Match;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreBoard {
    private final List<Match> matchList = new ArrayList<>();
    public void startGame( Match match) {
        if (matchList.contains(match)){
            throw new IllegalArgumentException("The match already exist");
    }
    matchList.add(match);
    }

    public boolean updateGame(Match match, int newHomeTeamScore, int newAwayTeamScore){
        if(newHomeTeamScore < 0 || newAwayTeamScore < 0){
            throw new IllegalArgumentException("Score cannot be negative");
        }
        int index = matchList.indexOf(match);
            if(index == -1){
                return false;
            }
        Match storedMatch = matchList.get(index);
        storedMatch.setHomeTeamScore(newHomeTeamScore);
        storedMatch.setAwayTeamScore(newAwayTeamScore);
        return true;
    }
    public boolean deleteGameAfterGameEnding(Match match){
        return matchList.remove(match);
    }
    public List<Match> getAllMatches() {
        return new ArrayList<>(matchList);
    }

    public List<Match> getSummary() {
        return matchList.stream()
                .sorted(Comparator
                        .comparingInt(Match::getTotalScore)
                        .thenComparing(Match::getAddedAt)
                        .reversed())
                .collect(Collectors.toList());
    }

}

