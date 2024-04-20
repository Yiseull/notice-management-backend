package com.noticemanagement.notice.api.dto.response;

import java.util.List;

public record NoticeResponse(
	NoticeInfo notice,
	List<FileInfo> files
) {
}
