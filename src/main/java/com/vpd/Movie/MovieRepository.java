package com.vpd.Movie;

import com.vpd.Travel.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

    Set<Movie> findByIdInAndTravel(List<String> ids, Travel travel);

    @Query("""
    SELECT m FROM Movie m 
    JOIN m.travel t
    WHERE t.id = :travelId
    AND LOWER(m.title) LIKE LOWER(CONCAT('%', :search, '%'))
    AND (:genres IS NULL OR :genresSize = 0 OR EXISTS (
        SELECT g FROM m.genres g WHERE g IN :genres
    ))
""")
    Page<Movie> searchMoviesByTravel(
            @Param("travelId") String travelId,
            @Param("search") String search,
            @Param("genres") List<String> genres,
            @Param("genresSize") int genresSize,
            Pageable pageable
    );
}
