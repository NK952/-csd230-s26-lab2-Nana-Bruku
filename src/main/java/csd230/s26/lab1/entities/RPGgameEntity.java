package csd230.s26.lab1.entities;

import csd230.s26.lab1.entities.VideoGameEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import java.util.Objects;
import java.util.Scanner;

@Entity
@DiscriminatorValue("RPG")
public class RPGgameEntity extends VideoGameEntity {

    private int mainStoryHours;
    private boolean hasCharacterCreation;

    // Required no-arg constructor for JPA
    public RPGgameEntity() {
        super();
    }

    public RPGgameEntity(String name, String platform, double price, int copies, boolean hasCharacterCreation) {
        super(name, platform, price, copies);
        this.hasCharacterCreation = hasCharacterCreation;
    }

    @Override
    public void sellItem() {
        System.out.println("Selling RPG Game for " + getPlatform() + " | Story Length: " + mainStoryHours + " hours | Custom Character: " + hasCharacterCreation);
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RPGgameEntity rpGgame)) return false;
        if (!super.equals(o)) return false;
        return mainStoryHours == rpGgame.mainStoryHours && hasCharacterCreation == rpGgame.hasCharacterCreation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mainStoryHours, hasCharacterCreation);
    }
}