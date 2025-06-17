package com.example.fastcampusmysql.domain.post;

import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.PostFixtureFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest
public class PostBulkInsertTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    public void bulkInsert() {
        var easyRandom = PostFixtureFactory.get(
                3L,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 2, 1)
        );

        var stopWatch = new StopWatch();
        stopWatch.start();

        var posts = IntStream.range(0, 1_000_000)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Post.class))
                .toList();

        stopWatch.stop();
        System.out.println("Bulk Insert Time: " + stopWatch.getTotalTimeMillis() + " ms");

        var queryStopWatch = new StopWatch();

        queryStopWatch.start();

        postRepository.bulkInsert(posts);

        queryStopWatch.stop();
        System.out.println("Bulk Insert Query Time: " + queryStopWatch.getTotalTimeSeconds() + " seconds");
    }
}
