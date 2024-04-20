package com.noticemanagement.notice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noticemanagement.notice.domain.File;

public interface FileRepository extends JpaRepository<File, Long> {
	List<File> findAllByNoticeId(Long noticeId);
}
