package com.footballworldcupscoreboard.service;

import com.footballworldcupscoreboard.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


class ScoreBoardTest {
    private ScoreBoard scoreBoard;
    private List<Match> matchList;

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoard();
        matchList = new ArrayList<>();
        matchList.add(new Match("Mexico", "Canada"));
        matchList.add(new Match("Spain", "Brazil", 0, 0));
        matchList.add(new Match("Germany", "France", 0, 0));
        matchList.add(new Match("Uruguay", "Italy", 0, 0));
        matchList.add(new Match("Argentina", "Australia", 0, 0));

        for (Match match : matchList) {
            scoreBoard.startGame(match);
        }
    }

    @Test
    public void should_start_game_with_initial_Score_Zero_Zero() {

        //given as Before each

        //when
        List<Match> matches = scoreBoard.getAllMatches();

        //Then
        Match firstMatch = matches.get(0);
        assertThat(matchList).hasSize(5);
        assertThat(firstMatch.getHomeTeam()).isEqualTo("Mexico");
        assertThat(firstMatch.getAwayTeam()).isEqualTo("Canada");
        assertThat(firstMatch.getHomeTeamScore()).isEqualTo(0);
        assertThat(firstMatch.getAwayTeamScore()).isEqualTo(0);
    }
@Test
    public void should_check_if_game_exist_before_updating(){

        //given
        Match getNonExistingMatch = new Match("Croatia", "Urugway");

        //when
        boolean updateResult = scoreBoard.updateGame(getNonExistingMatch,2,1);

        //Then
        assertThat(updateResult).isFalse();
}
@Test
    public void should_check_before_updating_if_the_match_is_correct(){

        //given
        Match match1 = new Match("Costarica", "Belgia");
        Match match2 = new Match("Costarica", "Belgia");

        //then
        assertThat(match1).isEqualTo(match2);
        assertThat(match1.hashCode()).isEqualTo(match2.hashCode());
}
@Test
    public void should_return_false_if_the_match_is_incorrect(){

        //given
        Match match1 = new Match("Costarica", "Belgia");
        Match match2 = new Match("Costarica", "Croatia");

        //then
        assertThat(match1).isNotEqualTo(match2);
}

@Test
    public void should_throw_exception_for_updating_negative_scores(){
        //given as Before each
        Match match = scoreBoard.getAllMatches().get(2);

    // when / then
    assertThatThrownBy(() -> scoreBoard.updateGame(match, -1, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Score cannot be negative");

    assertThatThrownBy(() -> scoreBoard.updateGame(match, 0, -1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Score cannot be negative");

}
@Test
    public void should_update_game(){

        //given as Before each

        //when
        Match spainBrazil = scoreBoard.getAllMatches().get(1);
        scoreBoard.updateGame(spainBrazil, 1,0);
        Match updatedMatchSpainBrazil = scoreBoard.getAllMatches().get(1);
        //then
        assertThat(spainBrazil.getHomeTeam()).isEqualTo("Spain");
        assertThat(spainBrazil.getAwayTeam()).isEqualTo("Brazil");
        assertThat(updatedMatchSpainBrazil.getHomeTeamScore()).isEqualTo(1);
        assertThat(updatedMatchSpainBrazil.getAwayTeamScore()).isEqualTo(0);
}
}
