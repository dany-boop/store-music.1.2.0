package g_v1.demo.repository;

import g_v1.demo.model.entity.Transaction;
import g_v1.demo.model.enums.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository
        extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findTransByUserIdAndStatus(String user, OrderStatus orderStatus);
}