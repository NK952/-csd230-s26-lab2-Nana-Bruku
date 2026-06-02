package csd230.s26.lab1.entities;

import csd230.s26.lab1.entities.VideoGameEntity;
import csd230.s26.lab1.pojos.ActionGame;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import java.util.Objects;
import java.util.Scanner;

@Entity
@DiscriminatorValue("ACTION")
public class ActionGameEntity extends VideoGameEntity {

    private boolean isMultiplayer;
    private String genreClassification;

    public ActionGameEntity() {
        super();
    }

    public ActionGameEntity(String name, String platform, double price, int copies, String genreClassification) {
        super(name, platform, price, copies);
        this.genreClassification = genreClassification;
    }

    @Override
    public void sellItem() {
        System.out.println("Selling Action Game for " + getPlatform() + " | Multiplayer: " + isMultiplayer + " | Sub-genre: " + genreClassification);
    }

    @Override
    public double getPrice() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ActionGameEntity game)) return false;
        if (!super.equals(o)) return false;
        return isMultiplayer == game.isMultiplayer && Objects.equals(genreClassification, game.genreClassification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isMultiplayer, genreClassification);
    }
}