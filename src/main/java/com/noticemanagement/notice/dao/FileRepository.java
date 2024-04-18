package com.noticemanagement.notice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noticemanagement.notice.domain.File;

public interface FileRepository extends JpaRepository<File, Long> {
}
