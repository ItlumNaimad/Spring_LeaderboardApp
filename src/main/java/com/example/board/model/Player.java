package com.example.board.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Encja reprezentująca gracza w systemie.
 * Przechowuje informacje o graczu, jego statystykach wygranych i przegranych.
 */
@Entity
public class Player {

    /**
     * Unikalny identyfikator gracza generowany automatycznie.
     * - GETTER -
     * - SETTER -
     *
     @return identyfikator gracza
      * @param id nowy identyfikator gracza

     */
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unikalna nazwa użytkownika gracza.
     * Nie może być pusta ani się powtarzać.
     * - GETTER -
     * - SETTER -
     *
     @return nazwa użytkownika gracza
      * @param nickname nowa nazwa użytkownika gracza

     */
    @Setter
    @Getter
    @Column(unique = true, nullable = false)
    private String nickname;

    @Version
    private Long version;

    /**
     * Liczba wygranych meczy gracza.
     * - GETTER -
     * - SETTER -
     *
     @return liczba wygranych meczy
      * @param wins nowa liczba wygranych meczy

     */
    @Setter
    @Getter
    private int wins = 0;

    /**
     * Liczba przegranych meczy gracza.
     * - GETTER -
     * - SETTER -
     *
     @return liczba przegranych meczy
      * @param losses nowa liczba przegranych meczy

     */
    @Setter
    @Getter
    private int losses = 0;

    /**
     * Konstruktor bezargumentowy wymagany przez JPA.
     */
    public Player() {}

    /**
     * Tworzy nowego gracza z podaną nazwą użytkownika.
     * @param nickname nazwa użytkownika gracza
     */
    public Player(String nickname) {
        this.nickname = nickname;
    }


}