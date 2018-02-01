package hello;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class HappyService {

    private Map<String,Integer> dirtyPersistence = new HashMap<>();

    public void reset(){
        dirtyPersistence = new HashMap<>();
    }


    public Collection<Integer> values() {
        return dirtyPersistence.values();
    }

    public Set<String> keySet() {
        return dirtyPersistence.keySet();
    }

    public void put(String name, Integer value) {
        dirtyPersistence.put(name,value);
    }
}
