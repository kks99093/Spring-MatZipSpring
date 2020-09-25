package com.koreait.matzip;

import java.io.File;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	public static void makeFolder(String path) {
		File dir = new File(path);
		if(!dir.exists()) {
		//만약 폴더가 없다면
			
			dir.mkdirs();
		//폴더를 만듬
		}
	}

	public static String getExt(String fileNm) {
		return fileNm.substring(fileNm.lastIndexOf("."));
	}
	
	//랜덤이름 얻어와서 .jpg만듬(UUID)
	public static String getRandomUUID(MultipartFile mf) {
		String originFileNm = mf.getOriginalFilename();
		String ext = getExt(originFileNm);
		return UUID.randomUUID() + ext;
	}
	
	
	public static String saveFile(String path, MultipartFile mf) {
		if(mf.isEmpty()) { return null; } //파일이 없었다면 null 리턴
		String saveFileNm = getRandomUUID(mf);
		
		try {
			mf.transferTo(new File(path + saveFileNm));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return saveFileNm; //null이 박혀있으면 파일이 없다는것, 문자열이 박혀있다면 파일이 있다는것
	}
	
	public static boolean delFile(String path) {
		File file = new File(path);
		if(file.exists()) {
			return file.delete(); //파일 삭제 성공하면 true가 넘어감
		}
		return false;
	}
	
}
