package webapp2.demo2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import webapp2.demo2.model.Hero;

@RestController
public class HeroCreatorController {
    private RestTemplate restTemplate;

    HeroCreatorController() {
        restTemplate = new RestTemplate();
    }

    @GetMapping(value = "/heroes", produces = "application/json")
    public String getHeroes() {
        String url = "http://localhost:8080/heroes";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    @GetMapping(value = "/random-name", produces = "application/json")
    public String getRandomName() {
        String url = "https://random-word-api.herokuapp.com/word";
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @GetMapping(value = "/random-hero", produces = "application/json")
    public String getRandomHero() {
        int rid = 1 + (int) (Math.random() * ((3 - 1) + 1));
        String url = "http://localhost:8080/heroes";
        String response = restTemplate.getForObject(url + "/" + rid, String.class);
        return response;
    }
@PostMapping("/random")
    public Hero createHero(){ // todo : mettre le type en query parameter, corriger le name en string
        Hero newHero = new Hero(15, getRandomName(), "wizard", 10);
        return newHero;
    }
}
