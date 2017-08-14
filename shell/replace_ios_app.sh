#!/bin/bash

# ********************************************
# RuiFuTech ios app Script
# Author: Timen.xu
# Version: 1.0.0
# Lastest update Date: 2016-08-10

# History
# 2016-08-18
# set shell choose environment

# ************** Set Variable ****************
env=$1
code=$2
Date=`date +%Y%m%d_%H%M`

# ********************************************
echo "下载最近的IOS代码"
cd /Users/jenkins/Desktop/Timen/ios_code/
rm -rf *
git init
git clone -b $code http://lihong.liu:Lihong.Liu001@10.9.19.245/ios/majikwealth.git

echo "进入iOS代码本地目录"
cd /Users/jenkins/Desktop/Timen/ios_code/majikwealth/RuiFuTech

# echo "更新iOS代码"
# git pull

if [ $env = "simulator_beta" ]; then
    echo "清理工程"
    xcodebuild -sdk iphonesimulator -target RuiFuTech_Beta clean
    echo "打包生成二进制.app"
    xcodebuild -sdk iphonesimulator -target RuiFuTech_Beta
elif [ $env = "simulator_live" ]; then
    echo "清理工程"
    xcodebuild -sdk iphonesimulator clean
    echo "打包生成二进制.app"
    xcodebuild -sdk iphonesimulator
elif [ $env = "real_beta" ]; then
    echo "清理工程"
    xcodebuild -sdk iphoneos -target RuiFuTech_Beta clean
    echo "打包生成二进制.app"
    xcodebuild -sdk iphoneos -target RuiFuTech_Beta CODE_SIGN_IDENTITY="iPhone Developer"
    echo ".app转化成.ipa"
    xcrun -sdk iphoneos PackageApplication -v /Users/jenkins/Desktop/Timen/ios_code/majikwealth/RuiFuTech/build/Release-iphoneos/RuiFuTech_Beta.app -o /Users/jenkins/version/v_daily/Beta/$Date"_Real_RuiFuTech_Beta.ipa"
elif [ $env = "real_live" ]; then
    echo "清理工程"
    xcodebuild -sdk iphoneos clean
    echo "打包生成二进制.app"
    xcodebuild -sdk iphoneos CODE_SIGN_IDENTITY="iPhone Developer"
    echo ".app转化成.ipa"
    xcrun -sdk iphoneos PackageApplication -v /Users/jenkins/Desktop/Timen/ios_code/majikwealth/RuiFuTech/build/Release-iphoneos/RuiFuTech.app -o /Users/jenkins/version/v_daily/Live/$Date"_Real_RuiFuTech.ipa"
fi
