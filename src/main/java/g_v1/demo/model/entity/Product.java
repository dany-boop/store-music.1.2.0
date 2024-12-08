package g_v1.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "m_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Product extends Entities {

    @Column(name = "model_name", nullable = false, length = 100)
    private String modelName;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "category")
    private String category;

    @Column(nullable = false)
    private Integer qty;

    @Lob
    @Column(name = "assets")
    private String assets; // Storing URL or image data

}