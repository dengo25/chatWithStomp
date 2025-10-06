package org.dengo.chat_backend.chat.repository;

import org.dengo.chat_backend.chat.domain.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {

}
