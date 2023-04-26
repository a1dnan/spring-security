package com.adn4n.springsecurity.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepo extends JpaRepository<Token, Integer> {

}
