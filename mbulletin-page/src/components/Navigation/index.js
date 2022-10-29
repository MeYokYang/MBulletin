import React from "react";
import { Link } from "react-router-dom";
import { Col, Row } from 'antd';

import './index.css';

export default class Navigation extends React.Component {
    render() {

        return <div className="Navigation">
            <Row>
                <Col span={2}><Link to="/"><div id="title">MBulletin</div></Link></Col>
                <Col span={20}></Col>
                <Col span={1}><Link to="/login"><div id="login">登录</div></Link></Col>
                <Col span={1}><Link to="/register"><div id="register">注册</div></Link></Col>
            </Row>



        </div>
    }
}


