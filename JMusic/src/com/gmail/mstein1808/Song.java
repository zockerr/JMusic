package com.gmail.mstein1808;
import java.util.HashMap;


public class Song {
	
	HashMap<String,String> Tags;
	String Filename;
	
	public Song(){
		Tags=new HashMap<String,String>();
	}
	
	public void setTag(String tagname, String value){
		Tags.put(tagname, value);
	}
	public void setFilename(String filename){
		Filename=filename.substring(17);
		Filename=Filename.replaceAll("%20"," ");
		Filename=Filename.replaceAll("%C3%B6","ö");
		Filename=Filename.replaceAll("%C3%A4","ä");
		Filename=Filename.replaceAll("%C3%A4","ä");
		Filename=Filename.replaceAll("%5B","[");
		Filename=Filename.replaceAll("%5D","]");
		Filename=Filename.replaceAll("%23","#");
		Filename=Filename.replaceAll("%C3%BC","ü");
		Filename=Filename.replaceAll("%23","#");
		Filename=Filename.replaceAll("%23","#");
		Filename=Filename.replaceAll("%25","%"); //This at the very End!!!
		System.out.println(Filename);
	}
	public String getTag(String tagname){
		return(Tags.get(tagname));
	}
	public String getFilename(){
		return(Filename);
	}

}
