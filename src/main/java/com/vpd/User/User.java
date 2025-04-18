package com.vpd.User;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vpd.Friendship.Friendship;
import com.vpd.Travel.Travel;
import com.vpd.UserMovie.UserMovie;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "requester")
    private List<Friendship> sentFriendships;

    @JsonIgnore
    @OneToMany(mappedBy = "receiver")
    private List<Friendship> receivedFriendships;

    @OneToMany(mappedBy = "user")
    private List<UserMovie> userMovies;

    @ManyToMany
    @JoinTable(
            name = "user_travel",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "travel_id"))
    private Set<Travel> travels;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
