package com.noticemanagement.notice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noticemanagement.notice.domain.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
