package vea.home.microservice.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

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

    @Version
    @ToString.Include
    private Long version;

    @ToString.Include
    private String name;

    @ToString.Include
    private LocalDateTime dateOfBirth;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private Set<Post> posts;

}
