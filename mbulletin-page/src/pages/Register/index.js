import React from "react";
import axios from "axios";

import './index.css';

import Navigation from '../../components/Navigation';
import Information from '../../components/Information';
import { Button, Col, Row, Input } from "antd";
import { Link } from "react-router-dom";

import md5 from "js-md5";

export default class Register extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            username: '',
            email: '',
            password: '',
            confirm_password: '',
            picture_check_code: '',
            check_picture_url: "http://localhost:10010/users/checkPicture?" + new Date().getTime(),

            prompt_message: {
                username: '',
                email: '',
                password: '',
                confirm_password: '',
                picture_check_code: ''
            },

            check_status: {
                username: false,
                email: false,
                password: false,
                confirm_password: false,
                picture_check_code: false
            }

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
        if (check_status.username && check_status.email && check_status.password && check_status.confirm_password && check_status.picture_check_code) {

            let submitData = {
                "username": this.state.username,
                "email": this.state.email,
                "password": md5(this.state.password)
            }

            axios({
                method: 'post',
                url: 'http://localhost:10010/users/registerUserInfo',
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                },
                data: JSON.stringify(submitData),
                dataType: 'Json',
                withCredentials: true
            }).then((res) => {
                window.location.replace("/register_success");
            }).catch((error) => {
                alert("服务器繁忙，注册失败！");
            })

        } else {
            this.checkUsernameAvailable();
            this.checkEmailIsAvailable();
            this.checkPasswordAvailable();
            this.checkConfirmPasswordAvailable();
            this.checkPictureCheckCode();
        }
    }

    checkEmailIsAvailable = () => {

        // 先更新校验状态
        let check_status = this.state.check_status;
        check_status.email = false;
        let prompt_message = this.state.prompt_message;
        prompt_message.email = '';
        this.setState({
            check_status,
            prompt_message
        })

        // 检查邮箱是否符合规范
        if (this.state.email === '') {
            let check_status = this.state.check_status;
            check_status.email = false;
            let prompt_message = this.state.prompt_message;
            prompt_message.email = '请输入!';
            this.setState({
                check_status,
                prompt_message
            })
            return;
        } else {
            if(!this.checkEmail()) {
                let check_status = this.state.check_status;
                check_status.email = false;
                let prompt_message = this.state.prompt_message;
                prompt_message.email = '不规范!';
                this.setState({
                    check_status,
                    prompt_message
                })
                return;
            }
        }

        axios({
            method: 'get',
            url: 'http://localhost:10010/users/mailRegistered/' + this.state.email,
            dataType: 'Json',
            withCredentials: true
        }).then((res) => {

            if (res.data.object === 0) {
                // 未注册
                let check_status = this.state.check_status;
                check_status.email = true;
                let prompt_message = this.state.prompt_message;
                prompt_message.email = '可用邮箱';
                this.setState({
                    check_status,
                    prompt_message
                });
            } else {
                // 已注册
                let check_status = this.state.check_status;
                check_status.email = false;
                let prompt_message = this.state.prompt_message;
                prompt_message.email = '已注册!';
                this.setState({
                    check_status,
                    prompt_message
                })
            }


        }).catch((error) => {
            // 后端接口调用出错
            let check_status = this.state.check_status;
            check_status.email = false;
            let prompt_message = this.state.prompt_message;
            prompt_message.email = '出错了!';
            this.setState({
                check_status,
                prompt_message
            })
        })


    }
    checkEmail = () => {
        const reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
        if (reg.test(this.state.email)) {
            return true;
        } else {
            return false;
        }
    }
    checkUsernameAvailable = () => {
        // 先更新校验状态
        let check_status = this.state.check_status;
        check_status.username = false;
        let prompt_message = this.state.prompt_message;
        prompt_message.username = '';
        this.setState({
            check_status,
            prompt_message
        })

        // 检查用户名是否符合规范
        if (this.state.username === '') {
            let check_status = this.state.check_status;
            check_status.username = false;
            let prompt_message = this.state.prompt_message;
            prompt_message.username = '请输入!';
            this.setState({
                check_status,
                prompt_message
            })
        } else {
            if(!this.checkUsername()) {
                let check_status = this.state.check_status;
                check_status.username = false;
                let prompt_message = this.state.prompt_message;
                prompt_message.username = '不规范!';
                this.setState({
                    check_status,
                    prompt_message
                })
            } else {
                let check_status = this.state.check_status;
                check_status.username = true;
                let prompt_message = this.state.prompt_message;
                prompt_message.username = '可用';
                this.setState({
                    check_status,
                    prompt_message
                })
            }
        }
    }
    checkUsername = () => {
        const reg = /(\b[A-Za-z][A-Za-z0-9]{5,11}\b)/;
        if (reg.test(this.state.username)) {
            return true;
        } else {
            return false;
        }
    }
    checkPasswordAvailable = () => {
        // 先更新校验状态
        let check_status = this.state.check_status;
        check_status.password = false;
        let prompt_message = this.state.prompt_message;
        prompt_message.password = '';
        this.setState({
            check_status,
            prompt_message
        })

        // 检查邮箱是否符合规范
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
            if(!this.checkPassword()) {
                let check_status = this.state.check_status;
                check_status.password = false;
                let prompt_message = this.state.prompt_message;
                prompt_message.password = '太弱!';
                this.setState({
                    check_status,
                    prompt_message
                })
            } else {
                let check_status = this.state.check_status;
                check_status.password = true;
                let prompt_message = this.state.prompt_message;
                prompt_message.password = '可用';
                this.setState({
                    check_status,
                    prompt_message
                })
            }
        }
    }
    checkPassword = () => {
        const reg = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,16}$/;
        if (reg.test(this.state.password)) {
            return true;
        } else {
            return false;
        }
    }
    checkConfirmPasswordAvailable = () => {
        // 先更新校验状态
        let check_status = this.state.check_status;
        check_status.confirm_password = false;
        let prompt_message = this.state.prompt_message;
        prompt_message.confirm_password = '';
        this.setState({
            check_status,
            prompt_message
        })

        // 检查密码是否符合规范
        if (this.state.confirm_password === '') {
            let check_status = this.state.check_status;
            check_status.confirm_password = false;
            let prompt_message = this.state.prompt_message;
            prompt_message.confirm_password = '请输入!';
            this.setState({
                check_status,
                prompt_message
            })
        } else {
            if(!this.checkConfirmPassword()) {
                let check_status = this.state.check_status;
                check_status.confirm_password = false;
                let prompt_message = this.state.prompt_message;
                prompt_message.confirm_password  = '不一致!';
                this.setState({
                    check_status,
                    prompt_message
                })
            } else {
                let check_status = this.state.check_status;
                check_status.confirm_password  = true;
                let prompt_message = this.state.prompt_message;
                prompt_message.confirm_password  = '通过';
                this.setState({
                    check_status,
                    prompt_message
                })
            }
        }
    }
    checkConfirmPassword = () => {
        if (this.state.password === this.state.confirm_password) {
            return true;
        } else {
            return false;
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

        return <div className="Register">

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

                    {/* 信息版 */}
                    <Col span={7}>
                        <div id="board">
                            <div id="main_title">你好!</div>
                            <div id="sub_title">欢迎注册MBulletin新用户!</div>
                            <div id="left_character">&nbsp;&nbsp;&nbsp;&nbsp;全球拥有超过 400 万个活跃域的注册商不足 12
                                家。MBulletin距离成为其中一家仅剩 <strong>3999999</strong> 位用户。</div>
                        </div>
                    </Col>

                    <Col span={2}></Col>

                    {/* 注册面板 */}
                    <Col span={7}>
                        <div id="register_board">

                            <Row>
                                <Col span={24}>
                                    <div>
                                        <h1 id="register_user_info">注册用户</h1>
                                    </div>
                                </Col>
                            </Row>

                            {/* 用户名 */}
                            <div className="row_div">
                                <Row>
                                    <Col span={1}></Col>
                                    <Col span={4}>
                                        <div className="left_prompt_msg">
                                            <label for="username">用户名</label>
                                        </div>
                                    </Col>
                                    <Col span={1}></Col>
                                    <Col span={14}>
                                        <div>
                                            <Input id="username" placeholder="请输入用户名" value={this.state.username} onChange={this.handleForm} name="username" onBlur={this.checkUsernameAvailable}/>
                                        </div>
                                    </Col>
                                    <Col span={4}>
                                        <div className="right_prompt_msg" style={ this.state.check_status.username ? { color: "blue"} : { color: "red"} }>
                                            {this.state.prompt_message.username}
                                        </div>
                                    </Col>
                                </Row>

                            </div>

                            {/* 用户邮箱 */}
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
                                            <Input id="email" placeholder="请输入注册邮箱" value={this.state.email} onChange={this.handleForm} name="email" onBlur={this.checkEmailIsAvailable}/>
                                        </div>
                                    </Col>
                                    <Col span={4}>
                                        <div className="right_prompt_msg" style={ this.state.check_status.email ? { color: "blue"} : { color: "red"} }>
                                            {this.state.prompt_message.email}
                                        </div>
                                    </Col>
                                </Row>

                            </div>

                            {/* 用户密码 */}
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
                                            <Input id="password" type="password" placeholder="请输入密码" value={this.state.password} onChange={this.handleForm} name="password" onBlur={this.checkPasswordAvailable}/>
                                        </div>
                                    </Col>
                                    <Col span={4}>
                                        <div className="right_prompt_msg" style={ this.state.check_status.password ? { color: "blue"} : { color: "red"} }>
                                            {this.state.prompt_message.password}
                                        </div>
                                    </Col>
                                </Row>

                            </div>

                            {/* 用户确认密码 */}
                            <div className="row_div">
                                <Row>
                                    <Col span={1}></Col>
                                    <Col span={4}>
                                        <div className="left_prompt_msg">
                                            <label htmlFor="confirmPassword">确认密码</label>
                                        </div>
                                    </Col>
                                    <Col span={1}></Col>
                                    <Col span={14}>
                                        <div>
                                            <Input id="confirmPassword" type="password" placeholder="请再次输入密码" value={this.state.confirm_password} onChange={this.handleForm} name="confirm_password" onBlur={this.checkConfirmPasswordAvailable}/>
                                        </div>
                                    </Col>
                                    <Col span={4}>
                                        <div className="right_prompt_msg" style={ this.state.check_status.confirm_password ? { color: "blue"} : { color: "red"} }>
                                            {this.state.prompt_message.confirm_password}
                                        </div>
                                    </Col>
                                </Row>

                            </div>

                            {/* 用户图片验证 */}
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
                                                    <Input id="pictureCheckCode" placeholder="图片验证码" value={this.state.picture_check_code} onChange={this.handleForm} name="picture_check_code" onBlur={this.checkPictureCheckCode}/>
                                                </div>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <Col span={24}>
                                                <div className="under_prompt_msg" style={ this.state.check_status.picture_check_code ? { color: "blue"} : { color: "red"} }>
                                                    {this.state.prompt_message.picture_check_code}
                                                </div>
                                            </Col>
                                        </Row>
                                    </Col>

                                    <Col span={1}></Col>
                                    <Col span={6}>
                                        <div>
                                            <img id="checkPicture" src={this.state.check_picture_url} onClick={this.handleCheckCodePicClick}/>
                                        </div>
                                    </Col>

                                    <Col span={4}></Col>
                                </Row>

                            </div>

                            {/* 注册按钮 */}
                            <Row>
                                <Col span={24}>
                                    <div id="register_btn_div">
                                        <div><Button type="primary" id="register_btn" onClick={this.handleSubmitButtonClick}>注册</Button></div>
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