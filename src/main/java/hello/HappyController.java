package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.*;

@Controller
public class HappyController {

    @Autowired
    HappyService happyRepository;

    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/vote", method = RequestMethod.GET)
    public String vote(@RequestParam(required = false) String name, Model model) {
        if(isBlank(name)){
            return "redirect:/";
        }
        model.addAttribute("avg", getAverage(happyRepository.values()));
        model.addAttribute("name", name);
        return "vote";
    }


    @RequestMapping(value = "/vote", method = RequestMethod.POST)
    public String vote(@RequestParam String name, @RequestParam Integer value,  Model model) {
        happyRepository.put(name,value);
        model.addAttribute("current",value);
        return vote(name, model);
    }

    @RequestMapping("/avg")
    public @ResponseBody Double avg() {
        return getAverage(happyRepository.values());
    }

    @RequestMapping("/resetAll")
    public String resetAll() {
        happyRepository.reset();
        return "ok";
    }

    @RequestMapping("/statistics")
    public String statitics() {
        return "statistics";
    }


    @RequestMapping("/statisticData")
    public @ResponseBody StatisticData statisticData() {
        Collection<Integer> votes = happyRepository.values();
        Set<String> names = happyRepository.keySet();

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