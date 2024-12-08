package g_v1.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import g_v1.demo.model.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

}
