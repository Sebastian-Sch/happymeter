package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Controller
public class HappyController {

    private Map<String,Integer> dirtyPersistence = new HashMap<>();


    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @RequestMapping("/vote")
    public String vote(@RequestParam String name, Model model) {
        model.addAttribute("avg", getAverage(dirtyPersistence.values()));
        model.addAttribute("name", name);
        model.addAttribute("current",dirtyPersistence.get(name));
        return "vote";
    }

    @RequestMapping("/avg")
    public @ResponseBody Double avg() {
        return getAverage(dirtyPersistence.values());
    }

    @RequestMapping("/give")
    public @ResponseBody String give(@RequestParam String name, @RequestParam Integer value) {
        dirtyPersistence.put(name,value);
        return "ok";
    }

    @RequestMapping("/resetAll")
    public String resetAll() {
        dirtyPersistence  = new HashMap<>();
        return "ok";
    }

    @RequestMapping("/statistics")
    public String statitics() {
        return "statistics";
    }


    @RequestMapping("/statisticData")
    public @ResponseBody StatisticData statisticData() {
        Collection<Integer> votes = dirtyPersistence.values();
        Set<String> names = dirtyPersistence.keySet();

        return getStatisticData(votes, names);
    }

    private StatisticData getStatisticData(Collection<Integer> votes, Set<String> names) {
        StatisticData statistics = new StatisticData();
        statistics.voteCounts = getVoteCounts(votes);
        statistics.average = getAverage(votes);
        statistics.names = new TreeSet<>(names);
        return statistics;
    }

    private Map<Integer, Integer> getVoteCounts(Collection<Integer> votes) {
        Map<Integer,Integer> valueCount = new HashMap<>();
        for(int vote : votes){
            if(valueCount.containsKey(vote)){
                Integer sum = valueCount.get(vote) + 1;
                valueCount.put(vote,sum);
            }else{
                valueCount.put(vote,1);
            }
        }
        return valueCount;
    }

    private double getAverage(Collection<Integer> votes) {
        int valueSum = 0;
        int count = 0;
        for(int vote : votes){
            count++;
            valueSum += vote;

        }
        return count == 0 ? 0 : BigDecimal.valueOf(valueSum).divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP).doubleValue();
    }

}