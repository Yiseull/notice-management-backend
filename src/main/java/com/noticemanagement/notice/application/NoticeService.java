package com.noticemanagement.notice.application;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.noticemanagement.global.error.exception.EntityNotFoundException;
import com.noticemanagement.global.error.exception.ErrorCode;
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
			filesToDelete.forEach(file -> new java.io.File(file.getId() + file.getExtension()).delete());
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
		files.forEach(file -> new java.io.File(file.getId() + file.getExtension()).delete());
		noticeRepository.delete(notice);
		fileRepository.deleteAll(files);
	}

	private void renameFiles(final List<MultipartFile> multipartFiles, final List<File> files) {
		IntStream.range(0, multipartFiles.size()).forEach(i -> {
			final MultipartFile multipartFile = multipartFiles.get(i);
			final File savedFile = files.get(i);
			try {
				final String originalFilename = multipartFile.getOriginalFilename();
				final String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
				multipartFile.transferTo(Paths.get(savedFile.getId() + extension));
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("파일 저장에 실패했습니다.", e);
			}
		});
	}
}
