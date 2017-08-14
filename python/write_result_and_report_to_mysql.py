# -*- coding:utf-8 -*-
import os
import sys
import MySQLdb
from bs4 import BeautifulSoup

# 外部传入执行任务时参数的编号
execute_id = sys.argv[1]


# 连接db
def connect_db(db):
    db = MySQLdb.connect(host="10.9.8.20",
                         port=3306,
                         user="monitor",
                         passwd="monitor123",
                         db=db,
                         charset="utf8")
    return db


# 请求mysql获取数据
def get_data(db, sql):
    conn = connect_db(db)
    cur = conn.cursor()
    cur.execute(sql)
    data = cur.fetchall()
    cur.close()
    conn.commit()
    conn.close()
    return data


def write_result():
    file_path = os.path.dirname(os.path.dirname(os.path.abspath(__file__))) + "/target/surefire-reports/TestReport.html"
    f = open(file_path, "r")
    html = f.read()
    # 测试结果写入MySQL
    soup = BeautifulSoup(html)
    # 执行case为一个时,显示为td,2个以上为th
    PassCase = int(soup.find_all("td", class_="numi")[0].get_text())
    FailCase = int(soup.find_all("td", class_="numi")[2].get_text())
    # 测试报告写入MySQL
    html = MySQLdb.escape_string(html)
    get_data("app", "update app_execute set test_result=\"%s\", test_report=\"%s\" where id=%s" % (str(PassCase) + "/" + str(PassCase+FailCase), html, str(execute_id)))

if __name__ == '__main__':
    write_result()




