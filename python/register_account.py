# -*- coding:utf-8 -*-
import requests
import sys


# 外部传入参数
reg_account = sys.argv[1]
reg_env = sys.argv[2]
reg_password = sys.argv[3]


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


# 注册
# account_type 0：app 手机号码注册 1：app 邮箱注册 2： web 手机号码注册 3：web 邮箱注册  4： esop 邮箱 注册
def register(account, pwd, captcha):
    if "@" not in account:
        reg_type = 0
    else:
        reg_type = 1
    url = base_url + "/v1/users"
    payload = {
        "registerName": account,
        "pwd": pwd,
        "aver": 1,
        "verificationCode": captcha,
        "type": reg_type
    }
    r = requests.post(url, json=payload, headers=headers)
    return r.json()


# 主函数
def main(account):
    print "Send a register code:"
    print send_code(account)
    print "Do register:"
    print register(account, reg_password, "1234")


if __name__ == '__main__':
    main(reg_account)



