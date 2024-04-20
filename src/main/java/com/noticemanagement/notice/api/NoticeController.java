package com.noticemanagement.notice.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.noticemanagement.notice.api.dto.request.NoticeCreateRequest;
import com.noticemanagement.notice.api.dto.request.NoticeModifyRequest;
import com.noticemanagement.notice.api.dto.response.NoticeCreateResponse;
import com.noticemanagement.notice.application.NoticeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {

	private final NoticeService noticeService;

	@PostMapping
	public ResponseEntity<NoticeCreateResponse> createNotice(
		@RequestPart final NoticeCreateRequest request,
		@RequestPart(required = false) final List<MultipartFile> files
	) {
		final Long noticeId = noticeService.createNotice(request.toNotice(), files);
		return ResponseEntity.ok(new NoticeCreateResponse(noticeId));
	}

	@PutMapping("/{noticeId}")
	public ResponseEntity<Void> modifyNotice(
		@PathVariable final Long noticeId,
		@RequestPart final NoticeModifyRequest request,
		@RequestPart(required = false) final List<MultipartFile> files
	) {
		noticeService.modifyNotice(noticeId, request.title(), request.content(), files);
		return ResponseEntity.ok().build();
	}
}
