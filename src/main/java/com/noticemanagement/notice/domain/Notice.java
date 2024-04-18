package com.noticemanagement.notice.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import com.noticemanagement.global.common.BaseEntity;
import com.noticemanagement.notice.domain.vo.Title;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "notices")
public class Notice extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;

	@Embedded
	private Title title;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;

	@Column(name = "end_time", nullable = false)
	private LocalDateTime endTime;

	@Column(name = "writer", nullable = false)
	private String writer;

	@Column(name = "views")
	private long views;

	@Builder
	private Notice(
		final String title,
		final String content,
		final LocalDateTime startTime,
		final LocalDateTime endTime,
		final String writer
	) {
		this.title = new Title(title);
		this.content = Objects.requireNonNull(content);
		this.startTime = startTime;
		this.endTime = endTime;
		this.writer = Objects.requireNonNull(writer);
	}
}
