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

    Set<Movie> findByIdInAndTravel(List<String> ids, Travel travel);
}
