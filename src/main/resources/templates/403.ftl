<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>403禁止页面模板</title>


    <style>

        * {
            margin: 0;
            padding: 0;
            border: 0;
            font-size: 100%;
            font: inherit;
            vertical-align: baseline;
            box-sizing: border-box;
            color: inherit;
        }

        body {
            background-image: linear-gradient(120deg, #4f0088 0%, #000000 100%);
            height: 100vh;
        }

        h1 {
            font-size: 45vw;
            text-align: center;
            position: fixed;
            width: 100vw;
            z-index: 1;
            color: #ffffff26;
            text-shadow: 0 0 50px rgba(0, 0, 0, 0.07);
            top: 50%;
            -webkit-transform: translateY(-50%);
            transform: translateY(-50%);
            font-family: "Montserrat", monospace;
        }

        div {
            background: rgba(0, 0, 0, 0);
            width: 70vw;
            position: relative;
            top: 50%;
            -webkit-transform: translateY(-50%);
            transform: translateY(-50%);
            margin: 0 auto;
            padding: 30px 30px 10px;
            box-shadow: 0 0 150px -20px rgba(0, 0, 0, 0.5);
            z-index: 3;
        }

        P {
            font-family: "Share Tech Mono", monospace;
            color: #f5f5f5;
            margin: 0 0 20px;
            font-size: 17px;
            line-height: 1.2;
        }

        span {
            color: #f0c674;
        }

        i {
            color: #8abeb7;
        }

        div a {
            text-decoration: none;
        }

        b {
            color: #81a2be;
        }

        a.avatar {
            position: fixed;
            bottom: 15px;
            right: -100px;
            -webkit-animation: slide 0.5s 4.5s forwards;
            animation: slide 0.5s 4.5s forwards;
            display: block;
            z-index: 4
        }

        a.avatar img {
            border-radius: 100%;
            width: 44px;
            border: 2px solid white;
        }

        @-webkit-keyframes slide {
            from {
                right: -100px;
                -webkit-transform: rotate(360deg);
                transform: rotate(360deg);
                opacity: 0;
            }
            to {
                right: 15px;
                -webkit-transform: rotate(0deg);
                transform: rotate(0deg);
                opacity: 1;
            }
        }

        @keyframes slide {
            from {
                right: -100px;
                -webkit-transform: rotate(360deg);
                transform: rotate(360deg);
                opacity: 0;
            }
            to {
                right: 15px;
                -webkit-transform: rotate(0deg);
                transform: rotate(0deg);
                opacity: 1;
            }
        }
    </style>
</head>
<body>
<h1>403</h1>
<div><p>> <span>错误代码 </span>: "<i>HTTP 403</i>"</p>
    <p>> <span>错误说明</span>: "<i>拒绝访问。您没有在此服务器上访问此页面的权限
        </i>"</p>
    <p>> <span>导致错误可能的原因</span>: [<b>执行禁止访问，禁止读访问，禁止写访问，需要ssl，需要ssl
            128，拒绝ip地址，需要客户端证书，拒绝站点访问，用户太多，配置无效，密码更改，映射拒绝访问，客户端证书被撤销，目录列表被拒绝，超出客户端访问许可，
            客户端证书不可信或无效，客户端证书已过期或尚未生效，护照登录失败，源访问被拒绝，无限深度被拒绝，来自同一客户端ip的请求太多
        </b></p>
    <p>> <span>您可以访问以下页面查询具体信息</span>: [ <a href="/sys/aboutUs">关于我们</a>, ...]</p>
    <p>> <span>祝您愉快:-)</span></p>
</div>


<script>
    var str = document.getElementsByTagName('div')[0].innerHTML.toString();
    var i = 0;
    document.getElementsByTagName('div')[0].innerHTML = "";

    setTimeout(function () {
        var se = setInterval(function () {
            i++;
            document.getElementsByTagName('div')[0].innerHTML = str.slice(0, i) + "|";
            if (i == str.length) {
                clearInterval(se);
                document.getElementsByTagName('div')[0].innerHTML = str;
            }
        }, 10);
    }, 0);
</script>

</body>
</html>
