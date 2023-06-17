package backend.entity;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "public", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class UserEntity {

    @Id
    @SequenceGenerator(name = "user_id_sequense", sequenceName = "user_id_sequense", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequense")
    @Column(name = "id", columnDefinition = "serial primary key")
    private Long id;

    private String name;

    private String surname;

    private String phone;

    @Column(name = "password", columnDefinition = "varchar(255) NOT NULL")
    private String password;
    @Column(name = "email", columnDefinition = "varchar(255) UNIQUE NOT NULL")
    private String email;

    @Column(name = "isAdmin", columnDefinition = "boolean")
    private boolean adminRights;

    public UserEntity(String email, String password, boolean adminRights) {
        this.email = email;
        this.password = password;
        this.adminRights = adminRights;
    }

    public UserEntity() {

    }

    public boolean isAdmin() {
        return adminRights;
    }
}
