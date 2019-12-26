package com.potential.bookapply.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.hr.address.domain.StaticVO;
import com.potential.bookapply.domain.BookApplyVO;


/**
 * @author sist
 *
 */
public class BookApplyDao {

	private String username = StaticVO.UserID;
	private List<BookApplyVO> list = new ArrayList<>();
	private List<BookApplyVO> myList = new ArrayList<>();
	private String FILE_PATH = "E:/JAVA/workspace_java/POTENTIAL_project01/data/java04/BookApplyList.csv"; 
	private static Logger LOG = Logger.getLogger(BookApplyDao.class);
	
	//초기화
	public BookApplyDao(){
		readFile();
	}
	
	//파일 -> list
	public void readFile(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
			String line = "";
			
			while((line=br.readLine())!=null){
				if(line.equals("")) break;
				
				String[] lineArr = line.split(",");
				BookApplyVO vo = new BookApplyVO(lineArr[0], lineArr[1], lineArr[2], lineArr[3], lineArr[4], lineArr[5], lineArr[6]);
				list.add(vo);
			}
		} catch (FileNotFoundException e) {
			LOG.debug("=====================================");
			LOG.debug("FileNotFoundException:"+e.getMessage());
			LOG.debug("=====================================");
		} catch (IOException e) {
			LOG.debug("=====================================");
			LOG.debug("IOException:"+e.getMessage());
			LOG.debug("=====================================");
		}
	}
	
	//리스트에 vo 추가하기
	public int do_save(BookApplyVO vo){
		int flag=0; 
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getTitle().replaceAll(" ", "").equalsIgnoreCase(vo.getTitle().replaceAll(" ", "")) && list.get(i).getPublisher().replaceAll(" ", "").equals(vo.getPublisher().replaceAll(" ", ""))){
				if(list.get(i).getStatus().equals("거부")){
					flag = -1;
					return flag;
				}else{
					flag = 0;
					return flag;
				}
			}
		}
		list.add(vo);
		flag = 1;
		return flag;
	}
	
	//파일 저장하기
	public int saveFile(){
		int writeCnt = 0;
		FileWriter writer = null;
		try {
			writer = new FileWriter(FILE_PATH);
			for(int i=0; i<list.size(); i++){
				BookApplyVO vo = list.get(i);
				String strVO = vo.getId()+","+vo.getTitle()+","+vo.getAuthor()+","+vo.getPublisher()+","+vo.getPubYear()+","+vo.getComment()+","+vo.getStatus()+"\n";
				writer.write(strVO);
				writeCnt++;
			}
		} catch (IOException e) {
			LOG.debug("=====================================");
			LOG.debug("IOException:"+e.getMessage());
			LOG.debug("=====================================");
		} finally{
			try {
				writer.close();
			} catch (IOException e) {
				LOG.debug("=====================================");
				LOG.debug("IOException:"+e.getMessage());
				LOG.debug("=====================================");
			}
		}
		return writeCnt;
	}
	
	//제목, 저자, 신청인 검색하기
	public List<BookApplyVO> do_search(String condition, String keyword){
		List<BookApplyVO> searchList = new ArrayList<>();
		Pattern p = Pattern.compile(".*"+keyword+".*");

		for(int i=0;i<myList.size();i++){
			BookApplyVO vo = myList.get(i);
			Matcher m = null;
			if(condition.equals("제목")){
				m = p.matcher(vo.getTitle());
			}else if(condition.equals("저자")){
				m = p.matcher(vo.getAuthor());
			}
			
			if (m.matches()) {
				searchList.add(vo);
			}
		}
		return searchList;
	}
	
	//사용자 전체 신청목록
	public List<BookApplyVO> getList(){
		return list;
	}
	
	//사용자별 신청목록
	public List<BookApplyVO> getMyList(){
		myList = new ArrayList<>();
		for(int i=0; i<list.size(); i++){
			BookApplyVO tmpVO = list.get(i);
			if(tmpVO.getId().equals(StaticVO.UserID)){
				myList.add(tmpVO);
			}
		}
		return myList;
	}
}