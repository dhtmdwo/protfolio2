package com.example.appapi.users.model;

import com.example.appapi.likes.model.Likes;
import com.example.appapi.store.model.AllowedStatus;
import com.example.appapi.store.review.model.StoreReview;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false, length=20, unique = true)
    private String userId;

    @Column(nullable = false, length=254)
    private String password;

    @Column(length=8)
    private String birthDate;

    @Column(nullable = false, length=10)
    private String name;

    @Column(length=254, unique = true)
    private String email;

    @Column(length=254)
    private String address;

    @Column(length=254)
    private String addressDetail;

    @Column(length=13)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType; // ENUM ('ADMIN', 'SELLER', 'CLIENT')

    @OneToMany
    private List<Likes> likesList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority(userType.toString());

        authorities.add(authority);
        return authorities;
    }

    @OneToMany
    private List<StoreReview> storeReviewList = new ArrayList<>();

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
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
}
