# -*- coding:utf-8 -*-
import requests
import sys

# 外部传入参数
reg_account = sys.argv[1]
reg_env = sys.argv[2]


# 初始化
if reg_env == "stage":
    base_url = "https://sso-stage.ruifusoft.com"
else:
    base_url = "https://sso-qa.ruifusoft.com"


# 检测用户是否已经注册
def check_user_exist(account):
    url = base_url + "/v1/users/check"
    if "@" in account:
        payload = {
            "type": "email",
            "value": account,
            "aver": 1
        }
    elif "@" not in account:
        payload = {
            "type": "mobile",
            "value": account,
            "aver": 1
        }
    else:
        return 0
    r = requests.post(url, json=payload)
    return r.json()


# 删除
def delete_account(uin):
    url = base_url + "/v1/users/delete/" + str(uin)
    r = requests.get(url)
    return r.json()


# 主函数
def main():
    check_user = check_user_exist(reg_account)
    uin = check_user.get("data").get("uin")
    print delete_account(uin)

if __name__ == '__main__':
    main()
