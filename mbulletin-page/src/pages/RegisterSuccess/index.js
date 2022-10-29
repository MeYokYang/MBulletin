import React from "react";

import './index.css';

export default class Home extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            countDown: 10
        }
    }

    componentDidMount() {
        let timer = setInterval(() => {

            this.setState((preState) => ({
                countDown: preState.countDown - 1
            }), () => {
                if (this.state.countDown === 0) {
                    clearInterval(timer);
                }
            });

        }, 1000)
    }

    handleBtnClick = () => {
        window.location.replace("/index");
    }

    render() {

        if (this.state.countDown === 0) {
            window.location.replace("/index");
        }

        return <div className="RegisterSuccess">

            <div><h1>恭喜成功注册MBulletin账户！</h1></div>
            <div><h2>请登录邮箱完成邮箱验证！验证成功后，即可登录。</h2></div>

            <div><span>{this.state.countDown}</span>秒后，自动跳转到主页……</div>
            <div><button onClick={this.handleBtnClick}>直接跳转</button></div>

        </div>
    }
}