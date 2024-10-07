package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.OpeningHoursRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "opening_hours")
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dayOfTheWeek;
    private String opening;
    private String closing;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "opening_hours")
    private Restaurant restaurant;

    public OpeningHours() {}

    public OpeningHours(OpeningHoursRequest openingHoursRequest) {
        this.dayOfTheWeek = openingHoursRequest.dayOfTheWeek();
        this.opening = openingHoursRequest.opening();
        this.closing = openingHoursRequest.closing();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getClosing() {
        return closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
