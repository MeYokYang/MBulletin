package top.meyok.user.service;

import javax.servlet.ServletOutputStream;

/**
 * @author meyok@meyok.org
 * @date 2022/8/31 8:45
 */
public interface CheckService {

    void fillInCheckCodePicture(int width, int height, String checkCode, ServletOutputStream servletOutputStream);


}
