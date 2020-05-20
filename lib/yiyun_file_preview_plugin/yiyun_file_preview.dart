import 'dart:async';

import 'package:flutter/services.dart';

class YiyunFilePreview {
  static const MethodChannel _channel =
      const MethodChannel('yiyun_file_preview');

  static Future<bool> IfFileExists(String path) async {
    final bool res = await _channel.invokeMethod('if_file_exists',<String,dynamic>{
      'path':path,
    });
    return res;
  }

  static Future<bool> CanOpenFile (String path) async {
    final bool res = await _channel.invokeMethod('can_open_file',<String,dynamic>{
      'path':path,
    });
    return res;
  }

  static Future<List> fileRead(String path) async {   //接受main.dart的参数
    //下面是把参数传递到底层
    final List res = await _channel.invokeMethod('fileRead',<String,dynamic>{
      'path':path,
    });
    return res;
  }

  static Future<bool> persiomReadFile() async{
    final bool res1 = await _channel.invokeMethod('permission_write_read');
    return res1;
  }

  static persionReadFileRequest() async{
    await _channel.invokeMethod('permission_write_read_request');
  }

  static Future<String> GetFilePath() async{
    final String res1 = await _channel.invokeMethod('get_file_path');
    return res1;
  }

}