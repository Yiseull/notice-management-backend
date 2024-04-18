package com.noticemanagement.global.error;

import com.noticemanagement.global.error.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

	private final String error;

	private final String code;

	private final String message;

	public static ErrorResponse from(final ErrorCode errorCode) {
		return new ErrorResponse(
			errorCode.name(),
			errorCode.getCode(),
			errorCode.getMessage()
		);
	}
}
