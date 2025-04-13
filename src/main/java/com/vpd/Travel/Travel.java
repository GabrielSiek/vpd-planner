package com.vpd.Travel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vpd.Collection.Collection;
import com.vpd.Movie.Movie;
import com.vpd.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "travels")
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @OneToMany(mappedBy = "travel", fetch = FetchType.LAZY)
    private List<Collection> collections;

    @OneToMany(mappedBy = "travel", fetch = FetchType.LAZY)
    private List<Movie> movies;

    @ManyToMany(mappedBy = "travels")
    @JsonIgnore
    private Set<User> users;

    private LocalDate startDate;

    private LocalDate endDate;



    //ainda precisa fazer a lógica de planejamento de viagem, roteiros, hotel, data etc.
}
