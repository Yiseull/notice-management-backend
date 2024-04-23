package com.noticemanagement.notice.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.noticemanagement.notice.api.dto.response.NoticeResponse;
import com.noticemanagement.notice.api.dto.response.NoticesResponse;
import com.noticemanagement.notice.application.NoticeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "공지사항 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {

	private final NoticeService noticeService;

	@Operation(summary = "공지사항 등록")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<NoticeCreateResponse> createNotice(
		@RequestPart final NoticeCreateRequest request,
		@RequestPart(required = false) final List<MultipartFile> files
	) {
		final Long noticeId = noticeService.createNotice(request.toNotice(), files);
		return ResponseEntity.ok(new NoticeCreateResponse(noticeId));
	}

	@Operation(summary = "공지사항 수정")
	@PutMapping(value = "/{noticeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> modifyNotice(
		@PathVariable final Long noticeId,
		@RequestPart final NoticeModifyRequest request,
		@RequestPart(required = false) final List<MultipartFile> files
	) {
		noticeService.modifyNotice(noticeId, request.title(), request.content(), files);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "공지사항 삭제")
	@DeleteMapping("/{noticeId}")
	public ResponseEntity<Void> deleteNotice(@PathVariable final Long noticeId) {
		noticeService.deleteNotice(noticeId);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "공지사항 조회")
	@GetMapping("/{noticeId}")
	public ResponseEntity<NoticeResponse> getNotice(@PathVariable final Long noticeId) {
		return ResponseEntity.ok(noticeService.getNotice(noticeId));
	}

	@Operation(summary = "공지사항 목록 조회")
	@GetMapping
	public ResponseEntity<NoticesResponse> getNotices() {
		return ResponseEntity.ok(noticeService.getNotices());
	}
}
