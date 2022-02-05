package vea.home.microservice.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(indexes = {
        @Index(name = "USER_IDX", columnList = "USER_ID")
})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @Version
    @ToString.Include
    private Long version;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;



}
