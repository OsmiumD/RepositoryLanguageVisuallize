package osmiumd.java2_backend.controller;

import org.springframework.web.bind.annotation.*;
import osmiumd.java2_backend.dto.Repository;
import osmiumd.java2_backend.mapper.RepositoryMapper;
import osmiumd.java2_backend.scraper.RepositoryScraper;

import java.util.List;

@RestController
public class TestController {

    private final RepositoryMapper rMapper;

    private final RepositoryScraper scraper;

    public TestController(RepositoryMapper rMapper, RepositoryScraper scraper) {
        this.rMapper = rMapper;
        this.scraper = scraper;
    }

    @GetMapping("/")
    public String test() {
        return "ok2";
    }

    @GetMapping("/all")
    public List<Repository> selectTest() {
        return rMapper.allRepos();
    }

    @GetMapping("/repos/{id}")
    public List<Repository> selectTest2(@PathVariable("id") int id) {
        return rMapper.searchRepos(id);
    }

    @GetMapping("/scrape/{language}/{starLimit}")
    public String scrapeMethod(@PathVariable("language") String lang, @PathVariable int starLimit) {
        if (scraper.running)
            return "Other task is running";
        if (lang == null || lang.length() == 0)
            return "Please specify language";
        scraper.timeStart = "";
        scraper.lang = lang;
        scraper.starLimit = starLimit;
        new Thread(scraper).start();
        return "done";
    }

    @PostMapping("/scrape")
    public String scrapePostDate(@RequestParam String lang, @RequestParam String date, @RequestParam Integer sMin,
                                 @RequestParam Integer sMax, @RequestParam Integer p) {
        if (scraper.running)
            return "Other task is running";
        if (lang == null || lang.length() == 0)
            return "Please specify language";
        if (date == null || date.length() == 0)
            scraper.timeStart = "";
        else if (!date.matches("\\d{4}-\\d{2}"))
            return "Date format not match";
        else
            scraper.timeStart = "+created:%3E=" + date;
        scraper.lang = lang;
        scraper.starLimit = sMin;
        scraper.starBound = sMax;
        scraper.pageStart = p;
        new Thread(scraper).start();
        return "done";
    }

    @PostMapping("/setPage")
    public String updatePageStart(@RequestParam("n") int n) {
        if (scraper.running)
            return "Other task is running";
        scraper.pageStart = n;
        return "done";
    }

    @PostMapping("/setStarBound")
    public String updateStarBound(@RequestParam("n") int n) {
        if (scraper.running)
            return "Other task is running";
        scraper.starBound = n;
        return "done";
    }
}
