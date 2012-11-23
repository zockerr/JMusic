package com.gmail.mstein1808;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.lang.Runnable;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

public class SyncCore implements Runnable{
	
	ArrayList<Playlist> Playlists;
	HashMap<String,Song> Songs;
	JProgressBar prog;
	String target;
	JLabel label;
	int selectedIndex;
	/**
	 * @param args
	 * @throws IOException
	 * @throws JDOMException
	 */
	public SyncCore() throws JDOMException, IOException {
		String filename = System.getProperty("user.home")+"/Music/iTunes/iTunes Music Library.xml";
		SAXBuilder Builder = new SAXBuilder(XMLReaders.DTDVALIDATING);
		Document doc = Builder.build(filename);
		Element root = doc.getRootElement();
		
		
		
		Element Trackroot = root.getChild("dict").getChild("dict");
		List<Element> Tracklist = Trackroot.getContent(new ElementFilter("dict"));
		Iterator<Element> TrackIterator = Tracklist.iterator();
		Songs = new HashMap<String,Song>();
		while (TrackIterator.hasNext()) {
			Element currentTrack = null;
			currentTrack = TrackIterator.next();
			Song currentSong = new Song();
			List<Element> currentTrackList = currentTrack.getContent(new ElementFilter());
			Iterator<Element> SongIterator = currentTrackList.iterator();
			while (SongIterator.hasNext()) {
				Element currentTagname = SongIterator.next();
				Element currentTagvalue = SongIterator.next();
				currentSong.setTag(currentTagname.getTextNormalize(),currentTagvalue.getTextNormalize());
			}
			currentSong.setFilename(currentSong.getTag("Location"));
			Songs.put(currentSong.getTag("Track ID"),currentSong);

		}
		
		
		
		List<Element> Rootlist=root.getChild("dict").getContent(new ElementFilter());
		Iterator<Element> RootIterator=Rootlist.iterator();
		Element Listroot=null;
		boolean yetFound=false;
		while(!yetFound){
			Element currentItem=RootIterator.next();
			if(currentItem.getTextNormalize().equals("Playlists")){
				Listroot=RootIterator.next();
				yetFound=true;
			}
		}
		Playlists =new ArrayList<Playlist>();
		Iterator<Element> ListIterator=Listroot.getContent(new ElementFilter()).iterator();
		while(ListIterator.hasNext()){
			Element currentList=ListIterator.next();
			Iterator<Element> currentListIterator=currentList.getContent(new ElementFilter()).iterator();
			Playlist currentPlaylist=new Playlist(Songs);
			while(currentListIterator.hasNext()){
				Element currentElement=currentListIterator.next();
				String currentText=currentElement.getTextNormalize();
				if(!currentElement.getName().equals("array")){
					if(currentText.equals("Name")){
						String Name=currentListIterator.next().getTextNormalize();
						currentPlaylist.setName(Name);
					}else if(currentText.equals("Playlist ID")){
						String ID=currentListIterator.next().getTextNormalize();
						currentPlaylist.setID(ID);
					}else if(currentText.equals("Playlist Items")){
						currentElement=currentListIterator.next();
						List<Element> TrackL=currentElement.getContent(new ElementFilter());
						Iterator<Element> Tracks = TrackL.iterator();
						while(Tracks.hasNext()){
							Element current=Tracks.next();
							String TID=current.getChild("integer").getText();
							currentPlaylist.add(TID);
						}
					}
					else{
						String val=currentListIterator.next().getName();
						if(val.equals("true")){
							currentPlaylist.setTag(currentText, true);
						}else if(val.equals("false")){
							currentPlaylist.setTag(currentText, false);
						}	
					}
				}
			}
			currentPlaylist.testSyncable();
			Playlists.add(currentPlaylist);
		}
		
		System.out.println("Done.");
	}
	public void sync(){
		synchronized(this){
			prog.setMinimum(0);
			prog.setMaximum(0);
			for(Playlist current:Playlists){
				if(current.getSync()==true){
					prog.setMaximum(prog.getMaximum()+current.size());
				}
			}
			label.setText(Messages.getString("MainFrame.ProgressLabel.inProgress")+prog.getValue()+"/"+prog.getMaximum());
		}
		for(Playlist current:Playlists){
			if(current.getSync()==true){
				File oldFile = new File((target+"/"+current.Name+".m3u")) ;
				if(oldFile.exists()){
					oldFile.delete();
				}
				for(Song currentSong:current){
					String FileTarget;
					switch(selectedIndex){
					case 0:
						FileTarget="/"+currentSong.getTag("Artist")+"/"+currentSong.getTag("Album")+"/";
						break;
					case 1:
						FileTarget="/"+currentSong.getTag("Album")+"/";
						break;
					case 2:
						FileTarget="/"+currentSong.getTag("Artist")+"/";
						break;
					case 3:
						FileTarget="/";
						break;
					default:
						FileTarget="/"+currentSong.getTag("Artist")+"/"+currentSong.getTag("Album")+"/";
						break;
					}
					String name=CopyFile(currentSong.getFilename(),target+FileTarget);
					String old = new String();
					try {
						FileReader fr = new FileReader(target+"/"+current.Name+".m3u");
						int currentchar =0;
						while(currentchar != -1){
							currentchar =fr.read();
							if(currentchar!= -1){
								old = old + (char)currentchar;
							}
						}
						fr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try{
						FileWriter fw=new FileWriter(target+"/"+current.Name+".m3u");
						fw.write(old+FileTarget+name+"\n");
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					synchronized(this){
						prog.setValue(prog.getValue()+1);
						label.setText(Messages.getString("MainFrame.ProgressLabel.inProgress")+prog.getValue()+"/"+prog.getMaximum());
					}
				}
			}
		}
		synchronized(this){
			prog.setValue(0);
			label.setText(Messages.getString("MainFrame.ProgressLabel.Done"));
		}
	}
	public static String CopyFile(String source, String target){
		File sourceFile=new File(source);
		File targetFile=new File(target+sourceFile.getName());
		System.out.println("Copying File: "+target+sourceFile.getName());
		try {
			if(!targetFile.exists()){
				BufferedInputStream bis=new BufferedInputStream(new FileInputStream(sourceFile));
				File targ = new File(target+sourceFile.getName());
				if(!targ.exists()){
					new File(target).mkdirs();
					targ.createNewFile();
				}
				BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(target+sourceFile.getName()));
				int b;
				while((b=bis.read())!=-1){
					bos.write(b);
				}
				bis.close();
				bos.close();
			}
			return sourceFile.getName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	@Override
	public void run() {
		this.sync();
	}
	public void setSyncTarget(String t){
		target = t;
		File f=new File(target+"/syncsettings.prop");
		if(f.exists()){
			importSettings(f);
		}
	}
	public void setStatusBar(JProgressBar bar){
		prog = bar;
	}
	public void setStatusField(JLabel lbl){
		label = lbl;
	}
	public void setSelectedIndex(int index){
		selectedIndex=index;
	}
	public void importSettings(File f){
		Properties prop=new Properties();
		try {
			prop.load(new FileReader(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Playlist p : Playlists){
			String ID=p.ID;
			p.setSync(Boolean.parseBoolean(prop.getProperty(ID)));
		}
		int i=Integer.parseInt(prop.getProperty("FileStructureIndex"));
		setSelectedIndex(i);
	}
	public void exportSettings(File f){
		Properties prop=new Properties();
		for(Playlist p : Playlists){
			prop.setProperty(p.ID, Boolean.toString(p.getSync()));
		}
		prop.setProperty("FileStructureIndex", Integer.toString(selectedIndex));
	}
}
