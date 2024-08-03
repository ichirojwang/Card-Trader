package ca.cmpt213.webserver.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokimonTest {

    private Tokimon tokimon;

    @BeforeEach
    void setUp() {
        tokimon = new Tokimon("AToki", "Fire", 10, "picture.jpg", 99);
        tokimon.setTid(1);
    }

    @AfterEach
    void tearDown() {
        tokimon = null;
    }

    @Test
    void getTid() {
        assertEquals(1, tokimon.getTid());
    }

    @Test
    void setTid() {
        tokimon.setTid(2);
        assertEquals(2, tokimon.getTid());
    }

    @Test
    void getName() {
        assertEquals("AToki", tokimon.getName());
    }

    @Test
    void setName() {
        tokimon.setName("newname");
        assertEquals("newname", tokimon.getName());
    }

    @Test
    void getType() {
        assertEquals("Fire", tokimon.getType());
    }

    @Test
    void setType() {
        tokimon.setType("Water");
        assertEquals("Water", tokimon.getType());
    }

    @Test
    void getRarity() {
        assertEquals(10, tokimon.getRarity());
    }

    @Test
    void setRarity() {
        tokimon.setRarity(5);
        assertEquals(5, tokimon.getRarity());
    }

    @Test
    void setRarityOutOfBounds() {
        tokimon.setRarity(500);
        assertEquals(1, tokimon.getRarity());
        tokimon.setRarity(-500);
        assertEquals(1, tokimon.getRarity());
    }

    @Test
    void getPictureUrl() {
        assertEquals("picture.jpg", tokimon.getPictureUrl());
    }

    @Test
    void setPictureUrl() {
        tokimon.setPictureUrl("newpicture.jpg");
        assertEquals("newpicture.jpg", tokimon.getPictureUrl());
    }

    @Test
    void getHp() {
        assertEquals(99, tokimon.getHp());
    }

    @Test
    void setHp() {
        tokimon.setHp(100);
        assertEquals(100, tokimon.getHp());
    }

    @Test
    void setHpOutOfBounds() {
        tokimon.setHp(-500);
        assertEquals(1, tokimon.getHp());
    }


}