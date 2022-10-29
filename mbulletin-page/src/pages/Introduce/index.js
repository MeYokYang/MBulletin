import React from "react";

import './index.css';

import Navigation from '../../components/Navigation';
import Information from '../../components/Information';
import {Button, Col, Row} from "antd";
import {Link} from "react-router-dom";

export default class Introduce extends React.Component {
    render() {
        return <div className="Introduce">

            {/* 导航条 */}
            <div>
                <Row>
                    <Col span={24}>
                        <Navigation />
                    </Col>
                </Row>
            </div>

            {/*内容*/}
            <div id="content">
                <Row>
                    <Col span={4}></Col>
                    <Col span={16}>
                        <div id="board">
                            <div id="character"><h1>还没写好！<br/>但你知道它是专注于校园通告的网站就行。</h1></div>
                            <div><p>...</p></div>
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
}