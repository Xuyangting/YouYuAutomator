# -*- coding:utf-8 -*-
import sys
import os
from ftplib import FTP


# 需要创建的文件夹名称,与质量中心执行任务的编号一致
dirName = sys.argv[1]


# 建立连接
def ftp_connect():
    ftp = FTP()
    ftp.connect("10.9.8.42", "21", timeout=10)
    ftp.login("timen.xu", "xyt407708323")
    return ftp


# 上传文件
def upload_file(ftp, remote_path, local_path):
    buf_size = 1024
    fp = open(local_path, 'rb')
    ftp.storbinary('STOR ' + remote_path, fp, buf_size)
    ftp.set_debuglevel(0)
    fp.close()


# 主方法
if __name__ == '__main__':
    ftp_conn = ftp_connect()
    ftp_conn.mkd("/" + dirName + "/")
    pic_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "..")) + "/screenshots/"
    for pic_name in os.walk(pic_path):
        for pic in pic_name[2]:
            upload_file(ftp_conn, "/" + dirName + "/" + pic, pic_path + pic)
    ftp_conn.quit()















