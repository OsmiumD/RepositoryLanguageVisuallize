package osmiumd.java2_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import osmiumd.java2_backend.dto.LanguageSum;
import osmiumd.java2_backend.dto.Repository;
import osmiumd.java2_backend.mapper.RepositoryMapper;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/repos")
public class FrontendController {

    private final RepositoryMapper rMapper;

    public FrontendController(RepositoryMapper rMapper) {
        this.rMapper = rMapper;
    }


    @GetMapping("/summary")
    public Map<String, Integer> getSummary() {
        Map<String, Integer> result = new HashMap<>();
        result.put("total", rMapper.allCount());
        result.put("java", rMapper.languageCount("Java"));
        result.put("kotlin", rMapper.languageCount("Kotlin"));
        result.put("scala", rMapper.languageCount("Scala"));
        result.put("groovy", rMapper.languageCount("Groovy"));
        return result;
    }

    @GetMapping("/language")
    public List<LanguageSum> langSum(@RequestParam String language, Integer aggr, Integer starLimit, Integer topLimit) {
        List<LanguageSum> dbResult = rMapper.languageSum(language, aggr, starLimit);
        List<LanguageSum> result = new ArrayList<>();
        int cnt = 0;
        long sum = 0, sumAll = 0;
        for (LanguageSum item : dbResult) {
            sumAll += item.getSum();
            cnt++;
            if (cnt <= topLimit) {
                LanguageSum addItem = new LanguageSum();
                addItem.setLanguage(item.getLanguage());
                addItem.setSum(item.getSum());
                result.add(addItem);
                sum += item.getSum();
            }
        }
        LanguageSum other = new LanguageSum();
        other.setLanguage("Other");
        other.setSum(sumAll - sum);
        result.add(other);
        return result;
    }

    @GetMapping("/topic")
    public List<LanguageSum> langTopic(@RequestParam String language, Integer aggr, Integer starLimit, Integer topLimit) {
        List<LanguageSum> dbResult = rMapper.languageTopic(language, aggr, starLimit);
        List<LanguageSum> result = new ArrayList<>();
        int cnt = 0;
        for (LanguageSum item : dbResult) {
            cnt++;
            if (cnt <= topLimit) {
                LanguageSum addItem = new LanguageSum();
                addItem.setLanguage(item.getLanguage());
                addItem.setSum(item.getSum());
                result.add(addItem);
            }
        }
        return result;
    }

    @GetMapping("/timeline")
    public Map<String, Object> timeline(@RequestParam Long timeStart, Long timeEnd, Integer starLimit) {
        timeStart = Math.max(timeStart, 1514736000000L);
        timeEnd = Math.min(timeEnd, 1651334400000L);
        Map<String, Object> result = new HashMap<>();
        List<List<Integer>> timeline = new ArrayList<>();
        List<String> times = new ArrayList<>();
        List<Integer> Java = new ArrayList<>();
        List<Integer> Kotlin = new ArrayList<>();
        List<Integer> Scala = new ArrayList<>();
        List<Integer> Groovy = new ArrayList<>();
        Date start = new Date(timeStart);
        Calendar end = Calendar.getInstance();
        end.setTime(new Date(timeEnd));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Calendar cal2 = Calendar.getInstance();
        end.set(Calendar.DAY_OF_MONTH, 2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while (calendar.before(end)) {
            String qStart = sdf.format(calendar.getTime());
            cal2.setTime(calendar.getTime());
            cal2.add(Calendar.MONTH, 1);
            cal2.add(Calendar.DAY_OF_MONTH, -1);
            List<LanguageSum> dbRes = rMapper.reposCreate(calendar.getTime(), cal2.getTime(), starLimit);
            Groovy.add(findResult(dbRes, "Groovy"));
            Java.add(findResult(dbRes, "Java"));
            Kotlin.add(findResult(dbRes, "Kotlin"));
            Scala.add(findResult(dbRes, "Scala"));
            times.add(qStart.substring(0, 7));
            calendar.add(Calendar.MONTH, 1);
        }
        timeline.add(Java);
        timeline.add(Kotlin);
        timeline.add(Scala);
        timeline.add(Groovy);
        result.put("times", times);
        result.put("data", timeline);
        return result;
    }

    int findResult(List<LanguageSum> list, String lang) {
        for (LanguageSum item : list) {
            if (lang.equals(item.getLanguage()))
                return item.getSum().intValue();
        }
        return 0;
    }

    @GetMapping("/page")
    Map<String, Object> getPage(@RequestParam Integer page, Integer pageSize) {
        List<Repository> repos = rMapper.getPage(pageSize, (page - 1) * pageSize);
        Map<String, Object> res = new HashMap<>();
        res.put("total", rMapper.allCount());
        res.put("data", repos);
        return res;
    }

    @GetMapping("/search")
    Map<String, Object> searchPage(@RequestParam Integer page, Integer pageSize, Long timeStart, Long timeEnd, String language, String search) {
        Date start = new Date(timeStart);
        Date end = new Date(timeEnd);
        if ("All".equals(language)) language = null;
        search = "%" + search.toLowerCase(Locale.ROOT) + "%";
        List<Repository> repos = rMapper.searchPage(pageSize, (page - 1) * pageSize, start, end, language, search);
        Map<String, Object> res = new HashMap<>();
        res.put("total", rMapper.searchPageCount(pageSize, (page - 1) * pageSize, start, end, language, search));
        res.put("data", repos);
        return res;
    }
}
