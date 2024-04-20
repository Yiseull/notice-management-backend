package com.noticemanagement.notice.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.noticemanagement.global.error.exception.EntityNotFoundException;
import com.noticemanagement.global.error.exception.ErrorCode;
import com.noticemanagement.notice.api.dto.response.FileInfo;
import com.noticemanagement.notice.api.dto.response.NoticeInfo;
import com.noticemanagement.notice.api.dto.response.NoticeResponse;
import com.noticemanagement.notice.dao.FileRepository;
import com.noticemanagement.notice.dao.NoticeRepository;
import com.noticemanagement.notice.domain.File;
import com.noticemanagement.notice.domain.Notice;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {

	private final NoticeRepository noticeRepository;
	private final FileRepository fileRepository;

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Transactional
	public Long createNotice(final Notice notice, final List<MultipartFile> multipartFiles) {
		final Notice savedNotice = noticeRepository.save(notice);
		if (multipartFiles == null) {
			return savedNotice.getId();
		}
		final List<File> files = File.of(multipartFiles, savedNotice.getId());
		final List<File> savedFiles = fileRepository.saveAll(files);
		renameFiles(multipartFiles, savedFiles);
		return savedNotice.getId();
	}

	@Transactional
	public void modifyNotice(
		final Long noticeId,
		final String title,
		final String content,
		final List<MultipartFile> multipartFiles
	) {
		final Notice foundNotice = noticeRepository.findById(noticeId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTICE_NOT_FOUND));
		foundNotice.modify(title, content);

		if (multipartFiles != null) {
			final List<File> filesToDelete = fileRepository.findAllByNoticeId(noticeId);
			filesToDelete.forEach(file -> new java.io.File(file.getFileName()).delete());
			fileRepository.deleteAll(filesToDelete);

			final List<File> files = File.of(multipartFiles, noticeId);
			final List<File> savedFiles = fileRepository.saveAll(files);
			renameFiles(multipartFiles, savedFiles);
		}
	}

	@Transactional
	public void deleteNotice(final Long noticeId) {
		final Notice notice = noticeRepository.findById(noticeId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTICE_NOT_FOUND));
		final List<File> files = fileRepository.findAllByNoticeId(noticeId);
		files.forEach(file -> new java.io.File(file.getFileName()).delete());
		noticeRepository.delete(notice);
		fileRepository.deleteAll(files);
	}

	@Transactional
	public NoticeResponse getNotice(final Long noticeId) {
		final Notice notice = noticeRepository.findById(noticeId)
			.orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTICE_NOT_FOUND));
		notice.increaseViews();
		final List<File> files = fileRepository.findAllByNoticeId(noticeId);
		return new NoticeResponse(NoticeInfo.from(notice), FileInfo.listOf(files));
	}

	private void renameFiles(final List<MultipartFile> multipartFiles, final List<File> files) {
		IntStream.range(0, multipartFiles.size()).forEach(i -> {
			final MultipartFile multipartFile = multipartFiles.get(i);
			final File savedFile = files.get(i);
			try {
				Path targetLocation = Paths.get(uploadDir, savedFile.getFileName());
				Files.createDirectories(targetLocation.getParent());
				multipartFile.transferTo(targetLocation);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("파일 저장에 실패했습니다.", e);
			}
		});
	}
}
