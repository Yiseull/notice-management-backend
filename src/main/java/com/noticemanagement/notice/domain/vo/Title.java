package com.noticemanagement.notice.domain.vo;

import java.util.Objects;

import com.noticemanagement.global.error.exception.ErrorCode;
import com.noticemanagement.global.error.exception.InvalidValueException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Title {

	public static final int MIN_TITLE_LENGTH = 1;
	public static final int MAX_TITLE_LENGTH = 30;

	@Column(name = "title", nullable = false, length = MAX_TITLE_LENGTH)
	private String title;

	public Title(final String title) {
		validate(title);
		this.title = Objects.requireNonNull(title);
	}

	private void validate(final String title) {
		if (title.length() < MIN_TITLE_LENGTH || title.length() > MAX_TITLE_LENGTH) {
			throw new InvalidValueException(ErrorCode.INVALID_TITLE_LENGTH);
		}
	}
}
