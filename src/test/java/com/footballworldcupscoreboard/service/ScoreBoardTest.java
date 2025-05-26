package com.footballworldcupscoreboard.service;

import com.footballworldcupscoreboard.model.AwayTeam;
import com.footballworldcupscoreboard.model.HomeTeam;
import com.footballworldcupscoreboard.model.Match;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class ScoreBoardTest {
    private ScoreBoard scoreBoard;
    private List<Match> matchList;

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoard();
        matchList = new ArrayList<>();
        matchList.add(new Match(new HomeTeam("Mexico"), new AwayTeam("Canada"), Clock.systemUTC()));
        matchList.add(new Match(new HomeTeam("Spain"), new AwayTeam("Brazil"), 0, 0, Clock.systemUTC()));
        matchList.add(new Match(new HomeTeam("Germany"), new AwayTeam("France"), 0, 0, Clock.systemUTC()));
        matchList.add(new Match(new HomeTeam("Uruguay"), new AwayTeam("Italy"), 0, 0, Clock.systemUTC()));
        matchList.add(new Match(new HomeTeam("Argentina"), new AwayTeam("Australia"), 0, 0, Clock.systemUTC()));

        for (Match match : matchList) {
            scoreBoard.startGame(match);
        }
    }

    @Test
    public void should_check_duplicates_before_start_game() {

        //given & when
        Match matchDuplicate = new Match(new HomeTeam("Mexico"), new AwayTeam("Canada"), Clock.systemUTC());

        //then
        assertThatThrownBy(() -> scoreBoard.startGame(matchDuplicate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The match already exist");

    }

    @Test
    public void should_start_game_with_initial_Score_Zero_Zero() {

        //when
        List<Match> matches = scoreBoard.getAllMatches();

        //Then
        Match firstMatch = matches.get(0);
        assertThat(matches).hasSize(5);
        assertThat(firstMatch.homeTeam().homeTeam()).isEqualTo("Mexico");
        assertThat(firstMatch.awayTeam().awayTeam()).isEqualTo("Canada");
        assertThat(firstMatch.homeTeamScore()).isEqualTo(0);
        assertThat(firstMatch.awayTeamScore()).isEqualTo(0);
    }

    @Test
    public void should_check_if_game_exist_before_updating() {

        //given & when
        Match getNonExistingMatch = new Match(new HomeTeam("Croatia"), new AwayTeam("Uruguay"), Clock.systemUTC());
        boolean updateResult = scoreBoard.updateGame(getNonExistingMatch, 2, 1);

        //Then
        assertThat(updateResult).isFalse();
    }

    @Test
    public void should_check_before_updating_if_the_match_is_correct() {

        //given & when
        Match match1 = new Match(new HomeTeam("Costa Rica"), new AwayTeam("Belgium"), Clock.systemUTC());
        Match match2 = new Match(new HomeTeam("Costa Rica"), new AwayTeam("Belgium"), Clock.systemUTC());

        //then
        assertThat(match1).isEqualTo(match2);
        assertThat(match1.hashCode()).isEqualTo(match2.hashCode());
    }

    @Test
    public void should_return_false_if_the_match_is_incorrect() {

        //given & when
        Match match1 = new Match(new HomeTeam("Costa Rica"), new AwayTeam("Belgia"), Clock.systemUTC());
        Match match2 = new Match(new HomeTeam("Costa Rica"), new AwayTeam("Croatia"), Clock.systemUTC());

        //then
        assertThat(match1).isNotEqualTo(match2);
    }

    @Test
    public void should_throw_exception_for_updating_negative_scores() {
        //when
        Match match = scoreBoard.getAllMatches().get(2);

        //then
        assertThatThrownBy(() -> scoreBoard.updateGame(match, -1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Score cannot be negative");

        assertThatThrownBy(() -> scoreBoard.updateGame(match, 0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Score cannot be negative");

    }

    @Test
    public void should_update_game() {

        //when
        Match spainBrazil = scoreBoard.getAllMatches().get(1);
        scoreBoard.updateGame(spainBrazil, 1, 0);
        Match updatedMatchSpainBrazil = scoreBoard.getAllMatches().get(1);
        //then
        assertThat(spainBrazil.homeTeam().homeTeam()).isEqualTo("Spain");
        assertThat(spainBrazil.awayTeam().awayTeam()).isEqualTo("Brazil");
        assertThat(updatedMatchSpainBrazil.homeTeamScore()).isEqualTo(1);
        assertThat(updatedMatchSpainBrazil.awayTeamScore()).isEqualTo(0);
    }

    @Test
    public void should_delete_Game_From_Live_Scoreboard_AfterGameEnding() {
        //given & when
        Match matchToRemove = scoreBoard.getAllMatches().stream()
                .filter(match -> match.homeTeam().homeTeam().equals("Mexico") && match.awayTeam().awayTeam().equals("Canada"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Match not found"));

        boolean removed = scoreBoard.deleteGameAfterGameEnding(matchToRemove);

        //then
        assertThat(removed).isTrue();
        assertThat(scoreBoard.getAllMatches()).hasSize(4);
    }

    @Test
    public void should_return_total_score_as_sum_of_both_teams_scores() {
        //given & when
        Match match = new Match(new HomeTeam("Argentina"), new AwayTeam("Brazil"), 2, 3, Clock.systemUTC());
        int totalScore = match.getTotalScore();

        //then
        assertThat(totalScore).isEqualTo(5);
    }

    @Test
    public void should_return_matches_sorted_by_total_score_and_recently_added() throws InterruptedException {
        //given & when
        Clock baseClock = Clock.fixed(Instant.parse("2024-01-01T10:00:00Z"), ZoneOffset.UTC);

        Match match1 = new Match(new HomeTeam("Team A"), new AwayTeam("Team B"), 1, 0, baseClock); // 1
        Match match2 = new Match(new HomeTeam("Team C"), new AwayTeam("Team D"), 2, 2, Clock.offset(baseClock, Duration.ofSeconds(10))); // 4
        Match match3 = new Match(new HomeTeam("Team E"), new AwayTeam("Team F"), 2, 2, Clock.offset(baseClock, Duration.ofSeconds(20))); // 4 (same score, later added)
        Match match4 = new Match(new HomeTeam("Team G"), new AwayTeam("Team H"), 3, 1, Clock.offset(baseClock, Duration.ofSeconds(30)));

        scoreBoard = new ScoreBoard(); // fresh scoreboard
        scoreBoard.startGame(match1);
        scoreBoard.startGame(match2);
        scoreBoard.startGame(match3);
        scoreBoard.startGame(match4);
        List<Match> summary = scoreBoard.getSummary();

        // then
        assertAll("Sorted matches by total score and addedAt",
                () -> assertThat(summary).hasSize(4),
                () -> assertThat(summary.get(0)).extracting(m -> m.homeTeam().homeTeam()).isEqualTo("Team G"), // highest score & most recent
                () -> assertThat(summary.get(1)).extracting(m -> m.homeTeam().homeTeam()).isEqualTo("Team E"), // same score, later added
                () -> assertThat(summary.get(2)).extracting(m -> m.homeTeam().homeTeam()).isEqualTo("Team C"),
                () -> assertThat(summary.get(3)).extracting(m -> m.homeTeam().homeTeam()).isEqualTo("Team A")
        );
    }

    @Test
    public void should_return_finished_matches_sorted_by_total_score_and_recently_added() throws InterruptedException {
        //given & when
        Clock baseClock = Clock.fixed(Instant.parse("2024-01-01T10:00:00Z"), ZoneOffset.UTC);

        Match match1 = new Match(new HomeTeam("Team A"), new AwayTeam("Team B"), 1, 0, baseClock); // 1
        Match match2 = new Match(new HomeTeam("Team C"), new AwayTeam("Team D"), 2, 2, Clock.offset(baseClock, Duration.ofSeconds(10))); // 4
        Match match3 = new Match(new HomeTeam("Team E"), new AwayTeam("Team F"), 2, 2, Clock.offset(baseClock, Duration.ofSeconds(20))); // 4 (same score, later added)
        Match match4 = new Match(new HomeTeam("Team G"), new AwayTeam("Team H"), 3, 1, Clock.offset(baseClock, Duration.ofSeconds(30)));

        scoreBoard = new ScoreBoard(); // fresh scoreboard
        scoreBoard.startGame(match1);
        scoreBoard.startGame(match2);
        scoreBoard.startGame(match3);
        scoreBoard.startGame(match4);
        scoreBoard.deleteGameAfterGameEnding(match1);
        scoreBoard.deleteGameAfterGameEnding(match2);
        scoreBoard.deleteGameAfterGameEnding(match3);
        scoreBoard.deleteGameAfterGameEnding(match4);
        List<Match> summaryOfFinishedMatches = scoreBoard.getSummaryOfFinishedMatches();

        // then
        assertAll("",
                () -> assertThat(summaryOfFinishedMatches).hasSize(4),
                () -> AssertionsForClassTypes.assertThat(summaryOfFinishedMatches.get(0)).extracting(m -> m.homeTeam().homeTeam()).isEqualTo("Team G"), // highest score & most recent
                () -> AssertionsForClassTypes.assertThat(summaryOfFinishedMatches.get(1)).extracting(m -> m.homeTeam().homeTeam()).isEqualTo("Team E"), // same score, later added
                () -> AssertionsForClassTypes.assertThat(summaryOfFinishedMatches.get(2)).extracting(m -> m.homeTeam().homeTeam()).isEqualTo("Team C"),
                () -> AssertionsForClassTypes.assertThat(summaryOfFinishedMatches.get(3)).extracting(m -> m.homeTeam().homeTeam()).isEqualTo("Team A")
        );
    }
}