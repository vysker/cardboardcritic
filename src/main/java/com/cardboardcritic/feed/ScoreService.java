package com.cardboardcritic.feed;

import com.cardboardcritic.db.entity.Game;
import com.cardboardcritic.db.entity.Review;
import com.cardboardcritic.db.repository.GameRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ScoreService {
    private final GameRepository gameRepository;

    public ScoreService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Uni<List<Game>> aggregateScores() {
        return gameRepository.listAll()
                .onItem().transformToMulti(games -> Multi.createFrom().iterable(games))
                .onItem().transformToUniAndMerge(game -> Mutiny.fetch(game.getReviews())
                        .map(reviews -> {
                            game.setReviews(reviews);
                            return game;
                        }))
                .onItem().call(game -> {
                    if (game.getReviews().isEmpty())
                        return null;

                    final List<Integer> scores = game.getReviews().stream()
                            .map(Review::getScore)
                            .filter(score -> score > 0) // Score==0 means unscored
                            .sorted()
                            .toList();
                    final int average = (int) game.getReviews().stream()
                            .mapToInt(Review::getScore)
                            .average().orElse(0);
                    // If the numbers of reviews is even, then there is no "middle" number, so we take the average
                    // of the middle two numbers
                    final int median = scores.size() % 2 == 0
                            ? (scores.get((scores.size() / 2) - 1) + scores.get(scores.size() / 2)) / 2
                            : scores.get(scores.size() / 2);
                    final long recommends = game.getReviews().stream()
                            .map(Review::isRecommended)
                            .takeWhile(thumbsUp -> thumbsUp)
                            .count();
                    final int recommended = (int) ((float) recommends / (float) game.getReviews().size() * 100f);

                    // This is how we would calculate the "mode", i.e. most frequently given score. But it's too swing-y
//                    final Map<Integer, Integer> frequencyTable = scores.stream()
//                            .collect(Collectors.groupingBy(x -> x, Collectors.summingInt(x -> 1)));
//                    final int mode = frequencyTable.entrySet().stream()
//                            .max(Comparator.comparingInt(Map.Entry::getValue))
//                            .map(Map.Entry::getKey)
//                            .orElse(0);

                    return gameRepository.update("average = ?1, median = ?2, recommended = ?3 where id = ?4",
                            average, median, recommended, game.getId());
                })
                .collect().where(x -> false).asList(); // Just drop items to prevent memory from growing too much
    }
}
