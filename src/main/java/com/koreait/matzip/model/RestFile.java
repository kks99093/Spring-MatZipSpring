package com.koreait.matzip.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
//파일 multiple로 받기위한 클래스
public class RestFile implements Serializable{// Serializable 객체직렬화?

	private static final long serialVersionUID = 1L;

	//변수는 디테일의 메뉴 form과 매칭시킨거
	private int i_rest;
	
	private List<MultipartFile> menu_pic;

	public int getI_rest() {
		return i_rest;
	}

	public void setI_rest(int i_rest) {
		this.i_rest = i_rest;
	}

	public List<MultipartFile> getMenu_pic() {
		return menu_pic;
	}

	public void setMenu_pic(List<MultipartFile> menu_pic) {
		this.menu_pic = menu_pic;
	}

	
	
}
