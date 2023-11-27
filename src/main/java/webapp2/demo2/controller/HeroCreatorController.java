package webapp2.demo2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Récupère les héros depuis l'API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Hero.class))}),
            @ApiResponse(responseCode = "400", description = "Liste non récupérée",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Liste non récupérée car elle n'existe pas",
                    content = @Content)})
    @GetMapping(value = "/heroes", produces = "application/json")
    public List<LinkedHashMap> getHeroes() {
        String url = "http://localhost:8080/heroes";
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        return response.getBody();
    }

    @Operation(summary = "Récupère un nom au hassard depuis l'API random-word-api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mot random récupéré",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Hero.class))}),
            @ApiResponse(responseCode = "400", description = "Mot non récupéré",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Mot non récupéré",
                    content = @Content)})
    @GetMapping(value = "/random-name", produces = "application/json")
    public String getRandomName() {
        ResponseEntity<Object> response = restTemplate.getForEntity("https://random-word-api.herokuapp.com/word", Object.class);
        String randomName = response.getBody().toString().replace('[', ' ').replace(']', ' ').trim(); // permet de formater le String en enlevant les signes en trop
        return randomName.substring(0, 1).toUpperCase() + randomName.substring(1); // permet de mettre la première lettre en capital
    }

    @Operation(summary = "Retourne un héros au hasard")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Héros au hasard retourné",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Hero.class))}),
            @ApiResponse(responseCode = "400", description = "Héros non récupéré",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Héros non récupéré car il n'y en a pas",
                    content = @Content)})
    @GetMapping(value = "/random-hero")
    public Object getRandomHero() {
        List heroesList = getHeroes();
        Collections.shuffle(heroesList);
        return heroesList.get(0);
    }

    public int getMaxID() {
        ArrayList<Integer> idMax = new ArrayList<>();
        for (int i = 0; i < getHeroes().size(); i++) {
            idMax.add((Integer) getHeroes().get(i).get("id"));
        }
        int key = idMax.size();
        return key;
    }

    public String getRandomClass() {
        ArrayList<String> classes = new ArrayList<>();
        classes.add("Warrior");
        classes.add("Wizard");
        Collections.shuffle(classes);
        return classes.get(0);
    }

    public int getRandomLife() {
        int rLife = 0 + (int) (Math.random() * (10 - 0) + 0);
        return rLife;
    }

    @Operation(summary = "Crée un personnage et l'ajoute dans l'app via l'API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Héros créé et ajouté",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Hero.class))}),
            @ApiResponse(responseCode = "400", description = "Héros non créé et non ajouté",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Héros non créé et non ajouté",
                    content = @Content)})
    @PostMapping("/random")
    public Object createHero() {
        Hero newHero = new Hero(getMaxID() + 1, getRandomName(), getRandomClass(), getRandomLife());
        URI response = restTemplate.postForLocation("http://localhost:8080/heroes", newHero);
        return getHeroes();
    }
}
