package top.meyok.user.service.impl;

import org.springframework.stereotype.Service;
import top.meyok.user.service.CheckService;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author meyok@meyok.org
 * @date 2022/8/31 8:49
 */
@Service
public class CheckServiceImpl implements CheckService {
    @Override
    public void fillInCheckCodePicture(int width, int height, String checkCode, ServletOutputStream servletOutputStream) {

        // 生成画板
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        // 填充背景
        g.setColor(Color.pink);
        g.fillRect(0, 0, width, height);

        // 填充边框
        g.setColor(Color.blue);
        g.drawRect(0, 0, width-1, height-1);

        // 填充校验码
        int length = checkCode.length();
        for (int i = 0; i < length; i++) {
            char c = checkCode.charAt(i);
            g.drawString(c+"", width/(length+1)*(i+1), height/2);
        }

        // 画干扰线
        g.setColor(Color.green);
        Random random = new Random();
        for (int i = 0; i < 10; ++i) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        // 输出校验图
        try {
            ImageIO.write(image, "jpg", servletOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}
