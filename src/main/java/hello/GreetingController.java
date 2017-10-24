package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Controller
public class GreetingController {

    private Map<String,Integer> dirtyPersistence = new HashMap<>();


    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @RequestMapping("/vote")
    public String vote(@RequestParam String name, Model model) {
        model.addAttribute("avg", getAvg());
        model.addAttribute("name", name);
        model.addAttribute("current",dirtyPersistence.get(name));
        return "vote";
    }

    private double getAvg() {
        return BigDecimal.valueOf(dirtyPersistence.values().stream().mapToDouble(x -> x).summaryStatistics().getAverage()).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @RequestMapping("/avg")
    public @ResponseBody Double greeting() {
        return getAvg();
    }

    @RequestMapping("/give")
    public @ResponseBody String greeting(@RequestParam String name, @RequestParam Integer value) {
        dirtyPersistence.put(name,value);
        return "ok";
    }

    @RequestMapping("/reset")
    public @ResponseBody String reset(@RequestParam String name) {
        dirtyPersistence.remove(name);
        return "ok";
    }
    @RequestMapping("/resetAll")
    public String resetAll() {
        dirtyPersistence  = new HashMap<>();
        return "ok";
    }

    @RequestMapping("/statistic")
    public String statitic(Model model) {
        return "statistic";
    }


    @RequestMapping("/statisticData")
    public @ResponseBody int[] statisticData() {
        return getValueCount();
    }

    private int[] getValueCount(){
        int[] valueCount = new int[10];
        for(int val : dirtyPersistence.values()){
            valueCount[val-1]=valueCount[val-1] + 1;
        }
        return valueCount;
    }
}