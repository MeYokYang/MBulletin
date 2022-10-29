import React from "react";
import { Col, Row } from 'antd';
import "./index.css"
import {Link} from "react-router-dom";


export default class Information extends React.Component {
    render() {
        return <div className="Information">
            <Row>
                <Col span={2}></Col>
                <Col span={8}>
                    <div id="statement">郑重声明</div>
                    <div id="message">&nbsp;&nbsp;&nbsp;&nbsp;该网站仅供于相互学习与思考，属于非商业盈利网站。网页上所有的图片、音频等均来源于网络，如有侵权，请联系作者（MeYok）删除。</div>
                </Col>
                <Col span={8}></Col>
                <Col span={4}>
                    <div id="contact">
                        Yi Yang(MeYok) FROM UESTC<br/>
                        ADDR: Jianshe north road 2 section, ChengDu<br/>
                        TEL: (86) 18982867218<br/>
                    </div>
                </Col>
                <Col span={2}></Col>
            </Row>
            <Row>
                <Col span={24}><div id="copyright"><h3>©2001-2022 MeYok.top 版权所有</h3></div></Col>
            </Row>

        </div>
    }
}