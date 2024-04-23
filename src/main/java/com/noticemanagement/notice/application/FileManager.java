package com.noticemanagement.notice.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.noticemanagement.notice.dao.FileRepository;
import com.noticemanagement.notice.domain.File;

import lombok.RequiredArgsConstructor;

/**
 * 파일을 관리하는 기능을 담당하는 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class FileManager {

	private final FileRepository fileRepository;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public void append(final List<MultipartFile> multipartFiles, final Long noticeId) {
		if (multipartFiles == null) {
			return;
		}

		final List<File> files = File.of(multipartFiles, noticeId);
		final List<File> savedFiles = fileRepository.saveAll(files);
		renameFiles(multipartFiles, savedFiles);
	}

	@Transactional(readOnly = true)
	public List<File> readAllByNoticeId(final Long noticeId) {
		return fileRepository.findAllByNoticeId(noticeId);
	}

	public void modify(final List<MultipartFile> multipartFiles, final Long noticeId) {
		if (multipartFiles == null) {
			return;
		}

		deleteAllByNoticeId(noticeId);
		append(multipartFiles, noticeId);
	}

	public void deleteAllByNoticeId(final Long noticeId) {
		final List<File> files = readAllByNoticeId(noticeId);
		files.forEach(file -> {
			try {
				Files.delete(Paths.get(uploadDir, file.getFileName()));
			} catch (IOException e) {
				throw new RuntimeException("파일 삭제에 실패했습니다.", e);
			}
		});
		fileRepository.deleteAll(files);
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
