package com.footballworldcupscoreboard.service;

import com.footballworldcupscoreboard.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


class ScoreBoardTest {
private ScoreBoard scoreBoard;
@BeforeEach
    void setUp() {
    scoreBoard = new ScoreBoard();
}
@Test
    public void should_start_game_with_initial_Score_Zero_Zero() {

    //given
    List<Match> matchList = List.of(
            new Match("Mexico", "Canada"),
            new Match("Spain", "Brazil", 0, 0),
            new Match("Germany", "France", 0, 0),
            new Match("Uruguay", "Italy", 0, 0),
            new Match("Argentina", "Australia", 0, 0)
    );
    for(Match match: matchList)
        scoreBoard.startGame(match);

    //when
    List<Match>matches =scoreBoard.getAllMatches();

    //Then
    Match firstMatch = matches.getFirst();
    assertThat(matchList).hasSize(5);
    assertThat(firstMatch.getHomeTeam()).isEqualTo("Mexico");
    assertThat(firstMatch.getAwayTeam()).isEqualTo("Canada");
    assertThat(firstMatch.getHomeTeamScore()).isEqualTo(0);
    assertThat(firstMatch.getAwayTeamScore()).isEqualTo(0);
    }
}
