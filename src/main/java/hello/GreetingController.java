package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GreetingController {

    Map<String,Integer> dirtyPersistence = new HashMap<>();

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="xx") String name, Model model) {
        model.addAttribute("avg", getAvg());
        model.addAttribute("name", name);
        model.addAttribute("current",dirtyPersistence.get(name));
        return "greeting";
    }

    private double getAvg() {
        return dirtyPersistence.values().stream().mapToDouble(x -> x).summaryStatistics().getAverage();
    }

    @RequestMapping("/avg")
    public @ResponseBody Double greeting(Model model) {
        return getAvg();
    }
    @RequestMapping("/give")
    public String greeting(@RequestParam String name, @RequestParam Integer value,  Model model) {
        dirtyPersistence.put(name,value);
        model.addAttribute("avg", getAvg());
        model.addAttribute("name", name);
        model.addAttribute("current",value);
        return "greeting";
    }

    @RequestMapping("/reset")
    public String reset(@RequestParam String name,  Model model) {
        dirtyPersistence.remove(name);
        model.addAttribute("avg", getAvg());
        model.addAttribute("name", name);
        model.addAttribute("current",0);
        return "greeting";
    }
    @RequestMapping("/resetAll")
    public String resetAll( Model model) {
        dirtyPersistence  = new HashMap<>();
        return "ok";
    }
}