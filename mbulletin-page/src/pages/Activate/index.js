import React, {useEffect} from "react";

import {useParams} from "react-router-dom";

import axios from "axios";

import './index.css';


export default function Activate() {
    let params = useParams();
    let submitData = {
        "email": params.email,
        "checkCode": params.checkCode
    }


    useEffect(() => {
        document.title = 'MBulletin账户激活';
        axios({
            method: 'post',
            url: 'http://localhost:10010/users/activateAccount',
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            },
            data: JSON.stringify(submitData),
            dataType: 'Json',
            withCredentials: true
        }).then((res) => {
            let data = res.data.message;
            alert(data);
        }).catch((error) => {
            alert("出错了！");
        })

    });

    return <div className="Activate">

        <div><h1>MBulletin 账户激活</h1></div>


        <div>激活邮箱: {params.email}</div><br/>
        <div>激活码: {params.checkCode}</div>


        <div><a href='http://localhost:3000'>点击前往MBulletin主页</a></div>


    </div>

}

