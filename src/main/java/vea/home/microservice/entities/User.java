package vea.home.microservice.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "\"USER\"")
public class User {

    @Id
    @ToString.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(name = "FIRST_NAME")
    private String firstName;

    @ToString.Include
    @Column(name = "LAST_NAME")
    private String lastName;
}
