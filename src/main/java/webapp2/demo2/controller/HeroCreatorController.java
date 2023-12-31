package webapp2.demo2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import webapp2.demo2.model.Hero;

import java.net.URI;
import java.util.*;

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
        return randomName.substring(0, 1).toUpperCase() + randomName.substring(1);
    }

    @GetMapping(value = "/random-hero", produces = "application/json")
    public Object getRandomHero() {
        List randomList = getHeroes();
        Collections.shuffle(randomList);
        return randomList.get(0);
    }

    public String getRadomClass() {
        ArrayList<String> classes = new ArrayList();
        classes.add("Warrior");
        classes.add("Wizard");
        Collections.shuffle(classes);
        return classes.get(0);
    }

    public int getRandomLife() {
        return 3 + (int) (Math.random() * ((10 - 3) + 1));
    }

    @PostMapping("/random")
    public void createHero() {
        Hero newHero = new Hero(getRandomName(), getRadomClass(), getRandomLife());
        Hero response = restTemplate.postForObject("http://localhost:8080/heroes", newHero, Hero.class);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
