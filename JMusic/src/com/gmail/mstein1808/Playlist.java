package com.gmail.mstein1808;

import java.util.ArrayList;
import java.util.HashMap;


public class Playlist extends ArrayList<Song>{
	
	final HashMap<String,Song> Library;
	HashMap<String, Boolean> Tags;
	String Name;
	String ID;
	private boolean sync=false;
	private boolean syncable;
	
	public Playlist(HashMap<String,Song> library){
		super();
		Library=library;
		Tags=new HashMap<String, Boolean>();
		
	}
	public void add(String TrackID) throws NullPointerException{
		super.add(Library.get(TrackID));
	}
	public void setName(String name){
		Name=name;
	}
	
	public void setID(String id){
		ID=id;
	}
	public void setTag(String key, boolean value){
		Tags.put(key, new Boolean(value));
	}
	public void setSync(boolean v){
		if(syncable){
			sync=v;
		}
	}
	public boolean getSync(){
		return sync;
	}
	public boolean isSyncable(){
		return syncable;
	}
	public void testSyncable(){
		syncable=false;
		boolean vis = false;
		boolean mov = false;
		boolean tvs = false;
		try{
			if (Tags.get("Visible")==true){
				vis = true;
			}
		}catch(NullPointerException e){
			vis = true;
		}
		try{
			if (Tags.get("TV Shows")==false){
				tvs = true;
			}
		}catch(NullPointerException e){
			tvs = true;
		}try{
			if (Tags.get("Movies")==false){
				mov = true;
			}
		}catch(NullPointerException e){
			mov = true;
		}
		if (vis==true&&tvs==true&&mov==true){
			syncable = true;
		}else{
			syncable = false;
		}
	}
}
