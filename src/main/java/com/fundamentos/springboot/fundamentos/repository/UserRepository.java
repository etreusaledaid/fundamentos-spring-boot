package com.fundamentos.springboot.fundamentos.repository;

import com.fundamentos.springboot.fundamentos.dto.UserDto;
import com.fundamentos.springboot.fundamentos.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//PagingAndSortingRepository se utiliza para paginación con Spring Boot
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Query("Select u from User u Where u.email=?1")
    Optional<User> findByUserEmail(String email);
    @Query("Select u from User u Where u.name like ?1%")
    List<User> findAndSort(String name, Sort sort);

    //Query methods
    List<User> findByName(String name);
    Optional<User> findByEmailAndName(String email, String name);

    List<User> findByNameLike(String name);

    List<User> findByNameOrEmail(String name, String email);

    List<User> findBybirthdateBetween(LocalDate begin, LocalDate end);

    List<User> findByNameLikeOrderByIdDesc(String name);

    List<User> findByNameContainingOrderByIdDesc(String name);

    //Uso de JPQL con named parameters
    @Query("SELECT new com.fundamentos.springboot.fundamentos.dto.UserDto(u.id, u.name, u.birthdate)"+
    " FROM User u"+
    " Where u.birthdate=:parametroFecha"+
    " And u.email=:parametroEmail")
    Optional<User> getAllByBirthdateAndEmail(@Param("parametroFecha") LocalDate date,
                                                @Param("parametroEmail") String email);

    //Metodo utilizado cuando usamos PagingAndSortingRepository
    List<User> findAll();
}