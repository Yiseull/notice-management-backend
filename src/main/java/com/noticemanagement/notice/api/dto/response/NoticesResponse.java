package com.noticemanagement.notice.api.dto.response;

import java.util.List;

public record NoticesResponse(
	List<NoticeInfo> notices
) {
}
