# -*- coding:utf-8 -*-
import sys

import requests

# 外部传入参数
reg_account = sys.argv[1]
reg_env = sys.argv[2]
new_password = sys.argv[3]


# 初始化
if reg_env == "stage":
    base_url = "https://sso-stage.ruifusoft.com"
else:
    base_url = "https://sso-qa.ruifusoft.com"

headers = {
    "content-type": "application/json",
    "X-device": "Android||osver=6.0.1||romver=EUI 8||model=LeMax2||identity=2134213||lang=zh_CN||network=wifi||channel=LeMarket",
    "X-product": "broker||version=1.6.0"
}


# 检测用户是否已经注册
def check_user_exist(account):
    url = base_url + "/v1/users/check"
    if "@" not in account:
        payload = {
            "type": "mobile",
            "value": account,
            "aver": 1
        }
    elif "@" in account:
        payload = {
            "type": "email",
            "value": account,
            "aver": 1
        }
    else:
        return 0
    r = requests.post(url, json=payload)
    return r.json()


# 发送验证码
# account_type 0：app 手机号码请求验证码， 1：app 邮箱请求验证码， 2： web 手机号码请求验证码 3：web 邮箱请求验证码
def send_code(account):
    if "@" not in account:
        reg_type = 0
    else:
        reg_type = 1
    url = base_url + "/v1/services/sms"
    payload = {
        "registerName": account,
        "verificationType": 0,
        "registerType": reg_type
    }
    r = requests.post(url, json=payload)
    return r.json()


# 获取重置密码的token
def get_token(uin, account, idNumber):
    url = base_url + "/v1/users/" + str(uin) + "/reset/token"
    payload = {
        "registerName": account,
        "verificationCode": "1234",
        "idNumber": idNumber
    }
    r = requests.post(url, json=payload, headers=headers)
    return r.json()


# 重置密码
def reset_password(uin, token, new_password):
    url = base_url + "/v1/users/" + str(uin) + "/reset/passwd"
    payload = {
        "token": token,
        "password": new_password,
        "aver": 1
    }
    r = requests.post(url, json=payload, headers=headers)
    return r.json()


# 主函数
def main():
    check_user = check_user_exist(reg_account)
    uin = check_user.get("data").get("uin")
    print u"获取uin: " + str(uin)
    send_code(reg_account)
    print u"发送验证码"
    login_data = get_token(uin, reg_account, "")
    token_data = login_data
    token = token_data.get("data").get("token")
    print u"获取token:" + str(token)
    print reset_password(uin, token, new_password)

if __name__ == '__main__':
    main()
