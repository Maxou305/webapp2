package webapp2.demo2.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Classe Hero permettant d'instancier des héros.
 */
@Schema (example = """
        {"id": int,
        "name": String,
        "type": String,
        "life": int}
        """)

public class Hero {
    private int id;
    private String name;
    private String type;
    private int life;

    public Hero() {
    }

    public Hero(String name, String type, int life){
        this.name = name;
        this.type = type;
        this.life = life;
    }
    public Hero(String name, String type, int life, int id){
        this(name, type, life);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    @Override
    public String toString() {
        return "Hero {" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", type = '" + type + '\'' +
                ", life = " + life +
                '}';
    }
}
