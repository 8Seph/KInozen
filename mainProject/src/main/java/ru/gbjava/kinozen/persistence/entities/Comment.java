package ru.gbjava.kinozen.persistence.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_comment")
public class Comment {

    @Id
    @Column(name = "id_comment")
    @GeneratedValue(strategy = IDENTITY)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;


    @Column(name = "text_comment")
    private String text_comment;

    @Column(name = "date_comment")
    private Date date_comment;
}
