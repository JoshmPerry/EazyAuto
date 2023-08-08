package cr;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;

import javax.imageio.ImageIO;
import Txt.TEditor;

public class ImgProcessor {
	
	String filenam;
	BufferedImage img;
	int[] inform = new int[5];
	
	public ImgProcessor(String file) {
		filenam=fixFile(file);
		inform[0]=0;
		inform[4]=2;
	}
	
	public ImgProcessor() {
		this("placeHolder");
	}
	
	public void changeFile(String file) {
		filenam=fixFile(file);
	}
	
	public void setImg() {
		try {
		img=ImageIO.read(new File(filenam+".png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		TEditor tx = new TEditor(filenam+".txt");
		inform[0]=Integer.parseInt(tx.readLine(1).substring(0,tx.readLine(1).indexOf(',')));
		inform[1]=Integer.parseInt(tx.readLineAfterChar(1,",").substring(0,tx.readLineAfterChar(1,",").indexOf(',')));
		String temp=tx.readLineAfterChar(1,",").substring(tx.readLineAfterChar(1,",").indexOf(",")+1);
		inform[2]=Integer.parseInt(temp.substring(0,temp.indexOf(",")));
		inform[3]=Integer.parseInt(temp.substring(temp.indexOf(",")+1,temp.length()));
	}
	
	public void setError(int i) {
		inform[4]=i;
	}
	
	private String fixFile(String fille) {
		try {
		return fille.substring(0,fille.indexOf('.'));
		}catch(Exception e) {
			return fille;
		}
	}
	
	private double compareImage(BufferedImage imgB) {
		if(img == null)
			setImg();
	    double percentage = 0;
	    try {
	    	int error = inform[4];
	        int count = 0;
	        int yMax=imgB.getHeight();
	        int xMax=imgB.getWidth();
	            for(int y=0;y<yMax;y++) {
	            	for(int x=0;x<xMax;x++) {
	            		Color a = new Color(img.getRGB(x, y),true);
	            		Color b = new Color(imgB.getRGB(x,y),true);
	            		if((a.getRed()>=(b.getRed()-error)&&a.getRed()<=(b.getRed()+error))&&(a.getGreen()>=(b.getGreen()-error)&&a.getGreen()<=(b.getGreen()+error))&&(a.getBlue()>=(b.getBlue()-error)&&a.getBlue()<=(b.getBlue()+error))) {
	            			
	            		} else {
	            			count++;
	            		}
	            	}
	            }
	            percentage=(double)count/(yMax*xMax);
	    } catch (Exception e) {
	        System.out.println("Failed to compare image files ...");
	    }
	    return 100-percentage*100;
	}
	
	public void makeImage(int x1,int y1,int x2,int y2, String filepath) {
		int distx=Math.abs(x1-x2);
		int disty=Math.abs(y1-y2);
		if(x1>x2)x1=x2;
		if(y1>y2)y1=y2;	
		TEditor tx = new TEditor(filepath+".txt");
		tx.eraseEntireFile();
		tx.write(x1+","+y1+","+distx+","+disty);
		try
        {
            Robot robo=new Robot();
            Rectangle captureSize=new Rectangle(x1,y1,distx,disty);
            System.out.println(captureSize.toString());

            BufferedImage image=robo.createScreenCapture(captureSize);

            ImageIO.write(image,"png",new File(filepath+".png"));
        } catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	public boolean checkImage(int val) {
		double value = checkImageLikeness();
		if(val>value)
			return false;
		return true;
	}
	
	public boolean checkImage() {
		return checkImage(95);
	}
	
	public double checkImageLikeness() {
		if(img == null)
			setImg();
		try
        {
            Robot robo=new Robot();
            Rectangle captureSize=new Rectangle(inform[0],inform[1],inform[2],inform[3]);
            System.out.println(captureSize.toString());
            BufferedImage image=robo.createScreenCapture(captureSize);
            return compareImage(image);
        } catch(Exception e) {
        	e.printStackTrace();
        }
		return 0;
	}
	
	public void makeImage(int x1,int y1,int x2,int y2) {
		makeImage(x1,y1,x2,y2,filenam);
	}
	public static void main(String[] args) {
		System.out.println("Started");
		ImgProcessor carl = new ImgProcessor("carl");
		carl.makeImage(50,100,175,300);
		System.out.println(carl.checkImageLikeness());
	}
	
	
	
}
