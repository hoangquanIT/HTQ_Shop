package com.quanht.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quanht.dto.AccountDto;
import com.quanht.dto.CustomerDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.*;

@NamedNativeQuery(
        name = "getAccounts",
        query = "SELECT a.id, a.name, a.email, a.phone, a.address, a.city_code, " +
                "GROUP_CONCAT(r.name) AS roles, a.created_at, a.updated_at " +
                "FROM account a LEFT JOIN account_role ar ON a.id = ar.account_id " +
                "LEFT JOIN role r ON r.id = ar.role_id WHERE r.id != 3 GROUP BY a.id",
        resultSetMapping = "Accounts"
)
@SqlResultSetMapping(
        name = "Accounts",
        classes = @ConstructorResult(
                targetClass = AccountDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "phone", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "city_code", type = Integer.class),
                        @ColumnResult(name = "roles", type = String.class),
                        @ColumnResult(name = "created_at", type = LocalDateTime.class),
                        @ColumnResult(name = "updated_at", type = LocalDateTime.class)
                }
        )
)
@NamedNativeQuery(
        name = "getCustomers",
        query = "SELECT a.id, a.name, a.email, a.phone, a.address, a.city_code, a.created_at " +
                "FROM account a LEFT JOIN account_role ar ON a.id = ar.account_id " +
                "LEFT JOIN role r ON r.id = ar.role_id WHERE r.id = 3",
        resultSetMapping = "Customers"
)
@SqlResultSetMapping(
        name = "Customers",
        classes = @ConstructorResult(
                targetClass = CustomerDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "email", type = String.class),
                        @ColumnResult(name = "phone", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "city_code", type = Integer.class),
                        @ColumnResult(name = "created_at", type = LocalDateTime.class)
                }
        )
)

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "account")
public class Account extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    @Pattern(regexp = "^[a-zA-Z][\\w]+@([\\w]+\\.[\\w]{2,}|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$")
    private String email;

    @Column(name = "phone", nullable = false, unique = true)
    @Pattern(regexp = "^(0|\\+84)[1-9][0-9]{8}$")
    private String phone;

    @Column(name = "password", nullable = false)
    @Pattern(regexp = "^((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{6,})$")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "city_code")
    private Integer cityCode;

    @Column(name = "enabled")
    private Boolean enabled = false;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "account", orphanRemoval = true)
    private List<Order> orders;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        this.roles.forEach(r -> roles.add(new SimpleGrantedAuthority("ROLE_" + r.getName()))); // ROLE_ADMIN, ROLE_USER
        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
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
        return enabled;
    }

    public Account(String name, String email, String phone, String password, String address, Integer cityCode, List<Role> roles) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.cityCode = cityCode;
        this.roles = roles;
    }

    @PrePersist
    void prePersist(){
        this.setCreatedAt(LocalDateTime.now());
        this.setUpdatedAt(LocalDateTime.now());
    }
}
