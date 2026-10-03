package paytm.com.example.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import paytm.com.example.Entity.IdempotencyRecord;

public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, Long> {

	Optional<IdempotencyRecord> findByIdempotencyKey(String idempotencyKey);

}
