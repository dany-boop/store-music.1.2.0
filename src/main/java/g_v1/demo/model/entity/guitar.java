package g_v1.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "guitars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class guitar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_name", nullable = false, length = 100)
    private String modelName;

    @Column(nullable = false)
    private Double price;

    @Lob
    @Column(name = "photo_url")
    private String photoUrl; // Storing URL or image data

    @Override
    public String toString() {
        return "Guitar{" +
                "id=" + id +
                ", modelName='" + modelName + '\'' +
                ", price=" + price +
                '}';
    }
}