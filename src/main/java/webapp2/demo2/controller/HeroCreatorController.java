package webapp2.demo2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import webapp2.demo2.model.Hero;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class HeroCreatorController {
    private RestTemplate restTemplate;

    HeroCreatorController() {
        restTemplate = new RestTemplate();
    }

    @GetMapping(value = "/heroes", produces = "application/json")
    public List<LinkedHashMap> getHeroes() {
        String url = "http://localhost:8080/heroes";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }

    @GetMapping(value = "/random-name", produces = "application/json")
    public String getRandomName() {
        String url = "https://random-word-api.herokuapp.com/word";
        String response = restTemplate.getForObject(url, String.class);
        return response.toString().replace('[', ' ').replace(']', ' ').trim();
    }

    @GetMapping(value = "/random-hero", produces = "application/json")
    public LinkedHashMap getRandomHero() {
        int rid = 0 + (int) (Math.random() * ((3 - 0) + 0));
        return getHeroes().get(rid);
    }

    public int getMaxID() {
        ArrayList<Integer> idMax = new ArrayList<>();
        for (int i = 0; i < getHeroes().size(); i++) {
            idMax.add((Integer) getHeroes().get(i).get("id"));
        }
        int key = idMax.size();
        return key;
    }

    @PostMapping("/random")
    public Object createHero() {
        Hero newHero = new Hero(getMaxID() + 1, getRandomName(), "wizard", 10);
        String url = "http://localhost:8080/heroes";
        Hero response = restTemplate.postForObject(url, newHero, Hero.class);
        return response;
    }
}
