package br.com.ifsp.ifome.entities;

import br.com.ifsp.ifome.dto.request.OpeningHoursRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "opening_hours")
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dayOfTheWeek;
    private String opening;
    private String closing;

    // TODO verificar se esta salvando corretamente
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "opening_hours")
    private Restaurant restaurant;

    public OpeningHours(OpeningHoursRequest openingHoursRequest) {
        this.dayOfTheWeek = openingHoursRequest.dayOfTheWeek();
        this.opening = openingHoursRequest.opening();
        this.closing = openingHoursRequest.closing();
    }
}
