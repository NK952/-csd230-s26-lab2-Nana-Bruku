package csd230.s26.lab1.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import java.util.Objects;
import java.util.Scanner;

@Entity
@DiscriminatorValue("VIDEOGAME")
public abstract class VideoGameEntity extends ProductEntity {

    private String name;
    private String platform;

    protected VideoGameEntity() {
        super();
    }

    public VideoGameEntity(String name, String platform, double price, int copies){
        super();
        this.name = name;
        this.platform = platform;
    }

    public String getPlatform(){ return platform; }
    public void setPlatform(String platform){ this.platform = platform; }
    public String getName(){ return name; }

    @Override
    public String toString(){
        return "Video Game{" +
                "platform='" + platform + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VideoGameEntity game)) return false;
        if (!super.equals(o)) return false;

        return Objects.equals(name, game.name)
                && Objects.equals(platform, game.platform);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, platform);
    }

}