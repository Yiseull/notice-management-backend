package com.noticemanagement.notice.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.noticemanagement.global.error.exception.ErrorCode;
import com.noticemanagement.global.error.exception.InvalidValueException;

class TitleTest {
	@ParameterizedTest
	@ValueSource(strings = {"제", "제목을 이렇게 작성하면 제목의 길이가 30자가 됩니다."})
	void 제목은_1자_이상_30자_이하이어야_한다(String title) {
		// when & then
		assertThatCode(() -> new Title(title))
			.doesNotThrowAnyException();
	}

	@ParameterizedTest
	@ValueSource(strings = {"", "제목을 이렇게 작성하면 제목의 길이가 30자가 넘습니다."})
	void 제목이_1자_미만이거나_30자_초과이면_예외가_발생한다(String title) {
		// when & then
		assertThatThrownBy(() -> new Title(title))
			.isInstanceOf(InvalidValueException.class)
			.hasMessage(ErrorCode.INVALID_TITLE_LENGTH.getMessage());
	}
}
