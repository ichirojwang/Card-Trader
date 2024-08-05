package ca.cmpt213.client;

public class Tokimon {

    private long tid;
    private String name;
    private String type;
    private int rarity;
    private String pictureUrl;
    private int hp;

    public Tokimon(String name, String type, int rarity, String pictureUrl, int hp) {
        this.name = name;
        this.type = type;
        this.setRarity(rarity);
        this.pictureUrl = pictureUrl;
        this.setHp(hp);
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
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

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        if (rarity >= 1 && rarity <= 10) {
            this.rarity = rarity;
        }
        else {
            this.rarity = 1;
        }

    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp > 0) {
            this.hp = hp;
        }
        else {
            this.hp = 1;
        }
    }
}
