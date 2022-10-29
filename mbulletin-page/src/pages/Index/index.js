import React from "react";
import { Link } from "react-router-dom";
import { Col, Row, Button } from 'antd';

import './index.css';

import Navigation from '../../components/Navigation';
import Information from '../../components/Information';



export default class Index extends React.Component {
    render() {
        return <div className="Index">

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
}