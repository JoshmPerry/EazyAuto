package cr;

import java.awt.MouseInfo;
import java.util.Scanner;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;
import Txt.TEditor;

public class Creator {
	
	static int[] coords = new int[3];
	
	public static void p(String ab) {
		System.out.println(ab);
	}
	public static int getX() {
		try {
			return (int)MouseInfo.getPointerInfo().getLocation().getX();
		} catch(Exception e) {
			System.out.println("Error finding mouse: "+e);
		}
		return 0;
	}
	public static int getY() {
		try {
			return (int)MouseInfo.getPointerInfo().getLocation().getY();
		} catch(Exception e) {
			System.out.println("Error finding mouse: "+e);
		}
		return 0;
	}
	public static void main(String[] args) {
		p("This program will allow you to simply select 2 spots on your screen then automatically record all input");
		Scanner scan = new Scanner(System.in);
		p("Please hover your mouse over where you want the image to be taken, top left then bottum right corner");
		p("Press 'q' in each of the corners. Once you do the second corner, the recording will begin");
		while(true) {
			coords[0]=0;
			coords[1]=0;
			coords[2]=0;
			Manager listener = new Manager();
			ImgProcessor photo = new ImgProcessor();
			p("Please type the file name:");
			String fille = scan.nextLine();
			if(fille.equals("image")) {
				p("Making an image.\nPlease type the file name:");
				fille = scan.nextLine();
				p("Now listening for 'q's for the image");
				GlobalKeyboardHook keyboard = new GlobalKeyboardHook(true);
				keyboard.addKeyListener(new GlobalKeyAdapter() {
					
					@Override public void keyPressed(GlobalKeyEvent event) {
						if(event.getVirtualKeyCode() == 81) {
							if(coords[0]==0) {
								//System.out.println("ran");
								coords[0]=1;
								coords[1]=getX();
								coords[2]=getY();
							}else {
								//System.out.println("ran2");
								coords[0]=2;
								keyboard.shutdownHook();
							}
						}}});
				long tim = System.nanoTime();
				while(coords[0]!=2) {
					System.out.println((System.nanoTime()-tim)/1000000000);
				}
				//System.out.println("ran3");
				int temp1=getX();
				int temp2=getY();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				photo.makeImage(coords[1], coords[2], temp1, temp2,fille+"img");
			}else {
			p("Please type the char you want to stop the recording");
			char chir = scan.nextLine().charAt(0);
			listener.changeFile(fille+"rd");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			p("Now listening for 'q's for the image");
			GlobalKeyboardHook keyboard = new GlobalKeyboardHook(true);
			keyboard.addKeyListener(new GlobalKeyAdapter() {
				
				@Override public void keyPressed(GlobalKeyEvent event) {
					if(event.getVirtualKeyCode() == 81) {
						if(coords[0]==0) {
							//System.out.println("ran");
							coords[0]=1;
							coords[1]=getX();
							coords[2]=getY();
						}else {
							//System.out.println("ran2");
							coords[0]=2;
							keyboard.shutdownHook();
						}
					}}});
			long tim = System.nanoTime();
			while(coords[0]!=2) {
				System.out.println((System.nanoTime()-tim)/1000000000);
			}
			//System.out.println("ran3");
			photo.makeImage(coords[1], coords[2], getX(), getY(),fille+"img");
			//System.out.println("ran4");
			listener.auto(chir);
			//p("In process of recording");
		}}
	}
}