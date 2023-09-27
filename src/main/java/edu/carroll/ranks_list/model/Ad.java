package edu.carroll.ranks_list.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "ad")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ad {
    @Id
    @GeneratedValue
    private Integer id;
    @NonNull private String name;
    private Float price;
    private String description;
    @Column(name = "saved")
    private Boolean saved = Boolean.FALSE;
    private byte[] image;

    public Ad() {
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

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    private static final String EOL = System.lineSeparator();
    private static final String TAB = "\t";

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Ad #").append(getId()).append(" [").append(EOL);
        builder.append(TAB).append("Title: ").append(name).append(EOL);
        builder.append(TAB).append("Price: ").append(price).append(EOL);
        builder.append(TAB).append("Description: ").append(description).append(EOL);
        builder.append(TAB).append("Saved Status: ").append(saved).append(EOL);
        builder.append("]").append(EOL);
        return builder.toString();
    }
}
