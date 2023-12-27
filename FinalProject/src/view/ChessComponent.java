package view;


import model.ChessPiece;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

/**
 * This is the equivalent of the ChessPiece class,
 * but this class only cares how to draw Chess on ChessboardComponent
 */
public class ChessComponent extends JComponent {
    private String skin = "default";
    private boolean selected;
    private ChessPiece chessPiece;
    Image image1;
    Image image2;
    Image image3;
    Image image4;

    private static int n = 0; //统计对象创建的次数
    public ChessComponent(int size, ChessPiece chessPiece,String skin) {
        this.skin = skin;
        this.selected = false;
        setSize(size/2, size/2);
        setLocation(0,0);
        setVisible(true);
        this.chessPiece = chessPiece;
        readImage();
        //n++;
        //System.out.println(n);
    }//冯俊铭 添加了一个skin
    public void readImage(){
        image1 = Toolkit.getDefaultToolkit().getImage("./icons/"+skin+"/1.png");
        image2 = Toolkit.getDefaultToolkit().getImage("./icons/"+skin+"/2.png");
        image3 = Toolkit.getDefaultToolkit().getImage("./icons/"+skin+"/3.png");
        image4 = Toolkit.getDefaultToolkit().getImage("./icons/"+skin+"/4.png");

    }//fjm 读图
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    protected void paintComponent(Graphics g) {
        ImageObserver imageObserver = new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return false;
            }
        };
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("Helvetica", Font.PLAIN, getWidth() / 2);
        //替换"💎"的图片放在这 50*50
        switch (this.chessPiece.getName()) {
            case "💎" -> {
                try {
                    g2.drawImage(image1, (getWidth() - image1.getWidth(imageObserver)) / 2, (getWidth() - image1.getWidth(imageObserver)) / 2, null);
                    //g.setColor(Color.red);
                    //g.drawOval(0, 0, getWidth(), getHeight());
                } catch (Exception e) {
                    System.out.println("没有读取到图片");
                    g2.setFont(font);
                    g2.setColor(this.chessPiece.getColor());
                    g2.drawString(this.chessPiece.getName(), getWidth() / 4, getHeight() * 5 / 8); // FIXME: Use library to find the correct offset.
                }
            }
            case "⚪" -> {
                //替换"⚪"的图片放在这 50*50
                try {
                    g2.drawImage(image2, (getWidth() - image2.getWidth(imageObserver)) / 2, (getWidth() - image2.getWidth(imageObserver)) / 2, null);
                } catch (Exception e) {
                    System.out.println("没有读取到图片");
                    g2.setFont(font);
                    g2.setColor(this.chessPiece.getColor());
                    g2.drawString(this.chessPiece.getName(), getWidth() / 4, getHeight() * 5 / 8); // FIXME: Use library to find the correct offset.
                }
            }
            case "▲" -> {
                //替换"▲"的图片放在这 50*50
                try {
                    g2.drawImage(image3, (getWidth() - image3.getWidth(imageObserver)) / 2, (getWidth() - image3.getWidth(imageObserver)) / 2, null);
                } catch (Exception e) {
                    System.out.println("没有读取到图片");
                    g2.setFont(font);
                    g2.setColor(this.chessPiece.getColor());
                    g2.drawString(this.chessPiece.getName(), getWidth() / 4, getHeight() * 5 / 8); // FIXME: Use library to find the correct offset.
                }
            }
            case "🔶" -> {
                //替换"🔶"的图片放在这 50*50
                try {
                    g2.drawImage(image4, (getWidth() - image4.getWidth(imageObserver)) / 2, (getWidth() - image4.getWidth(imageObserver)) / 2, null);
                } catch (Exception e) {
                    System.out.println("没有读取到图片");
                    g2.setFont(font);
                    g2.setColor(this.chessPiece.getColor());
                    g2.drawString(this.chessPiece.getName(), getWidth() / 4, getHeight() * 5 / 8); // FIXME: Use library to find the correct offset.
                }
            }
            default -> { //这是原来直接按照字符生成初始元素的代码
                g2.setFont(font);
                g2.setColor(this.chessPiece.getColor());
                g2.drawString(this.chessPiece.getName(), getWidth() / 4, getHeight() * 5 / 8); // FIXME: Use library to find the correct offset.
            }
        }
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.gray);
            g.drawOval(0, 0, getWidth(), getHeight());
        }
    }
    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }
}
