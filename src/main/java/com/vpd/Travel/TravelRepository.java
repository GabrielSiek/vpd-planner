package com.vpd.Travel;

import com.vpd.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravelRepository extends JpaRepository<Travel, String> {

    @Query("SELECT t FROM Travel t JOIN t.users u WHERE t.id = :travelId AND u = :user")
    Optional<Travel> findByIdAndUser(@Param("travelId") String travelId, @Param("user") User user);

}
