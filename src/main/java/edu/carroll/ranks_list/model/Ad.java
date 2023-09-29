package edu.carroll.ranks_list.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;



@Entity
@Table(name = "ad")
public class Ad {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image")
    private byte[] image;


    public Ad() {
    }

    public Ad(String name, Float price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    private static final String EOL = System.lineSeparator();

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Ad :").append(getDescription()).append(EOL);
        return builder.toString();
    }
}
