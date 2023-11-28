package webapp2.demo2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import webapp2.demo2.model.Hero;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
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
        Object response = restTemplate.getForObject(url, Object.class);
        String randomName = response.toString().replace('[', ' ').replace(']', ' ').trim();
        return randomName.substring(0,1).toUpperCase() + randomName.substring(1);
    }

    @GetMapping(value = "/random-hero", produces = "application/json")
    public Object getRandomHero() {
        List randomList = getHeroes();
        Collections.shuffle(randomList);
        return randomList.get(0);
    }

    public int getMaxID() {
        ArrayList<Integer> idMax = new ArrayList<>();
        for (int i = 0; i < getHeroes().size(); i++) {
            idMax.add((Integer) getHeroes().get(i).get("id"));
        }
        int key = idMax.size();
        return key;
    }

    public String getRadomClass(){
        ArrayList<String> classes = new ArrayList();
        classes.add("Warrior");
        classes.add("Wizard");
        Collections.shuffle(classes);
        return classes.get(0);
    }

    public int getRandomLife(){
        return 3 + (int)(Math.random() * ((10 - 3) + 1));
    }

    @PostMapping("/random")
    public Hero createHero() {
        Hero newHero = new Hero(getMaxID() + 1, getRandomName(), getRadomClass(), getRandomLife());
        Hero response = restTemplate.postForObject("http://localhost:8080/heroes", newHero, Hero.class);
        return response;
    }
}
