import React from "react";

import './index.css';

import Navigation from '../../components/Navigation';
import Information from '../../components/Information';
import {Button, Col, Input, Row} from "antd";
import {Link} from "react-router-dom";
import axios from "axios";
import md5 from "js-md5";

export default class Login extends React.Component {

    state = {
        email: '',
        password: '',
        picture_check_code: '',
        check_picture_url: "http://localhost:10010/users/checkPicture?" + new Date().getTime(),

        prompt_message: {
            email: '',
            password: '',
            picture_check_code: ''
        },

        check_status: {
            email: false,
            password: false,
            picture_check_code: false
        }

    }

    handleForm = e => {
        this.setState({
            [e.target.name]: e.target.value
        })
    }

    handleCheckCodePicClick = () => {

        if (this.state.check_status.picture_check_code === true) {
            return;
        }

        this.setState({
            check_picture_url: "http://localhost:10010/users/checkPicture?" + new Date().getTime()
        })
    }

    handleSubmitButtonClick = () => {
        let check_status = this.state.check_status;
        if (check_status.email && check_status.password && check_status.picture_check_code) {

            let submitData = {
                "email": this.state.email,
                "password": md5(this.state.password)
            }


            axios({
                method: 'post',
                url: 'http://localhost:10010/users/login',
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                },
                data: JSON.stringify(submitData),
                dataType: 'Json',
                withCredentials: true
            }).then((res) => {

                let object = res.data.object;
                if (object.loggedIn === 1) {
                    // 登录成功
                    window.location.replace("/home/" + object.username);
                } else {
                    // 登录失败

                    let check_status = this.state.check_status;
                    check_status.picture_check_code = false;
                    check_status.password = false;
                    let prompt_message = this.state.prompt_message;
                    prompt_message.picture_check_code = '';
                    prompt_message.password = '密码错误!';
                    this.setState({
                        picture_check_code: '',
                        check_status,
                        prompt_message
                    });

                    this.handleCheckCodePicClick();

                }

            }).catch((error) => {
                console.log(error);
                alert("服务器繁忙，注册失败！");
            })

        } else {
            this.checkEmailIsAvailable();
            this.checkPasswordAvailable();
            this.checkPictureCheckCode();
        }
    }

    checkEmailIsAvailable = () => {
        // 检查是否输入邮箱
        if (this.state.email === '') {
            let check_status = this.state.check_status;
            check_status.email = false;
            let prompt_message = this.state.prompt_message;
            prompt_message.email = '请输入!';
            this.setState({
                check_status,
                prompt_message
            })
        } else {

            axios({
                method: 'get',
                url: 'http://localhost:10010/users/mailRegistered/' + this.state.email,
                dataType: 'Json',
                withCredentials: true
            }).then((res) => {

                let account_status = res.data.object;

                let check_status = this.state.check_status;
                check_status.email = false;
                let prompt_message = this.state.prompt_message;
                prompt_message.email = '';


                if (account_status === 0) {
                    // 邮箱未注册
                    prompt_message.email = '未注册!';
                } else if (account_status === 1) {
                    // 邮箱未激活
                    prompt_message.email = '未激活!';
                } else if (account_status === 2) {
                    // 可登录
                    check_status.email = true;
                    prompt_message.email = '';
                } else if (account_status === 3) {
                    // 账户冻结，无法登录
                    prompt_message.email = '冻结中!';
                } else if (account_status === 4) {
                    // 服务出错，无法登录
                    prompt_message.email = '服务错误!';
                } else {
                    // 未知错误
                    prompt_message.email = '未知错误!';
                }
                this.setState({
                    check_status,
                    prompt_message
                })

            }).catch((error) => {
                alert("服务器繁忙，注册失败！");
            })


            let check_status = this.state.check_status;
            check_status.email = true;
            let prompt_message = this.state.prompt_message;
            prompt_message.email = '';
            this.setState({
                check_status,
                prompt_message
            })


        }
    }
    checkPasswordAvailable = () => {
        // 检查是否输入邮箱
        if (this.state.password === '') {
            let check_status = this.state.check_status;
            check_status.password = false;
            let prompt_message = this.state.prompt_message;
            prompt_message.password = '请输入!';
            this.setState({
                check_status,
                prompt_message
            })
        } else {
            let check_status = this.state.check_status;
            check_status.password = true;
            let prompt_message = this.state.prompt_message;
            prompt_message.password = '';
            this.setState({
                check_status,
                prompt_message
            })
        }
    }
    checkPictureCheckCode = () => {

        // 先更新校验状态
        let check_status = this.state.check_status;
        check_status.picture_check_code = false;
        let prompt_message = this.state.prompt_message;
        prompt_message.picture_check_code = '';
        this.setState({
            check_status,
            prompt_message
        })

        // 检查邮箱是否符合规范
        if (this.state.picture_check_code === '') {
            let check_status = this.state.check_status;
            check_status.picture_check_code = false;
            let prompt_message = this.state.prompt_message;
            prompt_message.picture_check_code = '请输入!';
            this.setState({
                check_status,
                prompt_message
            })
            return;
        }


        axios({
            method: 'get',
            url: 'http://localhost:10010/users/pictureCodeCheck/' + this.state.picture_check_code,
            dataType: 'Json',
            withCredentials: true
        }).then((res) => {

            if (res.data.object === true) {
                // 校验正确
                let check_status = this.state.check_status;
                check_status.picture_check_code = true;
                let prompt_message = this.state.prompt_message;
                prompt_message.picture_check_code = '成功';
                this.setState({
                    check_status,
                    prompt_message
                })
            } else {
                // 校验失败
                let check_status = this.state.check_status;
                check_status.picture_check_code = false;
                let prompt_message = this.state.prompt_message;
                prompt_message.picture_check_code = '错误！';
                this.setState({
                    picture_check_code: '',
                    check_status,
                    prompt_message
                });
                this.handleCheckCodePicClick();
            }

        }).catch((error) => {
            // 后端接口调用出错
            let check_status = this.state.check_status;
            check_status.picture_check_code = false;
            let prompt_message = this.state.prompt_message;
            prompt_message.picture_check_code = '出错了!';
            this.setState({
                check_status,
                prompt_message
            })
        })

    }


    render() {
        return <div className="Login">
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

                    <Col span={8}></Col>


                    {/* 登录栏 */}
                    <Col span={8}>
                        <div id="login_board">

                            {/* 标题栏 */}
                            <Row>
                                <Col span={24}>
                                    <div>
                                        <h1 id="login_user_info">用户登录</h1>
                                    </div>

                                </Col>
                            </Row>


                            {/* 邮箱栏 */}
                            <div className="row_div">
                                <Row>
                                    <Col span={1}></Col>
                                    <Col span={4}>
                                        <div className="left_prompt_msg">
                                            <label htmlFor="email">邮箱</label>
                                        </div>
                                    </Col>
                                    <Col span={1}></Col>
                                    <Col span={14}>
                                        <div>
                                            <Input id="email"
                                                   placeholder="请输入邮箱"
                                                   value={this.state.email}
                                                   onChange={this.handleForm}
                                                   name="email"
                                                   onBlur={this.checkEmailIsAvailable}/>
                                        </div>
                                    </Col>
                                    <Col span={4}>
                                        <div className="right_prompt_msg"
                                             style={this.state.check_status.email ? {color: "blue"} : {color: "red"}}>
                                            {this.state.prompt_message.email}
                                        </div>
                                    </Col>
                                </Row>

                            </div>


                            {/* 用户密码栏 */}
                            <div className="row_div">
                                <Row>
                                    <Col span={1}></Col>
                                    <Col span={4}>
                                        <div className="left_prompt_msg">
                                            <label htmlFor="password">密码</label>
                                        </div>
                                    </Col>
                                    <Col span={1}></Col>
                                    <Col span={14}>
                                        <div>
                                            <Input id="password"
                                                   type="password"
                                                   placeholder="请输入密码"
                                                   value={this.state.password}
                                                   onChange={this.handleForm}
                                                   name="password"
                                                   onBlur={this.checkPasswordAvailable}/>
                                        </div>
                                    </Col>
                                    <Col span={4}>
                                        <div className="right_prompt_msg"
                                             style={ this.state.check_status.password ? { color: "blue"} : { color: "red"} }>
                                            {this.state.prompt_message.password}
                                        </div>
                                    </Col>
                                </Row>

                            </div>

                            {/* 验证栏 */}
                            <div className="row_div">
                                <Row>
                                    <Col span={1}></Col>
                                    <Col span={4}>
                                        <div className="left_prompt_msg">
                                            <label htmlFor="pictureCheckCode">图片验证</label>
                                        </div>
                                    </Col>
                                    <Col span={1}></Col>

                                    <Col span={7}>
                                        <Row>
                                            <Col span={24}>
                                                <div>
                                                    <Input id="pictureCheckCode"
                                                           placeholder="图片验证码"
                                                           value={this.state.picture_check_code}
                                                           onChange={this.handleForm}
                                                           name="picture_check_code"
                                                           onBlur={this.checkPictureCheckCode}/>
                                                </div>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col span={24}>
                                                <div className="under_prompt_msg"
                                                     style={ this.state.check_status.picture_check_code ? { color: "blue"} : { color: "red"} }>
                                                    {this.state.prompt_message.picture_check_code}
                                                </div>
                                            </Col>
                                        </Row>
                                    </Col>

                                    <Col span={1}></Col>
                                    <Col span={6}>
                                        <div>
                                            <img id="checkPicture"
                                                 src={this.state.check_picture_url}
                                                 onClick={this.handleCheckCodePicClick}/>
                                        </div>
                                    </Col>

                                    <Col span={4}></Col>
                                </Row>

                            </div>


                            {/* 登录按钮 */}
                            <Row>
                                <Col span={24}>
                                    <div id="login_btn_div">
                                        <div>
                                            <Button type="primary" id="login_btn" onClick={this.handleSubmitButtonClick}>登录</Button>
                                        </div>
                                    </div>
                                </Col>
                            </Row>

                            {/* 忘记密码按钮 */}
                            <Row>
                                <Col span={24}>
                                    <div id="forget_password_div">
                                        <Link to="/reset" id="forget_password_btn">忘记密码？</Link>
                                    </div>
                                </Col>
                            </Row>


                        </div>


                    </Col>



                    <Col span={8}></Col>

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