package ru.gbjava.kinozen.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.gbjava.kinozen.persistence.entities.Content;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class DirectorDto {

    private String firstName;
    private String lastName;
    private String description;
    private String url;
    private Set<Content> contents;
}
