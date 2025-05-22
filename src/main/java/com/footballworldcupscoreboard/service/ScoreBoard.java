package com.footballworldcupscoreboard.service;
import com.footballworldcupscoreboard.model.Match;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {
    private final List<Match> matchList = new ArrayList<>();
    public void startGame( Match match){
        matchList.add(match);
    }

    public List<Match> getAllMatches() {
        return new ArrayList<>(matchList);
    }
}
