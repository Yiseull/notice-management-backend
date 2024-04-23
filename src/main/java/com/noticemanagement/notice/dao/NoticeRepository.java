package com.noticemanagement.notice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.noticemanagement.notice.domain.Notice;

import jakarta.persistence.LockModeType;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT n FROM Notice n WHERE n.id = :id")
	Notice findByIdWithPessimisticLock(Long id);
}
