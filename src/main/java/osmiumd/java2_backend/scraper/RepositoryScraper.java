package osmiumd.java2_backend.scraper;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import osmiumd.java2_backend.dto.Language;
import osmiumd.java2_backend.dto.Repository;
import osmiumd.java2_backend.dto.Topic;
import osmiumd.java2_backend.mapper.RepositoryMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Base64;

@Component
public class RepositoryScraper implements Runnable {
    final RepositoryMapper mapper;
    public boolean running;
    public String lang;
    public String timeStart;
    public int starLimit;
    public int pageStart = 1;
    public int starBound = 9999999;

    Logger logger = LoggerFactory.getLogger(RepositoryScraper.class);

    public RepositoryScraper(RepositoryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void run() {
        running = true;
        logger.info("Scrape started");
        int leastStar;
        for (int i = pageStart; i < 35; i++) {
            leastStar = scrapeRepos("https://api.github.com/search/repositories?" +
                    "q=language:" + lang + timeStart + "+stars:" + starLimit +
                    ".." + starBound +
                    "&sort=stars&page=" + i);
            logger.info("Page " + i + " complete. Stars: " + leastStar);
            if (leastStar == 0) break;
            if (leastStar == 99999) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("Scraping Complete");
        running = false;
    }

    public int scrapeRepos(String getQuery) {
        JSONObject getObj = get(getQuery);
        if (getObj == null) return 0;
        JSONArray repos = getObj.getJSONArray("items");
        if (repos == null) return 0;
        if (repos.length() == 0) return 0;
        int leastStar = 99999;
        try {
            for (Object o : repos) {
                JSONObject repo = (JSONObject) o;
                leastStar = Math.min(repo.getInt("stargazers_count"), leastStar);
                if (mapper.searchRepos(repo.getInt("id")).size() != 0)
                    continue;
                Repository repository = new Repository();
                repository.setId(repo.getInt("id"));
                repository.setName(repo.getString("name"));
                repository.setStars(repo.getInt("stargazers_count"));
                repository.setForks(repo.getInt("forks_count"));
                if (repo.get("language") == null || !repo.get("language").equals(lang)) {
                    logger.info(repo.get("language").getClass().toString());
                    logger.info("Found repository with wrong language with id " + repo.getInt("id"));
                    continue;
                }
                repository.setMain_language(repo.getString("language"));
                repository.setCreate_time(Date.valueOf(repo.getString("created_at").substring(0, 10)));
                mapper.insertRepos(repository);
                String languageURL = repo.getString("languages_url");
                JSONObject languageObj = get(languageURL);
                for (String key : languageObj.keySet()) {
                    Language lang = new Language();
                    lang.setRepos_id(repository.getId());
                    lang.setName(key);
                    lang.setCount(languageObj.getInt(key));
                    mapper.insertLang(lang);
                }
                for (Object o2 : repo.getJSONArray("topics")) {
                    Topic topic = new Topic();
                    topic.setRepos_id(repository.getId());
                    topic.setName((String) o2);
                    mapper.insertTopic(topic);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(getQuery);
            return 0;
        }
        return leastStar;
    }

    JSONObject get(String getString) {
        HttpGet get = new HttpGet(getString);
        String encoding = Base64.getEncoder().encodeToString("OsmiumD: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".getBytes(StandardCharsets.UTF_8));
        get.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        get.addHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json");
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = client.execute(get)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    return new JSONObject(EntityUtils.toString(response.getEntity()));
                } else {
                    logger.error("Get request failed with code " + response.getStatusLine().getStatusCode());
                    logger.error("Getting with " + getString);
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
