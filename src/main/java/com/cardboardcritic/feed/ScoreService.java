package com.cardboardcritic.feed;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.GameRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class ScoreService {
    public static final int UNSCORED = 0;

    private final GameRepository gameRepository;

    public ScoreService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Scheduled(cron = "{cbc.score.schedule}")
    void aggregateScoresOnSchedule() {
        aggregateScores();
    }

    @Transactional
    public void aggregateScores() {
        final List<Game> games = gameRepository.listAll();

        for (Game game : games) {
            if (game.getReviews().isEmpty())
                continue;

            final List<Integer> scores = game.getReviews().stream()
                    .map(Review::getScore)
                    .filter(score -> score > UNSCORED) // I.e. only calculate average/median with actual scores
                    .sorted()
                    .toList();

            final int average = scores.isEmpty() ? UNSCORED : getAverage(scores);
            final int median = scores.isEmpty() ? UNSCORED : getMedian(scores);
            final int recommended = getRecommendedPercentage(game);

            gameRepository.update("average = ?1, median = ?2, recommended = ?3 where id = ?4",
                    average, median, recommended, game.getId());
        }
    }

    private static int getAverage(List<Integer> scores) {
        return (int) scores.stream().mapToInt(x -> x).average().orElse(0);
    }

    private static int getMedian(List<Integer> scores) {
        // If the number of reviews is even, there's no "middle" number, so we take the average of the middle 2 numbers
        return scores.size() % 2 == 0
                ? (scores.get((scores.size() / 2) - 1) + scores.get(scores.size() / 2)) / 2
                : scores.get(scores.size() / 2);
    }

    private static int getMode(List<Integer> scores) {
        // This is how we would calculate the "mode", i.e. most frequently given score. But it's too swing-y
        final Map<Integer, Integer> frequencyTable = scores.stream()
                .collect(Collectors.groupingBy(x -> x, Collectors.summingInt(x -> 1)));
        return frequencyTable.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    private static int getRecommendedPercentage(Game game) {
        final long recommends = game.getReviews().stream()
                .map(Review::isRecommended)
                .takeWhile(thumbsUp -> thumbsUp)
                .count();
        if (recommends == 0)
            return 0;

        final BigDecimal recommendCount = new BigDecimal(recommends);
        final BigDecimal reviewCount = new BigDecimal(game.getReviews().size());
        // (recommendCount / reviewCount) * 100
        return recommendCount
                .divide(reviewCount, RoundingMode.HALF_UP)
                .movePointRight(2)
                .intValue();
    }
}
