package com.noticemanagement.notice.api.dto.response;

import java.util.List;

import com.noticemanagement.notice.domain.File;

public record FileInfo(
	String originalName,
	String fileUrl
) {
	public static List<FileInfo> listOf(final List<File> files) {
		return files.stream()
			.map(FileInfo::from)
			.toList();
	}

	private static FileInfo from(final File file) {
		return new FileInfo(
			file.getOriginalName(),
			file.getFileName()
		);
	}
}
