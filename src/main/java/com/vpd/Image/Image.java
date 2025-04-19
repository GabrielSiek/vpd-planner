package com.vpd.Image;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String imageName;

    private String imageType;

    @Column(name = "image_data", columnDefinition = "bytea")
    private byte[] imageData;

}
