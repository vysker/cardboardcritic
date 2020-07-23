import groovy.transform.ToString

import java.time.LocalDateTime

@ToString(includeNames = true)
class Review {
    String url, title, author
    LocalDateTime date
    List<String> paragraphs // usually the full review content, but not always the case, i.e. when review is paginated
    List<String> suggestedSummaries
}
