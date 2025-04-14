package com.vpd.Collection;

import com.vpd.Movie.Movie;
import com.vpd.Travel.Travel;
import com.vpd.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, String> {

    @Query("SELECT c FROM Collection c JOIN c.travel t JOIN t.users u WHERE c.id = :collectionId AND t.id = :travelId AND u = :user")
    Optional<Collection> findAuthorizedCollection(@Param("collectionId") String collectionId, @Param("travelId") String travelId, @Param("user") User user);

    @Query("SELECT m FROM Movie m JOIN m.travels t WHERE t = :travel AND m.id IN :ids")
    Set<Movie> findByIdInAndTravel(@Param("ids") List<String> ids, @Param("travel") Travel travel);

}
