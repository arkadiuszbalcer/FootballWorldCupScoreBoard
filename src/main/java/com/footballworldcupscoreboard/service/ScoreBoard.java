package com.footballworldcupscoreboard.service;
import com.footballworldcupscoreboard.model.Match;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {
    private final List<Match> matchList = new ArrayList<>();
    public void startGame( Match match){
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

        match.setHomeTeamScore(newHomeTeamScore);
        match.setAwayTeamScore(newAwayTeamScore);
        return true;
    }
    public List<Match> getAllMatches() {
        return new ArrayList<>(matchList);
    }
}
