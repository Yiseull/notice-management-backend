package com.noticemanagement.global.error.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// common
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C001", "Internal Server Error"),

	// notice
	NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "N001", "공지사항을 찾을 수 없습니다."),
	INVALID_TITLE_LENGTH(HttpStatus.BAD_REQUEST, "N002", "제목은 1자 이상 30자 이하여야 합니다."),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;
}
