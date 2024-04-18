package com.noticemanagement.global.error.exception;

public class EntityNotFoundException extends BusinessException {
	public EntityNotFoundException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
