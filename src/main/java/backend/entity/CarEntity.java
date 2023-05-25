package backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cars", schema = "public")
public class CarEntity {

    @Id
    @Column(name = "car_number", columnDefinition = "varchar(9) PRIMARY KEY NOT NULL")
    private String carNumber;

    @Column(name="car_brand", columnDefinition = "varchar(255) NOT NULL")
    private String carBrand;

    public CarEntity(String carNumber, String carBrand){
        this.carNumber = carNumber;
        this.carBrand = carBrand;
    }

    public CarEntity(){}
}
