import React, {useEffect} from "react";
import { Link } from "react-router-dom";
import { Col, Row, Button } from 'antd';

import {useParams} from "react-router-dom";
import axios from "axios";

import './index.css';

import Information from '../../components/Information';


export default function Home() {

    let params = useParams();

    useEffect(() => {
        // 判断用户是否登录
        axios({
            method: 'get',
            url: 'http://localhost:10010/users/checkLogged',
            dataType: 'Json',
            withCredentials: true
        }).then((res) => {
            if (!res.data.object) {
                window.location.replace("/index");
            }
        }).catch((error) => {
            alert("出错了！");
        })
    });


    return <div className="Home">

        {/* 导航条 */}
        <div id="home_navigation">
            <Row>
                <Col span={2}><Link to="/"><div id="home_title">MBulletin</div></Link></Col>
                <Col span={20}></Col>
                <Col span={1}><div id="logout" onClick={() => {
                    axios({
                        method: 'get',
                        url: 'http://localhost:10010/users/logout',
                        withCredentials: true
                    }).finally(() => {
                        window.location.replace("/index");
                    })
                }}>登出</div></Col>
                <Col span={1}><div id="register">{params.username}</div></Col>
            </Row>
        </div>

        {/*内容*/}
        <div id="content">
            <Row>
                <Col span={4}></Col>
                <Col span={16}>
                    <div id="board">
                        <div id="character"><h1>MBulletin,<br/>专注于校园通告的网站！</h1></div>
                        <div><p>...</p></div>
                        <div><Link to="/introduce"><Button type="primary">了解更多</Button></Link></div>
                    </div>
                </Col>
                <Col span={4}></Col>
            </Row>
        </div>

        {/* 底部信息 */}
        <div>
            <Row>
                <Col span={24}>
                    <Information />
                </Col>
            </Row>
        </div>
    </div>

}