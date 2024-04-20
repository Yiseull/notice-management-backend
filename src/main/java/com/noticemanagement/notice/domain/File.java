package com.noticemanagement.notice.domain;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "files")
public class File {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "notice_id", nullable = false)
	private Long noticeId;

	@Column(name = "extension", nullable = false)
	private String extension;

	public File(final Long noticeId, final String extension) {
		this.noticeId = noticeId;
		this.extension = extension;
	}

	public static List<File> of(final List<MultipartFile> files, final Long noticeId) {
		return files.stream()
			.map(file -> {
				final String originalFilename = file.getOriginalFilename();
				final String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
				return new File(noticeId, extension);
			})
			.toList();
	}
}
