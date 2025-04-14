package com.vpd.Movie;

import com.vpd.Travel.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

    @Query("SELECT m FROM Movie m JOIN m.travels t WHERE t = :travel AND m.id IN :ids")
    Set<Movie> findByIdInAndTravel(@Param("ids") List<String> ids, @Param("travel") Travel travel);

}
